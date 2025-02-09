package com.example.govai.services;

import com.example.govai.models.User;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.transaction.Transactional;

@Stateless
public class UserService {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Transactional
    public User registerUser(User user) {
        if (!em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email")
                .setParameter("email", user.getEmail())
                .getSingleResult().equals(0L)) {
            throw new RuntimeException("Email already registered");
        }

        user.setPassword(passwordHash.generate(user.getPassword().toCharArray()));

        em.persist(user);
        em.flush();

        return user;
    }

    public User findByEmail(String email) {
        return em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();
    }
}
