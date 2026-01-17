package com.notification;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@AnalyzeClasses(packages = "com.notification")
public class ArchitectureFitnessTest {

	// Rule 1: Controllers should never talk to Repositories directly
	@ArchTest
	static final ArchRule controllers_should_not_access_repositories =
			noClasses().that().areAnnotatedWith(Controller.class)
					.should().dependOnClassesThat().areAnnotatedWith(Repository.class)
					.because("Controllers should delegate to Services, not access the DB directly.");

	// Rule 2: Services must be in the correct package
	@ArchTest
	static final ArchRule services_should_be_in_service_package =
			classes().that().areAnnotatedWith(Service.class)
					.should().resideInAPackage("..service..")
					.because("Service logic must be centralized in the service package for discoverability.");


	// Rule 3: Repositories must be in the correct package
	@ArchTest
	static final ArchRule repositories_should_be_in_repository_package =
			classes().that().areAnnotatedWith(Repository.class)
					.should().resideInAPackage("..repository..")
					.because("Repository logic must be centralized in the repository package for discoverability.");

	// Rule 4: Services should not depend on Controllers
	@ArchTest
	static final ArchRule services_should_not_access_controllers =
			noClasses().that().areAnnotatedWith(Service.class)
					.should().dependOnClassesThat().areAnnotatedWith(Controller.class)
					.because("Services should be independent of the web layer.");

	// Rule 5: Repositories should not depend on Services or Controllers
	@ArchTest
	static final ArchRule repositories_should_not_access_services_or_controllers =
			noClasses().that().areAnnotatedWith(Repository.class)
					.should().dependOnClassesThat().areAnnotatedWith(Service.class)
					.orShould().dependOnClassesThat().areAnnotatedWith(Controller.class)
					.because("Repositories should only interact with the database layer.");
}