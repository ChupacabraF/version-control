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

  public static final String DATEINAME_VALID = "Dateiname";
  public static final String DATEINAME_INVALID = "Invalider Dateiname";
  public static final byte[] FILE = null;
  public static final int VERSION_ROOT = 0;
  public static final int VERSION_CHILD = 1;
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
  public void test_findAllRootDateien_ValidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME_VALID))
        .isEqualTo(dateiVersionList);
  }

  @Test
  public void test_findAllRootDateien_InvalidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME_INVALID))
        .isEqualTo(Collections.emptyList());
  }

  @Test
  public void test_findAllChildDateien_ValidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionChild();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME_VALID))
        .isEqualTo(dateiVersionList);
  }

  @Test
  public void test_findAllChildDateien_InvalidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionChild();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME_INVALID))
        .isEqualTo(Collections.emptyList());
  }

  @Test
  public void test_findHoechsteVersionFuerDateiname_ValidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(DATEINAME_VALID))
        .isEqualTo(VERSION_ROOT);
  }

  @Test
  public void test_findHoechsteVersionFuerDateiname_InvalidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(DATEINAME_INVALID))
        .isEqualTo(0);
  }

  private List<DateiVersion> createSingletonListOfDateiVersionRoot() {
    final DateiVersion dateiVersion =
        new DateiVersion(DATEINAME_VALID, FILE, VERSION_ROOT, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    return Collections.singletonList(dateiVersion);
  }

  private List<DateiVersion> createSingletonListOfDateiVersionChild() {
    final DateiVersion dateiVersion =
        new DateiVersion(DATEINAME_VALID, FILE, VERSION_CHILD, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    return Collections.singletonList(dateiVersion);
  }
}
