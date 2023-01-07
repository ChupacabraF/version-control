package com.example.application.views.main;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Versionsverwaltung")
@Route(value = "")
public class MainView extends HorizontalLayout {



    public MainView() {
        setSizeFull();
        final TabellenView dateiVersionTabelle = createDateiVersionTabelle();
        final NewDateiVersionPanel newDateiVersionPanel = createNewDateiVersionPanel();
        setFlexGrow(2, dateiVersionTabelle);
        setFlexGrow(1, newDateiVersionPanel);
        add(dateiVersionTabelle, newDateiVersionPanel);
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
