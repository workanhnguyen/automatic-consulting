package com.nva.server.views;

import com.nva.server.entities.User;
import com.nva.server.security.SecurityService;
import com.nva.server.views.action.ActionView;
import com.nva.server.views.entrance_method.EntranceMethodView;
import com.nva.server.views.entrance_method_group.EntranceMethodGroupView;
import com.nva.server.views.faculty.FacultyView;
import com.nva.server.views.home.HomeView;
import com.nva.server.views.major.MajorView;
import com.nva.server.views.scope.ScopeView;
import com.nva.server.views.topic.TopicView;
import com.nva.server.views.user.UserView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    private final SecurityService securityService;
    private H2 viewTitle;

    public MainLayout(@Autowired SecurityService securityService) {
        this.securityService = securityService;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        // Logout button
        Button logoutBtn = new Button("Logout", e -> securityService.logout());
        logoutBtn.getStyle().setCursor("pointer");
        logoutBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        HorizontalLayout layoutLeft = new HorizontalLayout(toggle, viewTitle);
        layoutLeft.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(layoutLeft, logoutBtn);
        header.setWidthFull();
        header.addClassNames("py-0", "pr-m");
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setAlignItems(FlexComponent.Alignment.CENTER);

        addToNavbar(true, header);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Admin Console");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private VerticalLayout createNavigation() {
        VerticalLayout nav = new VerticalLayout();
        nav.setWidthFull();
        nav.setPadding(false);

        SideNav primaryNav = new SideNav();
        primaryNav.setWidthFull();
        primaryNav.setLabel("Primary management");
        primaryNav.setCollapsible(true);
        primaryNav.addItem(new SideNavItem("Dashboard", HomeView.class, LineAwesomeIcon.HOME_SOLID.create()));
        primaryNav.addItem(new SideNavItem("User", UserView.class, LineAwesomeIcon.USER.create()));

        SideNavItem facultyNav = new SideNavItem("Faculty", FacultyView.class, LineAwesomeIcon.GRADUATION_CAP_SOLID.create());
        facultyNav.addItem(new SideNavItem("Major", MajorView.class, LineAwesomeIcon.SITEMAP_SOLID.create()));
        primaryNav.addItem(facultyNav);

        SideNavItem entranceMethodGroupNav = new SideNavItem("Entrance group", EntranceMethodGroupView.class, LineAwesomeIcon.OBJECT_GROUP_SOLID.create());
        entranceMethodGroupNav.addItem(new SideNavItem("Entrance method", EntranceMethodView.class, LineAwesomeIcon.WINDOW_RESTORE_SOLID.create()));
        primaryNav.addItem(entranceMethodGroupNav);

        SideNav dialogflowNav = new SideNav();
        dialogflowNav.setWidthFull();
        dialogflowNav.setLabel("Dialogflow management");
        dialogflowNav.setCollapsible(true);
        dialogflowNav.addItem(new SideNavItem("Action", ActionView.class, LineAwesomeIcon.SHAPES_SOLID.create()));
        dialogflowNav.addItem(new SideNavItem("Scope", ScopeView.class, LineAwesomeIcon.BINOCULARS_SOLID.create()));
        dialogflowNav.addItem(new SideNavItem("Topic", TopicView.class, LineAwesomeIcon.TAG_SOLID.create()));

        nav.add(primaryNav, dialogflowNav);
        return nav;
    }

    private Footer createFooter() {
        Footer footer = new Footer();
        User adminAccount = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VerticalLayout layout = new VerticalLayout();
        layout.add(new H6(String.format("%s %s", adminAccount.getLastName(), adminAccount.getFirstName())));
        layout.add(new Paragraph(adminAccount.getEmail()));
        layout.setPadding(false);
        layout.setSpacing(false);

        footer.add(layout);

        return footer;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
