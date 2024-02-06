package com.nva.server.views.user;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Role;
import com.nva.server.entities.User;
import com.nva.server.utils.CustomUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class EditUserForm extends UserForm {
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final Button deleteBtn = new Button("Delete");
    private final Checkbox isEnabled = new Checkbox("Status");
    private final ComboBox<Role> role = new ComboBox<>("Role");

    private final Span statusBadge = new Span("Status");
    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    public EditUserForm() {
        addClassName("edit-user-form");
        super.validate();

        configureField();
        getStatusBadge();

        add(
                super.getFirstName(),
                super.getLastName(),
                super.getEmail(),
                this.getShowedCreatedDate(),
                this.getShowedLastModifiedDate(),
                this.getRole(),
                this.statusBadge,
                this.createButtonLayout()
        );
    }

    private void getStatusBadge() {
        statusBadge.getElement().getThemeList().clear();
        statusBadge.getStyle().set("padding", "1em");
        statusBadge.getStyle().set("margin-top", "1em");

        updateStatusBadge();

        isEnabled.addValueChangeListener(event -> updateStatusBadge());
    }

    private void updateStatusBadge() {
        statusBadge.setText(isEnabled.getValue() ? "Active" : "Locked");
        statusBadge.getElement().getThemeList().clear();
        statusBadge.getElement().getThemeList().add(isEnabled.getValue() ? "badge success" : "badge error");
    }

    private void configureField() {
        super.getEmail().setReadOnly(true);

        role.setItems(Role.values());
        role.setItemLabelGenerator(role -> role == Role.ROLE_ADMIN ? "ADMIN" : "USER");
        role.setReadOnly(true);

        showedCreatedDate = new TextField("Created date");
        showedCreatedDate.setReadOnly(true);
        if (!createdDate.getValue().isEmpty()) {
            showedCreatedDate.setValue(
                    CustomUtils.convertMillisecondsToDate(
                            Long.parseLong(createdDate.getValue().replaceAll(",", "")),
                            CustomConstants.DATE_FORMAT_HH_MM_SS_DD_MM_YYYY));
        }
        createdDate.addValueChangeListener(e -> {
            if (!e.getValue().isEmpty())
                showedCreatedDate.setValue(
                        CustomUtils.convertMillisecondsToDate(
                                Long.parseLong(e.getValue().replaceAll(",", "")),
                                CustomConstants.DATE_FORMAT_HH_MM_SS_DD_MM_YYYY));
        });

        showedLastModifiedDate = new TextField("Last modified date");
        showedLastModifiedDate.setReadOnly(true);
        if (!lastModifiedDate.getValue().isEmpty()) {
            showedLastModifiedDate.setValue(
                    CustomUtils.convertMillisecondsToDate(
                            Long.parseLong(lastModifiedDate.getValue().replaceAll(",", "")),
                            CustomConstants.DATE_FORMAT_HH_MM_SS_DD_MM_YYYY));
        } else showedLastModifiedDate.setValue("Not edited");

        lastModifiedDate.addValueChangeListener(e -> {
            if (!e.getValue().isEmpty())
                showedLastModifiedDate.setValue(
                        CustomUtils.convertMillisecondsToDate(
                                Long.parseLong(e.getValue().replaceAll(",", "")),
                                CustomConstants.DATE_FORMAT_HH_MM_SS_DD_MM_YYYY));
            else showedLastModifiedDate.setValue("Not edited");
        });
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
