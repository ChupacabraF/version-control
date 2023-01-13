package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import java.nio.charset.StandardCharsets;

public class CompareDialog extends Dialog {

  public CompareDialog(final DateiVersion linkeDatei, final DateiVersion rechteDatei) {
    setHeaderTitle("Unterschiede der Dateien");
    Button closeButton = new Button("Schließen", e -> this.close());
    getFooter().add(closeButton);
    setWidthFull();

    final HorizontalLayout mainLayout =
        getLayout(linkeDatei, rechteDatei);
    add(mainLayout);
  }

  private static HorizontalLayout getLayout(DateiVersion linkeDatei,
                                            DateiVersion rechteDatei) {
    final HorizontalLayout mainLayout = new HorizontalLayout();
    mainLayout.setWidthFull();
    final TextArea linkeTa = new TextArea();
    linkeTa.setReadOnly(true);
    linkeTa.setLabel("Linke Datei");
    linkeTa.setWidth("50%");
    linkeTa.setValue(new String(linkeDatei.getFile(), StandardCharsets.UTF_8));
    final TextArea rechteTa = new TextArea();
    rechteTa.setLabel("Rechte Datei");
    rechteTa.setReadOnly(true);
    rechteTa.setWidth("50%");
    rechteTa.setValue(new String(rechteDatei.getFile(), StandardCharsets.UTF_8));
    mainLayout.add(linkeTa, rechteTa);
    return mainLayout;
  }
}
