package com.nva.server.views.home;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.services.UserService;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AccessDeniedErrorRouter;
import jakarta.annotation.security.RolesAllowed;

import java.util.Arrays;

@PageTitle("Dashboard | Console")
@Route(value = "admin/dashboard", layout = MainLayout.class)
@RouteAlias(value = "admin", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter()
public class HomeView extends VerticalLayout {
    private final UserService userService;
    public HomeView(UserService userService) {
        this.userService = userService;

        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(getUserStats());
    }

    private Component getUserStats() {
        Span stats = new Span(userService.countUsers() + " users");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }
}
