package com.nva.server.views.user;

import com.nva.server.dtos.ChangePasswordDto;
import com.nva.server.entities.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

@Getter
public class ChangePasswordForm extends FormLayout {
    private final Binder<ChangePasswordDto> changePasswordDtoBinder = new BeanValidationBinder<>(ChangePasswordDto.class);
    private ChangePasswordDto changePasswordDto;
    private User user;

    private final PasswordField password = new PasswordField("Old Password");
    private final PasswordField newPassword = new PasswordField("New Password");
    private final PasswordField confirmNewPassword = new PasswordField("Confirm New password");

    private final Button saveBtn = new Button("Save");
    private final Button cancelBtn = new Button("Cancel");

    public ChangePasswordForm() {
        validate();

        addClassName("change-password-form");
        configureFields();

        add(this.password, this.newPassword, this.confirmNewPassword, this.createButtonLayout());
    }

    public void setChangePasswordData(User user, ChangePasswordDto changePasswordDto) {
        this.changePasswordDto = changePasswordDto;
        this.user = user;
        changePasswordDtoBinder.readBean(changePasswordDto);
    }

    private void validate() {
        changePasswordDtoBinder.bindInstanceFields(this);
    }

    private void configureFields() {
        password.setPlaceholder("Enter your current password");
        newPassword.setPlaceholder("Enter your new password");
        confirmNewPassword.setPlaceholder("Enter confirm password");
    }

    protected Component createButtonLayout()
    {
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveBtn.addClickListener(event -> validateAndSave());
        cancelBtn.addClickListener(event -> fireEvent(new ChangePasswordForm.CloseEvent(this)));

        saveBtn.addClickShortcut(Key.ENTER);
        cancelBtn.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(saveBtn, cancelBtn);
    }

    private void validateAndSave() {
        try {
            if (!newPassword.getValue().equals(confirmNewPassword.getValue())) {
                confirmNewPassword.setInvalid(true);
                confirmNewPassword.setErrorMessage("Confirm new password do not match.");
                return;
            }

            changePasswordDtoBinder.writeBean(changePasswordDto);
            fireEvent(new SaveEvent(this, changePasswordDto));
        } catch (ValidationException ignored) {}
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class ChangePasswordFormEvent extends ComponentEvent<ChangePasswordForm> {
        private final ChangePasswordDto changePasswordDto;

        protected ChangePasswordFormEvent(ChangePasswordForm source, ChangePasswordDto changePasswordDto) {
            super(source, false);
            this.changePasswordDto = changePasswordDto;
        }
    }

    public static class SaveEvent extends ChangePasswordForm.ChangePasswordFormEvent {
        SaveEvent(ChangePasswordForm source, ChangePasswordDto changePasswordDto) {
            super(source, changePasswordDto);
        }
    }

    public static class CloseEvent extends ChangePasswordForm.ChangePasswordFormEvent {
        CloseEvent(ChangePasswordForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
