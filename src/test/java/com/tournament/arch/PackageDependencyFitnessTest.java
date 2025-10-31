package com.tournament.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class PackageDependencyFitnessTest {

    private final JavaClasses importedClasses =
            new ClassFileImporter()
                    .withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("com.tournament");

    @Test
    void controllersShouldNotAccessModelOrRepository() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..repository..", "..model..");

        rule.check(importedClasses);
    }

    @Test
    void servicesShouldNotAccessController() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..controller..");

        rule.check(importedClasses);
    }

    @Test
    void repositoryShouldNotAccessedByAnyPackage() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..repository..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..service..", "..controller..", "..dto..");

        rule.check(importedClasses);
    }

    @Test
    void dtoEMappersShouldNotAccessRepositoryORController() {
        ArchRule rule = noClasses()
                .that().resideInAnyPackage("..dto..", "..mapper..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..repository..", "..controller..");

        rule.check(importedClasses);
    }

    @Test
    void modelShouldNotAccessSuperiorPackages() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..model..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..service..", "..repository..", "..controller..");

        rule.check(importedClasses);
    }

    @Test
    void securityShouldNotAccessControllerORDTO() {
        ArchRule rule = noClasses()
                .that().resideInAPackage("..security..")
                .should().dependOnClassesThat()
                .resideInAnyPackage("..controller..", "..dto..");

        rule.check(importedClasses);
    }
}
