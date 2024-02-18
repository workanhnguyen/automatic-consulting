package com.nva.server.views.user;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.User;
import com.nva.server.utils.CustomUtils;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Getter
public abstract class UserForm extends FormLayout {
    private final Binder<User> userBinder = new BeanValidationBinder<>(User.class);
    private User user;

    // These fields must be similar to the entity fields
    private final TextField firstName = new TextField("First name");
    private final TextField lastName = new TextField("Last name");
    private final TextField email = new TextField("Email");
    private final TextField avatarLink = new TextField();

    private final Button saveBtn = new Button("Save");
    private final Button cancelBtn = new Button("Cancel");

    private final Avatar showedAvatar = new Avatar();
    MemoryBuffer buffer = new MemoryBuffer();
    private final Upload avatarUpload = new Upload(buffer);

    public UserForm() {
        configureFields();
    }

    private void configureFields() {
        firstName.setPlaceholder("Enter your first name");
        firstName.setWidthFull();

        lastName.setPlaceholder("Enter your last name");
        lastName.setWidthFull();

        email.setPlaceholder("Enter your email address");
        email.setWidthFull();

        showedAvatar.setWidth("100px");
        showedAvatar.setHeight("100px");

        if (!avatarLink.getValue().isEmpty()) {
            showedAvatar.setImage(avatarLink.getValue());
        };

        avatarLink.addValueChangeListener(e -> {
            if (!e.getValue().isEmpty())
                showedAvatar.setImage(avatarLink.getValue());;
        });

        configureAvatarUpload();
    }

    protected void clearForm() {
        setUser(null);
        showedAvatar.setImage("");
        avatarUpload.getElement().setPropertyJson("files", Json.createArray());
    }

    private void configureAvatarUpload() {
        avatarUpload.setAcceptedFileTypes(".jpg", ".png", ".jpeg");
        avatarUpload.setMaxFileSize(CustomConstants.MAX_SIZE_FILE_UPLOAD);
        avatarUpload.addSucceededListener(event -> {
            InputStream fileData = buffer.getInputStream();
            try {
                String fileType = event.getMIMEType().split("/")[1]; // Extracting file type from MIME type
                String base64Image = "data:image/" + fileType + ";base64," + CustomUtils.encodeInputStreamToBase64Binary(fileData);
                showedAvatar.setImage(base64Image);
            } catch (IOException ex) {
                CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
            }
        });

        avatarUpload.addFileRejectedListener(event -> {
            String errorMessage = event.getErrorMessage();
            CustomNotification.showNotification(errorMessage, "error", Notification.Position.TOP_CENTER, 3000);
        });
    }

    protected void validate() {
        userBinder.bindInstanceFields(this);
    }

    public void setUser(User user) {
        this.user = user;
        userBinder.readBean(user);
    }

    protected Component createButtonLayout() {
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveBtn.addClickListener(event -> validateAndSave());
        cancelBtn.addClickListener(event -> fireEvent(new CloseEvent(this)));

        saveBtn.addClickShortcut(Key.ENTER);
        cancelBtn.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(saveBtn, cancelBtn);
    }

    protected void validateAndSave() {
        try {
            userBinder.writeBean(user);
            fireEvent(new SaveEvent(this, user));
        } catch (ValidationException ignored) {}
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private final User user;

        protected UserFormEvent(UserForm source, User user) {
            super(source, false);
            this.user = user;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, User user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
