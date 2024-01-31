package com.nva.server.views.user;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.services.UserService;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
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

@PageTitle("User | Management")
@Route(value = "admin/users", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter()
public class UserView extends VerticalLayout {
    private final UserService userService;

    Grid<User> userGrid = new Grid<>(User.class);
    TextField filterText = new TextField();
    UserForm userForm;

    public UserView(UserService userService) {
        this.userService = userService;

        addClassName("user-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());

        // This function will be called when user types in search box
        updateUserList();
        closeEditor();
    }

    private void closeEditor() {
        userForm.setUser(null);
        userForm.setVisible(false);
        removeClassName("editing");
    }

    private void updateUserList() {
        userGrid.setItems(userService.getUsers(filterText.getValue()));
    }

    private void configureForm() {
        userForm = new UserForm(Arrays.stream(Role.values()).toList());
        userForm.setWidth("25em");

        userForm.addListener(UserForm.SaveEvent.class, this::saveUser);
        userForm.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        userForm.addListener(UserForm.CloseEvent.class, e -> closeEditor());
    }

    private void deleteUser(UserForm.DeleteEvent e) {
        userService.removeUser(e.getUser());
        updateUserList();
        closeEditor();
    }

    private void saveUser(UserForm.SaveEvent e) {
        if (userService.findByEmail(e.getUser().getEmail()).isEmpty()) {
            userService.saveUser(e.getUser());
            updateUserList();
            closeEditor();
        } else {
            CustomNotification.showNotification("Email is already exist", "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(userGrid, userForm);
        content.setFlexGrow(2, userGrid);
        content.setFlexGrow(1, userForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureGrid() {
        userGrid.addClassNames("user-grid");
        userGrid.setSizeFull();
        userGrid.setColumns("firstName", "lastName", "email"); // These properties must be similar to entity properties
        /**
         * Column used for foreign keys
         */
        userGrid.addColumn(User::getRole).setHeader("Role");
        userGrid.getColumns().forEach(col -> col.setAutoWidth(true)); //Show scrollbar when width turns to small

        userGrid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));
    }

    private void editUser(User user) {
        if (user == null) closeEditor();
        else {
            userForm.setUser(user);
            userForm.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateUserList());

        Button addUserBtn = new Button("Add user");
        addUserBtn.getStyle().setCursor("pointer");
        addUserBtn.addClickListener(e -> addUser());

        var toolbar = new HorizontalLayout(filterText, addUserBtn);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void addUser() {
        userGrid.asSingleSelect().clear();
        editUser(new User());
    }
}
