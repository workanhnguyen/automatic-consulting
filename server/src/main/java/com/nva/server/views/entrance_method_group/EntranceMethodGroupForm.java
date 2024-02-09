package com.nva.server.views.entrance_method_group;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.EntranceMethodGroup;
import com.nva.server.utils.CustomUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

@Getter
public class EntranceMethodGroupForm extends FormLayout {
    private final Binder<EntranceMethodGroup> entranceMethodGroupBinder = new BeanValidationBinder<>(EntranceMethodGroup.class);
    private EntranceMethodGroup entranceMethodGroup;

    // These fields must be similar to the entity fields
    private final TextField name = new TextField("Entrance method group name");
    private final TextArea note = new TextArea("Note");
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");

    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    public EntranceMethodGroupForm() {
        addClassName("entrance-method-group-form");
        validate();
        configureFields();

        add(
                this.getName(),
                this.getNote(),
                this.getShowedCreatedDate(),
                this.getShowedLastModifiedDate(),
                createButtonLayout()
        );
    }
    public void setEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup) {
        this.entranceMethodGroup = entranceMethodGroup;
        entranceMethodGroupBinder.readBean(entranceMethodGroup);
    }


    private Component createButtonLayout() {
        this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.saveButton.addClickListener(e -> validateAndSave());
        this.saveButton.addClickShortcut(Key.ENTER);
        this.saveButton.getStyle().setCursor("pointer");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteButton.getStyle().setCursor("pointer");
        this.deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, getEntranceMethodGroup())));

        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.cancelButton.getStyle().setCursor("pointer");
        this.cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        this.cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(this.saveButton, this.deleteButton, this.cancelButton);
    }

    private void validateAndSave() {
        try {
            entranceMethodGroupBinder.writeBean(entranceMethodGroup);
            fireEvent(new SaveEvent(this, entranceMethodGroup));
        } catch (ValidationException e) {
            // log.error(e.getMessage());
        }
    }

    private void configureFields() {
        name.setPlaceholder("Enter entrance method group name");

        note.setMaxHeight("10em");
        note.setPlaceholder("Enter note");

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
        } else showedLastModifiedDate.setValue("--");

        lastModifiedDate.addValueChangeListener(e -> {
            if (!e.getValue().isEmpty())
                showedLastModifiedDate.setValue(
                        CustomUtils.convertMillisecondsToDate(
                                Long.parseLong(e.getValue().replaceAll(",", "")),
                                CustomConstants.DATE_FORMAT_HH_MM_SS_DD_MM_YYYY));
            else showedLastModifiedDate.setValue("--");
        });
    }

    private void validate() {
        entranceMethodGroupBinder.bindInstanceFields(this);
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class EntranceMethodGroupFormEvent extends ComponentEvent<EntranceMethodGroupForm> {
        private final EntranceMethodGroup entranceMethodGroup;

        protected EntranceMethodGroupFormEvent(EntranceMethodGroupForm source, EntranceMethodGroup entranceMethodGroup) {
            super(source, false);
            this.entranceMethodGroup = entranceMethodGroup;
        }
    }

    public static class SaveEvent extends EntranceMethodGroupFormEvent {
        SaveEvent(EntranceMethodGroupForm source, EntranceMethodGroup entranceMethodGroup) {
            super(source, entranceMethodGroup);
        }
    }

    public static class CloseEvent extends EntranceMethodGroupFormEvent {
        CloseEvent(EntranceMethodGroupForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends EntranceMethodGroupFormEvent {
        DeleteEvent(EntranceMethodGroupForm source, EntranceMethodGroup entranceMethodGroup) {
            super(source, entranceMethodGroup);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
