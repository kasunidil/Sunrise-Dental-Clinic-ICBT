package lk.sunrise.dentalclinic.ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import lk.sunrise.dentalclinic.ui.controller.LoginViewController;
import lk.sunrise.dentalclinic.ui.util.Ui;

public class LoginView {

    private final StackPane root = new StackPane();

    private final TextField username = Ui.textField("Username");
    private final PasswordField password = Ui.passwordField("Password");
    private final Label error = new Label();
    private final Button login = Ui.button("Sign in", "primary-button");

    /*
     * Free dental clinic image from Unsplash.
     * You can replace this URL with another Unsplash image later.
     */
    private static final String BACKGROUND_IMAGE =
            "https://images.unsplash.com/photo-1609840114035-3c981b782dfe"
                    + "?auto=format&fit=crop&w=1920&q=85";

    public LoginView() {

        // =========================================================
        // BACKGROUND IMAGE
        // =========================================================

        ImageView backgroundImage = new ImageView();

        Image image = new Image(
                BACKGROUND_IMAGE,
                true
        );

        backgroundImage.setImage(image);
        backgroundImage.setPreserveRatio(false);

        // Make the image fill the complete login window
        backgroundImage.fitWidthProperty().bind(root.widthProperty());
        backgroundImage.fitHeightProperty().bind(root.heightProperty());

        // =========================================================
        // DARK / WHITE OVERLAY
        // Helps the login content remain readable
        // =========================================================

        Region backgroundOverlay = new Region();
        backgroundOverlay.setStyle(
                "-fx-background-color: rgba(0, 0, 0, 0.38);"
        );

        backgroundOverlay.prefWidthProperty().bind(root.widthProperty());
        backgroundOverlay.prefHeightProperty().bind(root.heightProperty());

        // =========================================================
        // MAIN CONTENT
        // =========================================================

        HBox layout = new HBox();
        layout.setAlignment(Pos.CENTER);
        layout.setFillHeight(true);

        // =========================================================
        // LEFT HERO SECTION
        // =========================================================

        VBox hero = new VBox(18);
        hero.getStyleClass().add("login-hero");

        hero.setPrefWidth(560);
        hero.setMaxWidth(560);

        Label eyebrow = new Label("SUNRISE DENTAL CLINIC");
        eyebrow.getStyleClass().add("login-eyebrow");

        Label heroTitle = new Label(
                "Care, made\nmore connected."
        );
        heroTitle.getStyleClass().add("login-hero-title");

        Label heroCopy = new Label(
                "A calm, focused workspace for every appointment, "
                        + "patient and payment."
        );

        heroCopy.getStyleClass().add("login-hero-copy");
        heroCopy.setWrapText(true);
        heroCopy.setMaxWidth(370);

        Region heroSpacer = new Region();
        VBox.setVgrow(heroSpacer, Priority.ALWAYS);

        Label quote = new Label(
                "A better clinic day starts with a clear view."
        );

        quote.getStyleClass().add("login-quote");
        quote.setWrapText(true);

        hero.getChildren().addAll(
                eyebrow,
                heroTitle,
                heroCopy,
                heroSpacer,
                quote
        );

        // =========================================================
        // LOGIN CARD
        // =========================================================

        VBox card = new VBox(15);
        card.getStyleClass().add("login-card");

        card.setMaxWidth(430);
        card.setPrefWidth(430);

        Label brand = new Label("SUNRISE / PORTAL");
        brand.getStyleClass().add("login-accent");

        Label title = new Label("Welcome back");
        title.getStyleClass().add("login-title");

        Label sub = new Label(
                "Sign in to continue to your clinic workspace."
        );

        sub.getStyleClass().add("label-muted");
        sub.setWrapText(true);

        // Input sizes
        username.setPrefHeight(44);
        username.setMaxWidth(Double.MAX_VALUE);

        password.setPrefHeight(44);
        password.setMaxWidth(Double.MAX_VALUE);

        login.setPrefHeight(44);
        login.setMaxWidth(Double.MAX_VALUE);

        error.getStyleClass().add("error-text");
        error.setWrapText(true);

        card.getChildren().addAll(
                brand,
                title,
                sub,

                Ui.fieldLabel("Username"),
                username,

                Ui.fieldLabel("Password"),
                password,

                login,
                error
        );

        // =========================================================
        // FORM PANEL
        // =========================================================

        StackPane formPanel = new StackPane(card);

        formPanel.setPadding(
                new Insets(40, 70, 40, 40)
        );

        HBox.setHgrow(
                formPanel,
                Priority.ALWAYS
        );

        // =========================================================
        // ADD CONTENT TO MAIN LAYOUT
        // =========================================================

        layout.getChildren().addAll(
                hero,
                formPanel
        );

        // =========================================================
        // ROOT
        //
        // Order is important:
        // 1. Background image
        // 2. Overlay
        // 3. Login content
        // =========================================================

        root.getChildren().addAll(
                backgroundImage,
                backgroundOverlay,
                layout
        );

        // =========================================================
        // CONTROLLER
        // =========================================================

        new LoginViewController(this).initialize();
    }

    // =============================================================
    // SCENE
    // =============================================================

    public Scene scene() {

        Scene scene = new Scene(
                root,
                1200,
                760
        );

        scene.getStylesheets().add(
                getClass()
                        .getResource(
                                "/lk/sunrise/dentalclinic/ui/app.css"
                        )
                        .toExternalForm()
        );

        return scene;
    }

    // =============================================================
    // GETTERS
    // =============================================================

    public TextField usernameField() {
        return username;
    }

    public PasswordField passwordField() {
        return password;
    }

    public Button loginButton() {
        return login;
    }

    public Label errorLabel() {
        return error;
    }

    public StackPane root() {
        return root;
    }
}

