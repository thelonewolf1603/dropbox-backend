package com.neeladri.dropbox_clone.dao;

import com.neeladri.dropbox_clone.models.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserDao extends JpaRepository<UserDetail, String> {

    public UserDetail findByUsername(String username);
}
