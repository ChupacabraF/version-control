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

  public static final String DATEINAME_VALID_1 = "Dateiname 1";
  public static final String DATEINAME_VALID_2 = "Dateiname 2";
  public static final String DATEINAME_VALID_3 = "Dateiname 3";
  public static final String DATEINAME_INVALID = "Invalider Dateiname";
  public static final byte[] FILE = null;
  public static final int VERSION_ROOT_1 = 0;
  public static final int VERSION_ROOT_2 = 1;
  public static final int VERSION_ROOT_3 = 2;
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
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME_VALID_1))
        .isEqualTo(dateiVersionList);
  }

  @Test
  public void test_findAllRootDateien_ValidDateinamenOf1() {
    final List<DateiVersion> dateiVersionList = createListOfDateiVersionRootSame();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME_VALID_1).size())
        .isEqualTo(1);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME_VALID_2).size())
        .isEqualTo(1);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME_VALID_3).size())
        .isEqualTo(1);
  }

  @Test
  public void test_findAllRootDateien_InvalidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(DATEINAME_INVALID))
        .isEqualTo(Collections.emptyList());
  }

  @Test
  public void test_findAllRootDateien_Null() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllRootDateien(null))
        .isEqualTo(dateiVersionList);
  }

  @Test
  public void test_findAllChildDateien_ValidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionChild();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME_VALID_1))
        .isEqualTo(dateiVersionList);
  }

  @Test
  public void test_findAllChildDateien_ValidDateinameOf1() {
    final List<DateiVersion> dateiVersionList = createListOfDateiVersionChild();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME_VALID_1).size())
        .isEqualTo(1);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME_VALID_2).size())
        .isEqualTo(1);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME_VALID_3).size())
        .isEqualTo(1);
  }

  @Test
  public void test_findAllChildDateien_InvalidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionChild();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(DATEINAME_INVALID))
        .isEqualTo(Collections.emptyList());
  }

  @Test
  public void test_findAllChildDateien_Null() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionChild();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(versionsverwaltungService.findAllChildDateien(null))
        .isEqualTo(Collections.emptyList());
  }

  @Test
  public void test_findHoechsteVersionFuerDateiname_ValidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(DATEINAME_VALID_1))
        .isEqualTo(VERSION_ROOT_1);
  }

  @Test
  public void test_findHoechsteVersionFuerDateiname_ValidDateinamenOf1() {
    final List<DateiVersion> dateiVersionList = createListOfDateiVersionRootDifferent();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(DATEINAME_VALID_1))
        .isEqualTo(VERSION_ROOT_1);

    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(DATEINAME_VALID_2))
        .isEqualTo(VERSION_ROOT_2);

    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(DATEINAME_VALID_3))
        .isEqualTo(VERSION_ROOT_3);
  }

  @Test
  public void test_findHoechsteVersionFuerDateiname_InvalidDateiname() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(DATEINAME_INVALID))
        .isEqualTo(0);
  }

  @Test
  public void test_findHoechsteVersionFuerDateiname_Null() {
    final List<DateiVersion> dateiVersionList = createSingletonListOfDateiVersionRoot();
    Mockito.when(dateiVersionDao.findAll()).thenReturn(dateiVersionList);
    Assertions.assertThat(
            versionsverwaltungService.findeHoechsteVersionFuerDateiname(null))
        .isEqualTo(0);
  }

  private List<DateiVersion> createSingletonListOfDateiVersionRoot() {
    final DateiVersion dateiVersion =
        new DateiVersion(DATEINAME_VALID_1, FILE, VERSION_ROOT_1, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    return Collections.singletonList(dateiVersion);
  }

  private List<DateiVersion> createSingletonListOfDateiVersionChild() {
    final DateiVersion dateiVersion =
        new DateiVersion(DATEINAME_VALID_1, FILE, VERSION_CHILD, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    return Collections.singletonList(dateiVersion);
  }

  private List<DateiVersion> createListOfDateiVersionRootSame() {
    final DateiVersion dateiVersion1 =
        new DateiVersion(DATEINAME_VALID_1, FILE, VERSION_ROOT_1, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    final DateiVersion dateiVersion2 =
        new DateiVersion(DATEINAME_VALID_2, FILE, VERSION_ROOT_1, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    final DateiVersion dateiVersion3 =
        new DateiVersion(DATEINAME_VALID_3, FILE, VERSION_ROOT_1, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    return List.of(dateiVersion1, dateiVersion2, dateiVersion3);
  }

  private List<DateiVersion> createListOfDateiVersionRootDifferent() {
    final DateiVersion dateiVersion1 =
        new DateiVersion(DATEINAME_VALID_1, FILE, VERSION_ROOT_1, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    final DateiVersion dateiVersion2 =
        new DateiVersion(DATEINAME_VALID_2, FILE, VERSION_ROOT_2, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    final DateiVersion dateiVersion3 =
        new DateiVersion(DATEINAME_VALID_3, FILE, VERSION_ROOT_3, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    return List.of(dateiVersion1, dateiVersion2, dateiVersion3);
  }

  private List<DateiVersion> createListOfDateiVersionChild() {
    final DateiVersion dateiVersion1 =
        new DateiVersion(DATEINAME_VALID_1, FILE, VERSION_CHILD, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    final DateiVersion dateiVersion2 =
        new DateiVersion(DATEINAME_VALID_2, FILE, VERSION_CHILD, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    final DateiVersion dateiVersion3 =
        new DateiVersion(DATEINAME_VALID_3, FILE, VERSION_CHILD, ERSTELLT_AM, KOMMENTAR, SPERRE,
            DATEITYP);
    return List.of(dateiVersion1, dateiVersion2, dateiVersion3);
  }
}
