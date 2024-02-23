package com.nva.server.views.suggested_question;

import com.nva.server.constants.CustomConstants;
import com.nva.server.entities.SuggestedQuestion;
import com.nva.server.services.SuggestedQuestionService;
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

@PageTitle("Suggested question | Management")
@Route(value = "admin/suggested-questions", layout = MainLayout.class)
@RolesAllowed("ROLE_ADMIN")
@AccessDeniedErrorRouter
public class SuggestedQuestionView extends VerticalLayout {
    private final SuggestedQuestionService suggestedQuestionService;

    private final Grid<SuggestedQuestion> suggestedQuestionGrid = new Grid<>(SuggestedQuestion.class);
    private final TextField filterText = new TextField();
    private final Dialog editSuggestedQuestionDialog = new Dialog();
    private final Dialog createNewSuggestedQuestionDialog = new Dialog();
    private final Dialog confirmDeleteSuggestedQuestionDialog = new Dialog();
    private SuggestedQuestionForm editSuggestedQuestionForm;
    private SuggestedQuestionForm createNewSuggestedQuestionForm;

    // Pagination variables
    private int pageNumber = 0;
    private final Button prevPageButton = new Button(new Icon(VaadinIcon.ANGLE_LEFT));
    private final Button nextPageButton = new Button(new Icon(VaadinIcon.ANGLE_RIGHT));
    private final Paragraph paginationLabel = new Paragraph();
    private final Map<String, Object> params = new HashMap<>();

    public SuggestedQuestionView(SuggestedQuestionService suggestedQuestionService) {
        this.suggestedQuestionService = suggestedQuestionService;

        addClassName("suggested-question-view");
        setSizeFull();
        configureGrid();
        configureForms();
        configureDialogs();

        add(getToolbar(), getContent());

        updateSuggestedQuestionList();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(suggestedQuestionGrid);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureDialogs() {
        configureEditSuggestedQuestionDialog();
        configureCreateNewSuggestedQuestionDialog();
        configureDeleteSuggestedQuestionDialog();
    }

    private void configureDeleteSuggestedQuestionDialog() {
        confirmDeleteSuggestedQuestionDialog.setHeaderTitle("Delete suggested question");
        confirmDeleteSuggestedQuestionDialog.add(new Paragraph("Are you sure you want to delete this suggested question?"));
        Paragraph subtitle = new Paragraph("It cannot be restored after deleting.");
        subtitle.getStyle().setFontWeight(800);
        confirmDeleteSuggestedQuestionDialog.add(subtitle);

        Button deleteButton = new Button("Delete", e -> {
            deleteSuggestedQuestion(editSuggestedQuestionForm.getSuggestedQuestion());
            closeConfirmDeleteSuggestedQuestionDialog();
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        deleteButton.getStyle().setCursor("pointer");

        Button cancelButton = new Button("Cancel", e -> closeConfirmDeleteSuggestedQuestionDialog());
        cancelButton.getStyle().setCursor("pointer");

        confirmDeleteSuggestedQuestionDialog.getFooter().add(deleteButton, cancelButton);

        add(confirmDeleteSuggestedQuestionDialog);
    }

    private void configureCreateNewSuggestedQuestionDialog() {
        createNewSuggestedQuestionDialog.setHeaderTitle("Add new scope");
        createNewSuggestedQuestionDialog.getFooter().add(createNewSuggestedQuestionForm.getSaveButton(), createNewSuggestedQuestionForm.getCancelButton());

        add(createNewSuggestedQuestionDialog);
    }

    private void configureEditSuggestedQuestionDialog() {
        editSuggestedQuestionDialog.setHeaderTitle("Edit suggested question");
        editSuggestedQuestionForm.getDeleteButton().setVisible(false);
        editSuggestedQuestionDialog.getFooter().add(editSuggestedQuestionForm.getSaveButton(), editSuggestedQuestionForm.getCancelButton());

        add(editSuggestedQuestionDialog);
    }

    private void configureForms() {
        editSuggestedQuestionForm = new SuggestedQuestionForm();
        editSuggestedQuestionForm.setWidth("25em");
        editSuggestedQuestionForm.setVisible(true);
        editSuggestedQuestionForm.addListener(SuggestedQuestionForm.SaveEvent.class, e -> editSuggestedQuestion(e.getSuggestedQuestion()));
        editSuggestedQuestionForm.addListener(SuggestedQuestionForm.DeleteEvent.class, e -> deleteSuggestedQuestion(e.getSuggestedQuestion()));
        editSuggestedQuestionForm.addListener(SuggestedQuestionForm.CloseEvent.class, e -> closeEditSuggestedQuestionDialog());

        createNewSuggestedQuestionForm = new SuggestedQuestionForm();
        createNewSuggestedQuestionForm.setWidth("25em");
        createNewSuggestedQuestionForm.setVisible(true);
        createNewSuggestedQuestionForm.getDeleteButton().setVisible(false);
        createNewSuggestedQuestionForm.getShowedCreatedDate().setVisible(false);
        createNewSuggestedQuestionForm.getShowedLastModifiedDate().setVisible(false);
        createNewSuggestedQuestionForm.addListener(SuggestedQuestionForm.SaveEvent.class, e -> saveSuggestedQuestion(e.getSuggestedQuestion()));
        createNewSuggestedQuestionForm.addListener(SuggestedQuestionForm.CloseEvent.class, e -> closeCreateNewSuggestedQuestionDialog());
    }

    private void closeCreateNewSuggestedQuestionDialog() {
        createNewSuggestedQuestionDialog.close();
        createNewSuggestedQuestionForm.setSuggestedQuestion(null);
        removeClassName("scope-creating");
    }

    private void saveSuggestedQuestion(SuggestedQuestion scope) {
        try {
            suggestedQuestionService.saveSuggestedQuestion(scope);
            updateSuggestedQuestionList();
            CustomNotification.showNotification("Created successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeCreateNewSuggestedQuestionDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void deleteSuggestedQuestion(SuggestedQuestion scope) {
        try {
            suggestedQuestionService.removeSuggestedQuestion(scope);
            updateSuggestedQuestionList();
            CustomNotification.showNotification("Deleted successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditSuggestedQuestionDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void editSuggestedQuestion(SuggestedQuestion scope) {
        try {
            suggestedQuestionService.editSuggestedQuestion(scope);
            updateSuggestedQuestionList();
            CustomNotification.showNotification("Updated successfully!", "success", Notification.Position.TOP_CENTER, 3000);

            closeEditSuggestedQuestionDialog();
        } catch (Exception ex) {
            CustomNotification.showNotification(ex.getMessage(), "error", Notification.Position.TOP_CENTER, 3000);
        }
    }

    private void configureGrid() {
        suggestedQuestionGrid.addClassName("suggested-question-grid");
        suggestedQuestionGrid.setSizeFull();
        suggestedQuestionGrid.setColumns("content");
        suggestedQuestionGrid.addColumn(scope -> CustomUtils.convertMillisecondsToDate(scope.getCreatedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Created date");
        suggestedQuestionGrid.addColumn(scope -> (scope.getLastModifiedDate() == null) ? "--" :
                CustomUtils.convertMillisecondsToDate(scope.getLastModifiedDate(), "HH:mm:ss dd-MM-yyyy")).setHeader("Last modified date");
        suggestedQuestionGrid.addColumn(scope -> (scope.getNote() == null || scope.getNote().isEmpty()) ? "--" : scope.getNote()).setHeader("Note");
        suggestedQuestionGrid.addColumn(new ComponentRenderer<>(MenuBar::new, this::configureMenuBar)).setHeader("Actions");

        suggestedQuestionGrid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void configureMenuBar(MenuBar menuBar, SuggestedQuestion scope) {
        menuBar.addItem("Edit", e -> openEditSuggestedQuestionDialog(scope)).getStyle().setCursor("pointer");
        menuBar.addItem("Delete", e -> openConfirmDeleteSuggestedQuestionDialog(scope)).getStyle().setCursor("pointer").setColor("red");
    }

    private void openConfirmDeleteSuggestedQuestionDialog(SuggestedQuestion scope) {
        editSuggestedQuestionForm.setSuggestedQuestion(scope);
        confirmDeleteSuggestedQuestionDialog.open();
    }

    private void closeConfirmDeleteSuggestedQuestionDialog() {
        confirmDeleteSuggestedQuestionDialog.close();
    }

    private void openEditSuggestedQuestionDialog(SuggestedQuestion scope) {
        if (scope == null) closeEditSuggestedQuestionDialog();
        else {
            editSuggestedQuestionForm.setSuggestedQuestion(scope);
            editSuggestedQuestionDialog.add(new HorizontalLayout(editSuggestedQuestionForm));
            editSuggestedQuestionDialog.open();
            addClassName("scope-editing");
        }
    }

    private void closeEditSuggestedQuestionDialog() {
        editSuggestedQuestionDialog.close();
        editSuggestedQuestionForm.setSuggestedQuestion(null);
        removeClassName("scope-editing");
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by content...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // LAZY: wait user stop typing --> do filter
        filterText.addValueChangeListener(e -> updateSuggestedQuestionList());

        Button addMajorButton = new Button("Add new");
        addMajorButton.getStyle().setCursor("pointer");
        addMajorButton.addClickListener(e -> openCreateNewSuggestedQuestionDialog());

        prevPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        prevPageButton.getStyle().setCursor(Cursor.POINTER.name());
        prevPageButton.setTooltipText("Previous page");
        prevPageButton.addClickListener(e -> {
            if (pageNumber > 0) {
                pageNumber--;
                updateSuggestedQuestionList();
            }
        });

        nextPageButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        nextPageButton.getStyle().setCursor(Cursor.POINTER.name());
        nextPageButton.setTooltipText("Next page");
        nextPageButton.addClickListener(e -> {
            long totaSuggestedQuestionsCount = (int) suggestedQuestionService.getSuggestedQuestionCount(new HashMap<>()); // Implement this method in your service
            int totalPages = (int) Math.ceil((double) totaSuggestedQuestionsCount / CustomConstants.SUGGESTED_QUESTION_PAGE_SIZE);
            if (pageNumber < totalPages - 1) {
                pageNumber++;
                updateSuggestedQuestionList();
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

    private void openCreateNewSuggestedQuestionDialog() {
        createNewSuggestedQuestionDialog.add(new HorizontalLayout(createNewSuggestedQuestionForm));
        createNewSuggestedQuestionDialog.open();
        createNewSuggestedQuestionForm.setSuggestedQuestion(new SuggestedQuestion());
        addClassName("scope-creating");
    }

    private void updateSuggestedQuestionList() {
        String keyword = filterText.getValue();
        params.put("pageNumber", pageNumber);
        params.put("pageSize", CustomConstants.SUGGESTED_QUESTION_PAGE_SIZE);
        params.put("keyword", keyword);

        List<SuggestedQuestion> scopes = suggestedQuestionService.getSuggestedQuestions(params);
        long totalSuggestedQuestions = suggestedQuestionService.getSuggestedQuestionCount(params);

        suggestedQuestionGrid.setItems(scopes);

        updatePaginationButtons();
        updatePaginationLabel(totalSuggestedQuestions);
    }

    private void updatePaginationLabel(long totalSuggestedQuestions) {
        int startSuggestedQuestion = pageNumber * CustomConstants.SUGGESTED_QUESTION_PAGE_SIZE + 1;
        int endSuggestedQuestion = (int) Math.min((long) (pageNumber + 1) * CustomConstants.SUGGESTED_QUESTION_PAGE_SIZE, totalSuggestedQuestions);

        // Update label with the current range of users being displayed
        paginationLabel.setText(startSuggestedQuestion + " - " + endSuggestedQuestion + " out of " + totalSuggestedQuestions);
    }

    private void updatePaginationButtons() {
        long totalSuggestedQuestionsCount = suggestedQuestionService.getSuggestedQuestionCount(new HashMap<>());
        int totalPages = (int) Math.ceil((double) totalSuggestedQuestionsCount / CustomConstants.SUGGESTED_QUESTION_PAGE_SIZE);

        prevPageButton.setEnabled(pageNumber > 0);
        nextPageButton.setEnabled(pageNumber < totalPages - 1);
    }
}
