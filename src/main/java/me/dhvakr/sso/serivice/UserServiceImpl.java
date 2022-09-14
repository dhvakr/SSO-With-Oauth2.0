package me.dhvakr.sso.serivice;

import me.dhvakr.sso.model.UserInfo;
import me.dhvakr.sso.repository.UserRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public StringBuilder userInfo(String idToken) {

        StringBuilder userInfo = new StringBuilder();
        String[] parts = idToken.split("\\.");

        JSONObject payload = new JSONObject(decode(parts[1]));
        String userName = payload.getString("name");
        String email = payload.getString("email");
        userInfo.append("Welcome, ").append(userName).append("<br><br>");
        userInfo.append("e-mail: ").append(email).append("<br><br>");
        new UserInfo(userName, email);

        return userInfo;
    }

    @Override
    public String addUser(String email,String userName) {
        UserInfo userInformation = new UserInfo(userName, email,System.currentTimeMillis());
        String existsUserName = userRepo.findByName(userName);
        userRepo.save(userInformation);
        return "save failed";
    }

    @Override
    public void update(String oldEmail, String newEmail) {
    }

    private static String decode(String encodedString) {
        return new String(Base64.getUrlDecoder().decode(encodedString));
    }
}
