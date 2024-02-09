package com.nva.server.views.topic;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Scope;
import com.nva.server.entities.Topic;
import com.nva.server.services.ScopeService;
import com.nva.server.services.TopicService;
import com.nva.server.utils.CustomUtils;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AccessDeniedErrorRouter;
import jakarta.annotation.security.RolesAllowed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PageTitle("Topic | Management")
@Route(value = "admin/topics", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class TopicView extends VerticalLayout {
    private final TopicService topicService;

    private final Grid<Topic> topicGrid = new Grid<>(Topic.class);
    private final TextField filterText = new TextField();
    private final Dialog editTopicDialog = new Dialog();
    private final Dialog createNewTopicDialog = new Dialog();
    private final Dialog confirmDeleteTopicDialog = new Dialog();
    private TopicForm editTopicForm;
    private TopicForm createNewTopicForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public TopicView(TopicService topicService) {
        this.topicService = topicService;

        addClassName("topic-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateTopicList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(topicGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditTopicDialog();
        configureCreateNewTopicDialog();
        configureDeleteTopicDialog();
    }

    private void configureDeleteTopicDialog() {
        confirmDeleteTopicDialog.setHeaderTitle("Delete topic");
        confirmDeleteTopicDialog.add(new Paragraph("Are you sure you want to delete this topic?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteTopicDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteTopic(editTopicForm.getTopic());
            closeConfirmDeleteTopicDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteTopicDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteTopicDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteTopicDialog);
    }

    private void configureCreateNewTopicDialog() {
        createNewTopicDialog.setHeaderTitle("Add new topic");
        createNewTopicDialog.getFooter().add(createNewTopicForm.getSaveButton(), createNewTopicForm.getCancelButton());

        add(createNewTopicDialog);
    }

    private void configureEditTopicDialog() {
        editTopicDialog.setHeaderTitle("Edit topic");
        editTopicForm.getDeleteButton().setVisible(false);
        editTopicDialog.getFooter().add(editTopicForm.getSaveButton(), editTopicForm.getCancelButton());

        add(editTopicDialog);
    }

    private void configureForms() {
        editTopicForm = new TopicForm();
        editTopicForm.setWidth("25em");
        editTopicForm.setVisible(true);
        editTopicForm.addListener(TopicForm.SaveEvent.class, e -> editTopic(e.getTopic()));
        editTopicForm.addListener(TopicForm.DeleteEvent.class, e -> deleteTopic(e.getTopic()));
        editTopicForm.addListener(TopicForm.CloseEvent.class, e -> closeEditTopicDialog());

        createNewTopicForm = new TopicForm();
        createNewTopicForm.setWidth("25em");
        createNewTopicForm.setVisible(true);
        createNewTopicForm.getDeleteButton().setVisible(false);
        createNewTopicForm.getShowedCreatedDate().setVisible(false);
        createNewTopicForm.getShowedLastModifiedDate().setVisible(false);
        createNewTopicForm.addListener(TopicForm.SaveEvent.class, e -> saveTopic(e.getTopic()));
        createNewTopicForm.addListener(TopicForm.CloseEvent.class, e -> closeCreateNewTopicDialog());
    }

    private void closeCreateNewTopicDialog() {
        createNewTopicDialog.close();
        createNewTopicForm.setTopic(null);
        removeClassName("topic-creating");
    }

    private void saveTopic(Topic topic) {
        try {
            topicService.saveTopic(topic);
            updateTopicList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewTopicDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteTopic(Topic topic) {
        try {
            topicService.removeTopic(topic);
            updateTopicList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditTopicDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editTopic(Topic topic) {
        try {
            topicService.editTopic(topic);
            updateTopicList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditTopicDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        topicGrid.addClassName("topic-grid");
        topicGrid.setSizeFull();
        topicGrid.setColumns("name", "description");
        topicGrid.addColumn(topic -> CustomUtils.convertMillisecondsToDate(topic.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        topicGrid.addColumn(topic -> (topic.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(topic.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        topicGrid.addColumn(topic -> (topic.getNote() == null || topic.getNote().isEmpty()) ? "--" : topic.getNote()).setHeader("Note");
        topicGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        topicGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, Topic topic) {
        menuBar.addItem("Edit", e -> openEditTopicDialog(topic)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteTopicDialog(topic)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteTopicDialog(Topic topic) {
        editTopicForm.setTopic(topic);
        confirmDeleteTopicDialog.open();
    }

    private void closeConfirmDeleteTopicDialog() {
        confirmDeleteTopicDialog.close();
    }

    private void openEditTopicDialog(Topic topic) {
        if (topic == null) closeEditTopicDialog();
        else {
            editTopicForm.setTopic(topic);
            editTopicDialog.add(new HorizontalLayout(editTopicForm));
            editTopicDialog.open();
            addClassName("topic-editing");
        }
    }

    private void closeEditTopicDialog() {
        editTopicDialog.close();
        editTopicForm.setTopic(null);
        removeClassName("topic-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateTopicList());

        Button addMajorButton = new Button("Add new topic");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewTopicDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateTopicList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totaTopicsCount = (int) topicService.getTopicCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totaTopicsCount / CustomConstants.TOPIC_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateTopicList();
            }
        });

        HorizontalLayout paginationController = new HorizontalLayout(paginationLabel, prevPageButton, nextPageButton);
        paginationController.setSpacing(true);

        HorizontalLayout toolbar = new HorizontalLayout(
                new HorizontalLayout(filterText, addMajorButton),
                paginationController
        );
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void openCreateNewTopicDialog() {
        createNewTopicDialog.add(new HorizontalLayout(createNewTopicForm));
        createNewTopicDialog.open();
        createNewTopicForm.setTopic(new Topic());
        addClassName("topic-creating");
    }

    private void updateTopicList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.TOPIC_PAGE_SIZE);
        params.put("keyword", keyword);

        List<Topic> topics = topicService.getTopics(params);
        long totalTopics = topicService.getTopicCount(params);

        topicGrid.setItems(topics);

        updatePaginationButtons();
        updatePaginationLabel(totalTopics);
    }

    private void updatePaginationLabel(long totalTopics) {
        int startTopic = pageNumber * CustomConstants.TOPIC_PAGE_SIZE + 1;
        int endTopic = (int) Math.min((long) (pageNumber + 1) * CustomConstants.TOPIC_PAGE_SIZE, totalTopics);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startTopic + " - " + endTopic + " out of " + totalTopics);
    }

    private void updatePaginationButtons() {
        long totalTopicsCount = topicService.getTopicCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalTopicsCount / CustomConstants.TOPIC_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
