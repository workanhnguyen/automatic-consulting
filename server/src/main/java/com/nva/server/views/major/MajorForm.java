package com.nva.server.views.major;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Faculty;
import com.nva.server.entities.Major;
import com.nva.server.services.FacultyService;
import com.nva.server.services.impl.FacultyServiceImpl;
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

import java.util.List;

@Getter
public class MajorForm extends FormLayout {
    private final FacultyService facultyService = new FacultyServiceImpl();

    private final Binder<Major> majorBinder = new BeanValidationBinder<>(Major.class);
    private Major major;

    // These fields must be similar to the entity fields
    private final TextField name = new TextField("Major name");
    private final TextArea note = new TextArea("Note");
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final ComboBox<Faculty> faculty = new ComboBox<>("Faculty");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");

    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    private final List<Faculty> faculties;

    public MajorForm(List<Faculty> faculties) {
        this.faculties = faculties;

        addClassName("edit-major-form");
        validate();
        configureFields();

        add(
                this.getName(),
                this.getFaculty(),
                this.getNote(),
                this.getShowedCreatedDate(),
                this.getShowedLastModifiedDate(),
                createButtonLayout()
        );
    }
    public void setMajor(Major major) {
        this.major = major;
        majorBinder.readBean(major);
    }


    private Component createButtonLayout() {
        this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.saveButton.addClickListener(e -> validateAndSave());
        this.saveButton.addClickShortcut(Key.ENTER);
        this.saveButton.getStyle().setCursor("pointer");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteButton.getStyle().setCursor("pointer");
        this.deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, getMajor())));

        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.cancelButton.getStyle().setCursor("pointer");
        this.cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        this.cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(this.saveButton, this.deleteButton, this.cancelButton);
    }

    private void validateAndSave() {
        try {
            majorBinder.writeBean(major);
            fireEvent(new SaveEvent(this, major));
        } catch (ValidationException e) {
            // log.error(e.getMessage());
        }
    }

    private void configureFields() {
        name.setRequired(true);
        name.setPlaceholder("Enter major name");

        faculty.setItems(faculties);
        faculty.setItemLabelGenerator(Faculty::getName);
        faculty.setRequired(true);
        faculty.setPlaceholder("Choose 1 faculty");

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
        majorBinder.bindInstanceFields(this);
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class MajorFormEvent extends ComponentEvent<MajorForm> {
        private final Major major;

        protected MajorFormEvent(MajorForm source, Major major) {
            super(source, false);
            this.major = major;
        }
    }

    public static class SaveEvent extends MajorFormEvent {
        SaveEvent(MajorForm source, Major major) {
            super(source, major);
        }
    }

    public static class CloseEvent extends MajorFormEvent {
        CloseEvent(MajorForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends MajorFormEvent {
        DeleteEvent(MajorForm source, Major major) {
            super(source, major);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
