package com.tournament.arch;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.dependencies.SliceRule;
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition;
import org.junit.jupiter.api.Test;

public class CycleFitnessTest {

    @Test
    public void noCyclesBetweenPackages() {
        JavaClasses importedClasses = new ClassFileImporter().withImportOption(new ImportOption.DoNotIncludeTests()).importPackages("com.tournament");

        SliceRule rule = SlicesRuleDefinition.slices()
                .matching("com.tournament.(*)..")
                .should().beFreeOfCycles();
        rule.check(importedClasses);

    }
}
