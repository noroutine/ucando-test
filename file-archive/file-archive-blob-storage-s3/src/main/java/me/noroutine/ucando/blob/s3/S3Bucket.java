package me.noroutine.ucando.blob.s3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import me.noroutine.ucando.StreamRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import java.io.InputStream;

/**
 * Created by oleksii on 14/08/14.
 */
@ApplicationScoped
@Default
public class S3Bucket implements StreamRepository {

    private String endpoint = "xxx";

    private String bucket = "xxx";

    private String accessKey = "xxx";

    private String secretKey = "xxx";

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
