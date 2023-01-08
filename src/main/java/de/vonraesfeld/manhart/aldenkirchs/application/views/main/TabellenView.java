package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.grid.Grid;
import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.Date;
import org.springframework.stereotype.Component;

@Component
public class TabellenView extends VerticalLayout {

  private final DateiVersionDao dateiVersionDao;
  private final MainView mainView;
  Grid<DateiVersion> grid;


  public TabellenView(DateiVersionDao dateiVersionDao, MainView mainView) {
    this.dateiVersionDao = dateiVersionDao;
    this.mainView = mainView;
    setSizeFull();
    configureGrid();
    add(getToolbar(), grid);
  }

  private void configureGrid() {
    grid = new Grid<>(DateiVersion.class);
    grid.setSizeFull();
    grid.addClassNames("dateiversion-grid");
    grid.asSingleSelect().addValueChangeListener(e -> editDateiVersion(e.getValue()));
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
    updateList();
  }

  private void editDateiVersion(DateiVersion value) {
    if (value ==  null){
      mainView.closeEditor();
    } else {
      mainView.getDateiVersionEditPanel().setDateiVersion(value);
      mainView.getDateiVersionEditPanel().setVisible(true);
      addClassName("editing");
    }
  }


  private HorizontalLayout getToolbar() {
    final TextField filterText = new TextField();
    filterText.setPlaceholder("Nach Dateiname filtern...");
    //Button, der Filter wieder löscht
    filterText.setClearButtonVisible(true);
    //feuert nicht bei jedem Change neue Datenbankabfrage
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(e -> updateList());

    final Button addDateiVersionButton = new Button("Datei hinzufügen");
    addDateiVersionButton.addClickListener(e -> addDateiVersion());

    final HorizontalLayout toolbar = new HorizontalLayout(filterText, addDateiVersionButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void addDateiVersion() {
    grid.asSingleSelect().clear();
    final DateiVersion dateiVersion = new DateiVersion();
    dateiVersion.setErstelltAm(new Date());
    editDateiVersion(dateiVersion);
  }

  public void updateList() {
    grid.setItems(dateiVersionDao.findAll());
  }


}
