package com.example.application.views.main;

import com.example.application.views.main.daos.DateiVersionDao;
import com.example.application.views.main.entities.DateiVersion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.value.ValueChangeMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//@Route(value = "")
//@PageTitle("Versionsverwaltung")
public class TabellenView extends VerticalLayout {

  @Autowired
  private DateiVersionDao dateiVersionDao;
  TreeGrid<DateiVersion> grid;


  public TabellenView() {
    this.dateiVersionDao = dateiVersionDao;
    setSizeFull();
    grid = new TreeGrid<>(DateiVersion.class);
    grid.setSizeFull();
    configureGrid();

    add(getToolbar(), grid);
  }

  private void configureGrid() {
    grid.addClassNames("dateiversion-grid");

//    grid.setColumns("Dateiname", "Version", "Content", "Zuletzt bearbeitet");
//    grid.addColumn(DateiVersion::getDateiname).setHeader("Dateiname");
//    grid.addColumn(DateiVersion::getVersion).setHeader("Version");
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    final TextField filterText = new TextField();
    filterText.setPlaceholder("Nach Dateiname filtern...");
    //Button, der Filter wieder löscht
    filterText.setClearButtonVisible(true);
    //feuert nicht bei jedem Change neue Datenbankabfrage
    filterText.setValueChangeMode(ValueChangeMode.LAZY);

    Button addContactButton = new Button("Datei hinzufügen");

    HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }


}
