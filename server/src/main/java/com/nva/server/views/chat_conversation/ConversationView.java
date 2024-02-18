package com.nva.server.views.chat_conversation;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Conversation;
import com.nva.server.services.ConversationService;
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

@PageTitle("Conversation | Management")
@Route(value = "admin/conversation", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class ConversationView extends VerticalLayout {
    private final ConversationService conversationService;

    private final Grid<Conversation> conversationGrid = new Grid<>(Conversation.class);
    private final TextField filterText = new TextField();
    private final Dialog editConversationDialog = new Dialog();
    private final Dialog createNewConversationDialog = new Dialog();
    private final Dialog confirmDeleteConversationDialog = new Dialog();
    private ConversationForm editConversationForm;
    private ConversationForm createNewConversationForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public ConversationView(ConversationService conversationService) {
        this.conversationService = conversationService;

        addClassName("conversation-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateConversationList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(conversationGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditConversationDialog();
        configureCreateNewConversationDialog();
        configureDeleteConversationDialog();
    }

    private void configureDeleteConversationDialog() {
        confirmDeleteConversationDialog.setHeaderTitle("Delete conversation");
        confirmDeleteConversationDialog.add(new Paragraph("Are you sure you want to delete this conversation?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteConversationDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteConversation(editConversationForm.getConversation());
            closeConfirmDeleteConversationDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteConversationDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteConversationDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteConversationDialog);
    }

    private void configureCreateNewConversationDialog() {
        createNewConversationDialog.setHeaderTitle("Add new");
        createNewConversationDialog.getFooter().add(createNewConversationForm.getSaveButton(), createNewConversationForm.getCancelButton());

        add(createNewConversationDialog);
    }

    private void configureEditConversationDialog() {
        editConversationDialog.setHeaderTitle("Edit major");
        editConversationForm.getDeleteButton().setVisible(false);
        editConversationDialog.getFooter().add(editConversationForm.getSaveButton(), editConversationForm.getCancelButton());

        add(editConversationDialog);
    }

    private void configureForms() {
        editConversationForm = new ConversationForm();
        editConversationForm.setWidth("25em");
        editConversationForm.setVisible(true);
        editConversationForm.getSaveButton().setVisible(false);
        editConversationForm.addListener(ConversationForm.CloseEvent.class, e -> closeEditConversationDialog());

        createNewConversationForm = new ConversationForm();
        createNewConversationForm.setWidth("25em");
        createNewConversationForm.setVisible(true);
        createNewConversationForm.getDeleteButton().setVisible(false);
        createNewConversationForm.getShowedCreatedDate().setVisible(false);
        createNewConversationForm.getShowedLastModifiedDate().setVisible(false);
        createNewConversationForm.addListener(ConversationForm.SaveEvent.class, e -> saveConversation(e.getConversation()));
        createNewConversationForm.addListener(ConversationForm.CloseEvent.class, e -> closeCreateNewConversationDialog());
    }

    private void closeCreateNewConversationDialog() {
        createNewConversationDialog.close();
        createNewConversationForm.setConversation(null);
        removeClassName("conversation-creating");
    }

    private void saveConversation(Conversation action) {
        try {
            conversationService.saveConversation(action);
            updateConversationList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewConversationDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteConversation(Conversation action) {
        try {
            conversationService.removeConversation(action);
            updateConversationList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditConversationDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editConversation(Conversation action) {
        try {
            conversationService.editConversation(action);
            updateConversationList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditConversationDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        conversationGrid.addClassName("conversation-grid");
        conversationGrid.setSizeFull();
        conversationGrid.setColumns("requestText", "responseText");
        conversationGrid.addColumn(conversation -> conversation.getUser().getEmail()).setHeader("User");
        conversationGrid.addColumn(conversation -> CustomUtils.convertMillisecondsToDate(conversation.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        conversationGrid.addColumn(conversation -> (conversation.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(conversation.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        conversationGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        conversationGrid.getColumns().forEach(col -> col.setWidth("200px"));
    }

    private void configureMenuBar(MenuBar menuBar, Conversation conversation) {
        menuBar.addItem("Detail", e -> openEditConversationDialog(conversation)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteConversationDialog(conversation)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteConversationDialog(Conversation conversation) {
        editConversationForm.setConversation(conversation);
        confirmDeleteConversationDialog.open();
    }

    private void closeConfirmDeleteConversationDialog() {
        confirmDeleteConversationDialog.close();
    }

    private void openEditConversationDialog(Conversation conversation) {
        if (conversation == null) closeEditConversationDialog();
        else {
            editConversationForm.setConversation(conversation);
            editConversationDialog.add(new HorizontalLayout(editConversationForm));
            editConversationDialog.open();
            addClassName("conversation-editing");
        }
    }

    private void closeEditConversationDialog() {
        editConversationDialog.close();
        editConversationForm.setConversation(null);
        removeClassName("conversation-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by keyword...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateConversationList());

        Button addMajorButton = new Button("Add new");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewConversationDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateConversationList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totalMajorsCount = (int) conversationService.getConversationCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totalMajorsCount / CustomConstants.CONVERSATION_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateConversationList();
            }
        });

        HorizontalLayout paginationController = new HorizontalLayout(paginationLabel, prevPageButton, nextPageButton);
        paginationController.setSpacing(true);

        HorizontalLayout toolbar = new HorizontalLayout(
                filterText,
                paginationController
        );
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void openCreateNewConversationDialog() {
        createNewConversationDialog.add(new HorizontalLayout(createNewConversationForm));
        createNewConversationDialog.open();
        createNewConversationForm.setConversation(new Conversation());
        addClassName("conversation-creating");
    }

    private void updateConversationList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.CONVERSATION_PAGE_SIZE);
        params.put("keyword", keyword);

        List<Conversation> conversation = conversationService.getConversation(params);
        long totalConversation = conversationService.getConversationCount(params);

        conversationGrid.setItems(conversation);

        updatePaginationButtons();
        updatePaginationLabel(totalConversation);
    }

    private void updatePaginationLabel(long totalConversation) {
        int startConversation = pageNumber * CustomConstants.CONVERSATION_PAGE_SIZE + 1;
        int endConversation = (int) Math.min((long) (pageNumber + 1) * CustomConstants.CONVERSATION_PAGE_SIZE, totalConversation);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startConversation + " - " + endConversation + " out of " + totalConversation);
    }

    private void updatePaginationButtons() {
        long totalConversationCount = conversationService.getConversationCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalConversationCount / CustomConstants.CONVERSATION_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
