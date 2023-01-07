package com.example.application.views.main.daos;

import com.example.application.views.main.entities.DateiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//@Repository
//@Service
@Bean
public interface DateiVersionDao extends JpaRepository<DateiVersion, Integer> {

}
