package com.tournament.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static org.junit.jupiter.api.Assertions.fail;

public class AnnotationFitness {

    @Test
    void controllersShouldBeAnnotatedWithRestControllerOrController() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.tournament");

        List<String> violations = new ArrayList<>();

        for (JavaClass jc : classes) {
            String pkg = jc.getPackageName();
            // include classes in any package segment named 'controller'
            if (pkg != null && pkg.matches(".*\\.controller(\\..*)?")) {
                boolean hasRest = jc.isAnnotatedWith(RestController.class);
                boolean hasController = jc.isAnnotatedWith(Controller.class);
                if (!hasRest && !hasController) {
                    violations.add(jc.getName());
                }
            }
        }

        if (!violations.isEmpty()) {
            String msg = "The following production controller classes are not annotated with @RestController or @Controller: "
                    + String.join(", ", violations);
            fail(msg);
        }
    }

    @Test
    void servicesShouldBeAnnotatedWithService() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.tournament");

        ArchRule rule = classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .because("Service classes should be annotated with @Service for Spring scanning");

        rule.check(classes);
    }

    @Test
    void repositoriesShouldBeAnnotatedWithRepository() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.tournament");

        ArchRule rule = classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(Repository.class)
                .because("Repository classes must be annotated with @Repository for proper persistence handling");

        rule.check(classes);
    }
}
