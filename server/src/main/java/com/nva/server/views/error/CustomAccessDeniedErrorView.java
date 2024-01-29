package com.nva.server.views.error;

import com.nva.server.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.HttpStatusCode;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;

@PermitAll
@RequiredArgsConstructor
public class CustomAccessDeniedErrorView extends VerticalLayout
        implements HasErrorParameter<AccessDeniedException>, HasDynamicTitle {
    private final SecurityService securityService;

    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<AccessDeniedException> parameter) {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button backToLoginBtn = new Button("Back to login");
        backToLoginBtn.addClickListener(clickEvent -> securityService.logout());
        backToLoginBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backToLoginBtn.getStyle().set("cursor", "pointer");

        add(new H2("Access Denied!"));
        add(new Paragraph("You do not have permission to access this page."));
        add(backToLoginBtn);
        return HttpStatusCode.UNAUTHORIZED.getCode();
    }
    @Override
    public String getPageTitle() {
        return "Access Denied | Admin";
    }
}
