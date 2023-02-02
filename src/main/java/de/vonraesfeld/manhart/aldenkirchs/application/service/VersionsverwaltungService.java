package de.vonraesfeld.manhart.aldenkirchs.application.service;

import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
public class VersionsverwaltungService {

  public static final Logger LOGGER = LoggerFactory.logger(VersionsverwaltungService.class);

  private final DateiVersionDao dateiVersionDao;

  public VersionsverwaltungService(final DateiVersionDao dateiVersionDao) {
    this.dateiVersionDao = dateiVersionDao;
  }

  public Set<String> findAllTags() {
    LOGGER.log(Logger.Level.DEBUG, "Method findAllTags started.");
    final HashSet<String> resultSet = new HashSet<>();
    final List<DateiVersion> alleDateiVersionen = dateiVersionDao.findAll();
    alleDateiVersionen.forEach(datei -> resultSet.addAll(datei.getTags()));
    LOGGER.log(Logger.Level.DEBUG, "Method findAllTags finished.");
    return resultSet;
  }

  public List<DateiVersion> findAllRootDateien(final String searchTerm) {
    LOGGER.log(Logger.Level.DEBUG, "findAllRootDateien started.");
    final List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle)) {
      return new ArrayList<>();
    }

    if (StringUtils.isNotBlank(searchTerm)) {
      LOGGER.log(Logger.Level.DEBUG, "findAllRootDateien fiinished.");
      return alle.stream().filter(dateiVersion -> dateiVersion.getVersion() == 0 &&
              StringUtils.containsIgnoreCase(dateiVersion.getDateiname(), searchTerm))
          .collect(Collectors.toList());
    } else {
      LOGGER.log(Logger.Level.DEBUG, "findAllRootDateien completed - no Root Files found.");
      return alle.stream().filter(dateiVersion -> dateiVersion.getVersion() == 0)
          .collect(Collectors.toList());
    }
  }

  public List<DateiVersion> findAllChildDateien(final String dateiname) {
    LOGGER.log(Logger.Level.DEBUG, "findAllChildDateien started.");
    final List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      LOGGER.log(Logger.Level.DEBUG, "findAllChildDateien finished - not Child Files found.");
      return new ArrayList<>();
    }
    LOGGER.log(Logger.Level.DEBUG, "findAllChildDateien finished.");
    return alle.stream().filter(dateiVersion -> dateiVersion.getVersion() > 0 &&
            dateiname.equals(dateiVersion.getDateiname()))
        .collect(Collectors.toList());
  }

  public void entferneAlleSperrenFuerDateiname(final String dateiname) {
    LOGGER.log(Logger.Level.DEBUG, "entferneAlleSperrenFuerDateiname started.");
    final List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      return;
    }
    alle.stream().filter(datei -> datei.getDateiname().equals(dateiname))
        .forEach(datei -> {
          datei.setGesperrt(Boolean.FALSE);
          dateiVersionDao.save(datei);
        });
    LOGGER.log(Logger.Level.DEBUG, "entferneAlleSperrenFuerDateiname finished.");
  }

  public int findeHoechsteVersionFuerDateiname(final String dateiname) {
    LOGGER.log(Logger.Level.DEBUG, "findeHoechsteVersionFuerDateiname started.");
    final List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      LOGGER.log(Logger.Level.DEBUG, "findeHoechsteVersionFuerDateiname finished - returned 0.");
      return 0;
    }

    final Optional<DateiVersion> optional = alle.stream()
        .filter(dateiVersion -> dateiname.equals(dateiVersion.getDateiname()))
        .max(Comparator.comparingInt(DateiVersion::getVersion));
    LOGGER.log(Logger.Level.DEBUG, "findeHoechsteVersionFuerDateiname finished.");
    return optional.map(DateiVersion::getVersion).orElse(0);
  }

  public DateiVersion saveDateiVersion(final DateiVersion dateiVersion) {
    LOGGER.log(Logger.Level.DEBUG, "saving File Version");
    return dateiVersionDao.save(dateiVersion);
  }

  public void deleteDateiVersion(final DateiVersion dateiVersion) {
    LOGGER.log(Logger.Level.DEBUG, "deleting File Version");
    dateiVersionDao.delete(dateiVersion);
  }


}
