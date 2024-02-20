package com.nva.server.views.home;

import com.nva.server.services.UserService;
import com.nva.server.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AccessDeniedErrorRouter;
import jakarta.annotation.security.RolesAllowed;

//@PageTitle("Dashboard | Console")
//@Route(value = "admin/dashboard", layout = MainLayout.class)
//@RouteAlias(value = "admin", layout = MainLayout.class)
//@RouteAlias(value = "", layout = MainLayout.class)
//@RolesAllowed("ROLE_ADMIN")
//@AccessDeniedErrorRouter()
public class HomeView extends VerticalLayout {
//    private final UserService userService;
//    public HomeView(UserService userService) {
//        this.userService = userService;
//
//        addClassName("dashboard-view");
//        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
//        add(getUserStats());
//    }
//
//    private Component getUserStats() {
//        Span stats = new Span(userService.countUsers() + " users");
//        stats.addClassNames("text-xl", "mt-m");
//        return stats;
//    }
}
