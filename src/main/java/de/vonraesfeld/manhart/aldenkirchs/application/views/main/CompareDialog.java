package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

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

  private HorizontalLayout getLayout(DateiVersion linkeDatei,
                                     DateiVersion rechteDatei) {
    final HorizontalLayout mainLayout = new HorizontalLayout();
    mainLayout.setWidthFull();
    String stringLinks = new String(linkeDatei.getFile(), StandardCharsets.UTF_8);
    String stringRechts = new String(rechteDatei.getFile(), StandardCharsets.UTF_8);

    final VerticalLayout diffLayout = new VerticalLayout();
    diffLayout.setWidth("50%");
    diffLayout.setSpacing(false);
    DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
    LinkedList<DiffMatchPatch.Diff> diffs = diffMatchPatch.diffMain(stringLinks, stringRechts);
    boolean ersteTextbox = true;
    for (DiffMatchPatch.Diff diff : diffs) {
      final TextArea textArea = new TextArea();
      textArea.setReadOnly(true);
      textArea.setWidth("100%");
      textArea.setValue(diff.text);
      if (ersteTextbox) {
        textArea.setLabel("Version " + rechteDatei.getVersion() + ":");
        textArea.addClassName("paddingTopNull");
        ersteTextbox = false;
      }

      if (DiffMatchPatch.Operation.DELETE.equals(diff.operation)) {
        textArea.addClassName("textareaDelete");
      }
      if (DiffMatchPatch.Operation.INSERT.equals(diff.operation)) {
        textArea.addClassName("textareaNew");
      }
      diffLayout.add(textArea);
    }


    final TextArea linkeTa = new TextArea();
    linkeTa.setReadOnly(true);
    linkeTa.setLabel("Version " + linkeDatei.getVersion() + ":");
    linkeTa.setWidth("50%");
    linkeTa.setValue(new String(linkeDatei.getFile(), StandardCharsets.UTF_8));

    mainLayout.add(linkeTa, diffLayout);
    return mainLayout;
  }
}
