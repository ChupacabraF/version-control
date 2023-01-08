package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

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
    private final NewDateiVersionPanel newDateiVersionPanel;


    public MainView(DateiVersionDao dateiVersionDao,
                    VersionsverwaltungService versionsverwaltungService) {
        this.dateiVersionDao = dateiVersionDao;
        this.versionsverwaltungService = versionsverwaltungService;
        setSizeFull();
        dateiVersionTabelle = createDateiVersionTabelle();
        newDateiVersionPanel = createNewDateiVersionPanel();
        setFlexGrow(2, dateiVersionTabelle);
        setFlexGrow(1, newDateiVersionPanel);
        add(dateiVersionTabelle, newDateiVersionPanel);


    }

    private void updateList() {
        dateiVersionTabelle.updateList(dateiVersionDao.findAll());
    }

    private TabellenView createDateiVersionTabelle(){
        final TabellenView dateiVersionTabelle = new TabellenView();
        return dateiVersionTabelle;
    }

    private NewDateiVersionPanel createNewDateiVersionPanel(){
        final NewDateiVersionPanel newDateiVersionPanel = new NewDateiVersionPanel();
        newDateiVersionPanel.setWidth("25em");
        return newDateiVersionPanel;
    }

}
