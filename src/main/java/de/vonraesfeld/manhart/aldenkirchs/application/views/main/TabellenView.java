package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.value.ValueChangeMode;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TabellenView extends VerticalLayout {

  TreeGrid<DateiVersion> grid;


  public TabellenView() {
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
    // TODO: items über MainView holen?
//    filterText.addValueChangeListener(e -> updateList());

    Button addContactButton = new Button("Datei hinzufügen");

    HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  public void updateList(List<DateiVersion> items) {
    grid.setItems(items);
  }


}
