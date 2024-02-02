package com.nva.server.views.user;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Getter
public class UserForm extends FormLayout {
    Binder<User> userBinder = new BeanValidationBinder<>(User.class);
    private User user;

    // These fields must be similar to the entity fields
    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    EmailField email = new EmailField("Email");
    PasswordField password = new PasswordField("Password");
    ComboBox<Role> role = new ComboBox<>("Role");

    Button saveBtn = new Button("Save");
    Button deleteBtn = new Button("Delete");
    Button cancelBtn = new Button("Cancel");


    public UserForm(List<Role> roles) {
        addClassName("user-form");
        userBinder.bindInstanceFields(this);

        role.setItems(roles);
        role.setItemLabelGenerator(Role::name);

        add(firstName, lastName, email, password, role, createButtonLayout());
    }

    public void setUser(User user) {
        this.user = user;
        userBinder.readBean(user);
    }

    private Component createButtonLayout() {
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveBtn.addClickListener(event -> validateAndSave());
        deleteBtn.addClickListener(event -> fireEvent(new DeleteEvent(this, user)));
        cancelBtn.addClickListener(event -> fireEvent(new CloseEvent(this)));

        saveBtn.addClickShortcut(Key.ENTER);
        cancelBtn.addClickShortcut(Key.ESCAPE);
        return new HorizontalLayout(saveBtn, deleteBtn, cancelBtn);
    }

    private void validateAndSave() {
        try {
            userBinder.writeBean(user);
            fireEvent(new SaveEvent(this, user));
        } catch (ValidationException e) {
            log.error(e.getMessage());
        }
    }

    // -----------------------------------------------
    // Events

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

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, User user) {
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
