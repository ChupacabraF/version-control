package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import de.vonraesfeld.manhart.aldenkirchs.application.service.VersionsverwaltungService;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.io.IOUtils;

public class DateiVersionEditPanel extends FormLayout {

  private final VersionsverwaltungService versionsverwaltungService;
  private Upload dateiUpload;
  private final Anchor downloadLink = new Anchor("", "");
  private final TextField kommentar = new TextField("Kommentar");
  private final IntegerField version = new IntegerField("Version");
  private final Checkbox gesperrt = new Checkbox("Datei für Änderungen gesperrt?");
  private final Label downloadLinkLabel = new Label("Downloadlink:");
  private final MultiSelectComboBox<String> tagBox = new MultiSelectComboBox<>("Tags zuordnen");
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
    version.setWidth("50px");
    downloadLinkLabel.setVisible(false);
    tagBox.setItems(versionsverwaltungService.findAllTags());
    tagBox.addValueChangeListener(event -> {
      dateiVersion.setTags(event.getValue());
    });

    final HorizontalLayout tagErstellenLayout =
        createTagErstellenLayout();

    add(tagErstellenLayout);
    add(tagBox);
    add(kommentar);
    add(version);
    add(gesperrt);
    add(downloadLinkLabel);
    add(downloadLink);
    add(dateiUpload);
  }

  private HorizontalLayout createTagErstellenLayout() {
    final HorizontalLayout tagErstellenLayout = new HorizontalLayout();
    tagErstellenLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    final Button tagErstellenButton = new Button("Tag erstellen");
    final TextField tagErstellenInputField = new TextField();
    tagErstellenButton.addClickShortcut(Key.ENTER);
    tagErstellenButton.addClickListener(
        event -> {
          final String neuerTag = tagErstellenInputField.getValue();
          final Set<String> currentTagsList = tagBox.getValue();
          final ArrayList<String> newTagsList = new ArrayList<>();
          newTagsList.addAll(currentTagsList);
          newTagsList.add(neuerTag);
          final List<String> currentItems =
              tagBox.getDataProvider().fetch(new Query<>()).collect(Collectors.toList());
          if (!currentItems.contains(neuerTag)) {
            currentItems.add(neuerTag);
          }
          tagBox.setItems(currentItems);
          tagBox.setValue(new HashSet<>(newTagsList));
        });
    tagErstellenLayout.add(tagErstellenInputField, tagErstellenButton);
    return tagErstellenLayout;
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
    tagBox.setValue(dateiVersion.getTags());
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
