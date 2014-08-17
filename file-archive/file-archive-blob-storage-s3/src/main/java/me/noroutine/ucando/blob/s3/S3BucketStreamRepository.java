package me.noroutine.ucando.blob.s3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import me.noroutine.ucando.StreamRepository;

import java.io.InputStream;

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
        ObjectMetadata objectMetadata = new ObjectMetadata();
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uuid, stream, objectMetadata);
        try {
            getS3().putObject(putObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
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

    private AmazonS3 getS3() {
        AmazonS3Client s3 = new com.amazonaws.services.s3.AmazonS3Client(new BasicAWSCredentials(this.accessKey, this.secretKey));
        s3.setEndpoint(this.endpoint);
        return s3;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}


//AmazonS3 s3Client = getS3();
//int partSize = 10 * 1024 * 1024; // Set part size to 5 MB.
//
//byte[] buffer = new byte[partSize];
//
//// Create a list of UploadPartResponse objects. You get one of these for
//// each part upload.
//List<PartETag> partETags = new ArrayList<PartETag>();
//
//// Step 1: Initialize.
//InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucket, uuid);
//InitiateMultipartUploadResult initResponse =
//        s3Client.initiateMultipartUpload(initRequest);
//
//try {
//        // Step 2: Upload parts.
//        // long filePosition = 0;
//
//        int bytesRead;
//        int partNumber = 1;
//        do {
//        // Last part can be less than 5 MB. Adjust part size.
//        // partSize = Math.min(partSize, (contentLength - filePosition));
//        bytesRead = stream.read(buffer, 0, partSize);
//        if (bytesRead > 0) {
//        // Create request to upload a part.
//        UploadPartRequest uploadRequest = new UploadPartRequest()
//        .withBucketName(bucket).withKey(uuid)
//        .withUploadId(initResponse.getUploadId()).withPartNumber(partNumber)
//        .withInputStream(new ByteArrayInputStream(buffer, 0, bytesRead))
//        //                        .withFileOffset(filePosition)
//        //                        .withFile(file)
//        .withPartSize(partSize);
//
//        // Upload part and add response to our list.
//        partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());
//        // filePosition += partSize;
//        partNumber++;
//        }
//
//        } while (bytesRead != -1);
//
//        // Step 3: Complete.
//        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucket, uuid, initResponse.getUploadId(), partETags);
//
//        s3Client.completeMultipartUpload(compRequest);
//
//        return true;
//        } catch (Exception e) {
//        s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(bucket, uuid, initResponse.getUploadId()));
//        return false;
//        }