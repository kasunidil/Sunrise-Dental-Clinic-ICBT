package lk.sunrise.dentalclinic.ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import lk.sunrise.dentalclinic.entity.Dentist;
import lk.sunrise.dentalclinic.ui.controller.DentistViewController;
import lk.sunrise.dentalclinic.ui.util.Ui;

public class DentistView {
    private final VBox root = new VBox(20);
    private final TextField search = Ui.textField("Search dentist, SLMC or code");
    private final TableView<Dentist> table = new TableView<>();
    private final TextField name = Ui.textField("Full name"), slmc = Ui.textField("SLMC number"), special = Ui.textField("Specialization"), contact = Ui.textField("Contact number"), email = Ui.textField("Email address"), fee = Ui.textField("Consultation fee");
    private final TextField start = Ui.textField("09:00"), end = Ui.textField("17:00");
    private final CheckBox available = new CheckBox("Available for appointments");
    private final Button save = Ui.button("Add dentist", "primary-button"), update = Ui.button("Save changes", "primary-button"), reset = Ui.button("Reset", "outline-button");
    private final Dialog<Void> editor = new Dialog<>();
    private final Label mode = new Label();
    private final DentistViewController controller;

    public DentistView() {
        controller = new DentistViewController(this);
        root.getStyleClass().add("management-page"); root.setPadding(new Insets(30, 32, 36, 32));
        buildEditor();
        VBox copy = new VBox(4, styled("DENTIST DIRECTORY", "patient-eyebrow"), styled("Dentist profiles", "patient-title"), styled("Manage clinic practitioners, availability and consultation fees.", "patient-subtitle"));
        Button add = Ui.button("Add dentist", "primary-button"); add.setOnAction(e -> controller.openCreate());
        Region heroSpace = new Region(); HBox.setHgrow(heroSpace, Priority.ALWAYS);
        HBox hero = new HBox(18, copy, heroSpace, add); hero.getStyleClass().add("patient-hero"); hero.setAlignment(Pos.CENTER_LEFT);
        Button find = Ui.button("Search", "secondary-button"); find.setOnAction(e -> controller.search()); search.setOnAction(e -> controller.search());
        HBox searchRow = new HBox(10, search, find); searchRow.getStyleClass().add("patient-search-row"); HBox.setHgrow(search, Priority.ALWAYS);
        Button edit = Ui.button("Edit selected", "outline-button"); edit.setOnAction(e -> controller.openEdit());
        VBox recordCopy = new VBox(3, styled("All dentists", "patient-panel-title"), styled("Select a practitioner to update their profile.", "patient-panel-subtitle"));
        Region tableSpace = new Region(); HBox.setHgrow(tableSpace, Priority.ALWAYS);
        HBox header = new HBox(10, recordCopy, tableSpace, edit); header.setAlignment(Pos.CENTER_LEFT);
        table.getColumns().addAll(col("Code", "dentistCode"), col("Name", "fullName"), col("SLMC", "slmcNumber"), col("Specialization", "specialization"), col("Fee", "consultationFee"), col("Available", "available"));
        table.setPlaceholder(new Label("No dentists found.")); table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); table.setPrefHeight(460);
        table.setRowFactory(tv -> { TableRow<Dentist> row = new TableRow<>(); row.setOnMouseClicked(e -> { if (!row.isEmpty() && e.getClickCount() == 2) controller.openEdit(); }); return row; });
        VBox records = new VBox(18, header, table); records.getStyleClass().add("patient-records-panel");
        root.getChildren().addAll(hero, searchRow, records); controller.initialize();
    }
    private Label styled(String value, String css) { Label l = new Label(value); l.getStyleClass().add(css); return l; }
    private void buildEditor() {
        editor.setTitle("Dentist profile"); editor.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); editor.getDialogPane().getStyleClass().add("patient-editor-dialog");
        editor.getDialogPane().getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/app.css").toExternalForm()); editor.getDialogPane().getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/patient.css").toExternalForm());
        mode.getStyleClass().add("patient-modal-mode"); Label heading = styled("Dentist details", "patient-modal-title"); HBox modalHeader = new HBox(10, heading, mode); modalHeader.setAlignment(Pos.CENTER_LEFT);
        GridPane grid = Ui.grid(); grid.setHgap(16); grid.setVgap(12);
        field(grid, 0, 0, "Full name", name); field(grid, 2, 0, "SLMC number", slmc); field(grid, 0, 1, "Specialization", special); field(grid, 2, 1, "Contact number", contact); field(grid, 0, 2, "Email address", email); field(grid, 2, 2, "Consultation fee (LKR)", fee); field(grid, 0, 3, "Start time", start); field(grid, 2, 3, "End time", end); grid.add(available, 1, 4, 3, 1);
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS); HBox actions = new HBox(10, reset, spacer, save, update); actions.setAlignment(Pos.CENTER_RIGHT);
        VBox content = new VBox(18, modalHeader, new Separator(), grid, actions); content.getStyleClass().add("patient-editor-content"); content.setPrefWidth(680); editor.getDialogPane().setContent(content);
    }
    private void field(GridPane grid, int col, int row, String label, javafx.scene.Node node) { grid.add(Ui.fieldLabel(label), col, row); grid.add(node, col + 1, row); GridPane.setHgrow(node, Priority.ALWAYS); if (node instanceof Region r) r.setMaxWidth(Double.MAX_VALUE); }
    private <T> TableColumn<Dentist, T> col(String text, String property) { TableColumn<Dentist, T> c = new TableColumn<>(text); c.setCellValueFactory(new PropertyValueFactory<>(property)); return c; }
    public void showEditor(boolean create) { mode.setText(create ? "NEW DENTIST" : "EDITING PROFILE"); save.setVisible(create); save.setManaged(create); update.setVisible(!create); update.setManaged(!create); editor.show(); }
    public void closeEditor() { editor.close(); } public VBox root(){return root;} public TextField search(){return search;} public TextField name(){return name;} public TextField slmc(){return slmc;} public TextField special(){return special;} public TextField contact(){return contact;} public TextField email(){return email;} public TextField fee(){return fee;} public TextField start(){return start;} public TextField end(){return end;} public CheckBox available(){return available;} public Button save(){return save;} public Button update(){return update;} public Button reset(){return reset;} public TableView<Dentist> table(){return table;}
}
