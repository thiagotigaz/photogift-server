package com.potlach.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public interface IS3Client {

    List<String> listObjects(String bucketName, String prefix);

    void deleteObject(String bucketName, String key);

    URL getUrl(String bucketName, String key);

    void putObject(String bucketName, String key, String content);
    void putObject(String bucketName, String key, InputStream stream);
    void putObject(String bucketName, String key, File file);

    InputStream getObjectContentStream(String bucketName, String key);

}