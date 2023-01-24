package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import de.vonraesfeld.manhart.aldenkirchs.application.service.VersionsverwaltungService;

public class BearbeitenDialog extends Dialog {

  final DateiVersionEditPanel editPanel;
  private final DateiVersion dateiVersion;
  private final TabellenView tabellenView;
  private final VersionsverwaltungService versionsverwaltungService;

  public BearbeitenDialog(DateiVersion dateiVersion, TabellenView tabellenView,
                          VersionsverwaltungService versionsverwaltungService) {
    this.dateiVersion = dateiVersion;
    this.tabellenView = tabellenView;
    this.versionsverwaltungService = versionsverwaltungService;
    setHeaderTitle("Eintrag bearbeiten");
    setMaxWidth("400px");

    configureFooter();

    editPanel =
        new DateiVersionEditPanel(versionsverwaltungService);
    editPanel.setDateiVersion(dateiVersion);
    add(editPanel);
  }

  private void configureFooter() {
    Button closeButton = new Button("Schließen", e -> this.close());
    Button saveButton = new Button("Speichern", e -> this.validateAndSave());
    Button deleteButton = new Button("Löschen", e -> this.delete());
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    getFooter().add(saveButton, deleteButton, closeButton);
  }

  private void delete() {
    versionsverwaltungService.deleteDateiVersion(dateiVersion);
    tabellenView.updateList(null);
    final Notification notification = Notification.show("Datei wurde gelöscht.");
    notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
    notification.setPosition(Notification.Position.BOTTOM_CENTER);
    close();
  }

  private void validateAndSave() {
    boolean success = editPanel.validateAndSave();
    tabellenView.updateList(null);
    if (success) {
      final Notification notification = Notification.show("Datei erfolgreich gespeichert.");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      notification.setPosition(Notification.Position.BOTTOM_CENTER);
    } else {
      final Notification notification = Notification.show("Speichern der Datei fehlgeschlagen!");
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      notification.setPosition(Notification.Position.BOTTOM_CENTER);
    }
    close();
  }

}
