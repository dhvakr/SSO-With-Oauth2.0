package me.dhvakr.sso.repository;

import me.dhvakr.sso.model.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface UserEmailRepoCustom {
    List<UserInfo> getUserEmail(String email);
    void updateEmail(String email,String newEmail);
    List<UserInfo> selectMyClassByDate(Date date);
}

