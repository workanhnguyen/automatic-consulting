package com.nva.server.views.error;

import com.nva.server.views.user.UserView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.HttpStatusCode;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;

@PermitAll
@RequiredArgsConstructor
public class CustomNotFoundView extends VerticalLayout implements HasErrorParameter<NotFoundException> {

    @Override
    public int setErrorParameter(BeforeEnterEvent beforeEnterEvent, ErrorParameter<NotFoundException> errorParameter) {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button backToHomePageBtn = new Button("Back to page");
        backToHomePageBtn.addClickListener(clickEvent -> UI.getCurrent().navigate(UserView.class));
        backToHomePageBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        backToHomePageBtn.getStyle().set("cursor", "pointer");

        add(new H2("Page not found!"));
        add(new Paragraph("This page is not available or doesn't exist."));
        add(backToHomePageBtn);
        return HttpStatusCode.NOT_FOUND.getCode();
    }
}
