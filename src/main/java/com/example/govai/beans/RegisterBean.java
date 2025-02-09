package com.example.govai.beans;

import com.example.govai.models.User;
import com.example.govai.services.AuthService;
import com.example.govai.services.UserService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Named
@Getter
@Setter
@RequestScoped
public class RegisterBean {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    @Inject
    private UserService userService;

    @Inject
    private AuthService authService;

    public String register() throws IOException {
        if (!password.equals(confirmPassword)) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords don't match", null));
            return null;
        }

        try {
            User user = new User();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(password);

            user = userService.registerUser(user);
            String token = authService.loginUser(email, password);

            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            externalContext.getSessionMap().put("token", token);

            externalContext.redirect(externalContext.getRequestContextPath() + "/dashboard.xhtml");
            return null;
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Registration failed: " + e.getMessage(), null));
            return null;
        }
    }
}
