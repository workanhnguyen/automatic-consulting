package com.nva.server.views.faculty;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Faculty;
import com.nva.server.services.FacultyService;
import com.nva.server.utils.CustomUtils;
import com.nva.server.views.MainLayout;
import com.nva.server.views.components.CustomNotification;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
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

@PageTitle("Faculty | Management")
@Route(value = "admin/faculties", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class FacultyView extends VerticalLayout {
    private final FacultyService facultyService;

    private final Grid<Faculty> facultyGrid = new Grid<>(Faculty.class);
    private final TextField filterText = new TextField();
    private final Dialog editFacultyDialog = new Dialog();
    private final Dialog createNewFacultyDialog = new Dialog();
    private final Dialog confirmDeleteFacultyDialog = new Dialog();
    private final FacultyForm editFacultyForm = new FacultyForm();
    private final FacultyForm createNewFacultyForm = new FacultyForm();

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public FacultyView(FacultyService facultyService) {
        this.facultyService = facultyService;

        addClassName("faculty-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateFacultyList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(facultyGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditFacultyDialog();
        configureCreateNewFacultyDialog();
        configureDeleteFacultyDialog();
    }

    private void configureDeleteFacultyDialog() {
        confirmDeleteFacultyDialog.setHeaderTitle("Delete faculty");
        confirmDeleteFacultyDialog.add(new Paragraph("Are you sure you want to delete this faculty?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteFacultyDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteFaculty(editFacultyForm.getFaculty());
            closeConfirmDeleteFacultyDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteFacultyDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteFacultyDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteFacultyDialog);
    }

    private void configureCreateNewFacultyDialog() {
        createNewFacultyDialog.setHeaderTitle("Add new faculty");
        createNewFacultyDialog.getFooter().add(createNewFacultyForm.getSaveButton(), createNewFacultyForm.getCancelButton());

        add(createNewFacultyDialog);
    }

    private void configureEditFacultyDialog() {
        editFacultyDialog.setHeaderTitle("Edit faculty");
        editFacultyForm.getDeleteButton().setVisible(false);
        editFacultyDialog.getFooter().add(editFacultyForm.getSaveButton(), editFacultyForm.getCancelButton());

        add(editFacultyDialog);
    }

    private void configureForms() {
        editFacultyForm.setWidth("25em");
        editFacultyForm.setVisible(true);
        editFacultyForm.addListener(FacultyForm.SaveEvent.class, e -> editFaculty(e.getFaculty()));
        editFacultyForm.addListener(FacultyForm.DeleteEvent.class, e -> deleteFaculty(e.getFaculty()));
        editFacultyForm.addListener(FacultyForm.CloseEvent.class, e -> closeEditFacultyDialog());

        createNewFacultyForm.setWidth("25em");
        createNewFacultyForm.setVisible(true);
        createNewFacultyForm.getDeleteButton().setVisible(false);
        createNewFacultyForm.getShowedCreatedDate().setVisible(false);
        createNewFacultyForm.getShowedLastModifiedDate().setVisible(false);
        createNewFacultyForm.addListener(FacultyForm.SaveEvent.class, e -> saveFaculty(e.getFaculty()));
        createNewFacultyForm.addListener(FacultyForm.CloseEvent.class, e -> closeCreateNewFacultyDialog());
    }

    private void closeCreateNewFacultyDialog() {
        createNewFacultyDialog.close();
        createNewFacultyForm.setFaculty(null);
        removeClassName("faculty-creating");
    }

    private void saveFaculty(Faculty faculty) {
        try {
            facultyService.saveFaculty(faculty);
            updateFacultyList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewFacultyDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteFaculty(Faculty faculty) {
        try {
            facultyService.removeFaculty(faculty);
            updateFacultyList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditFacultyDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editFaculty(Faculty faculty) {
        try {
            facultyService.editFaculty(faculty);
            updateFacultyList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditFacultyDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        facultyGrid.addClassName("faculty-grid");
        facultyGrid.setSizeFull();
        facultyGrid.setColumns("name");
        facultyGrid.addColumn(faculty -> (faculty.getNote() == null || faculty.getNote().isEmpty()) ? "--" : faculty.getNote()).setHeader("Note");
        facultyGrid.addColumn(faculty -> CustomUtils.convertMillisecondsToDate(faculty.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        facultyGrid.addColumn(faculty -> (faculty.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(faculty.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        facultyGrid.addColumn(
                new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions").setTextAlign(ColumnTextAlign.CENTER);

        facultyGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, Faculty faculty) {
        menuBar.addItem("Edit", e -> openEditDialog(faculty)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteDialog(faculty)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteDialog(Faculty faculty) {
        editFacultyForm.setFaculty(faculty);
        confirmDeleteFacultyDialog.open();
    }

    private void closeConfirmDeleteFacultyDialog() {
        confirmDeleteFacultyDialog.close();
    }

    private void openEditDialog(Faculty faculty) {
        if (faculty == null) closeEditFacultyDialog();
        else {
            editFacultyForm.setFaculty(faculty);
            editFacultyDialog.add(new HorizontalLayout(editFacultyForm));
            editFacultyDialog.open();
            addClassName("faculty-editing");
        }
    }

    private void closeEditFacultyDialog() {
        editFacultyDialog.close();
        editFacultyForm.setFaculty(null);
        removeClassName("faculty-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateFacultyList());

        Button addFacultyButton = new Button("Add faculty");
        addFacultyButton.getStyle().setCursor("pointer");
        addFacultyButton.addClickListener(e -> openCreateFacultyDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateFacultyList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totalFacultiesCount = (int) facultyService.getFacultyCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totalFacultiesCount / CustomConstants.FACULTY_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateFacultyList();
            }
        });

        HorizontalLayout paginationController = new HorizontalLayout(paginationLabel, prevPageButton, nextPageButton);
        paginationController.setSpacing(true);

        HorizontalLayout toolbar = new HorizontalLayout(
                new HorizontalLayout(filterText, addFacultyButton),
                paginationController
        );
        toolbar.setWidthFull();
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);

        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void openCreateFacultyDialog() {
        createNewFacultyDialog.add(new HorizontalLayout(createNewFacultyForm));
        createNewFacultyDialog.open();
        createNewFacultyForm.setFaculty(new Faculty());
        addClassName("faculty-creating");
    }

    private void updateFacultyList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.FACULTY_PAGE_SIZE);
        params.put("keyword", keyword);

        List<Faculty> faculties = facultyService.getFaculties(params);
        long totalFaculties = facultyService.getFacultyCount(params);

        facultyGrid.setItems(faculties);

        updatePaginationButtons();
        updatePaginationLabel(totalFaculties);
    }

    private void updatePaginationLabel(long totalFaculties) {
        int startFaculty = pageNumber * CustomConstants.FACULTY_PAGE_SIZE + 1;
        int endFaculty = (int) Math.min((long) (pageNumber + 1) * CustomConstants.FACULTY_PAGE_SIZE, totalFaculties);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startFaculty + " - " + endFaculty + " out of " + totalFaculties);
    }

    private void updatePaginationButtons() {
        long totalFacultiesCount = facultyService.getFacultyCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalFacultiesCount / CustomConstants.FACULTY_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
