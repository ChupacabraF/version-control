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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class VersionsverwaltungService {

  private final DateiVersionDao dateiVersionDao;

  public VersionsverwaltungService(final DateiVersionDao dateiVersionDao) {
    this.dateiVersionDao = dateiVersionDao;
  }

  public Set<String> findAllTags() {
    final HashSet<String> resultSet = new HashSet<>();
    final List<DateiVersion> alleDateiVersionen = dateiVersionDao.findAll();
    alleDateiVersionen.forEach(datei -> resultSet.addAll(datei.getTags()));
    return resultSet;
  }

  public List<DateiVersion> findAllRootDateien(final String searchTerm) {
    final List<DateiVersion> alle = dateiVersionDao.findAll();
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
    final List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      return new ArrayList<>();
    }

    return alle.stream().filter(dateiVersion -> dateiVersion.getVersion() > 0 &&
            dateiname.equals(dateiVersion.getDateiname()))
        .collect(Collectors.toList());
  }

  public void entferneAlleSperrenFuerDateiname(final String dateiname) {
    final List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      return;
    }
    alle.stream().filter(datei -> datei.getDateiname().equals(dateiname))
        .forEach(datei -> {
          datei.setGesperrt(Boolean.FALSE);
          dateiVersionDao.save(datei);
        });
  }

  public int findeHoechsteVersionFuerDateiname(final String dateiname) {
    final List<DateiVersion> alle = dateiVersionDao.findAll();
    if (CollectionUtils.isEmpty(alle) || StringUtils.isBlank(dateiname)) {
      return 0;
    }

    final Optional<DateiVersion> optional = alle.stream()
        .filter(dateiVersion -> dateiname.equals(dateiVersion.getDateiname()))
        .max(Comparator.comparingInt(DateiVersion::getVersion));

    return optional.map(DateiVersion::getVersion).orElse(0);
  }

  public DateiVersion saveDateiVersion(final DateiVersion dateiVersion) {
    return dateiVersionDao.save(dateiVersion);
  }

  public void deleteDateiVersion(final DateiVersion dateiVersion) {
    dateiVersionDao.delete(dateiVersion);
  }


}
