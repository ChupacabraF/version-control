package de.vonraesfeld.manhart.aldenkirchs.application.service;

import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VersionsverwaltungService {

  private DateiVersionDao dateiVersionDao;

  public VersionsverwaltungService(DateiVersionDao dateiVersionDao) {
    this.dateiVersionDao = dateiVersionDao;
  }

  public List<DateiVersion> findAllRootDateien(final String searchTerm) {
    List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle)) {
      return new ArrayList<>();
    }

    if (StringUtils.isNotBlank(searchTerm)) {
      return alle.stream().filter(dateiVersion -> dateiVersion.getVersion() == 0 &&
              StringUtils.containsIgnoreCase(dateiVersion.getDateiname(), searchTerm))
          .collect(Collectors.toList());
    } else {
      return alle.stream().filter(dateiVersion -> dateiVersion.getVersion() == 0)
          .collect(Collectors.toList());
    }
  }

  public List<DateiVersion> findAllChildDateien(final String dateiname) {
    List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      return new ArrayList<>();
    }

    return alle.stream().filter(dateiVersion -> dateiVersion.getVersion() > 0 &&
            dateiname.equals(dateiVersion.getDateiname()))
        .collect(Collectors.toList());
  }

  public int findHoechsteVersionFuerDateiname(final String dateiname) {
    List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      return 0;
    }

    return alle.stream().filter(dateiVersion -> dateiname.equals(dateiVersion.getDateiname()))
        .max(Comparator.comparingInt(DateiVersion::getVersion)).get().getVersion();
  }
}
