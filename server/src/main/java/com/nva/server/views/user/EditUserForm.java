package com.nva.server.views.user;

import com.nva.server.entities.User;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;

@Getter
public class EditUserForm extends UserForm {
    private final Button deleteBtn = new Button("Delete");

    public EditUserForm() {
        addClassName("edit-user-form");
        super.validation();

        add(super.getFirstName(), super.getLastName(), super.getEmail(), this.createButtonLayout());
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
