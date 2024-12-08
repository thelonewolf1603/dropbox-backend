package com.neeladri.dropbox_clone.dao;

import com.neeladri.dropbox_clone.models.FileMetadata;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileDao extends JpaRepository<FileMetadata, Long> {

    public List<FileMetadata> findByAuthor(String author);

    @Transactional
    @Modifying
    @Query("DELETE FROM FileMetadata f WHERE f.file_name = :filename AND f.author = :user")
    public void deleteByFileCustom(String filename, String user);
}
