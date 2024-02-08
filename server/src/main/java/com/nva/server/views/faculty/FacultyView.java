package com.nva.server.views.faculty;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.Faculty;
import com.nva.server.entities.Major;
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

import java.util.*;

@PageTitle("Faculty | Management")
@Route(value = "admin/faculties", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class FacultyView extends VerticalLayout {
    private final FacultyService facultyService;
    private final MajorService majorService;

    private final Grid<Faculty> facultyGrid = new Grid<>(Faculty.class);
    private final Grid<Major> majorGrid = new Grid<>(Major.class);
    private final TextField filterText = new TextField();
    private final Dialog editFacultyDialog = new Dialog();
    private final Dialog createNewFacultyDialog = new Dialog();
    private final Dialog confirmDeleteFacultyDialog = new Dialog();
    private final FacultyForm editFacultyForm = new FacultyForm();
    private final FacultyForm createNewFacultyForm = new FacultyForm();

    // Faculty pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    // Majors of faculty pagination variables
    private int majorPageNumber = 0;
    private final Button prevMajorPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextMajorPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph majorPaginationLabel = new Paragraph();
    private Map<String, Object> majorParams = new HashMap<>();

    public FacultyView(FacultyService facultyService, MajorService majorService) {
        this.facultyService = facultyService;
        this.majorService = majorService;

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
        // Faculty grid
        facultyGrid.addClassName("faculty-grid");
        facultyGrid.setSizeFull();
        facultyGrid.setColumns("name");
        facultyGrid.addColumn(faculty -> CustomUtils.convertMillisecondsToDate(faculty.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        facultyGrid.addColumn(faculty -> (faculty.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(faculty.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        facultyGrid.addColumn(faculty -> (faculty.getNote() == null || faculty.getNote().isEmpty()) ? "--" : faculty.getNote()).setHeader("Note");
        facultyGrid.addColumn(
                new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions").setTextAlign(ColumnTextAlign.CENTER);

        facultyGrid.getColumns().forEach(col -> col.setAutoWidth(true));

        // Major of faculty grid
        majorGrid.addClassName("majors-of-faculty-grid");
        majorGrid.setWidthFull();
        majorGrid.setHeight("300px");
        majorGrid.setColumns("name");
        majorGrid.addColumn(major -> CustomUtils.convertMillisecondsToDate(major.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        majorGrid.addColumn(major -> (major.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(major.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        majorGrid.addColumn(major -> (major.getNote() == null || major.getNote().isEmpty()) ? "--" : major.getNote()).setHeader("Note");
        majorGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, Faculty faculty) {
        menuBar.addItem("Edit", e -> openEditDialog(faculty)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteDialog(faculty)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteDialog(Faculty faculty) {
        editFacultyForm.setFaculty(faculty);

        Paragraph title = new Paragraph(String.format("Are you sure you want to delete %s faculty?", editFacultyForm.getFaculty().getName()));
        Paragraph subtitle = new Paragraph();
        subtitle.getStyle().setFontWeight(700);

        List<Major> majors = majorService.getMajors(Collections.singletonMap("facultyId", String.valueOf(editFacultyForm.getFaculty().getId())));

        if (!majors.isEmpty()) {
            updateMajorList();

            confirmDeleteFacultyDialog.removeAll();
            majorGrid.setItems(majors);
            confirmDeleteFacultyDialog.add(title);
            subtitle.setText(String.format("There is list of majors of %s faculty. If you still want to delete, majors related to %s faculty will be deleted also.", editFacultyForm.getFaculty().getName(), editFacultyForm.getFaculty().getName()));
            confirmDeleteFacultyDialog.add(subtitle);
            confirmDeleteFacultyDialog.setSizeFull();
            confirmDeleteFacultyDialog.add(getMajorPagination());
            confirmDeleteFacultyDialog.add(majorGrid);
        } else {
            confirmDeleteFacultyDialog.removeAll();
            confirmDeleteFacultyDialog.add(title);
            subtitle.setText("It cannot be restored after deleting.");
            confirmDeleteFacultyDialog.add(subtitle);
            confirmDeleteFacultyDialog.remove(majorGrid);
            confirmDeleteFacultyDialog.setSizeUndefined();
//            confirmDeleteFacultyDialog.setWidth("360px");
//            confirmDeleteFacultyDialog.setHeight("240px");
        }
        confirmDeleteFacultyDialog.open();
    }

    private Component getMajorPagination() {
        prevMajorPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevMajorPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevMajorPageButton.setTooltipText("Previous page");
        prevMajorPageButton.addClickListener(e -> {
            if (majorPageNumber > 0) {
                majorPageNumber--;
                updateMajorList();
            }
        });

        nextMajorPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextMajorPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextMajorPageButton.setTooltipText("Next page");
        nextMajorPageButton.addClickListener(e -> {
            long totalMajorsCount = (int) majorService.getMajorCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totalMajorsCount / CustomConstants.MAJOR_PAGE_SIZE);
            if (majorPageNumber < totalPages - 1) {
                majorPageNumber++;
                updateMajorList();
            }
        });

        HorizontalLayout majorPaginationController = new HorizontalLayout(majorPaginationLabel, prevMajorPageButton, nextMajorPageButton);
        majorPaginationController.setSpacing(true);
        majorPaginationController.setWidthFull();
        majorPaginationController.setJustifyContentMode(JustifyContentMode.END);

        majorPaginationController.addClassName("major-pagination-controller");

        return majorPaginationController;
    }

    private void updateMajorList() {
        majorParams.put("pageNumber", majorPageNumber);
        majorParams.put("pageSize", CustomConstants.MAJOR_PAGE_SIZE);

        List<Major> majors = majorService.getMajors(majorParams);
        long totalMajors = majorService.getMajorCount(majorParams);

        majorGrid.setItems(majors);

        updateMajorPaginationButtons();
        updateMajorPaginationLabel(totalMajors);
    }

    private void updateMajorPaginationLabel(long totalMajors) {
        int startMajor = majorPageNumber * CustomConstants.MAJOR_PAGE_SIZE + 1;
        int endMajor = (int) Math.min((long) (majorPageNumber + 1) * CustomConstants.MAJOR_PAGE_SIZE, totalMajors);

        // Update label with the current range of users being displayed
        majorPaginationLabel.setText(startMajor + " - " + endMajor + " out of " + totalMajors);
    }

    private void updateMajorPaginationButtons() {
        long totalMajorsCount = majorService.getMajorCount(Collections.singletonMap("facultyId", String.valueOf(editFacultyForm.getFaculty().getId())));
        int totalPages = (int) Math.ceil((double) totalMajorsCount / CustomConstants.MAJOR_PAGE_SIZE);

        prevMajorPageButton.setEnabled(majorPageNumber > 0);
        nextMajorPageButton.setEnabled(majorPageNumber < totalPages - 1);
    }

    private void closeConfirmDeleteFacultyDialog() {
        confirmDeleteFacultyDialog.close();
        majorGrid.setItems(new ArrayList<>());
        majorPageNumber = 0;
        majorParams = new HashMap<>();
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
