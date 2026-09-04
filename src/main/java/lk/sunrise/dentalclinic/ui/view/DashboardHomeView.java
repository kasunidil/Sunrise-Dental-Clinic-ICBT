package lk.sunrise.dentalclinic.ui.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import lk.sunrise.dentalclinic.controller.PatientController;
import lk.sunrise.dentalclinic.controller.ReportController;
import lk.sunrise.dentalclinic.dto.ReportRequestDTO;
import lk.sunrise.dentalclinic.dto.RevenueReportDTO;
import lk.sunrise.dentalclinic.entity.Appointment;
import lk.sunrise.dentalclinic.entity.AppointmentStatus;
import lk.sunrise.dentalclinic.ui.session.SessionContext;
import lk.sunrise.dentalclinic.ui.util.Ui;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DashboardHomeView {
    private final VBox root = new VBox(22);
    private final PatientController patients = new PatientController();
    private final ReportController reports = new ReportController();

    public DashboardHomeView() {
        root.getStyleClass().add("dashboard-home");
        root.setPadding(new Insets(30, 32, 36, 32));

        DashboardData data = loadData();
        root.getChildren().addAll(buildWelcome(), buildKpis(data), buildWorkspace(data.appointments()), buildRevenue());
    }

    private DashboardData loadData() {
        try {
            List<Appointment> appointments = reports.dailyAppointments(new ReportRequestDTO(LocalDate.now(), LocalDate.now(), null, "SCREEN"));
            RevenueReportDTO revenue = reports.monthlyRevenue(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
            return new DashboardData(patients.search("").size(), appointments,
                    appointments.stream().filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count(), revenue.getRevenue());
        } catch (Exception ignored) {
            return new DashboardData(0, List.of(), 0, BigDecimal.ZERO);
        }
    }

    private HBox buildWelcome() {
        VBox copy = new VBox(6);
        Label eyebrow = new Label("CLINIC COMMAND CENTER");
        eyebrow.getStyleClass().add("dash-eyebrow");
        Label title = new Label("Good morning, " + SessionContext.getInstance().getFullName());
        title.getStyleClass().add("dash-title");
        Label sub = new Label("A clean view of your day, patients and clinic performance.");
        sub.getStyleClass().add("dash-subtitle");
        copy.getChildren().addAll(eyebrow, title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        VBox date = new VBox(4);
        date.setAlignment(Pos.CENTER_RIGHT);
        Label day = new Label(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE")));
        day.getStyleClass().add("dash-date-day");
        Label fullDate = new Label(LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        fullDate.getStyleClass().add("dash-date-value");
        date.getChildren().addAll(day, fullDate);

        HBox welcome = new HBox(24, copy, spacer, date);
        welcome.getStyleClass().add("dash-welcome");
        welcome.setAlignment(Pos.CENTER_LEFT);
        return welcome;
    }

    private HBox buildKpis(DashboardData data) {
        HBox row = new HBox(16);
        row.getStyleClass().add("dash-kpi-row");
        row.getChildren().addAll(
                kpi("Registered patients", String.valueOf(data.patientCount()), "Patient directory", FontAwesomeSolid.USERS),
                kpi("Appointments today", String.valueOf(data.appointments().size()), "Today's calendar", FontAwesomeSolid.CALENDAR_ALT),
                kpi("Completed visits", String.valueOf(data.completed()), "Care delivered", FontAwesomeSolid.CHECK_CIRCLE),
                kpi("Monthly revenue", "LKR " + money(data.revenue()), "Collected this month", FontAwesomeSolid.MONEY_BILL));
        for (var card : row.getChildren()) HBox.setHgrow(card, Priority.ALWAYS);
        return row;
    }

    private VBox kpi(String label, String value, String detail, FontAwesomeSolid icon) {
        HBox head = new HBox();
        Label iconLabel = new Label();
        iconLabel.setGraphic(Ui.icon(icon, 17));
        iconLabel.getStyleClass().add("dash-kpi-icon");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        head.getChildren().addAll(iconLabel, spacer);
        Label amount = new Label(value);
        amount.getStyleClass().add("dash-kpi-value");
        Label name = new Label(label);
        name.getStyleClass().add("dash-kpi-label");
        Label caption = new Label(detail);
        caption.getStyleClass().add("dash-kpi-caption");
        VBox card = new VBox(8, head, amount, name, caption);
        card.getStyleClass().add("dash-kpi-card");
        return card;
    }

    private HBox buildWorkspace(List<Appointment> appointments) {
        VBox schedule = scheduleCard(appointments);
        VBox status = statusCard(appointments);
        schedule.setPrefWidth(760);
        HBox.setHgrow(schedule, Priority.ALWAYS);
        HBox workspace = new HBox(18, schedule, status);
        workspace.getStyleClass().add("dash-workspace");
        return workspace;
    }

    private VBox scheduleCard(List<Appointment> appointments) {
        VBox card = new VBox(14);
        card.getStyleClass().add("dash-panel");
        HBox heading = panelHeading("Today's schedule", "Your appointments in one place");
        TableView<Appointment> table = new TableView<>();
        table.getColumns().addAll(
                column("Appointment", Appointment::getAppointmentNo),
                column("Patient", a -> a.getPatient() == null ? "" : a.getPatient().getFullName()),
                column("Dentist", a -> a.getDentist() == null ? "" : a.getDentist().getFullName()),
                column("Time", a -> a.getStartTime() == null ? "" : a.getStartTime().toString()),
                column("Status", a -> a.getStatus() == null ? "" : a.getStatus().name()));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No appointments scheduled for today."));
        table.setItems(javafx.collections.FXCollections.observableArrayList(appointments));
        table.getStyleClass().add("dash-schedule-table");
        table.setPrefHeight(290);
        card.getChildren().addAll(heading, table);
        return card;
    }

    private VBox statusCard(List<Appointment> appointments) {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("dash-panel", "dash-status-panel");
        card.getChildren().add(panelHeading("Visit status", "Today's appointment mix"));
        PieChart chart = new PieChart();
        chart.setLegendVisible(true);
        chart.setLabelsVisible(false);
        chart.setPrefSize(280, 245);
        for (AppointmentStatus state : AppointmentStatus.values()) {
            long count = appointments.stream().filter(a -> a.getStatus() == state).count();
            if (count > 0) chart.getData().add(new PieChart.Data(state.name(), count));
        }
        if (chart.getData().isEmpty()) chart.setTitle("No visits yet");
        chart.getStyleClass().add("dash-status-chart");
        card.getChildren().add(chart);
        return card;
    }

    private VBox buildRevenue() {
        VBox card = new VBox(12);
        card.getStyleClass().addAll("dash-panel", "dash-revenue-panel");
        card.getChildren().add(panelHeading("Revenue performance", "Monthly billing collected"));
        BarChart<String, Number> chart = new BarChart<>(new CategoryAxis(), new NumberAxis());
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefHeight(230);
        try {
            RevenueReportDTO report = reports.monthlyRevenue(LocalDate.now().getYear(), LocalDate.now().getMonthValue());
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>(LocalDate.now().getMonth().name(), report.getRevenue()));
            chart.getData().add(series);
        } catch (Exception ignored) { }
        chart.getStyleClass().add("dash-revenue-chart");
        card.getChildren().add(chart);
        return card;
    }

    private HBox panelHeading(String title, String subtitle) {
        VBox text = new VBox(3);
        Label heading = new Label(title);
        heading.getStyleClass().add("dash-panel-title");
        Label sub = new Label(subtitle);
        sub.getStyleClass().add("dash-panel-subtitle");
        text.getChildren().addAll(heading, sub);
        return new HBox(text);
    }

    private <T> TableColumn<Appointment, T> column(String title, java.util.function.Function<Appointment, T> fn) {
        TableColumn<Appointment, T> column = new TableColumn<>(title);
        column.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(fn.apply(cell.getValue())));
        return column;
    }

    private String money(BigDecimal value) {
        return (value == null ? BigDecimal.ZERO : value).setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
    }

    private record DashboardData(int patientCount, List<Appointment> appointments, long completed, BigDecimal revenue) { }
    public VBox root() { return root; }
}
