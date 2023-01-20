package de.vonraesfeld.manhart.aldenkirchs.application.service;

import de.vonraesfeld.manhart.aldenkirchs.application.daos.DateiVersionDao;
import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class VersionsverwaltungServiceTest {

  public static final String DATEINAME = "Dateiname";
  public static final byte[] FILE = null;
  public static final int VERSION_ROOT = 0;
  public static final int VERSION_CHILD = 1;
  public static final Date ZULETZT_BEARBEITET = new Date();
  public static final Date ERSTELLT_AM = new Date();
  public static final String KOMMENTAR = "Kommentar";
  public static final Boolean SPERRE = Boolean.TRUE;
  public static final String DATEITYP = "Dateityp";

  private DateiVersionDao dateiVersionDao;
  private VersionsverwaltungService versionsverwaltungService;

  @Before
  public void setUp() {
    dateiVersionDao = Mockito.mock(DateiVersionDao.class);
    versionsverwaltungService = new VersionsverwaltungService(dateiVersionDao);
  }

  @Test
  public void test_findAllRootDateien() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME))
        .isEqualTo(dateiVersionList);
  }

  @Test
  public void test_findAllChildDateien() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionChild();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME))
        .isEqualTo(dateiVersionList);
  }

  @Test
  public void test_findHoechsteVersionFuerDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findHoechsteVersionFuerDateiname(DATEINAME))
        .isEqualTo(VERSION_ROOT);
  }

  private List<DateiVersion> createSingletonListOfDateiVersionRoot() {
    final DateiVersion dateiVersion = new DateiVersion(DATEINAME, FILE, VERSION_ROOT,
        ZULETZT_BEARBEITET,
        ERSTELLT_AM, KOMMENTAR, SPERRE, DATEITYP);
    return Collections.singletonList(dateiVersion);
  }

  private List<DateiVersion> createSingletonListOfDateiVersionChild() {
    final DateiVersion dateiVersion = new DateiVersion(DATEINAME, FILE, VERSION_CHILD,
        ZULETZT_BEARBEITET,
        ERSTELLT_AM, KOMMENTAR, SPERRE, DATEITYP);
    return Collections.singletonList(dateiVersion);
  }
}
