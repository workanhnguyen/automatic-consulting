package com.nva.server.views.entrance_method;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.EntranceMethod;
import com.nva.server.services.EntranceMethodGroupService;
import com.nva.server.services.EntranceMethodService;
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

@PageTitle("Entrance method | Management")
@Route(value = "admin/entrance-methods", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class EntranceMethodView extends VerticalLayout {
    private final EntranceMethodService entranceMethodService;
    private final EntranceMethodGroupService entranceMethodGroupService;

    private final Grid<EntranceMethod> entranceMethodGrid = new Grid<>(EntranceMethod.class);
    private final TextField filterText = new TextField();
    private final Dialog editEntranceMethodDialog = new Dialog();
    private final Dialog createNewEntranceMethodDialog = new Dialog();
    private final Dialog confirmDeleteEntranceMethodDialog = new Dialog();
    private EntranceMethodForm editEntranceMethodForm;
    private EntranceMethodForm createNewEntranceMethodForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public EntranceMethodView(EntranceMethodService entranceMethodService, EntranceMethodGroupService entranceMethodGroupService) {
        this.entranceMethodService = entranceMethodService;
        this.entranceMethodGroupService = entranceMethodGroupService;

        addClassName("entrance-method-group-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateEntranceMethodList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(entranceMethodGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditEntranceMethodDialog();
        configureCreateNewEntranceMethodDialog();
        configureDeleteEntranceMethodDialog();
    }

    private void configureDeleteEntranceMethodDialog() {
        confirmDeleteEntranceMethodDialog.setHeaderTitle("Delete entrance method group");
        confirmDeleteEntranceMethodDialog.add(new Paragraph("Are you sure you want to delete this entrance method group?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteEntranceMethodDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteEntranceMethod(editEntranceMethodForm.getEntranceMethod());
            closeConfirmDeleteEntranceMethodDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteEntranceMethodDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteEntranceMethodDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteEntranceMethodDialog);
    }

    private void configureCreateNewEntranceMethodDialog() {
        createNewEntranceMethodDialog.setHeaderTitle("Add new entrance method group");
        createNewEntranceMethodDialog.getFooter().add(createNewEntranceMethodForm.getSaveButton(), createNewEntranceMethodForm.getCancelButton());

        add(createNewEntranceMethodDialog);
    }

    private void configureEditEntranceMethodDialog() {
        editEntranceMethodDialog.setHeaderTitle("Edit entrance method group");
        editEntranceMethodForm.getDeleteButton().setVisible(false);
        editEntranceMethodDialog.getFooter().add(editEntranceMethodForm.getSaveButton(), editEntranceMethodForm.getCancelButton());

        add(editEntranceMethodDialog);
    }

    private void configureForms() {
        editEntranceMethodForm = new EntranceMethodForm(entranceMethodGroupService);
        editEntranceMethodForm.setWidth("25em");
        editEntranceMethodForm.setVisible(true);
        editEntranceMethodForm.addListener(EntranceMethodForm.SaveEvent.class, e -> editEntranceMethod(e.getEntranceMethod()));
        editEntranceMethodForm.addListener(EntranceMethodForm.DeleteEvent.class, e -> deleteEntranceMethod(e.getEntranceMethod()));
        editEntranceMethodForm.addListener(EntranceMethodForm.CloseEvent.class, e -> closeEditEntranceMethodDialog());

        createNewEntranceMethodForm = new EntranceMethodForm(entranceMethodGroupService);
        createNewEntranceMethodForm.setWidth("25em");
        createNewEntranceMethodForm.setVisible(true);
        createNewEntranceMethodForm.getDeleteButton().setVisible(false);
        createNewEntranceMethodForm.getShowedCreatedDate().setVisible(false);
        createNewEntranceMethodForm.getShowedLastModifiedDate().setVisible(false);
        createNewEntranceMethodForm.addListener(EntranceMethodForm.SaveEvent.class, e -> saveEntranceMethod(e.getEntranceMethod()));
        createNewEntranceMethodForm.addListener(EntranceMethodForm.CloseEvent.class, e -> closeCreateNewEntranceMethodDialog());
    }

    private void closeCreateNewEntranceMethodDialog() {
        createNewEntranceMethodDialog.close();
        createNewEntranceMethodForm.setEntranceMethod(null);
        removeClassName("entrance-method-group-creating");
    }

    private void saveEntranceMethod(EntranceMethod entranceMethod) {
        try {
            entranceMethodService.saveEntranceMethod(entranceMethod);
            updateEntranceMethodList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewEntranceMethodDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteEntranceMethod(EntranceMethod entranceMethod) {
        try {
            entranceMethodService.removeEntranceMethod(entranceMethod);
            updateEntranceMethodList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditEntranceMethodDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editEntranceMethod(EntranceMethod entranceMethod) {
        try {
            entranceMethodService.editEntranceMethod(entranceMethod);
            updateEntranceMethodList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditEntranceMethodDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        entranceMethodGrid.addClassName("entrance-method-grid");
        entranceMethodGrid.setSizeFull();
        entranceMethodGrid.setColumns("name");
        entranceMethodGrid.addColumn(em -> em.getEntranceMethodGroup().getName()).setHeader("Group");
        entranceMethodGrid.addColumn(em -> CustomUtils.convertMillisecondsToDate(em.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        entranceMethodGrid.addColumn(em -> (em.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(em.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        entranceMethodGrid.addColumn(em -> (em.getNote() == null || em.getNote().isEmpty()) ? "--" : em.getNote()).setHeader("Note");
        entranceMethodGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        entranceMethodGrid.getColumns().forEach(col -> col.setWidth("200px"));
    }


    private void configureMenuBar(MenuBar menuBar, EntranceMethod entranceMethod) {
        menuBar.addItem("Edit", e -> openEditEntranceMethodDialog(entranceMethod)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteEntranceMethodDialog(entranceMethod)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteEntranceMethodDialog(EntranceMethod entranceMethod) {
        editEntranceMethodForm.setEntranceMethod(entranceMethod);
        confirmDeleteEntranceMethodDialog.open();
    }

    private void closeConfirmDeleteEntranceMethodDialog() {
        confirmDeleteEntranceMethodDialog.close();
    }

    private void openEditEntranceMethodDialog(EntranceMethod entranceMethod) {
        if (entranceMethod == null) closeEditEntranceMethodDialog();
        else {
            editEntranceMethodForm.setEntranceMethod(entranceMethod);
            editEntranceMethodDialog.add(new HorizontalLayout(editEntranceMethodForm));
            editEntranceMethodDialog.open();
            addClassName("entrance-method-group-editing");
        }
    }

    private void closeEditEntranceMethodDialog() {
        editEntranceMethodDialog.close();
        editEntranceMethodForm.setEntranceMethod(null);
        removeClassName("entrance-method-group-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateEntranceMethodList());

        Button addMajorButton = new Button("Add new");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewEntranceMethodDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateEntranceMethodList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totaEntranceMethodsCount = (int) entranceMethodService.getEntranceMethodCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totaEntranceMethodsCount / CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateEntranceMethodList();
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

    private void openCreateNewEntranceMethodDialog() {
        createNewEntranceMethodDialog.add(new HorizontalLayout(createNewEntranceMethodForm));
        createNewEntranceMethodDialog.open();
        createNewEntranceMethodForm.setEntranceMethod(new EntranceMethod());
        addClassName("entrance-method-group-creating");
    }

    private void updateEntranceMethodList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE);
        params.put("keyword", keyword);

        List<EntranceMethod> entranceMethods = entranceMethodService.getEntranceMethods(params);
        long totalEntranceMethods = entranceMethodService.getEntranceMethodCount(params);

        entranceMethodGrid.setItems(entranceMethods);

        updatePaginationButtons();
        updatePaginationLabel(totalEntranceMethods);
    }

    private void updatePaginationLabel(long totalEntranceMethods) {
        int startEntranceMethod = pageNumber * CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE + 1;
        int endEntranceMethod = (int) Math.min((long) (pageNumber + 1) * CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE, totalEntranceMethods);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startEntranceMethod + " - " + endEntranceMethod + " out of " + totalEntranceMethods);
    }

    private void updatePaginationButtons() {
        long totalEntranceMethodsCount = entranceMethodService.getEntranceMethodCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalEntranceMethodsCount / CustomConstants.ENTRANCE_METHOD_GROUP_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
