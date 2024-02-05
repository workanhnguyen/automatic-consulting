package com.nva.server.views.user;

import com.nva.server.entities.User;
import com.nva.server.services.UserService;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AccessDeniedErrorRouter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope("prototype")
@PageTitle("User | Management")
@Route(value = "admin/users", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter()
@Getter
@Slf4j
public class UserView extends VerticalLayout {
    private final UserService userService;

    private final Grid<User> userGrid = new Grid<>(User.class);
    private final TextField filterText = new TextField();
    private final Dialog confirmDeleteUserDialog = new Dialog();
    private final Dialog editUserDialog = new Dialog();
    private final Dialog createNewUserDialog = new Dialog();
    private final UserForm editUserForm = new EditUserForm();
    private final UserForm createNewUserForm= new CreateNewUserForm();


    public UserView(UserService userService) {
        this.userService = userService;

        addClassName("user-view");
        setSizeFull();
        configureGrid();
        add(getToolbar(), getContent());

        configureForm();
        configureDialog();

        updateUserList();
    }

    private void configureDialog() {
        configureConfirmDeleteUserDialog();
        configureEditUserDialog();
        configureCreateNewUserDialog();
    }

    private void configureCreateNewUserDialog() {
        createNewUserDialog.setHeaderTitle("Add new user");
        createNewUserDialog.getFooter().add(createNewUserForm.getSaveBtn(), createNewUserForm.getCancelBtn());

        add(editUserDialog);
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(userGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureEditUserDialog() {
        editUserDialog.setHeaderTitle("Edit user");
        EditUserForm tempForm = (EditUserForm) editUserForm;
        editUserDialog.getFooter().add(editUserForm.getSaveBtn(), tempForm.getDeleteBtn(), editUserForm.getCancelBtn());

        add(editUserDialog);
    }

    private void configureConfirmDeleteUserDialog() {
        confirmDeleteUserDialog.setHeaderTitle("Delete user");
        confirmDeleteUserDialog.add(new Paragraph("Are you sure you want to delete this user?"));

        Button deleteButton = new Button("Delete", e -> {
            deleteUser(editUserForm.getUser());
            confirmDeleteUserDialog.close();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> confirmDeleteUserDialog.close());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteUserDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteUserDialog);
    }

    private void closeEditor() {
//        editUserForm.setUser(null);
//        editUserForm.setVisible(false);
        editUserDialog.close();
        userGrid.asSingleSelect().clear();
        removeClassName("editing");
    }

    private void closeCreator() {
        createNewUserDialog.close();
        createNewUserForm.setUser(null);
        createNewUserForm.setVisible(false);
        removeClassName("creating");
    }

    private void updateUserList() {
        userGrid.setItems(userService.getUsers(filterText.getValue()));
    }

    private void configureForm() {
        editUserForm.setWidth("25em");
        editUserForm.setVisible(true);
        editUserForm.addListener(UserForm.SaveEvent.class, e -> editUser(e.getUser()));
        editUserForm.addListener(EditUserForm.DeleteEvent.class, e -> confirmDeleteUserDialog.open());
        editUserForm.addListener(UserForm.CloseEvent.class, e -> closeEditor());

        createNewUserForm.setWidth("25em");
        createNewUserForm.setVisible(true);
        createNewUserForm.addListener(UserForm.SaveEvent.class, e -> saveUser(e.getUser()));
        createNewUserForm.addListener(UserForm.CloseEvent.class, e -> closeCreator());
    }

    private void editUser(User user) {
        try {
            userService.editUser(user);
            updateUserList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditor();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteUser(User user) {
        try {
            userService.removeUser(user);
            updateUserList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditor();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void saveUser(User user) {
        try {
            userService.saveUser(user);
            updateUserList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreator();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }


    private void configureGrid() {
        userGrid.addClassNames("user-grid");
        userGrid.setSizeFull();
        userGrid.setColumns("firstName", "lastName", "email"); // These properties must be similar to entity properties
        /**
         * Column used for foreign keys
         */
        userGrid.addColumn(User::getRole).setHeader("Role");
        userGrid.addColumn(
                new ComponentRenderer<>(MenuBar::new, (menuBar, person) -> {
                    menuBar.addItem("Edit", e -> openEditor(editUserForm.getUser())).getStyle().setCursor("pointer");
                    menuBar.addItem("Change password", e -> {}).getStyle().setCursor("pointer");
                    menuBar.addItem("Delete", e -> confirmDeleteUserDialog.open()).getStyle().setColor("red").setCursor("pointer");
                })).setHeader("Actions").setTextAlign(ColumnTextAlign.CENTER);
        userGrid.getColumns().forEach(col -> col.setAutoWidth(true)); //Show scrollbar when width turns to small

        userGrid.asSingleSelect().addValueChangeListener(e -> openEditor(e.getValue()));
    }

    private void openEditor(User user) {
        if (user == null) closeEditor();
        else {
            editUserForm.setUser(user);
            editUserDialog.add(new HorizontalLayout(editUserForm));
            editUserDialog.open();
            addClassName("editing");
            log.error(user.toString());
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateUserList());

        Button addUserBtn = new Button("Add user");
        addUserBtn.getStyle().setCursor("pointer");
        addUserBtn.addClickListener(e -> openCreator());

        var toolbar = new HorizontalLayout(filterText, addUserBtn);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void openCreator() {
        createNewUserDialog.add(new HorizontalLayout(createNewUserForm));
        createNewUserDialog.open();

        userGrid.asSingleSelect().clear();
        editUserForm.setVisible(false);
        createNewUserForm.setVisible(true);
        createNewUserForm.setUser(new User());
        addClassName("creating");
    }
}
