package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import org.springframework.stereotype.Component;

@PageTitle("Versionsverwaltung")
@Route(value = "")
@Component
public class MainView extends HorizontalLayout {

  private DateiVersionDao dateiVersionDao;
  private VersionsverwaltungService versionsverwaltungService;

  private final TabellenView dateiVersionTabelle;
  private final DateiVersionEditPanel dateiVersionEditPanel;


  public MainView(DateiVersionDao dateiVersionDao,
                  VersionsverwaltungService versionsverwaltungService) {
    this.dateiVersionDao = dateiVersionDao;
    this.versionsverwaltungService = versionsverwaltungService;
    setSizeFull();
    dateiVersionTabelle = createDateiVersionTabelle();
    dateiVersionEditPanel = createDateiVersionEditPanel();
    setFlexGrow(2, dateiVersionTabelle);
    setFlexGrow(1, dateiVersionEditPanel);
    add(dateiVersionTabelle, dateiVersionEditPanel);

    closeEditor();
  }

  public void closeEditor() {
    dateiVersionEditPanel.setDateiVersion(null);
    dateiVersionEditPanel.setVisible(false);
    removeClassName("editing");
  }

  private TabellenView createDateiVersionTabelle() {
    final TabellenView dateiVersionTabelle = new TabellenView(dateiVersionDao, this);
    return dateiVersionTabelle;
  }

  private DateiVersionEditPanel createDateiVersionEditPanel() {
    final DateiVersionEditPanel dateiVersionEditPanel = new DateiVersionEditPanel();
    dateiVersionEditPanel.setWidth("30em");
    dateiVersionEditPanel.addClassName("dateiversionEditPanel");

    dateiVersionEditPanel.addListener(DateiVersionEditPanel.SaveEvent.class,
        this::saveDateiVersion);
    dateiVersionEditPanel.addListener(DateiVersionEditPanel.DeleteEvent.class,
        this::deleteDateiVersion);
    dateiVersionEditPanel.addListener(DateiVersionEditPanel.CloseEvent.class, e -> closeEditor());
    return dateiVersionEditPanel;
  }

  private void deleteDateiVersion(DateiVersionEditPanel.DeleteEvent event) {
    dateiVersionDao.delete(event.getDateiVersion());
    dateiVersionTabelle.updateList();
    closeEditor();
  }

  private void saveDateiVersion(DateiVersionEditPanel.SaveEvent event) {
    dateiVersionDao.save(event.getDateiVersion());
    dateiVersionTabelle.updateList();
    closeEditor();
  }

  public DateiVersionEditPanel getDateiVersionEditPanel() {
    return dateiVersionEditPanel;
  }
}
