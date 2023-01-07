package com.example.application.views.main;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;

public class NewDateiVersionPanel extends FormLayout {

  Button save = new Button("Speichern");
  Button delete = new Button("Löschen");
  Button close = new Button("Abbrechen");
  Upload dateiUpload = new Upload();
  TextField kommentar = new TextField("Kommentar");
  NumberField version = new NumberField("Version");

  public NewDateiVersionPanel() {
    add(kommentar, version, dateiUpload, createButtonsLayout());
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    return new HorizontalLayout(save, delete, close);
  }
}
