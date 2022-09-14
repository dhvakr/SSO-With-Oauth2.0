package me.dhvakr.sso.repository;

import me.dhvakr.sso.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<UserInfo,Integer>, UserEmailRepoCustom {
    @Query(value="SELECT user_name FROM user_info where user_name=?1",nativeQuery = true)
    String findByName(String userName);
}
