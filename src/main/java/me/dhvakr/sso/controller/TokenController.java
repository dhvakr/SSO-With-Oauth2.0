package me.dhvakr.sso.controller;


import me.dhvakr.sso.model.UserInfo;
import me.dhvakr.sso.repository.UserRepo;
import me.dhvakr.sso.serivice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
public class TokenController<T> {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    EntityManager entityManager;

    @GetMapping("/welcome")
    public StringBuilder getAlbums(Model model, @AuthenticationPrincipal OidcUser principal) {

        // Get ID Token Object
        OidcIdToken idTokenObject = principal.getIdToken();
        // Get ID Token Value
        String idToken = idTokenObject.getTokenValue();

        return userService.userInfo(idToken);
    }

    @GetMapping("/adduser/{email}/{username}")
    public String addUser(@PathVariable("email") String email, @PathVariable("username") String userName) {
        return userService.addUser(email, userName);
    }

    @GetMapping("/getemail/criteria/query/{email}")
    public List<UserInfo> getEmail(@PathVariable("email") String email) {
        return userRepo.getUserEmail(email);
    }

    @GetMapping("/update/{old}/{new}")
    public void update(@PathVariable("old") String old, @PathVariable("new") String newEmail) {
        userService.update(old, newEmail);
    }

    @GetMapping("/groupby")
    public List<Long> grpby()
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<UserInfo> infoRoot = criteriaQuery.from(UserInfo.class);
        criteriaQuery.groupBy(infoRoot.get("time"));
        criteriaQuery.where(criteriaBuilder.equal(criteriaBuilder.function("date", Date.class, infoRoot.get("time")), "12/07/2022"));

        TypedQuery<Long> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @GetMapping("/grp")
    public List<UserInfo> grp()
    {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserInfo> criteriaQuery = cb.createQuery(UserInfo.class);
        Root<UserInfo> infoRoot = criteriaQuery.from(UserInfo.class);
        criteriaQuery.groupBy(infoRoot.get("time"));
        TypedQuery<UserInfo> result=entityManager.createQuery(criteriaQuery);
        return result.getResultList();
    }

    @GetMapping("/timetodate")
    public List<UserInfo> timeToDate()
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserInfo> criteriaQuery = criteriaBuilder.createQuery(UserInfo.class);
        Root<UserInfo> from = criteriaQuery.from(UserInfo.class);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneOffset.UTC));

        criteriaQuery.groupBy(from.<java.sql.Date>get("time"));
        ParameterExpression<Calendar> parameter = criteriaBuilder.parameter(Calendar.class);
        TypedQuery<UserInfo> query = entityManager.createQuery(criteriaQuery);
        List<UserInfo> results = query.getResultList();
        return results;
    }

    @GetMapping("/math")
    public List<Number> math()
    {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Number> criteriaQuery = criteriaBuilder.createQuery(Number.class);
        Subquery<Long> subcq = criteriaQuery.subquery(Long.class);
        Root from = criteriaQuery.from(UserInfo.class);


        subcq.select(from.get("time"));
        subcq.groupBy(from.get("time"));
        criteriaQuery.select(criteriaBuilder.count(subcq));

        Expression<Long> dateAndTime=criteriaBuilder.quot(from.get("time"),1000);
        Expression<Long> mul= criteriaBuilder.quot(criteriaBuilder.quot(from.get("time"),1000),86400000);
        Expression<Number> diff=criteriaBuilder.diff(criteriaBuilder.quot(from.get("time"),1000),mul);
        criteriaQuery.select(diff);
        subcq.select(from.get("time"));
        TypedQuery<Number> query = entityManager.createQuery(criteriaQuery);
        List<Number> results = query.getResultList();
        System.out.println(results);
        return  results;
    }
}
