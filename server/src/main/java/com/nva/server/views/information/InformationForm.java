package com.nva.server.views.information;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Action;
import com.nva.server.entities.Information;
import com.nva.server.entities.Scope;
import com.nva.server.entities.Topic;
import com.nva.server.services.ActionService;
import com.nva.server.services.ScopeService;
import com.nva.server.services.TopicService;
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
import org.springframework.data.domain.Sort;

import java.util.HashMap;

@Getter
public class InformationForm extends FormLayout {
    private final ActionService actionService;
    private final ScopeService scopeService;
    private final TopicService topicService;
    private final Binder<Information> informationBinder = new BeanValidationBinder<>(Information.class);
    private Information information;

    // These fields must be similar to the entity fields
    private final ComboBox<Action> action = new ComboBox<>("Action");
    private final ComboBox<Scope> scope = new ComboBox<>("Scope");
    private final ComboBox<Topic> topic = new ComboBox<>("Topic");
    private final TextArea content = new TextArea("Content");
    private final TextArea note = new TextArea("Note");
    private final TextField createdDate = new TextField("Created date");
    private final TextField lastModifiedDate = new TextField("Last modified date");
    private final Button saveButton = new Button("Save");
    private final Button cancelButton = new Button("Cancel");
    private final Button deleteButton = new Button("Delete");

    private TextField showedCreatedDate;
    private TextField showedLastModifiedDate;

    public InformationForm(ActionService actionService, ScopeService scopeService, TopicService topicService) {
        this.actionService = actionService;
        this.scopeService = scopeService;
        this.topicService = topicService;

        addClassName("information-form");
        validate();
        configureFields();

        add(
                this.getAction(),
                this.getScope(),
                this.getTopic(),
                this.getContent(),
                this.getNote(),
                this.getShowedCreatedDate(),
                this.getShowedLastModifiedDate(),
                createButtonLayout()
        );
    }
    public void setInformation(Information information) {
        this.information = information;
        informationBinder.readBean(information);
    }


    private Component createButtonLayout() {
        this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.saveButton.addClickListener(e -> validateAndSave());
        this.saveButton.addClickShortcut(Key.ENTER);
        this.saveButton.getStyle().setCursor("pointer");

        this.deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        this.deleteButton.getStyle().setCursor("pointer");
        this.deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, getInformation())));

        this.cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        this.cancelButton.getStyle().setCursor("pointer");
        this.cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
        this.cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(this.saveButton, this.deleteButton, this.cancelButton);
    }

    private void validateAndSave() {
        try {
            informationBinder.writeBean(information);
            fireEvent(new SaveEvent(this, information));
        } catch (ValidationException e) {
            // log.error(e.getMessage());
        }
    }

    private void configureFields() {
        content.setPlaceholder("Enter content");
        content.setMaxHeight("10em");

        action.setPlaceholder("Choose 1 action");
        action.setItems(actionService.getActions(new HashMap<>()));
        action.setItemLabelGenerator(a -> String.format("%s - %s", a.getName(), a.getDescription()));

        scope.setPlaceholder("Choose 1 scope");
        scope.setItems(scopeService.getScopes(new HashMap<>()));
        scope.setItemLabelGenerator(s -> String.format("%s - %s", s.getName(), s.getDescription()));

        topic.setPlaceholder("Choose 1 topic");
        topic.setItems(topicService.getAllTopics(Sort.by(Sort.Direction.ASC, "name")));
        topic.setItemLabelGenerator(t -> String.format("%s - %s", t.getName(), t.getDescription()));

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
        informationBinder.bindInstanceFields(this);
    }

    // ---------------------- Events -------------------------
    @Getter
    public static abstract class InformationFormEvent extends ComponentEvent<InformationForm> {
        private final Information information;

        protected InformationFormEvent(InformationForm source, Information information) {
            super(source, false);
            this.information = information;
        }
    }

    public static class SaveEvent extends InformationFormEvent {
        SaveEvent(InformationForm source, Information information) {
            super(source, information);
        }
    }

    public static class CloseEvent extends InformationFormEvent {
        CloseEvent(InformationForm source) {
            super(source, null);
        }
    }

    public static class DeleteEvent extends InformationFormEvent {
        DeleteEvent(InformationForm source, Information information) {
            super(source, information);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
