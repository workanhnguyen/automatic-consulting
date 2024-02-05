package com.nva.server.views.user;

import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import lombok.Getter;

@Getter
public class EditUserForm extends UserForm {
    private final Button deleteBtn = new Button("Delete");
    private final PasswordField password = new PasswordField("Password");
    private final PasswordField confirmPassword = new PasswordField("Confirm Password");
    private final Checkbox isEnabled = new Checkbox();
    private final ComboBox<Role> role = new ComboBox<>("Role");

    public EditUserForm() {
        addClassName("edit-user-form");
        super.validation();

        configureField();

        add(
                super.getFirstName(),
                super.getLastName(),
                super.getEmail(),
                this.getRole(),
                this.getStatusBadge(),
                this.createButtonLayout()
        );
    }

    private Component getStatusBadge() {
        Span statusBadge = new Span();

        if (isEnabled.getValue()) {
            statusBadge.add("Active");
            statusBadge.getElement().getThemeList().add("badge success");
        } else {
            statusBadge.add("Locked");
            statusBadge.getElement().getThemeList().add("badge error");
        }

        statusBadge.getStyle().set("padding", "1em");  // Adjust the value as needed
        statusBadge.getStyle().set("margin-top", "1em");

        return statusBadge;
    }

    private void configureField() {
        super.getEmail().setReadOnly(true);

        role.setItems(Role.values());
        role.setItemLabelGenerator(role -> role == Role.ROLE_ADMIN ? "ADMIN" : "USER");
        role.setReadOnly(true);
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
