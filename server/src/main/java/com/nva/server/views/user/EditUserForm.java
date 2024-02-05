package com.nva.server.views.user;

import com.nva.server.entities.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import lombok.Getter;

@Getter
public class EditUserForm extends UserForm {
    private final Button deleteBtn = new Button("Delete");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField confirmPassword = new PasswordField("Confirm Password");

    public EditUserForm() {
        addClassName("edit-user-form");
        super.validation();

        initLayout();

        add(super.getFirstName(), super.getLastName(), super.getEmail(), this.createButtonLayout());
    }

    private void initLayout() {
        super.getEmail().setReadOnly(true);
    }

    @Override
    protected Component createButtonLayout() {
        super.createButtonLayout();

        this.deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteBtn.addClickListener(event -> super.fireEvent(new DeleteEvent(this, super.getUser())));

        return new HorizontalLayout(super.getSaveBtn(), this.deleteBtn, super.getCancelBtn());
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(EditUserForm source, User user) {
            super(source, user);
        }
    }
}
