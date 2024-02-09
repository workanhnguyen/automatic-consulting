package com.nva.server.views.entrance_method;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.EntranceMethod;
import com.nva.server.entities.EntranceMethodGroup;
import com.nva.server.services.EntranceMethodGroupService;
import com.nva.server.utils.CustomUtils;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class EntranceMethodForm extends FormLayout {
    private final EntranceMethodGroupService entranceMethodGroupService;
    private final Binder<EntranceMethod> entranceMethodBinder = new BeanValidationBinder<>(EntranceMethod.class);
    private EntranceMethod entranceMethod;

    // These fields must be similar to the entity fields
    private final TextArea name = new TextArea("Entrance method name");
    private final TextArea note = new TextArea("Note");
    private final ComboBox<EntranceMethodGroup> entranceMethodGroup = new ComboBox<>("Group belongs");
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");

    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    public EntranceMethodForm(EntranceMethodGroupService entranceMethodGroupService) {
        this.entranceMethodGroupService = entranceMethodGroupService;
        addClassName("entrance-method-form");
        validate();
        configureFields();

        add(
                this.getName(),
                this.getEntranceMethodGroup(),
                this.getNote(),
                this.getShowedCreatedDate(),
                this.getShowedLastModifiedDate(),
                createButtonLayout()
        );
    }
    public void setEntranceMethod(EntranceMethod entranceMethod) {
        this.entranceMethod = entranceMethod;
        entranceMethodBinder.readBean(entranceMethod);
    }


    private Component createButtonLayout() {
        this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.saveButton.addClickListener(e -> validateAndSave());
        this.saveButton.addClickShortcut(Key.ENTER);
        this.saveButton.getStyle().setCursor("pointer");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteButton.getStyle().setCursor("pointer");
        this.deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, getEntranceMethod())));

        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.cancelButton.getStyle().setCursor("pointer");
        this.cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        this.cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(this.saveButton, this.deleteButton, this.cancelButton);
    }

    private void validateAndSave() {
        try {
            entranceMethodBinder.writeBean(entranceMethod);
            fireEvent(new SaveEvent(this, entranceMethod));
        } catch (ValidationException e) {
            // log.error(e.getMessage());
        }
    }

    private void configureFields() {
        name.setPlaceholder("Enter entrance method name");
        name.setMaxHeight("10em");

        note.setMaxHeight("10em");
        note.setPlaceholder("Enter note");

        entranceMethodGroup.setItems(entranceMethodGroupService.getEntranceMethodGroups(new HashMap<>()));
        entranceMethodGroup.setItemLabelGenerator(EntranceMethodGroup::getName);
        entranceMethodGroup.setRequired(true);
        entranceMethodGroup.setPlaceholder("Choose 1 group");

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
        entranceMethodBinder.bindInstanceFields(this);
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class EntranceMethodFormEvent extends ComponentEvent<EntranceMethodForm> {
        private final EntranceMethod entranceMethod;

        protected EntranceMethodFormEvent(EntranceMethodForm source, EntranceMethod entranceMethod) {
            super(source, false);
            this.entranceMethod = entranceMethod;
        }
    }

    public static class SaveEvent extends EntranceMethodFormEvent {
        SaveEvent(EntranceMethodForm source, EntranceMethod entranceMethod) {
            super(source, entranceMethod);
        }
    }

    public static class CloseEvent extends EntranceMethodFormEvent {
        CloseEvent(EntranceMethodForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends EntranceMethodFormEvent {
        DeleteEvent(EntranceMethodForm source, EntranceMethod entranceMethod) {
            super(source, entranceMethod);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
