package me.noroutine.ucando.blob.s3;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import me.noroutine.ucando.StreamRepository;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleksii on 14/08/14.
 */
public class S3BucketStreamRepository implements StreamRepository {

    private String endpoint;

    private String bucket;

    private String accessKey;

    private String secretKey;

    @Override
    public boolean write(String uuid, InputStream stream) {
        final AmazonS3 s3 = getS3();
        final int partSize = 10*1024*1024; // Set part size to 5 MB.

        final byte[] buffer = new byte[partSize];
        int bytesRead;

        int initialBytesRead = 0;
        try {
            initialBytesRead = stream.read(buffer, 0, partSize);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (initialBytesRead == -1) {
            return false;
        }

        // https://issues.jboss.org/browse/RESTEASY-545, ideally it would be a wrapper stream, but i'm too sleepy to do that
        //
        // we need to try eagerly read the stream, happens only on first chunk
        // we therefore try ONLY ONCE to read more if stream didn't return EOF and initial read block is smaller than the buffer
        if (initialBytesRead < partSize) {
            try {
                bytesRead = stream.read(buffer, initialBytesRead, partSize - initialBytesRead);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            if (bytesRead == -1) {
                // initial is all we have
                bytesRead = initialBytesRead;
            } else {
                bytesRead = initialBytesRead + bytesRead;
            }

            if (bytesRead < partSize) {
                // fuck that, still not enough data to fill the buffer, we go with regular upload request with regular upload

                try {
                    getS3().putObject(new PutObjectRequest(bucket, uuid, new ByteArrayInputStream(buffer, 0, bytesRead), new ObjectMetadata()));
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } else {
            bytesRead = initialBytesRead;
        }

        // start multi part upload

        // Create a list of UploadPartResponse objects. You get one of these for
        // each part upload.
        List<PartETag> partETags = new ArrayList<>();

        // Step 1: Initialize.
        InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, uuid);
        InitiateMultipartUploadResult initResponse =
                s3.initiateMultipartUpload(initRequest);

        try {
            // Step 2: Upload parts.

            int partNumber = 1;
            do {
                // we have first chunk in buffer already, we can just start this loop with upload

                // Create request to upload a part.
                UploadPartRequest uploadRequest = new UploadPartRequest()
                        .withBucketName(bucket).withKey(uuid)
                        .withUploadId(initResponse.getUploadId()).withPartNumber(partNumber)
                        .withInputStream(new ByteArrayInputStream(buffer, 0, bytesRead))
                        .withPartSize(bytesRead);

                // Upload part and add response to our list.
                partETags.add(s3.uploadPart(uploadRequest).getPartETag());

                // get next part, it part can be less than buffer size
                bytesRead = stream.read(buffer, 0, partSize);
                partNumber++;
            } while (bytesRead != -1);

            // Step 3: Complete.
            CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucket, uuid, initResponse.getUploadId(), partETags);

            s3.completeMultipartUpload(compRequest);

            return true;
        } catch (Exception e) {
            s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucket, uuid, initResponse.getUploadId()));
            return false;
        }
    }

    @Override
    public InputStream read(String uuid) {
        try {
            S3Object object = getS3().getObject(bucket, uuid);
            return object.getObjectContent();
        } catch (AmazonS3Exception as3e) {
            return null;
        }
    }

    @Override
    public boolean delete(String uuid) {
        try {
            getS3().deleteObject(bucket, uuid);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static volatile AmazonS3 s3 = null;

    private AmazonS3 getS3() {
        if (s3 == null) {
            synchronized (this.getClass()) {
                ClientConfiguration clientConfiguration = new ClientConfiguration();
                s3 = new AmazonS3Client(new BasicAWSCredentials(this.accessKey, this.secretKey), clientConfiguration);
                s3.setEndpoint(this.endpoint);
            }
            return s3;
        }
        return s3;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}