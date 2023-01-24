package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import de.vonraesfeld.manhart.aldenkirchs.application.service.VersionsverwaltungService;
import java.io.ByteArrayInputStream;
import org.apache.commons.io.IOUtils;

public class DateiVersionEditPanel extends FormLayout {

  private final VersionsverwaltungService versionsverwaltungService;
  private Upload dateiUpload;
  private final Anchor downloadLink = new Anchor("", "");
  private final TextField kommentar = new TextField("Kommentar");
  private final IntegerField version = new IntegerField("Version");
  private final Checkbox gesperrt = new Checkbox("Datei für Änderungen gesperrt?");
  private final Label downloadLinkLabel = new Label("Downloadlink:");
  private DateiVersion dateiVersion;
  final Binder<DateiVersion> binder = new BeanValidationBinder<>(DateiVersion.class);

  public DateiVersionEditPanel(final VersionsverwaltungService versionsverwaltungService) {
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
    dateiUpload.setClassName("dateiUpload");
  }

  private String getFileExtension(final String filename) {
    final int lastIndexOf = filename.lastIndexOf(".");
    if (lastIndexOf == -1) {
      return ""; // keine Dateiendung vorhanden
    }
    return filename.substring(lastIndexOf + 1);
  }

  public void setDateiVersion(final DateiVersion dateiVersion) {
    this.dateiVersion = dateiVersion;
    binder.readBean(dateiVersion);
    addDownloadLink();
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

  public boolean validateAndSave() {
    try {
      binder.writeBean(dateiVersion);
      versionsverwaltungService.saveDateiVersion(dateiVersion);
      return true;
    } catch (final ValidationException e) {
      e.printStackTrace();
      return false;
    }
  }

}
