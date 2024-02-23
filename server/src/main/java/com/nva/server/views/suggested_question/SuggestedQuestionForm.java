package com.nva.server.views.suggested_question;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.SuggestedQuestion;
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
public class SuggestedQuestionForm extends FormLayout {
    private final Binder<SuggestedQuestion> suggestedQuestionBinder = new BeanValidationBinder<>(SuggestedQuestion.class);
    private SuggestedQuestion suggestedQuestion;

    // These fields must be similar to the entity fields
    private final TextArea content = new TextArea("Content");
    private final TextArea note = new TextArea("Note");
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");

    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    public SuggestedQuestionForm() {
        addClassName("suggested-question-form");
        validate();
        configureFields();

        add(
                this.getContent(),
                this.getNote(),
                this.getShowedCreatedDate(),
                this.getShowedLastModifiedDate(),
                createButtonLayout()
        );
    }
    public void setSuggestedQuestion(SuggestedQuestion suggestedQuestion) {
        this.suggestedQuestion = suggestedQuestion;
        suggestedQuestionBinder.readBean(suggestedQuestion);
    }


    private Component createButtonLayout() {
        this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.saveButton.addClickListener(e -> validateAndSave());
        this.saveButton.addClickShortcut(Key.ENTER);
        this.saveButton.getStyle().setCursor("pointer");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteButton.getStyle().setCursor("pointer");
        this.deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, getSuggestedQuestion())));

        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.cancelButton.getStyle().setCursor("pointer");
        this.cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        this.cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(this.saveButton, this.deleteButton, this.cancelButton);
    }

    private void validateAndSave() {
        try {
            suggestedQuestionBinder.writeBean(suggestedQuestion);
            fireEvent(new SaveEvent(this, suggestedQuestion));
        } catch (ValidationException e) {
            // log.error(e.getMessage());
        }
    }

    private void configureFields() {
        content.setPlaceholder("Enter content");
        content.setMaxHeight("10em");

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
        suggestedQuestionBinder.bindInstanceFields(this);
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class SuggestedQuestionFormEvent extends ComponentEvent<SuggestedQuestionForm> {
        private final SuggestedQuestion suggestedQuestion;

        protected SuggestedQuestionFormEvent(SuggestedQuestionForm source, SuggestedQuestion suggestedQuestion) {
            super(source, false);
            this.suggestedQuestion = suggestedQuestion;
        }
    }

    public static class SaveEvent extends SuggestedQuestionFormEvent {
        SaveEvent(SuggestedQuestionForm source, SuggestedQuestion suggestedQuestion) {
            super(source, suggestedQuestion);
        }
    }

    public static class CloseEvent extends SuggestedQuestionFormEvent {
        CloseEvent(SuggestedQuestionForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends SuggestedQuestionFormEvent {
        DeleteEvent(SuggestedQuestionForm source, SuggestedQuestion suggestedQuestion) {
            super(source, suggestedQuestion);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
