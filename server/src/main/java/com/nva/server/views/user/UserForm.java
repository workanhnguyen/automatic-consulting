package com.nva.server.views.user;

import com.nva.server.entities.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
    }

    protected void clearForm() {
        setUser(null);
        showedAvatar.setImage("");
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
        saveBtn.getStyle().setCursor(Cursor.POINTER.name());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelBtn.getStyle().setCursor(Cursor.POINTER.name());

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
