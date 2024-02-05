package com.nva.server.views.user;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.services.UserService;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
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
    private final UserForm createNewUserForm = new CreateNewUserForm();


    public UserView(UserService userService) {
        this.userService = userService;

        addClassName("user-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureDialog();

        add(getToolbar(), getContent());

        updateUserList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(userGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialog() {
        configureConfirmDeleteUserDialog();
        configureEditUserDialog();
        configureCreateNewUserDialog();
    }

    private void configureCreateNewUserDialog() {
        createNewUserDialog.setHeaderTitle("Add new user");
        createNewUserForm.getSaveBtn().getStyle().setCursor("pointer");
        createNewUserForm.getCancelBtn().getStyle().setCursor("pointer");

        createNewUserDialog.getFooter().add(createNewUserForm.getSaveBtn(), createNewUserForm.getCancelBtn());

        add(editUserDialog);
    }

    private void configureEditUserDialog() {
        editUserDialog.setHeaderTitle("Edit user");
        EditUserForm tempForm = (EditUserForm) editUserForm;
        tempForm.getDeleteBtn().setVisible(false);
        editUserForm.getSaveBtn().getStyle().setCursor("pointer");
        editUserForm.getCancelBtn().getStyle().setCursor("pointer");

        editUserDialog.getFooter().add(editUserForm.getSaveBtn(), editUserForm.getCancelBtn());

        add(editUserDialog);
    }

    private void configureConfirmDeleteUserDialog() {
        confirmDeleteUserDialog.setHeaderTitle("Delete user");
        confirmDeleteUserDialog.add(new Paragraph("Are you sure you want to delete this user?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteUserDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteUser(editUserForm.getUser());
            closeConfirmDeleteDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteUserDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteUserDialog);
    }

    private void closeEditor() {
        editUserDialog.close();
        removeClassName("editing");
    }

    private void closeCreator() {
        createNewUserDialog.close();
        removeClassName("creating");
    }

    private void updateUserList() {
        userGrid.setItems(userService.getUsers(filterText.getValue()));
    }

    private void configureForm() {
        editUserForm.setWidth("25em");
        editUserForm.setVisible(true);
        editUserForm.addListener(UserForm.SaveEvent.class, e -> editUser(e.getUser()));
        editUserForm.addListener(EditUserForm.DeleteEvent.class, e -> deleteUser(e.getUser()));
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

    private void toggleLockUser(User user) {
        try {
            userService.toggleLockUser(user.getEmail());
            updateUserList();

            String msg = user.getIsEnabled() ? "Locked successfully!" : "Unlocked successfully!";
            CustomNotification.showNotification(msg, "success", Notification.Position.TOP_CENTER, 3000);

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
        userGrid.addColumn(user -> user.getRole().equals(Role.ROLE_ADMIN) ? "ADMIN" : "USER").setHeader("Role");
        userGrid.addColumn(new ComponentRenderer<>(Span::new, (badge, user) -> {
            if (user.isEnabled()) {
                badge.add("Active");
                badge.getElement().getThemeList().add("badge success");
            } else {
                badge.add("Locked");
                badge.getElement().getThemeList().add("badge error");
            }
        })).setHeader("Status");
        userGrid.addColumn(
                new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions").setTextAlign(ColumnTextAlign.CENTER);
        userGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, User user) {
        menuBar.setOpenOnHover(true);
        menuBar.addItem("Edit", e -> openEditor(user)).getStyle().setCursor("pointer");

        Text text = new Text("");
        if (user.getIsEnabled()) text.setText("Lock user");
        else text.setText("Unlock user");

        menuBar.addItem(text, e -> toggleLockUser(user)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteDialog(user)).getStyle().setColor("red").setCursor("pointer");
    }

    private void openConfirmDeleteDialog(User user) {
        editUserForm.setUser(user);
        confirmDeleteUserDialog.open();
    }

    private void closeConfirmDeleteDialog() {
        confirmDeleteUserDialog.close();
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
        createNewUserForm.setUser(new User());
        addClassName("creating");
    }
}
