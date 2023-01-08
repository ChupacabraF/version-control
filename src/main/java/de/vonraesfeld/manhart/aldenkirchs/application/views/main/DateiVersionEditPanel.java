package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;

public class DateiVersionEditPanel extends FormLayout {

  private Button save = new Button("Speichern");
  private Button delete = new Button("Löschen");
  private Button close = new Button("Abbrechen");
  private Upload dateiUpload = new Upload();
  private TextField kommentar = new TextField("Kommentar");
  private IntegerField version = new IntegerField("Version");

  private DateiVersion dateiVersion;

  Binder<DateiVersion> binder = new BeanValidationBinder<>(DateiVersion.class);

  public DateiVersionEditPanel() {
    binder.bindInstanceFields(this);
    add(kommentar, version, dateiUpload, createButtonsLayout());
  }

  public void setDateiVersion(DateiVersion dateiVersion) {
    this.dateiVersion = dateiVersion;
    binder.readBean(dateiVersion);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    save.addClickShortcut(Key.ENTER);
    close.addClickShortcut(Key.ESCAPE);

    save.addClickListener(event -> validateAndSave());
    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, dateiVersion)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    return new HorizontalLayout(save, delete, close);
  }

  private void validateAndSave() {
    try {
      binder.writeBean(dateiVersion);
      fireEvent(new SaveEvent(this, dateiVersion));
    } catch (ValidationException e){
      e.printStackTrace();
    }
  }


  // Events
  public static abstract class DateiVersionPanelEvent extends ComponentEvent<DateiVersionEditPanel> {
    private DateiVersion dateiVersion;

    protected DateiVersionPanelEvent(DateiVersionEditPanel source, DateiVersion dateiVersion) {
      super(source, false);
      this.dateiVersion = dateiVersion;
    }

    public DateiVersion getDateiVersion() {
      return dateiVersion;
    }
  }

  public static class SaveEvent extends DateiVersionPanelEvent {
    SaveEvent(DateiVersionEditPanel source, DateiVersion dateiVersion) {
      super(source, dateiVersion);
    }
  }

  public static class DeleteEvent extends DateiVersionPanelEvent {
    DeleteEvent(DateiVersionEditPanel source, DateiVersion dateiVersion) {
      super(source, dateiVersion);
    }

  }

  public static class CloseEvent extends DateiVersionPanelEvent {
    CloseEvent(DateiVersionEditPanel source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
