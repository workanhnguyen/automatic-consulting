package com.nva.server.views.entrance_method_group;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.EntranceMethodGroup;
import com.nva.server.entities.Topic;
import com.nva.server.services.EntranceMethodGroupService;
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

@PageTitle("Entrance method group | Management")
@Route(value = "admin/entrance-method-groups", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class EntranceMethodGroupView extends VerticalLayout {
    private final EntranceMethodGroupService entranceMethodGroupService;

    private final Grid<EntranceMethodGroup> entranceMethodGroupGrid = new Grid<>(EntranceMethodGroup.class);
    private final TextField filterText = new TextField();
    private final Dialog editEntranceMethodGroupDialog = new Dialog();
    private final Dialog createNewEntranceMethodGroupDialog = new Dialog();
    private final Dialog confirmDeleteEntranceMethodGroupDialog = new Dialog();
    private EntranceMethodGroupForm editEntranceMethodGroupForm;
    private EntranceMethodGroupForm createNewEntranceMethodGroupForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public EntranceMethodGroupView(EntranceMethodGroupService entranceMethodGroupService) {
        this.entranceMethodGroupService = entranceMethodGroupService;

        addClassName("entrance-method-group-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateEntranceMethodGroupList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(entranceMethodGroupGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditEntranceMethodGroupDialog();
        configureCreateNewEntranceMethodGroupDialog();
        configureDeleteEntranceMethodGroupDialog();
    }

    private void configureDeleteEntranceMethodGroupDialog() {
        confirmDeleteEntranceMethodGroupDialog.setHeaderTitle("Delete entrance method group");
        confirmDeleteEntranceMethodGroupDialog.add(new Paragraph("Are you sure you want to delete this entrance method group?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteEntranceMethodGroupDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteEntranceMethodGroup(editEntranceMethodGroupForm.getEntranceMethodGroup());
            closeConfirmDeleteEntranceMethodGroupDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteEntranceMethodGroupDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteEntranceMethodGroupDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteEntranceMethodGroupDialog);
    }

    private void configureCreateNewEntranceMethodGroupDialog() {
        createNewEntranceMethodGroupDialog.setHeaderTitle("Add new entrance method group");
        createNewEntranceMethodGroupDialog.getFooter().add(createNewEntranceMethodGroupForm.getSaveButton(), createNewEntranceMethodGroupForm.getCancelButton());

        add(createNewEntranceMethodGroupDialog);
    }

    private void configureEditEntranceMethodGroupDialog() {
        editEntranceMethodGroupDialog.setHeaderTitle("Edit entrance method group");
        editEntranceMethodGroupForm.getDeleteButton().setVisible(false);
        editEntranceMethodGroupDialog.getFooter().add(editEntranceMethodGroupForm.getSaveButton(), editEntranceMethodGroupForm.getCancelButton());

        add(editEntranceMethodGroupDialog);
    }

    private void configureForms() {
        editEntranceMethodGroupForm = new EntranceMethodGroupForm();
        editEntranceMethodGroupForm.setWidth("25em");
        editEntranceMethodGroupForm.setVisible(true);
        editEntranceMethodGroupForm.addListener(EntranceMethodGroupForm.SaveEvent.class, e -> editEntranceMethodGroup(e.getEntranceMethodGroup()));
        editEntranceMethodGroupForm.addListener(EntranceMethodGroupForm.DeleteEvent.class, e -> deleteEntranceMethodGroup(e.getEntranceMethodGroup()));
        editEntranceMethodGroupForm.addListener(EntranceMethodGroupForm.CloseEvent.class, e -> closeEditEntranceMethodGroupDialog());

        createNewEntranceMethodGroupForm = new EntranceMethodGroupForm();
        createNewEntranceMethodGroupForm.setWidth("25em");
        createNewEntranceMethodGroupForm.setVisible(true);
        createNewEntranceMethodGroupForm.getDeleteButton().setVisible(false);
        createNewEntranceMethodGroupForm.getShowedCreatedDate().setVisible(false);
        createNewEntranceMethodGroupForm.getShowedLastModifiedDate().setVisible(false);
        createNewEntranceMethodGroupForm.addListener(EntranceMethodGroupForm.SaveEvent.class, e -> saveEntranceMethodGroup(e.getEntranceMethodGroup()));
        createNewEntranceMethodGroupForm.addListener(EntranceMethodGroupForm.CloseEvent.class, e -> closeCreateNewEntranceMethodGroupDialog());
    }

    private void closeCreateNewEntranceMethodGroupDialog() {
        createNewEntranceMethodGroupDialog.close();
        createNewEntranceMethodGroupForm.setEntranceMethodGroup(null);
        removeClassName("entrance-method-group-creating");
    }

    private void saveEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup) {
        try {
            entranceMethodGroupService.saveEntranceMethodGroup(entranceMethodGroup);
            updateEntranceMethodGroupList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewEntranceMethodGroupDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup) {
        try {
            entranceMethodGroupService.removeEntranceMethodGroup(entranceMethodGroup);
            updateEntranceMethodGroupList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditEntranceMethodGroupDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editEntranceMethodGroup(EntranceMethodGroup entranceMethodGroup) {
        try {
            entranceMethodGroupService.editEntranceMethodGroup(entranceMethodGroup);
            updateEntranceMethodGroupList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditEntranceMethodGroupDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        entranceMethodGroupGrid.addClassName("entrance-method-group-grid");
        entranceMethodGroupGrid.setSizeFull();
        entranceMethodGroupGrid.setColumns("name");
        entranceMethodGroupGrid.addColumn(emg -> CustomUtils.convertMillisecondsToDate(emg.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        entranceMethodGroupGrid.addColumn(emg -> (emg.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(emg.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        entranceMethodGroupGrid.addColumn(emg -> (emg.getNote() == null || emg.getNote().isEmpty()) ? "--" : emg.getNote()).setHeader("Note");
        entranceMethodGroupGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        entranceMethodGroupGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, EntranceMethodGroup entranceMethodGroup) {
        menuBar.addItem("Edit", e -> openEditEntranceMethodGroupDialog(entranceMethodGroup)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteEntranceMethodGroupDialog(entranceMethodGroup)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteEntranceMethodGroupDialog(EntranceMethodGroup entranceMethodGroup) {
        editEntranceMethodGroupForm.setEntranceMethodGroup(entranceMethodGroup);
        confirmDeleteEntranceMethodGroupDialog.open();
    }

    private void closeConfirmDeleteEntranceMethodGroupDialog() {
        confirmDeleteEntranceMethodGroupDialog.close();
    }

    private void openEditEntranceMethodGroupDialog(EntranceMethodGroup entranceMethodGroup) {
        if (entranceMethodGroup == null) closeEditEntranceMethodGroupDialog();
        else {
            editEntranceMethodGroupForm.setEntranceMethodGroup(entranceMethodGroup);
            editEntranceMethodGroupDialog.add(new HorizontalLayout(editEntranceMethodGroupForm));
            editEntranceMethodGroupDialog.open();
            addClassName("entrance-method-group-editing");
        }
    }

    private void closeEditEntranceMethodGroupDialog() {
        editEntranceMethodGroupDialog.close();
        editEntranceMethodGroupForm.setEntranceMethodGroup(null);
        removeClassName("entrance-method-group-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateEntranceMethodGroupList());

        Button addMajorButton = new Button("Add new");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewEntranceMethodGroupDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateEntranceMethodGroupList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totaEntranceMethodGroupsCount = (int) entranceMethodGroupService.getEntranceMethodGroupCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totaEntranceMethodGroupsCount / CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateEntranceMethodGroupList();
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

    private void openCreateNewEntranceMethodGroupDialog() {
        createNewEntranceMethodGroupDialog.add(new HorizontalLayout(createNewEntranceMethodGroupForm));
        createNewEntranceMethodGroupDialog.open();
        createNewEntranceMethodGroupForm.setEntranceMethodGroup(new EntranceMethodGroup());
        addClassName("entrance-method-group-creating");
    }

    private void updateEntranceMethodGroupList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE);
        params.put("keyword", keyword);

        List<EntranceMethodGroup> entranceMethodGroups = entranceMethodGroupService.getEntranceMethodGroups(params);
        long totalEntranceMethodGroups = entranceMethodGroupService.getEntranceMethodGroupCount(params);

        entranceMethodGroupGrid.setItems(entranceMethodGroups);

        updatePaginationButtons();
        updatePaginationLabel(totalEntranceMethodGroups);
    }

    private void updatePaginationLabel(long totalEntranceMethodGroups) {
        int startEntranceMethodGroup = pageNumber * CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE + 1;
        int endEntranceMethodGroup = (int) Math.min((long) (pageNumber + 1) * CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE, totalEntranceMethodGroups);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startEntranceMethodGroup + " - " + endEntranceMethodGroup + " out of " + totalEntranceMethodGroups);
    }

    private void updatePaginationButtons() {
        long totalEntranceMethodGroupsCount = entranceMethodGroupService.getEntranceMethodGroupCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalEntranceMethodGroupsCount / CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
