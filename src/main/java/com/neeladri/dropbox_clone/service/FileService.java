package com.neeladri.dropbox_clone.service;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.neeladri.dropbox_clone.dao.FileDao;
import com.neeladri.dropbox_clone.dao.UserDao;
import com.neeladri.dropbox_clone.models.FileMetadata;
import com.neeladri.dropbox_clone.models.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class FileService {

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    FileDao fileDao;

    @Autowired
    UserDao userDao;

    @Autowired
    private AmazonS3 s3Client;

    public List<FileMetadata> getFileList(String userId)
    {
        return fileDao.findByAuthor(userId);
    }

    private FileMetadata createFileMetadata(String fileName, String user)
    {
        FileMetadata entry = new FileMetadata();
        entry.file_name = fileName;
        entry.author = user;
        entry.created = new Date();

        return entry;
    }

    public String uploadFile(MultipartFile file, String user) {
        try
        {
            File fileObj = convertMultiPartFileToFile(file);
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = user + "/" + fileName;
            s3Client.putObject(new PutObjectRequest(bucketName, filePath, fileObj));
            fileDao.save(createFileMetadata(fileName, user));
            fileObj.delete();
            return "File uploaded : " + fileName;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public byte[] downloadFile(String fileName, String user) {
        String filePath = user + "/" + fileName;
        S3Object s3Object = s3Client.getObject(bucketName, filePath);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            byte[] content = IOUtils.toByteArray(inputStream);
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String deleteFile(String fileName, String user) {
        String filePath = user + "/" + fileName;
        s3Client.deleteObject(bucketName, filePath);
        fileDao.deleteByFileCustom(fileName, user);
        return fileName + " removed ...";
    }


    private File convertMultiPartFileToFile(MultipartFile file) {

        File convertedFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertedFile;
    }

    public boolean login(String username, String password) {
        UserDetail user = userDao.findByUsername(username);

        if(user == null)
        {
            UserDetail newUser = new UserDetail();
            newUser.password = password;
            newUser.username = username;
            userDao.save(newUser);
            return true;
        }
        else {
            String entryPass = user.password;
            return Objects.equals(entryPass, password);
        }
    }
}
