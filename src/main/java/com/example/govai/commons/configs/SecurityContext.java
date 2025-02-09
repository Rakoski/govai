package com.example.govai.commons.configs;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@Named
@RequestScoped
public class SecurityContext implements Serializable {
    private String username;
    private Set<String> roles;

    public boolean hasRole(String role) {
        return roles != null && roles.contains(role);
    }
}