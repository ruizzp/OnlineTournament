package com.tournament.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class InheritanceFitness {

    @Test
    void controllersShouldExtendBaseController_or_followNamingConvention() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.tournament");

        ArchRule rule;
        try {
            Class<?> base = Class.forName("com.tournament.controller.BaseController");
            rule = classes()
                    .that().resideInAPackage("..controller..")
                    .should().beAssignableTo(base)
                    .because("All controllers should inherit common behavior from BaseController");
        } catch (ClassNotFoundException e) {
            // Fallback if BaseController doesn't exist: enforce naming convention only on production classes
            rule = classes()
                    .that().resideInAPackage("..controller..")
                    .should().haveSimpleNameEndingWith("Controller")
                    .because("BaseController not found: enforce that controller classes follow the '*Controller' naming convention");
        }

        rule.check(classes);
    }

    @Test
    void onlyControllersShouldExtendBaseController_or_enforceNaming() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.tournament");

        ArchRule rule;
        try {
            Class<?> base = Class.forName("com.tournament.controller.BaseController");
            rule = classes()
                    .that().areAssignableTo(base)
                    .should().resideInAPackage("..controller..")
                    .because("Only classes in ..controller.. should extend BaseController");
        } catch (ClassNotFoundException e) {
            rule = classes()
                    .that().resideInAPackage("..controller..")
                    .should().haveSimpleNameEndingWith("Controller")
                    .because("No BaseController found; enforcing naming convention instead");
        }

        rule.check(classes);
    }
}
