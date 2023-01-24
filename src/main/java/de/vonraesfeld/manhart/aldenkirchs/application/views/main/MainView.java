package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import de.vonraesfeld.manhart.aldenkirchs.application.service.VersionsverwaltungService;
import org.springframework.stereotype.Component;

@PageTitle("Versionsverwaltung")
@Route(value = "")
@Component
@UIScope
public class MainView extends VerticalLayout {

  public MainView(final VersionsverwaltungService versionsverwaltungService) {
    setSizeFull();
    setAlignItems(Alignment.CENTER);

    final HorizontalLayout tableLayout = new HorizontalLayout();
    tableLayout.setSizeFull();
    final TabellenView dateiVersionTabelle =
        new TabellenView(versionsverwaltungService);
    tableLayout.add(dateiVersionTabelle);

    final H1 ueberschrift = new H1("Versionsverwaltung");

    add(ueberschrift, tableLayout);
  }

}
