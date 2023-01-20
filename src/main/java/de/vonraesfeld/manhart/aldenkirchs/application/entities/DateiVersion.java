package de.vonraesfeld.manhart.aldenkirchs.application.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "dateiVersion")
public class DateiVersion extends AbstractEntity {

  String dateiname;
  @Lob
  byte[] file;
  int version;
  Date zuletztBearbeitet;
  Date erstelltAm;
  String kommentar;
  Boolean gesperrt = Boolean.FALSE;
  String dateityp;

  public DateiVersion(final String dateiname, final byte[] file, final int version,
      final Date zuletztBearbeitet, final Date erstelltAm, final String kommentar,
      final Boolean gesperrt, final String dateityp) {
    this.dateiname = dateiname;
    this.file = file;
    this.version = version;
    this.zuletztBearbeitet = zuletztBearbeitet;
    this.erstelltAm = erstelltAm;
    this.kommentar = kommentar;
    this.gesperrt = gesperrt;
    this.dateityp = dateityp;
  }

  public DateiVersion() {

  }

  public Boolean getGesperrt() {
    return gesperrt;
  }

  public void setGesperrt(final Boolean gesperrt) {
    this.gesperrt = gesperrt;
  }

  public String getDateityp() {
    return dateityp;
  }

  public void setDateityp(final String dateityp) {
    this.dateityp = dateityp;
  }

  public String getDateiname() {
    return dateiname;
  }

  public void setDateiname(final String dateiname) {
    this.dateiname = dateiname;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(final byte[] file) {
    this.file = file;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(final int version) {
    this.version = version;
  }

  public Date getZuletztBearbeitet() {
    return zuletztBearbeitet;
  }

  public void setZuletztBearbeitet(final Date zuletztBearbeitet) {
    this.zuletztBearbeitet = zuletztBearbeitet;
  }

  public Date getErstelltAm() {
    return erstelltAm;
  }

  public void setErstelltAm(final Date erstelltAm) {
    this.erstelltAm = erstelltAm;
  }

  public String getKommentar() {
    return kommentar;
  }

  public void setKommentar(final String kommentar) {
    this.kommentar = kommentar;
  }
}
