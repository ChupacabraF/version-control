package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;

public class Events {

  public static abstract class DateiVersionPanelEvent
      extends ComponentEvent<DateiVersionEditPanel> {
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
      Notification notification = Notification.show("Datei gelöscht.");
      notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
      notification.setPosition(Notification.Position.BOTTOM_CENTER);
    }
  }

  public static class CloseEvent extends DateiVersionPanelEvent {
    CloseEvent(DateiVersionEditPanel source) {
      super(source, null);
    }
  }

  public static class ClearUploadListEvent extends ComponentEvent<MainView> {
    public ClearUploadListEvent(MainView source) {
      super(source, false);
    }
  }

}
