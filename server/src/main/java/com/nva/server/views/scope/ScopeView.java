package com.nva.server.views.scope;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Scope;
import com.nva.server.services.ScopeService;
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

@PageTitle("Scope | Management")
@Route(value = "admin/scopes", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class ScopeView extends VerticalLayout {
    private final ScopeService scopeService;

    private final Grid<Scope> scopeGrid = new Grid<>(Scope.class);
    private final TextField filterText = new TextField();
    private final Dialog editScopeDialog = new Dialog();
    private final Dialog createNewScopeDialog = new Dialog();
    private final Dialog confirmDeleteScopeDialog = new Dialog();
    private ScopeForm editScopeForm;
    private ScopeForm createNewScopeForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public ScopeView(ScopeService scopeService) {
        this.scopeService = scopeService;

        addClassName("scope-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateScopeList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(scopeGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditScopeDialog();
        configureCreateNewScopeDialog();
        configureDeleteScopeDialog();
    }

    private void configureDeleteScopeDialog() {
        confirmDeleteScopeDialog.setHeaderTitle("Delete scope");
        confirmDeleteScopeDialog.add(new Paragraph("Are you sure you want to delete this scope?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteScopeDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteScope(editScopeForm.getScope());
            closeConfirmDeleteScopeDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteScopeDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteScopeDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteScopeDialog);
    }

    private void configureCreateNewScopeDialog() {
        createNewScopeDialog.setHeaderTitle("Add new scope");
        createNewScopeDialog.getFooter().add(createNewScopeForm.getSaveButton(), createNewScopeForm.getCancelButton());

        add(createNewScopeDialog);
    }

    private void configureEditScopeDialog() {
        editScopeDialog.setHeaderTitle("Edit scope");
        editScopeForm.getDeleteButton().setVisible(false);
        editScopeDialog.getFooter().add(editScopeForm.getSaveButton(), editScopeForm.getCancelButton());

        add(editScopeDialog);
    }

    private void configureForms() {
        editScopeForm = new ScopeForm();
        editScopeForm.setWidth("25em");
        editScopeForm.setVisible(true);
        editScopeForm.addListener(ScopeForm.SaveEvent.class, e -> editScope(e.getScope()));
        editScopeForm.addListener(ScopeForm.DeleteEvent.class, e -> deleteScope(e.getScope()));
        editScopeForm.addListener(ScopeForm.CloseEvent.class, e -> closeEditScopeDialog());

        createNewScopeForm = new ScopeForm();
        createNewScopeForm.setWidth("25em");
        createNewScopeForm.setVisible(true);
        createNewScopeForm.getDeleteButton().setVisible(false);
        createNewScopeForm.getShowedCreatedDate().setVisible(false);
        createNewScopeForm.getShowedLastModifiedDate().setVisible(false);
        createNewScopeForm.addListener(ScopeForm.SaveEvent.class, e -> saveScope(e.getScope()));
        createNewScopeForm.addListener(ScopeForm.CloseEvent.class, e -> closeCreateNewScopeDialog());
    }

    private void closeCreateNewScopeDialog() {
        createNewScopeDialog.close();
        createNewScopeForm.setScope(null);
        removeClassName("scope-creating");
    }

    private void saveScope(Scope scope) {
        try {
            scopeService.saveScope(scope);
            updateScopeList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewScopeDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteScope(Scope scope) {
        try {
            scopeService.removeScope(scope);
            updateScopeList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditScopeDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editScope(Scope scope) {
        try {
            scopeService.editScope(scope);
            updateScopeList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditScopeDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        scopeGrid.addClassName("scope-grid");
        scopeGrid.setSizeFull();
        scopeGrid.setColumns("name", "description");
        scopeGrid.addColumn(scope -> CustomUtils.convertMillisecondsToDate(scope.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        scopeGrid.addColumn(scope -> (scope.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(scope.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        scopeGrid.addColumn(scope -> (scope.getNote() == null || scope.getNote().isEmpty()) ? "--" : scope.getNote()).setHeader("Note");
        scopeGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        scopeGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, Scope scope) {
        menuBar.addItem("Edit", e -> openEditScopeDialog(scope)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteScopeDialog(scope)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteScopeDialog(Scope scope) {
        editScopeForm.setScope(scope);
        confirmDeleteScopeDialog.open();
    }

    private void closeConfirmDeleteScopeDialog() {
        confirmDeleteScopeDialog.close();
    }

    private void openEditScopeDialog(Scope scope) {
        if (scope == null) closeEditScopeDialog();
        else {
            editScopeForm.setScope(scope);
            editScopeDialog.add(new HorizontalLayout(editScopeForm));
            editScopeDialog.open();
            addClassName("scope-editing");
        }
    }

    private void closeEditScopeDialog() {
        editScopeDialog.close();
        editScopeForm.setScope(null);
        removeClassName("scope-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateScopeList());

        Button addMajorButton = new Button("Add new scope");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewScopeDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateScopeList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totaScopesCount = (int) scopeService.getScopeCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totaScopesCount / CustomConstants.SCOPE_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateScopeList();
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

    private void openCreateNewScopeDialog() {
        createNewScopeDialog.add(new HorizontalLayout(createNewScopeForm));
        createNewScopeDialog.open();
        createNewScopeForm.setScope(new Scope());
        addClassName("scope-creating");
    }

    private void updateScopeList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.SCOPE_PAGE_SIZE);
        params.put("keyword", keyword);

        List<Scope> scopes = scopeService.getScopes(params);
        long totalScopes = scopeService.getScopeCount(params);

        scopeGrid.setItems(scopes);

        updatePaginationButtons();
        updatePaginationLabel(totalScopes);
    }

    private void updatePaginationLabel(long totalScopes) {
        int startScope = pageNumber * CustomConstants.SCOPE_PAGE_SIZE + 1;
        int endScope = (int) Math.min((long) (pageNumber + 1) * CustomConstants.SCOPE_PAGE_SIZE, totalScopes);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startScope + " - " + endScope + " out of " + totalScopes);
    }

    private void updatePaginationButtons() {
        long totalScopesCount = scopeService.getScopeCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalScopesCount / CustomConstants.SCOPE_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
