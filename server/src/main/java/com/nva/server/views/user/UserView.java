package com.nva.server.views.user;

import com.nva.server.constants.CustomConstants;
import com.nva.server.dtos.ChangePasswordDto;
import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.services.UserService;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Dialog changePasswordDialog = new Dialog();

    private final UserForm editUserForm = new EditUserForm();
    private final UserForm createNewUserForm = new CreateNewUserForm();
    private final ChangePasswordForm changePasswordForm = new ChangePasswordForm();

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

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
        configureChangePasswordDialog();
    }

    private void configureChangePasswordDialog() {
        changePasswordDialog.setHeaderTitle("Change Password");
        changePasswordForm.getSaveBtn().getStyle().setCursor("pointer");
        changePasswordForm.getSaveBtn().setText("Change password");
        changePasswordForm.getCancelBtn().getStyle().setCursor("pointer");

        changePasswordDialog.getFooter().add(changePasswordForm.getSaveBtn(), changePasswordForm.getCancelBtn());

        add(changePasswordDialog);
    }

    private void configureCreateNewUserDialog() {
        createNewUserDialog.setHeaderTitle("Add new user");
        createNewUserForm.getSaveBtn().getStyle().setCursor("pointer");
        createNewUserForm.getCancelBtn().getStyle().setCursor("pointer");

        createNewUserDialog.getFooter().add(createNewUserForm.getSaveBtn(), createNewUserForm.getCancelBtn());

        add(createNewUserDialog);
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
        Paragraph subtitle = new Paragraph("All data related to this user are also deleted! Do you want to continue?");
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
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.USER_PAGE_SIZE);
        params.put("keyword", keyword);

        List<User> users = userService.getUsers(params);
        long totalUsers = userService.getUserCount(params);

        userGrid.setItems(users);

        updatePaginationButtons();
        updatePaginationLabel(totalUsers);
    }

    private void updatePaginationButtons() {
        // Enable/disable "Previous" button
        prevPageButton.setEnabled(pageNumber > 0);

        // Enable/disable "Next" button
        long totalUsersCount = userService.countUsers();
        int totalPages = (int) Math.ceil((double) totalUsersCount / CustomConstants.USER_PAGE_SIZE);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }

    private void updatePaginationLabel(long totalUsers) {
        int startUser =  pageNumber * CustomConstants.USER_PAGE_SIZE + 1;
        int endUser = (int) Math.min((long) (pageNumber + 1) * CustomConstants.USER_PAGE_SIZE, totalUsers);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startUser + " - " + endUser + " out of " + totalUsers);
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

        changePasswordForm.setWidth("25em");
        changePasswordForm.setVisible(true);
        changePasswordForm.addListener(ChangePasswordForm.SaveEvent.class, e -> changePassword(e.getChangePasswordDto()));
        changePasswordForm.addListener(ChangePasswordForm.CloseEvent.class, e -> closeChangePasswordDialog());
    }

    private void changePassword(ChangePasswordDto changePasswordDto) {
        try {
            userService.changePassword(changePasswordForm.getUser().getEmail(), changePasswordDto);
            updateUserList();
            CustomNotification.showNotification("Password has been changed successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeChangePasswordDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
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

        // Column used for foreign keys
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
        String lockText = user.getIsEnabled() ? "Lock user" : "Unlock user";

        menuBar.addItem("Edit", e -> openEditor(user)).getStyle().setCursor("pointer");
        menuBar.addItem(lockText, e -> toggleLockUser(user)).getStyle().setCursor("pointer");
        menuBar.addItem("Change password", e -> openChangePasswordDialog(user)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteDialog(user)).getStyle().setColor("red").setCursor("pointer");
    }

    private void openChangePasswordDialog(User user) {
        changePasswordDialog.add(new HorizontalLayout(changePasswordForm));
        changePasswordDialog.open();
        changePasswordForm.setChangePasswordData(user, new ChangePasswordDto());
        addClassName("changing-password");
    }

    private void closeChangePasswordDialog() {
        changePasswordDialog.close();
        removeClassName("changing-password");
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

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateUserList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totalUsersCount = (int) userService.countUsers(); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totalUsersCount / CustomConstants.USER_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateUserList();
            }
        });

        HorizontalLayout paginationController = new HorizontalLayout(paginationLabel, prevPageButton, nextPageButton);
        paginationController.setSpacing(true);

        HorizontalLayout toolbar = new HorizontalLayout(
                new HorizontalLayout(filterText, addUserBtn),
                paginationController
        );
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);

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
