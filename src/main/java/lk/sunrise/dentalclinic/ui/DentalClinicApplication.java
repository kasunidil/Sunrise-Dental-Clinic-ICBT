package lk.sunrise.dentalclinic.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lk.sunrise.dentalclinic.ui.view.LoginView;

public class DentalClinicApplication extends Application {
    @Override public void start(Stage stage) {
        Navigation.init(stage);
        stage.setTitle("Sunrise Dental Clinic Management System");
        // Keep the native Windows title bar and its minimize/maximize/close controls available.
        stage.setResizable(true);
        Scene scene = new LoginView().scene();
        stage.setScene(scene); stage.setMinWidth(900); stage.setMinHeight(520); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
