package com.potlach.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class S3ClientAmazon implements IS3Client {

    private AmazonS3Client client;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public S3ClientAmazon(final AWSCredentials credentials) {
        client = new AmazonS3Client(credentials);
    }

    @Override
    public List<String> listObjects(String bucketName, String prefix) {
        log.trace("listObjects(bucketName={}, prefix={})", bucketName, prefix);
        final ObjectListing listing = client.listObjects(bucketName, prefix);

        List<String> objectNames = new ArrayList<String>();
        for (S3ObjectSummary object : listing.getObjectSummaries()) {
            objectNames.add(object.getKey());
        }

        return objectNames;
    }

    @Override
    public void deleteObject(String bucketName, String key) {
        log.trace("deleteObject(bucketName={}, key={})", bucketName, key);
        client.deleteObject(bucketName, key);
    }

    @Override
    public URL getUrl(String bucketName, String key) {
        log.trace("getUrl(bucketName={}, key={})", bucketName, key);
        return client.getUrl(bucketName, key);
    }

    @Override
    public void putObject(String bucketName, String key, String content) {
        log.trace("putObject(bucketName={}, key={}, content.length={})", bucketName, key, content.length());
        InputStream input = new ByteArrayInputStream(content.getBytes());
        client.putObject(bucketName, key, input, new ObjectMetadata());
    }

    @Override
    public void putObject(String bucketName, String key, InputStream stream) {
        log.trace("putObjectStream(bucketName={}, key={})", bucketName, key);
        client.putObject(bucketName, key, stream, new ObjectMetadata());
    }
    
    @Override
    public void putObject(String bucketName, String key, File file) {
        log.trace("putObjectStream(bucketName={}, key={}, file={})", bucketName, key, file.getAbsolutePath());
        client.putObject(bucketName, key, file);
    }

    @Override
    public InputStream getObjectContentStream(String bucketName, String key) {
        log.trace("getObjectContentStream(bucketName={}, key={})", bucketName, key);
        final S3Object object = client.getObject(bucketName, key);
        return object.getObjectContent();
    }


}
