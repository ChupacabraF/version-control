package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.shared.Registration;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import de.vonraesfeld.manhart.aldenkirchs.application.service.VersionsverwaltungService;
import java.io.ByteArrayInputStream;
import org.apache.commons.io.IOUtils;

public class DateiVersionEditPanel extends FormLayout {

  private final MainView mainView;
  private final VersionsverwaltungService versionsverwaltungService;
  private final Button save = new Button("Speichern");
  private final Button delete = new Button("Löschen");
  private final Button close = new Button("Abbrechen");
  private Upload dateiUpload;
  private final Anchor downloadLink = new Anchor("", "");
  private final TextField kommentar = new TextField("Kommentar");
  private final IntegerField version = new IntegerField("Version");

  private final Checkbox gesperrt = new Checkbox("Datei für Änderungen gesperrt?");
  private final Label downloadLinkLabel = new Label("Downloadlink:");


  private DateiVersion dateiVersion;

  Binder<DateiVersion> binder = new BeanValidationBinder<>(DateiVersion.class);

  public DateiVersionEditPanel(final MainView mainView,
      final VersionsverwaltungService versionsverwaltungService) {
    this.mainView = mainView;
    this.versionsverwaltungService = versionsverwaltungService;
    binder.bindInstanceFields(this);
    binder.bind(gesperrt, DateiVersion::getGesperrt, DateiVersion::setGesperrt);

    //Inputfelder erstellen bzw. konfigurieren
    createDateiUploadFeld();
    gesperrt.setClassName("gesperrt");
    version.setStepButtonsVisible(true);
    //TODO: keine Ahnung warum das nicht funktioniert
    version.setWidth("7em");
    downloadLinkLabel.setVisible(false);

    add(kommentar);
    add(version);
    add(gesperrt);
    add(downloadLinkLabel);
    add(downloadLink);
    add(dateiUpload);
    add(createButtonsLayout());
  }

  private void createDateiUploadFeld() {
    final MemoryBuffer memoryBuffer = new MemoryBuffer();
    dateiUpload = new Upload(memoryBuffer);
    dateiUpload.addSucceededListener(event -> {
      final String fileName = memoryBuffer.getFileName();
      dateiVersion.setDateiname(fileName);
      dateiVersion.setDateityp(getFileExtension(fileName));
      try {
        final byte[] bytes = IOUtils.toByteArray(memoryBuffer.getInputStream());
        dateiVersion.setFile(bytes);
      } catch (final Exception e) {
        e.printStackTrace();
      }
      if (!versionsverwaltungService.findAllRootDateien(fileName).isEmpty()) {
        version.setValue(versionsverwaltungService.findHoechsteVersionFuerDateiname(fileName) + 1);
      }
    });
    mainView.addListener(Events.ClearUploadListEvent.class,
        e -> dateiUpload.clearFileList());
    dateiUpload.setClassName("dateiUpload");
  }

  private String getFileExtension(final String filename) {
    final int lastIndexOf = filename.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return ""; // empty extension
    }
    return filename.substring(lastIndexOf + 1);
  }

  public void setDateiVersion(final DateiVersion dateiVersion) {
    this.dateiVersion = dateiVersion;
    binder.readBean(dateiVersion);
    addDownloadLink();
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new Events.DeleteEvent(this, dateiVersion)));
    close.addClickListener(event -> fireEvent(new Events.CloseEvent(this)));

    final HorizontalLayout hl = new HorizontalLayout(save, delete, close);
    hl.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    return hl;
  }

  private void addDownloadLink() {
    if (dateiVersion != null && dateiVersion.getFile() != null &&
        dateiVersion.getDateiname() != null) {
      final StreamResource streamResource = new StreamResource(dateiVersion.getDateiname(),
          (InputStreamFactory) () -> new ByteArrayInputStream(dateiVersion.getFile()));
      downloadLink.setHref(streamResource);
      downloadLink.setText(String.format("%s (%d KB)", dateiVersion.getDateiname(),
          dateiVersion.getFile().length / 1024));
      downloadLink.getElement().setAttribute("download", true);
      downloadLinkLabel.setVisible(true);
    } else {
      downloadLink.setText("");
      downloadLink.setHref("");
      downloadLinkLabel.setVisible(false);
    }
  }

  private void validateAndSave() {
    try {
      binder.writeBean(dateiVersion);
      fireEvent(new Events.SaveEvent(this, dateiVersion));
      final Notification notification = Notification.show("Datei erfolgreich hochgeladen.");
      notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      notification.setPosition(Notification.Position.BOTTOM_CENTER);
    } catch (final ValidationException e) {
      e.printStackTrace();
      final Notification notification = Notification.show("Hochladen fehlgeschlagen.");
      notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
      notification.setPosition(Notification.Position.BOTTOM_CENTER);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(final Class<T> eventType,
      final ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

}
