package com.nva.server.views.user;

import com.nva.server.constants.CustomConstants;
import com.nva.server.dtos.ChangePasswordDto;
import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.services.UserService;
import com.nva.server.utils.CustomUtils;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AccessDeniedErrorRouter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import elemental.json.Json;
import jakarta.annotation.security.RolesAllowed;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringComponent
@Scope("prototype")
@PageTitle("User | Management")
@Route(value = "admin/users", layout = MainLayout.class)
@RouteAlias(value = "admin", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter()
@Getter
@Slf4j
public class UserView extends VerticalLayout {
    private final UserService userService;

    private final Grid<User> userGrid = new Grid<>(User.class, false);
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

    MemoryBuffer buffer = new MemoryBuffer();
    private final Upload avatarUpload = new Upload(buffer);

    public UserView(UserService userService) {
        this.userService = userService;

        addClassName("user-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureDialog();
        configureAvatarUpload();

        add(getToolbar(), getContent());

        updateUserList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(userGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureAvatarUpload() {
        avatarUpload.setAcceptedFileTypes(".jpg", ".png", ".jpeg");
        avatarUpload.setMaxFileSize(CustomConstants.MAX_SIZE_FILE_UPLOAD);
        avatarUpload.addSucceededListener(this::updateUploadedAvatar);

        avatarUpload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            CustomNotification.showNotification(errorMessage, "error", Notification.Position.TOP_CENTER, 3000);
        });
    }

    protected void updateUploadedAvatar(SucceededEvent event) {
        InputStream fileData = buffer.getInputStream();
        try {
            String fileType = event.getMIMEType().split("/")[1]; // Extracting file type from MIME type
            String base64Image = "data:image/" + fileType + ";base64," + CustomUtils.encodeInputStreamToBase64Binary(fileData);
            editUserForm.getShowedAvatar().setImage(base64Image);
            editUserForm.getUser().setAvatarBase64(base64Image);

            fileData.close();
        } catch (IOException ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
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
        editUserForm.getStyle().setWidth("25em");

        VerticalLayout avatarLayout = new VerticalLayout(editUserForm.getShowedAvatar(), avatarUpload);
        avatarLayout.setPadding(false);
        avatarLayout.getStyle().setPaddingTop("16px");
        avatarLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.add(avatarLayout, editUserForm);
        dialogLayout.setPadding(false);

        editUserDialog.add(dialogLayout);
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
        editUserForm.clearForm();
        avatarUpload.getElement().setPropertyJson("files", Json.createArray());
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
        int startUser = pageNumber * CustomConstants.USER_PAGE_SIZE + 1;
        int endUser = (int) Math.min((long) (pageNumber + 1) * CustomConstants.USER_PAGE_SIZE, totalUsers);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startUser + " - " + endUser + " out of " + totalUsers);
    }

    private void configureForm() {
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
        userGrid.addColumn(createUserRenderer()).setHeader("User");

        // Column used for foreign keys
        userGrid.addColumn(user -> user.getRole().equals(Role.ROLE_ADMIN) ? "ADMIN" : "USER").setHeader("Role");
        userGrid.addColumn(createStatusComponentRenderer()).setHeader("Status");
        userGrid.addColumn(createMenubarComponentRenderer()).setHeader("Actions");
        userGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private ComponentRenderer<MenuBar, User> createMenubarComponentRenderer() {
        return new ComponentRenderer<>(MenuBar::new, (menuBar, user) -> {
            String lockText = user.getIsEnabled() ? "Lock user" : "Unlock user";
            MenuItem menuItem = menuBar.addItem(LineAwesomeIcon.ELLIPSIS_H_SOLID.create());

            SubMenu subMenu = menuItem.getSubMenu();

            subMenu.addItem("Edit", e -> openEditor(user)).getStyle().setCursor("pointer");
            subMenu.addItem(lockText, e -> toggleLockUser(user)).getStyle().setCursor("pointer");
            subMenu.addItem("Change password", e -> openChangePasswordDialog(user)).getStyle().setCursor("pointer");
            subMenu.addItem("Delete", e -> openConfirmDeleteDialog(user)).getStyle().setColor("red").setCursor("pointer");
        });
    }

    private ComponentRenderer<Span, User> createStatusComponentRenderer() {
        return new ComponentRenderer<>(Span::new, (badge, user) -> {
            if (user.isEnabled()) {
                badge.add("Active");
                badge.getElement().getThemeList().add("badge success");
            } else {
                badge.add("Locked");
                badge.getElement().getThemeList().add("badge error");
            }
        });
    }

    private Renderer<User> createUserRenderer() {
        return LitRenderer.<User>of(
                        "<vaadin-horizontal-layout style=\"align-items: center;\" theme=\"spacing\">"
                                + "<vaadin-avatar img=\"${item.avatarLink}\" name=\"${item.email}\" alt=\"${item.email}-avatar\"></vaadin-avatar>"
                                + "  <vaadin-vertical-layout style=\"line-height: var(--lumo-line-height-m);\">"
                                + "    <span> ${item.lastName} ${item.firstName}</span>"
                                + "    <span style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">"
                                + "      ${item.email}" + "    </span>"
                                + "  </vaadin-vertical-layout>"
                                + "</vaadin-horizontal-layout>")
                .withProperty("avatarLink", User::getAvatarLink)
                .withProperty("firstName", User::getFirstName)
                .withProperty("lastName", User::getLastName)
                .withProperty("email", User::getEmail);
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

    public void openEditor(User user) {
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
