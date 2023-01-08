package de.vonraesfeld.manhart.aldenkirchs.application.daos;

import de.vonraesfeld.manhart.aldenkirchs.application.entities.DateiVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateiVersionDao extends JpaRepository<DateiVersion, Integer> {

}
