package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializableBiConsumer;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.spring.annotation.UIScope;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import de.vonraesfeld.manhart.aldenkirchs.application.service.VersionsverwaltungService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
@UIScope
public class TabellenView extends VerticalLayout {

  private final VersionsverwaltungService versionsverwaltungService;
  TreeGrid<DateiVersion> grid;
  Button dateiBearbeitenButton;
  Button compareDateienButton;


  public TabellenView(final VersionsverwaltungService versionsverwaltungService) {
    this.versionsverwaltungService = versionsverwaltungService;
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
      dateiBearbeitenButton.setEnabled(grid.getSelectedItems().size() == 1);
      compareDateienButton.setEnabled(grid.getSelectedItems().size() == 2);
    });
    grid.addComponentColumn(dateiVersion -> {
      if (dateiVersion.getVersion() > 0) {
        return new Label("");
      }
      final Image image = new Image();
      image.setWidth("40px");
      image.setHeight("40px");
      if ("txt".equals(dateiVersion.getDateityp())) {
        image.setSrc("txt-file.png");
        image.setAlt("txt");
        return image;
      } else if ("pdf".equals(dateiVersion.getDateityp())) {
        image.setSrc("pdf-file.png");
        image.setAlt("pdf");
        return image;
      } else if ("json".equals(dateiVersion.getDateityp())) {
        image.setSrc("json-file.png");
        image.setAlt("json");
        return image;
      } else if ("java".equals(dateiVersion.getDateityp())) {
        image.setSrc("java-file.png");
        image.setAlt("java");
        return image;
      } else if ("xml".equals(dateiVersion.getDateityp())) {
        image.setSrc("xml-file.png");
        image.setAlt("xml");
        return image;
      }
      return new Label(dateiVersion.getDateityp());
    }).setHeader("Typ").setWidth("50px").setAutoWidth(false).setSortable(true);
    grid.addHierarchyColumn(DateiVersion::getDateiname).setHeader("Dateiname").setAutoWidth(true)
        .setSortable(true);
    grid.addColumn(DateiVersion::getVersion).setHeader("Version").setSortable(true);
    grid.addColumn(createGesperrtSpalte()).setHeader("Status").setSortable(true);
    grid.addColumn(DateiVersion::getKommentar).setHeader("Kommentar").setAutoWidth(true)
        .setSortable(false);
    grid.addColumn(createTagSpalte()).setHeader("Tags").setSortable(false);
    grid.addColumn(new LocalDateTimeRenderer<>(
            (ValueProvider<DateiVersion, LocalDateTime>) dateiVersion -> LocalDateTime.ofInstant(
                dateiVersion.getErstelltAm().toInstant(),
                ZoneId.systemDefault()), "dd.MM.yyyy HH:mm"))
        .setHeader("Erstellt am").setAutoWidth(true);
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
    updateList(null);
  }

  private ComponentRenderer<Span, DateiVersion> createTagSpalte() {
    final SerializableBiConsumer<Span, DateiVersion> componentUpdater = (
        span, dateiVersion) -> {
      final FlexLayout flexLayout = new FlexLayout();
      dateiVersion.getTags().forEach(tag -> {
        final Span tagSpan = new Span();
        tagSpan.getElement().setAttribute("theme", "badge");
        tagSpan.addClassName("tagSpan");
        tagSpan.setText(tag);
        flexLayout.add(tagSpan);
      });
      span.add(flexLayout);
    };
    return new ComponentRenderer<>(Span::new, componentUpdater);
  }

  private ComponentRenderer<Span, DateiVersion> createGesperrtSpalte() {
    final SerializableBiConsumer<Span, DateiVersion> componentUpdater = (
        span, dateiVersion) -> {
      Boolean gesperrt = dateiVersion.getGesperrt();
      String theme = String.format("badge %s",
          Boolean.TRUE.equals(gesperrt) ? "error" : "success");
      span.getElement().setAttribute("theme", theme);
      span.setText(Boolean.TRUE.equals(gesperrt) ? "Gesperrt" : "Verfügbar");
    };
    return new ComponentRenderer<>(Span::new, componentUpdater);
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
        e -> updateList(filterText.getValue()));

    compareDateienButton = new Button("Vergleichen");
    compareDateienButton.setEnabled(grid.getSelectedItems().size() == 2);
    compareDateienButton.addClickListener(e -> {
      if (grid.getSelectedItems().size() == 2) {
        List<DateiVersion> ausgewaehlteDateien = new ArrayList<>(grid.getSelectedItems());
        new CompareDialog(ausgewaehlteDateien.get(0), ausgewaehlteDateien.get(1)).open();
      }
    });

    final Button addNewDateiButton = new Button("Datei hinzufügen");
    addNewDateiButton.addClickListener(e -> addDateiVersion());

    dateiBearbeitenButton = new Button("Bearbeiten");
    dateiBearbeitenButton.setEnabled(grid.getSelectedItems().size() == 1);
    dateiBearbeitenButton.addClickListener(e -> {
      Optional<DateiVersion> ausgewaehlteDatei = grid.getSelectedItems().stream().findFirst();
      final BearbeitenDialog bearbeitenDialog =
          new BearbeitenDialog(ausgewaehlteDatei.get(), this, versionsverwaltungService);
      bearbeitenDialog.open();
    });

    final HorizontalLayout toolbar =
        new HorizontalLayout(filterText, compareDateienButton, dateiBearbeitenButton,
            addNewDateiButton);
    toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
    toolbar.setWidthFull();
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void addDateiVersion() {
    final DateiVersion dateiVersion = new DateiVersion();
    dateiVersion.setErstelltAm(new Date());
    new BearbeitenDialog(dateiVersion, this, versionsverwaltungService).open();
  }

  public void updateList(final String searchTerm) {
    grid.getTreeData().clear();
    for (DateiVersion root : versionsverwaltungService.findAllRootDateien(searchTerm)) {
      grid.getTreeData().addItem(null, root);
      grid.getTreeData()
          .addItems(root, versionsverwaltungService.findAllChildDateien(root.getDateiname()));
    }
    grid.getDataProvider().refreshAll();
  }


}
