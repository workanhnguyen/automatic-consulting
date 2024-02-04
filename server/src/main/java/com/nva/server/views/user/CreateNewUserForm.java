package com.nva.server.views.user;

import com.vaadin.flow.component.textfield.PasswordField;
import lombok.Getter;

@Getter
public class CreateNewUserForm extends UserForm {
    PasswordField password = new PasswordField("Password");

    public CreateNewUserForm() {
        addClassName("create-new-user-form");
        super.validation();

        add(super.getFirstName(), super.getLastName(), super.getEmail(), this.password, super.createButtonLayout());
    }
}
