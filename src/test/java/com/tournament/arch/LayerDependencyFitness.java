package com.tournament.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class LayerDependencyFitness {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(
        "com.tournament.controller",
        "com.tournament.service",
        "com.tournament.repository"
    );

    @Test
    void layeredArchitectureShouldBeRespected() {
        ArchRule rule = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller..")
            .layer("Service").definedBy("..service..")
            .layer("Repository").definedBy("..repository..")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");

        rule.check(importedClasses);
    }
}
