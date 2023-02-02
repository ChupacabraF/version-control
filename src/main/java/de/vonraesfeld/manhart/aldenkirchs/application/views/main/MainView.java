package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import de.vonraesfeld.manhart.aldenkirchs.application.service.VersionsverwaltungService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import static de.vonraesfeld.manhart.aldenkirchs.application.Application.LOGGER;

@PageTitle("Versionsverwaltung")
@Route(value = "")
@Component
@UIScope
public class MainView extends VerticalLayout {

  public MainView(final VersionsverwaltungService versionsverwaltungService) {
    LOGGER.log(Logger.Level.INFO, "MainView initialized.");
    setSizeFull();
    setAlignItems(Alignment.CENTER);

    final HorizontalLayout tableLayout = new HorizontalLayout();
    tableLayout.setSizeFull();
    final TabellenView dateiVersionTabelle =
        new TabellenView(versionsverwaltungService);
    tableLayout.add(dateiVersionTabelle);
    LOGGER.log(Logger.Level.INFO, "TabellenView, tableLayout created.");

    final H1 ueberschrift = new H1("Versionsverwaltung");

    add(ueberschrift, tableLayout);
  }

}
