package com.nva.server.views.chat_conversation;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Conversation;
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
public class ConversationForm extends FormLayout {
    private final Binder<Conversation> conversationBinder = new BeanValidationBinder<>(Conversation.class);
    private Conversation conversation;

    // These fields must be similar to the entity fields
    private final TextArea requestText = new TextArea("Question");
    private final TextArea responseText = new TextArea("Answer");
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");

    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    public ConversationForm() {

        addClassName("conversation-form");
        validate();
        configureFields();

        add(
                this.getRequestText(),
                this.getResponseText(),
                this.getShowedCreatedDate(),
                this.getShowedLastModifiedDate(),
                createButtonLayout()
        );
    }
    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        conversationBinder.readBean(conversation);
    }


    private Component createButtonLayout() {
        this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.saveButton.addClickListener(e -> validateAndSave());
        this.saveButton.addClickShortcut(Key.ENTER);
        this.saveButton.getStyle().setCursor("pointer");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteButton.getStyle().setCursor("pointer");
        this.deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, getConversation())));

        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.cancelButton.getStyle().setCursor("pointer");
        this.cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        this.cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(this.saveButton, this.deleteButton, this.cancelButton);
    }

    private void validateAndSave() {
        try {
            conversationBinder.writeBean(conversation);
            fireEvent(new SaveEvent(this, conversation));
        } catch (ValidationException e) {
            // log.error(e.getMessage());
        }
    }

    private void configureFields() {
        requestText.setPlaceholder("Enter question");
        requestText.setMaxHeight("10em");

        responseText.setMaxHeight("10em");
        responseText.setPlaceholder("Enter answer");

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
        conversationBinder.bindInstanceFields(this);
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class ConversationFormEvent extends ComponentEvent<ConversationForm> {
        private final Conversation conversation;

        protected ConversationFormEvent(ConversationForm source, Conversation conversation) {
            super(source, false);
            this.conversation = conversation;
        }
    }

    public static class SaveEvent extends ConversationFormEvent {
        SaveEvent(ConversationForm source, Conversation conversation) {
            super(source, conversation);
        }
    }

    public static class CloseEvent extends ConversationFormEvent {
        CloseEvent(ConversationForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends ConversationFormEvent {
        DeleteEvent(ConversationForm source, Conversation conversation) {
            super(source, conversation);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
