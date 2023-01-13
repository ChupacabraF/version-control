package de.vonraesfeld.manhart.aldenkirchs.application.views.main;

import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class VersionsverwaltungService {

  private DateiVersionDao dateiVersionDao;

  public VersionsverwaltungService(DateiVersionDao dateiVersionDao) {
    this.dateiVersionDao = dateiVersionDao;
  }

  public List<DateiVersion> findByDateiname(final String searchTerm) {
    List<DateiVersion> alle = dateiVersionDao.findAll();
    if (searchTerm == null) {
      return alle;
    }

    return alle.stream().filter(
            (dateiVersion -> StringUtils.containsIgnoreCase(dateiVersion.getDateiname(), searchTerm)))
        .collect(
            Collectors.toList());
  }

  public DateiVersionDao getDateiVersionDao() {
    return dateiVersionDao;
  }
}
