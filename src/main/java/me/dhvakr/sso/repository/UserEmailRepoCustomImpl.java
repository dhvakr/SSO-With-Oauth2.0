package me.dhvakr.sso.repository;

import me.dhvakr.sso.model.UserInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.List;

import static org.springframework.security.oauth2.core.oidc.OidcScopes.EMAIL;

@Repository
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailRepoCustomImpl implements UserEmailRepoCustom {

    @Autowired
    EntityManager entityManager;

    @Override
    public List<UserInfo> getUserEmail(String email) {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<UserInfo> criteriaQuery = criteriaBuilder.createQuery(UserInfo.class);
            Root<UserInfo> infoRoot = criteriaQuery.from(UserInfo.class);

            Predicate authorEmailPredicate = criteriaBuilder.equal(infoRoot.get(EMAIL), email);

            criteriaQuery.where(authorEmailPredicate);

            TypedQuery<UserInfo> query = entityManager.createQuery(criteriaQuery);
            return query.getResultList();
        }

    @Override
    public void updateEmail(String oldEmail,String newEmail) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

            // create update
            CriteriaUpdate<UserInfo> update = cb.
                    createCriteriaUpdate(UserInfo.class);

            // set the root class
            Root e = update.from(UserInfo.class);

            // set update and where clause
            update.set(EMAIL, newEmail);
            update.where(cb.greaterThanOrEqualTo(e.get(EMAIL), oldEmail));

            // perform update
            this.entityManager.createQuery(update).executeUpdate();
        }

        @Override
        public List<UserInfo> selectMyClassByDate(Date date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserInfo> cq = cb.createQuery(UserInfo.class);
        Root<UserInfo> myClass = cq.from(UserInfo.class);
        cq.select(myClass);
        cq.where(cb.equal(cb.function("date", Date.class,
                myClass.get("timestamp")), date));
        final TypedQuery<UserInfo> tq = entityManager.createQuery(cq);
        List<UserInfo> myClassList = tq.getResultList();
        return myClassList;
    }
}
