package me.dhvakr.sso.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String email;
    private long time;

    public UserInfo(String userName, String email) {
        this.userName=userName;
        this.email=email;
    }

    public UserInfo(String userName, String email, long currentTimeMillis) {
        this.userName=userName;
        this.email=email;
        this.time=currentTimeMillis;
    }
}
