package lk.sunrise.dentalclinic.ui.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import lk.sunrise.dentalclinic.controller.AuthController;
import lk.sunrise.dentalclinic.entity.UserRole;
import lk.sunrise.dentalclinic.ui.util.Ui;

public class UserManagementView {
    private final VBox root = new VBox(20);
    private final TextField username = Ui.textField("Username"), fullName = Ui.textField("Full name"), email = Ui.textField("Email address");
    private final PasswordField password = Ui.passwordField("Temporary password");
    private final ComboBox<UserRole> role = new ComboBox<>();
    private final Dialog<Void> dialog = new Dialog<>();
    public UserManagementView() {
        root.setPadding(new Insets(30, 32, 36, 32)); root.getStyleClass().add("management-page"); buildDialog();
        Label eyebrow = new Label("ACCESS CONTROL"); eyebrow.getStyleClass().add("patient-eyebrow"); Label title = new Label("Team accounts"); title.getStyleClass().add("patient-title"); Label copy = new Label("Create secure accounts for your clinic team and assign only the access they need."); copy.getStyleClass().add("patient-subtitle");
        Button create = Ui.button("Create account", "primary-button"); create.setOnAction(e -> { clear(); dialog.show(); });
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS); HBox hero = new HBox(18, new VBox(4, eyebrow, title, copy), spacer, create); hero.setAlignment(Pos.CENTER_LEFT); hero.getStyleClass().add("patient-hero");
        VBox info = Ui.card("How access works"); Label text = new Label("Accounts are created with a role-based workspace. Administrators can manage dentists, treatments and accounts; clinical and reception roles only see the tools relevant to their day."); text.setWrapText(true); text.getStyleClass().add("label-muted"); info.getChildren().add(text);
        root.getChildren().addAll(hero, info);
    }
    private void buildDialog() {
        dialog.setTitle("Create team account"); dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE); dialog.getDialogPane().getStyleClass().add("patient-editor-dialog");
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/app.css").toExternalForm()); dialog.getDialogPane().getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/patient.css").toExternalForm());
        role.setItems(FXCollections.observableArrayList(UserRole.values())); role.setPromptText("Select role");
        Label title = new Label("New team member"); title.getStyleClass().add("patient-modal-title"); Label tag = new Label("NEW ACCOUNT"); tag.getStyleClass().add("patient-modal-mode"); HBox header = new HBox(10, title, tag); header.setAlignment(Pos.CENTER_LEFT);
        GridPane grid = Ui.grid(); grid.setHgap(16); grid.setVgap(12); field(grid, 0, 0, "Full name", fullName); field(grid, 2, 0, "Username", username); field(grid, 0, 1, "Email address", email); field(grid, 2, 1, "Temporary password", password); field(grid, 0, 2, "Role", role);
        Button cancel = Ui.button("Cancel", "outline-button"); cancel.setOnAction(e -> dialog.close()); Button save = Ui.button("Create account", "primary-button"); save.setOnAction(e -> create()); Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox content = new VBox(18, header, new Separator(), grid, new HBox(10, cancel, spacer, save)); content.getStyleClass().add("patient-editor-content"); content.setPrefWidth(620); dialog.getDialogPane().setContent(content);
    }
    private void field(GridPane g, int col, int row, String label, javafx.scene.Node node) { g.add(Ui.fieldLabel(label), col, row); g.add(node, col + 1, row); GridPane.setHgrow(node, Priority.ALWAYS); if (node instanceof Region r) r.setMaxWidth(Double.MAX_VALUE); }
    private void create() { try { if (new AuthController().register(username.getText(), password.getText(), fullName.getText(), email.getText(), role.getValue())) { dialog.close(); Ui.notify(root, "User created", "The account was saved to MySQL.", false); clear(); } } catch (Exception e) { Ui.error(root, e); } }
    private void clear() { username.clear(); password.clear(); fullName.clear(); email.clear(); role.setValue(null); }
    public VBox root() { return root; }
}
