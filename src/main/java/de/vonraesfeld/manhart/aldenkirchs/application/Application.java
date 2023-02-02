package de.vonraesfeld.manhart.aldenkirchs.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import de.vonraesfeld.manhart.aldenkirchs.application.views.main.MainView;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication(scanBasePackages = "de.vonraesfeld.manhart.aldenkirchs.application")
@Theme(value = "versionsverwaltung")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@EnableJpaRepositories("de.vonraesfeld.manhart.aldenkirchs.application.daos")
@EntityScan("de.vonraesfeld.manhart.aldenkirchs.application.entities")
public class Application implements AppShellConfigurator {
    public static final Logger LOGGER = LoggerFactory.logger(MainView.class);
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        LOGGER.log(Logger.Level.INFO, "Application started.");
    }
}
