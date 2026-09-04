package lk.sunrise.dentalclinic.ui.controller;

import java.math.BigDecimal;
import java.time.LocalTime;
import lk.sunrise.dentalclinic.controller.DentistController;
import lk.sunrise.dentalclinic.dto.DentistDTO;
import lk.sunrise.dentalclinic.entity.Dentist;
import lk.sunrise.dentalclinic.ui.util.Ui;
import lk.sunrise.dentalclinic.ui.view.DentistView;

public class DentistViewController {
    private final DentistView view; private final DentistController controller = new DentistController();
    public DentistViewController(DentistView view) { this.view = view; }
    public void initialize() { view.save().setOnAction(e -> save()); view.update().setOnAction(e -> update()); view.reset().setOnAction(e -> clear()); search(); }
    public void search() { try { view.table().getItems().setAll(controller.search(view.search().getText())); } catch (Exception e) { Ui.error(view.root(), e); } }
    public void openCreate() { clear(); view.showEditor(true); }
    public void openEdit() { Dentist d = view.table().getSelectionModel().getSelectedItem(); if (d == null) { Ui.error(view.root(), new IllegalArgumentException("Select a dentist first.")); return; } load(d); view.showEditor(false); }
    private void load(Dentist d) { view.name().setText(d.getFullName()); view.slmc().setText(d.getSlmcNumber()); view.special().setText(d.getSpecialization()); view.contact().setText(d.getContactNumber()); view.email().setText(d.getEmail()); view.fee().setText(d.getConsultationFee().toPlainString()); view.start().setText(d.getWorkingHoursStart().toString()); view.end().setText(d.getWorkingHoursEnd().toString()); view.available().setSelected(d.isAvailable()); }
    private void clear() { view.name().clear(); view.slmc().clear(); view.special().clear(); view.contact().clear(); view.email().clear(); view.fee().clear(); view.start().setText("09:00"); view.end().setText("17:00"); view.available().setSelected(true); }
    private DentistDTO dto(int id) { return new DentistDTO(id, null, view.name().getText(), view.slmc().getText(), view.special().getText(), view.contact().getText(), view.email().getText(), new BigDecimal(view.fee().getText()), LocalTime.parse(view.start().getText()), LocalTime.parse(view.end().getText()), view.available().isSelected()); }
    private void save() { try { DentistDTO d = controller.register(dto(0)); search(); view.closeEditor(); Ui.notify(view.root(), "Dentist added", d.getDentistCode() + " was saved.", false); } catch (Exception e) { Ui.error(view.root(), e); } }
    private void update() { try { Dentist d = view.table().getSelectionModel().getSelectedItem(); if (d == null) throw new IllegalArgumentException("Select a dentist first."); controller.update(dto(d.getDentistId())); search(); view.closeEditor(); Ui.notify(view.root(), "Dentist updated", "Changes saved.", false); } catch (Exception e) { Ui.error(view.root(), e); } }
}
