package com.nva.server.views.action;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Action;
import com.nva.server.entities.Major;
import com.nva.server.services.ActionService;
import com.nva.server.services.FacultyService;
import com.nva.server.services.MajorService;
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

@PageTitle("Action | Management")
@Route(value = "admin/actions", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class ActionView extends VerticalLayout {
    private final ActionService actionService;

    private final Grid<Action> actionGrid = new Grid<>(Action.class);
    private final TextField filterText = new TextField();
    private final Dialog editActionDialog = new Dialog();
    private final Dialog createNewActionDialog = new Dialog();
    private final Dialog confirmDeleteActionDialog = new Dialog();
    private ActionForm editActionForm;
    private ActionForm createNewActionForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public ActionView(ActionService actionService) {
        this.actionService = actionService;

        addClassName("major-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateActionList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(actionGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditActionDialog();
        configureCreateNewActionDialog();
        configureDeleteActionDialog();
    }

    private void configureDeleteActionDialog() {
        confirmDeleteActionDialog.setHeaderTitle("Delete action");
        confirmDeleteActionDialog.add(new Paragraph("Are you sure you want to delete this action?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteActionDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteAction(editActionForm.getAction());
            closeConfirmDeleteActionDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteActionDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteActionDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteActionDialog);
    }

    private void configureCreateNewActionDialog() {
        createNewActionDialog.setHeaderTitle("Add new action");
        createNewActionDialog.getFooter().add(createNewActionForm.getSaveButton(), createNewActionForm.getCancelButton());

        add(createNewActionDialog);
    }

    private void configureEditActionDialog() {
        editActionDialog.setHeaderTitle("Edit major");
        editActionForm.getDeleteButton().setVisible(false);
        editActionDialog.getFooter().add(editActionForm.getSaveButton(), editActionForm.getCancelButton());

        add(editActionDialog);
    }

    private void configureForms() {
        editActionForm = new ActionForm();
        editActionForm.setWidth("25em");
        editActionForm.setVisible(true);
        editActionForm.addListener(ActionForm.SaveEvent.class, e -> editAction(e.getAction()));
        editActionForm.addListener(ActionForm.DeleteEvent.class, e -> deleteAction(e.getAction()));
        editActionForm.addListener(ActionForm.CloseEvent.class, e -> closeEditActionDialog());

        createNewActionForm = new ActionForm();
        createNewActionForm.setWidth("25em");
        createNewActionForm.setVisible(true);
        createNewActionForm.getDeleteButton().setVisible(false);
        createNewActionForm.getShowedCreatedDate().setVisible(false);
        createNewActionForm.getShowedLastModifiedDate().setVisible(false);
        createNewActionForm.addListener(ActionForm.SaveEvent.class, e -> saveAction(e.getAction()));
        createNewActionForm.addListener(ActionForm.CloseEvent.class, e -> closeCreateNewActionDialog());
    }

    private void closeCreateNewActionDialog() {
        createNewActionDialog.close();
        createNewActionForm.setAction(null);
        removeClassName("action-creating");
    }

    private void saveAction(Action action) {
        try {
            actionService.saveAction(action);
            updateActionList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewActionDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteAction(Action action) {
        try {
            actionService.removeAction(action);
            updateActionList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditActionDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editAction(Action action) {
        try {
            actionService.editAction(action);
            updateActionList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditActionDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        actionGrid.addClassName("action-grid");
        actionGrid.setSizeFull();
        actionGrid.setColumns("name", "description");
        actionGrid.addColumn(action -> CustomUtils.convertMillisecondsToDate(action.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        actionGrid.addColumn(action -> (action.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(action.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        actionGrid.addColumn(action -> (action.getNote() == null || action.getNote().isEmpty()) ? "--" : action.getNote()).setHeader("Note");
        actionGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        actionGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, Action action) {
        menuBar.addItem("Edit", e -> openEditActionDialog(action)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteActionDialog(action)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteActionDialog(Action action) {
        editActionForm.setAction(action);
        confirmDeleteActionDialog.open();
    }

    private void closeConfirmDeleteActionDialog() {
        confirmDeleteActionDialog.close();
    }

    private void openEditActionDialog(Action action) {
        if (action == null) closeEditActionDialog();
        else {
            editActionForm.setAction(action);
            editActionDialog.add(new HorizontalLayout(editActionForm));
            editActionDialog.open();
            addClassName("action-editing");
        }
    }

    private void closeEditActionDialog() {
        editActionDialog.close();
        editActionForm.setAction(null);
        removeClassName("action-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateActionList());

        Button addMajorButton = new Button("Add new action");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewActionDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateActionList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totalMajorsCount = (int) actionService.getActionCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totalMajorsCount / CustomConstants.ACTION_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateActionList();
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

    private void openCreateNewActionDialog() {
        createNewActionDialog.add(new HorizontalLayout(createNewActionForm));
        createNewActionDialog.open();
        createNewActionForm.setAction(new Action());
        addClassName("action-creating");
    }

    private void updateActionList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.ACTION_PAGE_SIZE);
        params.put("keyword", keyword);

        List<Action> actions = actionService.getActions(params);
        long totalActions = actionService.getActionCount(params);

        actionGrid.setItems(actions);

        updatePaginationButtons();
        updatePaginationLabel(totalActions);
    }

    private void updatePaginationLabel(long totalActions) {
        int startAction = pageNumber * CustomConstants.ACTION_PAGE_SIZE + 1;
        int endAction = (int) Math.min((long) (pageNumber + 1) * CustomConstants.ACTION_PAGE_SIZE, totalActions);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startAction + " - " + endAction + " out of " + totalActions);
    }

    private void updatePaginationButtons() {
        long totalActionsCount = actionService.getActionCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalActionsCount / CustomConstants.ACTION_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
