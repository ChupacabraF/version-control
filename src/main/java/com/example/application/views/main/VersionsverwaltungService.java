package com.example.application.views.main;

import com.example.application.views.main.daos.DateiVersionDao;
import org.springframework.stereotype.Service;

@Service
public class VersionsverwaltungService {

  private final DateiVersionDao dateiVersionDao;

  public VersionsverwaltungService(DateiVersionDao dateiVersionDao) {
    this.dateiVersionDao = dateiVersionDao;
  }


}
