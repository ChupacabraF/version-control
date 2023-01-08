package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import org.springframework.stereotype.Service;

@Service
public class VersionsverwaltungService {

  private DateiVersionDao dateiVersionDao;

  public VersionsverwaltungService(DateiVersionDao dateiVersionDao) {
    this.dateiVersionDao = dateiVersionDao;
  }

  public DateiVersionDao getDateiVersionDao() {
    return dateiVersionDao;
  }
}
