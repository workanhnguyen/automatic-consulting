package com.nva.server.views.login;

import com.nva.server.services.AuthenticationService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        loginForm.setAction("login");

        add(new H1("Admin Login"), loginForm);
    }

    private boolean hasAdminRole(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    private void showLoginError(LoginForm loginForm, String message) {
        loginForm.setError(true);
        LoginI18n customI18n = LoginI18n.createDefault();
        LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();
        errorMessage.setMessage(message);
        customI18n.setErrorMessage(errorMessage);
        loginForm.setI18n(customI18n);
    }



    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            loginForm.setError(true);
            LoginI18n customI18n = LoginI18n.createDefault();
            LoginI18n.ErrorMessage errorMessage = new LoginI18n.ErrorMessage();

            // Check if the user has the role ROLE_USER
            if (hasUserRole()) {
                errorMessage.setMessage("Only user with role ADMIN can login!");
            } else {
                // Redirect to "/login?error" if the user has the role ROLE_USER
                UI.getCurrent().navigate("login?error");
                return;
            }

            customI18n.setErrorMessage(errorMessage);
            loginForm.setI18n(customI18n);
        }
    }

    private boolean hasUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_USER"));
    }
}
