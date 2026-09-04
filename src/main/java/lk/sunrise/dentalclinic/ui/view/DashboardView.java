package lk.sunrise.dentalclinic.ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lk.sunrise.dentalclinic.entity.UserRole;
import lk.sunrise.dentalclinic.ui.Navigation;
import lk.sunrise.dentalclinic.ui.controller.DashboardViewController;
import lk.sunrise.dentalclinic.ui.session.SessionContext;
import lk.sunrise.dentalclinic.ui.util.Ui;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

/** Signed-in application shell with a compact horizontal navigation. */
public class DashboardView {
    private final BorderPane root = new BorderPane();
    private final HBox nav = new HBox(4);
    private final StackPane content = new StackPane();
    private final Label pageTitle = new Label("Dashboard");
    private final Label userLabel = new Label();
    private final DashboardViewController controller;

    public DashboardView() {
        root.getStyleClass().add("app-shell");
        root.setTop(buildTopbar());
        content.getStyleClass().add("app-content");
        content.setPadding(Insets.EMPTY);
        root.setCenter(content);
        controller = new DashboardViewController(this);
        controller.initialize();
    }

    private HBox buildTopbar() {
        HBox bar = new HBox(14);
        bar.getStyleClass().addAll("topbar", "app-navbar");
        bar.setAlignment(Pos.CENTER_LEFT);
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/lk/sunrise/dentalclinic/ui/clinic-logo.png")));
        logo.setFitHeight(34); logo.setFitWidth(34); logo.setPreserveRatio(true);
        Label brand = new Label("SUNRISE"); brand.getStyleClass().add("nav-brand");
        Label clinic = new Label("DENTAL CLINIC"); clinic.getStyleClass().add("nav-brand-sub");
        HBox brandBox = new HBox(8, logo, new VBox(0, brand, clinic)); brandBox.setAlignment(Pos.CENTER_LEFT);
        Separator separator = new Separator(); separator.setOrientation(javafx.geometry.Orientation.VERTICAL); separator.getStyleClass().add("nav-separator");
        nav.getStyleClass().add("top-nav-list"); nav.setAlignment(Pos.CENTER_LEFT);
        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        userLabel.getStyleClass().add("role-pill");
        Button expand = Ui.iconButton("Maximize window", FontAwesomeSolid.EXPAND, "nav-icon-button");
        expand.setOnAction(e -> Navigation.stage().setMaximized(!Navigation.stage().isMaximized()));
        Button logout = new Button("Sign out", Ui.icon(FontAwesomeSolid.SIGN_OUT_ALT, 14));
        logout.getStyleClass().add("nav-logout-button"); logout.setOnAction(e -> controller.logout());
        bar.getChildren().addAll(brandBox, separator, nav, spacer, userLabel, expand, logout);
        return bar;
    }

    public Scene scene() {
        Scene s = new Scene(root, 1280, 820);
        s.getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/app.css").toExternalForm());
        s.getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/dashboard.css").toExternalForm());
        s.getStylesheets().add(getClass().getResource("/lk/sunrise/dentalclinic/ui/patient.css").toExternalForm());
        return s;
    }
    public HBox nav() { return nav; }
    public StackPane content() { return content; }
    public Label pageTitle() { return pageTitle; }
    public Label userLabel() { return userLabel; }
    public boolean canAccess(String key) {
        UserRole r = SessionContext.getInstance().getRole();
        return switch (key) {
            case "dashboard" -> r != null;
            case "patients", "appointments", "billing" -> r == UserRole.ADMIN || r == UserRole.RECEPTIONIST || r == UserRole.DENTIST;
            case "dentists", "treatments" -> r == UserRole.ADMIN;
            case "history" -> r == UserRole.ADMIN || r == UserRole.DENTIST;
            case "reports" -> r == UserRole.ADMIN || r == UserRole.MANAGEMENT;
            case "users" -> r == UserRole.ADMIN;
            default -> false;
        };
    }
}
