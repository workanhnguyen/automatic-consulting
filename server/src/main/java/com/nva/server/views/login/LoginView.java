package com.nva.server.views.login;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Login")
@Route(value = "/admin/login")
public class LoginView extends VerticalLayout {

    public LoginView() {
        setSpacing(false);

        H2 header = new H2("Login Page");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");

        add(header);
    }

}
