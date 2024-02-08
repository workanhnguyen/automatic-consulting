package com.nva.server.views.major;

import com.nva.server.constants.CustomConstants;
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

@PageTitle("Major | Management")
@Route(value = "admin/majors", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class MajorView extends VerticalLayout {
    private final MajorService majorService;
    private final FacultyService facultyService;

    private final Grid<Major> majorGrid = new Grid<>(Major.class);
    private final TextField filterText = new TextField();
    private final Dialog editMajorDialog = new Dialog();
    private final Dialog createNewMajorDialog = new Dialog();
    private final Dialog confirmDeleteMajorDialog = new Dialog();
    private MajorForm editMajorForm;
    private MajorForm createNewMajorForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public MajorView(MajorService majorService, FacultyService facultyService) {
        this.majorService = majorService;
        this.facultyService = facultyService;

        addClassName("major-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateMajorList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(majorGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditMajorDialog();
        configureCreateNewMajorDialog();
        configureDeleteMajorDialog();
    }

    private void configureDeleteMajorDialog() {
        confirmDeleteMajorDialog.setHeaderTitle("Delete major");
        confirmDeleteMajorDialog.add(new Paragraph("Are you sure you want to delete this major?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteMajorDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteMajor(editMajorForm.getMajor());
            closeConfirmDeleteMajorDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteMajorDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteMajorDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteMajorDialog);
    }

    private void configureCreateNewMajorDialog() {
        createNewMajorDialog.setHeaderTitle("Add new major");
        createNewMajorDialog.getFooter().add(createNewMajorForm.getSaveButton(), createNewMajorForm.getCancelButton());

        add(createNewMajorDialog);
    }

    private void configureEditMajorDialog() {
        editMajorDialog.setHeaderTitle("Edit major");
        editMajorForm.getDeleteButton().setVisible(false);
        editMajorDialog.getFooter().add(editMajorForm.getSaveButton(), editMajorForm.getCancelButton());

        add(editMajorDialog);
    }

    private void configureForms() {
        editMajorForm = new MajorForm(facultyService.getFaculties(new HashMap<>()));
        editMajorForm.setWidth("25em");
        editMajorForm.setVisible(true);
        editMajorForm.addListener(MajorForm.SaveEvent.class, e -> editMajor(e.getMajor()));
        editMajorForm.addListener(MajorForm.DeleteEvent.class, e -> deleteMajor(e.getMajor()));
        editMajorForm.addListener(MajorForm.CloseEvent.class, e -> closeEditMajorDialog());

        createNewMajorForm = new MajorForm(facultyService.getFaculties(new HashMap<>()));
        createNewMajorForm.setWidth("25em");
        createNewMajorForm.setVisible(true);
        createNewMajorForm.getDeleteButton().setVisible(false);
        createNewMajorForm.getShowedCreatedDate().setVisible(false);
        createNewMajorForm.getShowedLastModifiedDate().setVisible(false);
        createNewMajorForm.addListener(MajorForm.SaveEvent.class, e -> saveMajor(e.getMajor()));
        createNewMajorForm.addListener(MajorForm.CloseEvent.class, e -> closeCreateNewMajorDialog());
    }

    private void closeCreateNewMajorDialog() {
        createNewMajorDialog.close();
        createNewMajorForm.setMajor(null);
        removeClassName("major-creating");
    }

    private void saveMajor(Major major) {
        try {
            majorService.saveMajor(major);
            updateMajorList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewMajorDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteMajor(Major major) {
        try {
            majorService.removeMajor(major);
            updateMajorList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditMajorDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editMajor(Major major) {
        try {
            majorService.editMajor(major);
            updateMajorList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditMajorDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        majorGrid.addClassName("major-grid");
        majorGrid.setSizeFull();
        majorGrid.setColumns("name");
        majorGrid.addColumn(major -> major.getFaculty().getName()).setHeader("Faculty");
        majorGrid.addColumn(major -> CustomUtils.convertMillisecondsToDate(major.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        majorGrid.addColumn(major -> (major.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(major.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        majorGrid.addColumn(major -> (major.getNote() == null || major.getNote().isEmpty()) ? "--" : major.getNote()).setHeader("Note");
        majorGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        majorGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, Major major) {
        menuBar.addItem("Edit", e -> openEditMajorDialog(major)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteMajorDialog(major)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteMajorDialog(Major major) {
        editMajorForm.setMajor(major);
        confirmDeleteMajorDialog.open();
    }

    private void closeConfirmDeleteMajorDialog() {
        confirmDeleteMajorDialog.close();
    }

    private void openEditMajorDialog(Major major) {
        if (major == null) closeEditMajorDialog();
        else {
            editMajorForm.setMajor(major);
            editMajorDialog.add(new HorizontalLayout(editMajorForm));
            editMajorDialog.open();
            addClassName("major-editing");
        }
    }

    private void closeEditMajorDialog() {
        editMajorDialog.close();
        editMajorForm.setMajor(null);
        removeClassName("major-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateMajorList());

        Button addMajorButton = new Button("Add new major");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewMajorDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateMajorList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totalMajorsCount = (int) majorService.getMajorCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totalMajorsCount / CustomConstants.MAJOR_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateMajorList();
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

    private void openCreateNewMajorDialog() {
        createNewMajorDialog.add(new HorizontalLayout(createNewMajorForm));
        createNewMajorDialog.open();
        createNewMajorForm.setMajor(new Major());
        addClassName("major-creating");
    }

    private void updateMajorList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.FACULTY_PAGE_SIZE);
        params.put("keyword", keyword);

        List<Major> majors = majorService.getMajors(params);
        long totalMajors = majorService.getMajorCount(params);

        majorGrid.setItems(majors);

        updatePaginationButtons();
        updatePaginationLabel(totalMajors);
    }

    private void updatePaginationLabel(long totalMajors) {
        int startMajor = pageNumber * CustomConstants.MAJOR_PAGE_SIZE + 1;
        int endMajor = (int) Math.min((long) (pageNumber + 1) * CustomConstants.MAJOR_PAGE_SIZE, totalMajors);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startMajor + " - " + endMajor + " out of " + totalMajors);
    }

    private void updatePaginationButtons() {
        long totalMajorsCount = majorService.getMajorCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalMajorsCount / CustomConstants.MAJOR_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
