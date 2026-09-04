package lk.sunrise.dentalclinic.ui.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import lk.sunrise.dentalclinic.entity.Gender;
import lk.sunrise.dentalclinic.entity.Patient;
import lk.sunrise.dentalclinic.ui.controller.PatientViewController;
import lk.sunrise.dentalclinic.ui.util.Ui;

public class PatientView {
    private final VBox root = new VBox(20);
    private final PatientViewController controller;
    private final TextField search = Ui.textField("Search by name, code or phone number");
    private final TableView<Patient> table = new TableView<>();
    private final Button addPatient = Ui.button("Add patient", "primary-button");
    private final Button editPatient = Ui.button("Edit selected", "outline-button");
    private final Button viewDetails = Ui.button("View profile", "outline-button");

    /* The editor fields live exclusively inside the modal. */
    private final TextField name = Ui.textField("Full name");
    private final TextField contact = Ui.textField("Contact number");
    private final TextField email = Ui.textField("Email address");
    private final TextField address = Ui.textField("Home address");
    private final DatePicker dob = new DatePicker();
    private final ComboBox<Gender> gender = new ComboBox<>();
    private final TextArea history = new TextArea();
    private final Button save = Ui.button("Register patient", "primary-button");
    private final Button update = Ui.button("Save changes", "primary-button");
    private final Button clear = Ui.button("Reset", "outline-button");
    private final Label mode = new Label();
    private final Dialog<Void> editor = new Dialog<>();

    public PatientView() {
        controller = new PatientViewController(this);
        root.getStyleClass().add("patient-page");
        root.setPadding(new Insets(30, 32, 36, 32));
        root.setFillWidth(true);
        buildEditor();

        VBox pageCopy = new VBox(4);
        Label eyebrow = new Label("PATIENT DIRECTORY");
        eyebrow.getStyleClass().add("patient-eyebrow");
        Label title = new Label("Patient records");
        title.getStyleClass().add("patient-title");
        Label sub = new Label("Keep every patient profile accurate, accessible and secure.");
        sub.getStyleClass().add("patient-subtitle");
        pageCopy.getChildren().addAll(eyebrow, title, sub);
        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);
        HBox hero = new HBox(20, pageCopy, titleSpacer, addPatient);
        hero.getStyleClass().add("patient-hero");
        hero.setAlignment(Pos.CENTER_LEFT);
        addPatient.setPrefHeight(40);

        Button searchButton = Ui.button("Search", "secondary-button");
        HBox searchRow = new HBox(10, search, searchButton);
        searchRow.getStyleClass().add("patient-search-row");
        search.setPrefHeight(42);
        HBox.setHgrow(search, Priority.ALWAYS);
        searchButton.setPrefHeight(42);
        searchButton.setOnAction(e -> controller.search());
        search.setOnAction(e -> controller.search());

        HBox tableHeader = new HBox(10);
        VBox recordsCopy = new VBox(3);
        Label recordsTitle = new Label("All patients");
        recordsTitle.getStyleClass().add("patient-panel-title");
        Label recordsSub = new Label("Select a record to view or update its profile.");
        recordsSub.getStyleClass().add("patient-panel-subtitle");
        recordsCopy.getChildren().addAll(recordsTitle, recordsSub);
        Region recordSpacer = new Region();
        HBox.setHgrow(recordSpacer, Priority.ALWAYS);
        tableHeader.getChildren().addAll(recordsCopy, recordSpacer, viewDetails, editPatient);
        tableHeader.setAlignment(Pos.CENTER_LEFT);

        table.getColumns().addAll(col("Patient ID", "patientCode"), col("Full name", "fullName"),
                col("Date of birth", "dateOfBirth"), col("Gender", "gender"),
                col("Phone", "contactNumber"), col("Email", "email"));
        table.setPlaceholder(new Label("No patient records found."));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.getStyleClass().add("patient-table");
        table.setPrefHeight(460);
        table.setRowFactory(tv -> {
            TableRow<Patient> row = new TableRow<>();
            row.setOnMouseClicked(e -> { if (!row.isEmpty() && e.getClickCount() == 2) controller.openEdit(); });
            return row;
        });

        VBox records = new VBox(18, tableHeader, table);
        records.getStyleClass().add("patient-records-panel");
        root.getChildren().addAll(hero, searchRow, records);

        addPatient.setOnAction(e -> controller.openCreate());
        editPatient.setOnAction(e -> controller.openEdit());
        viewDetails.setOnAction(e -> controller.viewSelected());
        controller.initialize();
    }

    private void buildEditor() {
        editor.setTitle("Patient profile");
        editor.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        editor.getDialogPane().getStyleClass().add("patient-editor-dialog");
        editor.getDialogPane().getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/app.css").toExternalForm());
        editor.getDialogPane().getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/patient.css").toExternalForm());

        mode.getStyleClass().add("patient-modal-mode");
        Label heading = new Label("Patient details");
        heading.getStyleClass().add("patient-modal-title");
        HBox modalHeader = new HBox(10, heading, mode);
        modalHeader.setAlignment(Pos.CENTER_LEFT);

        gender.setItems(FXCollections.observableArrayList(Gender.values()));
        gender.setPromptText("Select gender");
        dob.setPromptText("Select date of birth");
        history.setPromptText("Relevant medical notes, allergies or history");
        history.setPrefRowCount(4);
        history.setWrapText(true);

        GridPane fields = Ui.grid();
        fields.getStyleClass().add("patient-editor-grid");
        fields.setHgap(16);
        fields.setVgap(12);
        addField(fields, 0, 0, "Full name", name); addField(fields, 2, 0, "Date of birth", dob);
        addField(fields, 0, 1, "Gender", gender); addField(fields, 2, 1, "Phone number", contact);
        addField(fields, 0, 2, "Email address", email); addField(fields, 2, 2, "Address", address);
        fields.add(Ui.fieldLabel("Medical history"), 0, 3);
        fields.add(history, 1, 3, 3, 1);
        GridPane.setHgrow(history, Priority.ALWAYS);

        Region actionSpacer = new Region();
        HBox.setHgrow(actionSpacer, Priority.ALWAYS);
        HBox actions = new HBox(10, clear, actionSpacer, save, update);
        actions.setAlignment(Pos.CENTER_RIGHT);
        VBox content = new VBox(18, modalHeader, new Separator(), fields, actions);
        content.getStyleClass().add("patient-editor-content");
        content.setPrefWidth(680);
        editor.getDialogPane().setContent(content);
    }

    private void addField(GridPane grid, int col, int row, String label, javafx.scene.Node node) {
        grid.add(Ui.fieldLabel(label), col, row);
        grid.add(node, col + 1, row);
        GridPane.setHgrow(node, Priority.ALWAYS);
        if (node instanceof Region region) region.setMaxWidth(Double.MAX_VALUE);
    }

    private <T> TableColumn<Patient, T> col(String text, String property) {
        TableColumn<Patient, T> column = new TableColumn<>(text);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        return column;
    }

    public void showEditor(boolean create) {
        mode.setText(create ? "NEW PATIENT" : "EDITING PROFILE");
        save.setVisible(create); save.setManaged(create);
        update.setVisible(!create); update.setManaged(!create);
        editor.show();
    }
    public void closeEditor() { editor.close(); }
    public VBox root() { return root; }
    public TextField search() { return search; } public TextField name() { return name; }
    public TextField contact() { return contact; } public TextField email() { return email; } public TextField address() { return address; }
    public DatePicker dob() { return dob; } public ComboBox<Gender> gender() { return gender; } public TextArea history() { return history; }
    public Button save() { return save; } public Button update() { return update; } public Button clear() { return clear; }
    public Button viewDetails() { return viewDetails; } public TableView<Patient> table() { return table; } public Label mode() { return mode; }
}
