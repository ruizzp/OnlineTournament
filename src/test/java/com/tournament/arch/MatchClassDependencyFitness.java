package com.tournament.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

public class MatchClassDependencyFitness {

    private final JavaClasses importedClasses = new ClassFileImporter().importPackages(
        "com.tournament.controller",
        "com.tournament.service",
        "com.tournament.repository"
    );

    @Test
    void matchClassesShouldOnlyBeAccessedByOtherMatchClasses() {
        ArchRule rule = ArchRuleDefinition.classes()
            .that().haveNameMatching(".*[Mm]atch.*")
            .should().onlyHaveDependentClassesThat().haveSimpleName(".*[Mm]atch.*");

        rule.check(importedClasses);
    }
}
