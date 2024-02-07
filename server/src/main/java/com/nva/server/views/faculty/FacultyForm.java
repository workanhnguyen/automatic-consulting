package com.nva.server.views.faculty;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Faculty;
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
public class FacultyForm extends FormLayout {
    private final Binder<Faculty> facultyBinder = new BeanValidationBinder<>(Faculty.class);
    private Faculty faculty;

    // These fields must be similar to the entity fields
    private final TextField name = new TextField("Faculty name");
    private final TextArea note = new TextArea("Note");
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");

    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    public FacultyForm() {
        addClassName("edit-faculty-form");
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
    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
        facultyBinder.readBean(faculty);
    }


    private Component createButtonLayout() {
        this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.saveButton.addClickListener(e -> validateAndSave());
        this.saveButton.addClickShortcut(Key.ENTER);
        this.saveButton.getStyle().setCursor("pointer");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteButton.getStyle().setCursor("pointer");
        this.deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, getFaculty())));

        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.cancelButton.getStyle().setCursor("pointer");
        this.cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        this.cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(this.saveButton, this.deleteButton, this.cancelButton);
    }

    private void validateAndSave() {
        try {
            facultyBinder.writeBean(faculty);
            fireEvent(new SaveEvent(this, faculty));
        } catch (ValidationException e) {
            // log.error(e.getMessage());
        }
    }

    private void configureFields() {
        name.setRequired(true);
        name.setPlaceholder("Enter faculty name");

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
        facultyBinder.bindInstanceFields(this);
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class FacultyFormEvent extends ComponentEvent<FacultyForm> {
        private final Faculty faculty;

        protected FacultyFormEvent(FacultyForm source, Faculty faculty) {
            super(source, false);
            this.faculty = faculty;
        }
    }

    public static class SaveEvent extends FacultyForm.FacultyFormEvent {
        SaveEvent(FacultyForm source, Faculty faculty) {
            super(source, faculty);
        }
    }

    public static class CloseEvent extends FacultyForm.FacultyFormEvent {
        CloseEvent(FacultyForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends FacultyForm.FacultyFormEvent {
        DeleteEvent(FacultyForm source, Faculty faculty) {
            super(source, faculty);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
