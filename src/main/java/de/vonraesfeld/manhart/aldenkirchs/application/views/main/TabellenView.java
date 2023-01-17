package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TabellenView extends VerticalLayout {

  private final DateiVersionDao dateiVersionDao;
  private final VersionsverwaltungService versionsverwaltungService;
  private final MainView mainView;
  TreeGrid<DateiVersion> grid;


  public TabellenView(DateiVersionDao dateiVersionDao,
                      VersionsverwaltungService versionsverwaltungService, MainView mainView) {
    this.dateiVersionDao = dateiVersionDao;
    this.versionsverwaltungService = versionsverwaltungService;
    this.mainView = mainView;
    setSizeFull();
    configureGrid();
    HorizontalLayout toolbar = getToolbar();
    add(toolbar, grid);
  }

  private void configureGrid() {
    grid = new TreeGrid<>();
    grid.setSizeFull();
    grid.setSelectionMode(Grid.SelectionMode.MULTI);
    grid.addClassNames("dateiversion-grid");
    grid.asMultiSelect().addValueChangeListener(e -> {
      if (e.getValue().size() == 1) {
        editDateiVersion(e.getValue().iterator().next());
      } else {
        mainView.closeEditor();
      }
    });
    grid.addColumn(DateiVersion::getDateityp).setHeader("Typ").setWidth("2em").setSortable(true);
    grid.addHierarchyColumn(DateiVersion::getDateiname).setHeader("Dateiname").setAutoWidth(true)
        .setSortable(true);
    grid.addColumn(DateiVersion::getVersion).setHeader("Version").setWidth("2em").setSortable(true);
    grid.addColumn(createGesperrtSpalte()).setHeader("Status").setAutoWidth(true).setSortable(true);
    grid.addColumn(DateiVersion::getKommentar).setHeader("Kommentar").setAutoWidth(true)
        .setSortable(false);
    grid.addColumn(DateiVersion::getZuletztBearbeitet).setHeader("Zuletzt Bearbeitet")
        .setAutoWidth(true).setSortable(true);
    grid.addColumn(DateiVersion::getErstelltAm).setHeader("Erstellt am").setAutoWidth(true)
        .setSortable(true);
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
    updateList();
  }

  private ComponentRenderer<Span, DateiVersion> createGesperrtSpalte() {
    return new ComponentRenderer<>(Span::new, statusComponentUpdater);
  }

  private SerializableBiConsumer<Span, DateiVersion> statusComponentUpdater = (
      span, dateiVersion) -> {
    Boolean gesperrt = dateiVersion.getGesperrt();
    String theme = String.format("badge %s",
        Boolean.TRUE.equals(gesperrt) ? "error" : "success");
    span.getElement().setAttribute("theme", theme);
    span.setText(Boolean.TRUE.equals(gesperrt) ? "Gesperrt" : "Verfügbar");
  };

  private void editDateiVersion(DateiVersion value) {
    if (value == null) {
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
    filterText.setWidth("13em");
    //Button, der Filter wieder löscht
    filterText.setClearButtonVisible(true);
    //feuert nicht bei jedem Change neue Datenbankabfrage
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    filterText.addValueChangeListener(
        e -> grid.setItems(versionsverwaltungService.findByDateiname(filterText.getValue())));

    final Button compareDateien = new Button("Vergleichen");
    compareDateien.addClickListener(e -> {
      if (grid.getSelectedItems().size() == 2) {
        List<DateiVersion> ausgewaehlteDateien = new ArrayList<>(grid.getSelectedItems());
        new CompareDialog(ausgewaehlteDateien.get(0), ausgewaehlteDateien.get(1)).open();
      }
    });

    final Button addDateiVersionButton = new Button("Datei hinzufügen");
    addDateiVersionButton.addClickListener(e -> addDateiVersion());

    final HorizontalLayout toolbar =
        new HorizontalLayout(filterText, compareDateien, addDateiVersionButton);
    toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
    toolbar.setWidthFull();
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void addDateiVersion() {
    grid.asMultiSelect().clear();
    final DateiVersion dateiVersion = new DateiVersion();
    dateiVersion.setErstelltAm(new Date());
    editDateiVersion(dateiVersion);
  }

  public void updateList() {
    grid.getTreeData().clear();
    for (DateiVersion root : versionsverwaltungService.findAllRootDateien()) {
      grid.getTreeData().addItem(null, root);
      grid.getTreeData()
          .addItems(root, versionsverwaltungService.findAllChildDateien(root.getDateiname()));
    }
  }


}
