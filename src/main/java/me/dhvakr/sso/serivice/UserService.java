package me.dhvakr.sso.serivice;

public interface UserService {
    StringBuilder userInfo(String idToken);
    String addUser(String email,String userName);
    void update(String oldEmail,String newEmail);
}
