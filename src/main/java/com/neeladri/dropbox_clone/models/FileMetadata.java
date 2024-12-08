package com.neeladri.dropbox_clone.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Data
@Entity
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    public String file_name;
    public String author;
    public Date created;
}
