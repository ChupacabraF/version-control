package de.vonraesfeld.manhart.aldenkirchs.application.entities;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dateiVersion")
public class DateiVersion extends AbstractEntity {

  String dateiname;
  byte[] file;
  int version;
  Date zuletztBearbeitet;
  Date erstelltAm;
  String kommentar;


  Boolean gesperrt = Boolean.FALSE;

  String dateityp;

  public Boolean getGesperrt() {
    return gesperrt;
  }

  public void setGesperrt(Boolean gesperrt) {
    this.gesperrt = gesperrt;
  }

  public String getDateityp() {
    return dateityp;
  }

  public void setDateityp(String dateityp) {
    this.dateityp = dateityp;
  }

  public String getDateiname() {
    return dateiname;
  }

  public void setDateiname(String dateiname) {
    this.dateiname = dateiname;
  }

  public byte[] getFile() {
    return file;
  }

  public void setFile(byte[] file) {
    this.file = file;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }

  public Date getZuletztBearbeitet() {
    return zuletztBearbeitet;
  }

  public void setZuletztBearbeitet(Date zuletztBearbeitet) {
    this.zuletztBearbeitet = zuletztBearbeitet;
  }

  public Date getErstelltAm() {
    return erstelltAm;
  }

  public void setErstelltAm(Date erstelltAm) {
    this.erstelltAm = erstelltAm;
  }

  public String getKommentar() {
    return kommentar;
  }

  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }
}
