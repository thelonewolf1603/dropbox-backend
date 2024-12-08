package com.neeladri.dropbox_clone.controller;

import com.neeladri.dropbox_clone.models.DownloadFileRequest;
import com.neeladri.dropbox_clone.models.FileMetadata;
import com.neeladri.dropbox_clone.models.UserDetail;
import com.neeladri.dropbox_clone.service.FileService;
import feign.Response;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("files")
public class FileController {

    @Autowired
    FileService fIleService;

    @GetMapping("welcome")
    public String welcome()
    {
        return "<b>Hello everybody</b>";
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@RequestBody UserDetail user)
    {
        boolean validate = fIleService.login(user.username, user.password);
        if(validate)
        {
            return ResponseEntity.ok("logged in successfully with username: " + user.username);
        }
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("please check your credentials");
    }

    @GetMapping("{userId}")
    public ResponseEntity<List<FileMetadata>> getFiles(@PathVariable String userId)
    {
        List<FileMetadata> fileList = new ArrayList<>();
        try
        {
            fileList = fIleService.getFileList(userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(fileList);
    }

    @PostMapping("/{userId}/upload")
    public ResponseEntity<String> uploadFile(@RequestParam(value = "file") MultipartFile file, @PathVariable String userId) {
        return new ResponseEntity<>(fIleService.uploadFile(file,userId), HttpStatus.OK);
    }

    @PostMapping("/{userId}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@RequestBody DownloadFileRequest file, @PathVariable String userId) {
        byte[] data = fIleService.downloadFile(file.file_name, userId);
        ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + file.file_name + "\"")
                .body(resource);
    }

    @PostMapping("/{userId}/delete")
    public ResponseEntity<String> deleteFile(@RequestBody DownloadFileRequest file, @PathVariable String userId) {
        return new ResponseEntity<>(fIleService.deleteFile(file.file_name, userId), HttpStatus.OK);
    }

}

