package com.notification.fitness;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Fitness Function: Service Layer Coupling
 * 
 * Thresholds:
 * - Maximum 3 service dependencies per service class
 * - Maximum 5 repository dependencies per service class (informational)
 */
public class ServiceCouplingFitnessFunction {

    private static final int MAX_SERVICE_DEPENDENCIES = 3;
    private static final int MAX_REPOSITORY_DEPENDENCIES = 5;
    private static final String SERVICE_PACKAGE = "com/notification/service";
    private static final String SRC_PATH = "src/main/java";

    public static void main(String[] args) {
        ServiceCouplingFitnessFunction fitnessFunction = new ServiceCouplingFitnessFunction();
        boolean passed = fitnessFunction.execute();
        System.exit(passed ? 0 : 1);
    }

    public boolean execute() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  SERVICE LAYER COUPLING FITNESS FUNCTION");
        System.out.println("=".repeat(60));
        System.out.println("Architectural Characteristic: Modifiability");
        System.out.println("Service Dependency Threshold: Maximum " + MAX_SERVICE_DEPENDENCIES + " per service");
        System.out.println("Repository Dependency Warning: Maximum " + MAX_REPOSITORY_DEPENDENCIES + " per service");
        System.out.println("=".repeat(60) + "\n");


        try {
            Path servicePath = Paths.get(SRC_PATH, SERVICE_PACKAGE);
            
            if (!Files.exists(servicePath)) {
                System.err.println("!!! Service package not found: " + servicePath);
                return false;
            }

            List<ServiceAnalysis> analyses = analyzeServices(servicePath);
            
            return reportResults(analyses);

        } catch (IOException e) {
            System.err.println("!!! Error analyzing services: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private List<ServiceAnalysis> analyzeServices(Path servicePath) throws IOException {
        List<ServiceAnalysis> analyses = new ArrayList<>();

        try (Stream<Path> paths = Files.walk(servicePath)) {
            List<Path> serviceFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".java"))
                    .filter(p -> !p.toString().contains("skiResortSites"))
                    .filter(p -> !p.getFileName().toString().equals("NotificationService.java")) // Interface
                    .filter(p -> !p.getFileName().toString().equals("PushNotificationService.java")) // Interface
                    .collect(Collectors.toList());

            for (Path serviceFile : serviceFiles) {
                String fileName = serviceFile.getFileName().toString();
                
                if (fileName.endsWith("Service.java") || fileName.endsWith("ServiceImpl.java")) {
                    ServiceAnalysis analysis = analyzeServiceFile(serviceFile);
                    analyses.add(analysis);
                }
            }
        }

        return analyses;
    }

    private ServiceAnalysis analyzeServiceFile(Path serviceFile) throws IOException {
        String fileName = serviceFile.getFileName().toString();
        String className = fileName.replace(".java", "");
        String content = Files.readString(serviceFile);

        Set<String> serviceDependencies = extractDependencies(content, "Service");
        Set<String> repositoryDependencies = extractDependencies(content, "Repository");

        return new ServiceAnalysis(className, serviceDependencies, repositoryDependencies);
    }

    private Set<String> extractDependencies(String content, String suffix) {
        // Use TreeSet with case-insensitive comparator to avoid duplicates like "Repository" and "repository"
        Set<String> dependencies = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

        // Pattern for constructor parameters
        Pattern constructorPattern = Pattern.compile(
                "public\\s+\\w+\\s*\\([^)]*\\)",
                Pattern.MULTILINE
        );

        // Pattern for field declarations
        Pattern fieldPattern = Pattern.compile(
                "private\\s+(?:final\\s+)?(\\w+" + suffix + ")\\s+\\w+\\s*;",
                Pattern.MULTILINE
        );

        // Extract from constructor
        Matcher constructorMatcher = constructorPattern.matcher(content);
        while (constructorMatcher.find()) {
            String match = constructorMatcher.group(0);
            Pattern depPattern = Pattern.compile("\\b(\\w+" + suffix + ")\\b");
            Matcher depMatcher = depPattern.matcher(match);
            while (depMatcher.find()) {
                dependencies.add(depMatcher.group(1));
            }
        }

        // Extract from fields
        Matcher fieldMatcher = fieldPattern.matcher(content);
        while (fieldMatcher.find()) {
            dependencies.add(fieldMatcher.group(1));
        }

        return dependencies;
    }

    private boolean reportResults(List<ServiceAnalysis> analyses) {
        List<ServiceAnalysis> violations = new ArrayList<>();
        List<ServiceAnalysis> warnings = new ArrayList<>();
        List<ServiceAnalysis> passed = new ArrayList<>();

        for (ServiceAnalysis analysis : analyses) {
            System.out.println("Analyzing: " + analysis.className);
            System.out.println("   Service Dependencies: " + analysis.serviceDependencyCount + 
                             (analysis.serviceDependencies.isEmpty() ? "" : 
                              " (" + String.join(", ", analysis.serviceDependencies) + ")"));
            System.out.println("   Repository Dependencies: " + analysis.repositoryDependencyCount + 
                             (analysis.repositoryDependencies.isEmpty() ? "" :
                              " (" + String.join(", ", analysis.repositoryDependencies) + ")"));
            
            if (analysis.hasServiceViolation()) {
                System.out.println("   Status: !!! VIOLATION (service dependencies exceed threshold)");
                violations.add(analysis);
            } else if (analysis.hasRepositoryWarning()) {
                System.out.println("   Status:  WARNING (many repository dependencies)");
                warnings.add(analysis);
            } else {
                System.out.println("   Status: ✓ PASS");
                passed.add(analysis);
            }
            System.out.println();
        }

        // Summary
        System.out.println("=".repeat(60));
        System.out.println("  RESULTS");
        System.out.println("=".repeat(60));
        System.out.println("Total Services Analyzed: " + analyses.size());
        System.out.println("✓ Passed: " + passed.size());
        System.out.println("!  Warnings: " + warnings.size());
        System.out.println("!!! Violations: " + violations.size());
        System.out.println();

        if (!violations.isEmpty()) {
            System.out.println("!!! SERVICE COUPLING VIOLATIONS:");
            System.out.println();
            for (ServiceAnalysis violation : violations) {
                System.out.println("  • " + violation.className + 
                                 " has " + violation.serviceDependencyCount + 
                                 " service dependencies (max: " + MAX_SERVICE_DEPENDENCIES + ")");
                System.out.println("    Dependencies: " + String.join(", ", violation.serviceDependencies));
            }
            System.out.println();
        }

        if (!warnings.isEmpty()) {
            System.out.println("!  REPOSITORY DEPENDENCY WARNINGS:");
            System.out.println();
            for (ServiceAnalysis warning : warnings) {
                System.out.println("  • " + warning.className + 
                                 " has " + warning.repositoryDependencyCount + 
                                 " repository dependencies (recommended max: " + MAX_REPOSITORY_DEPENDENCIES + ")");
            }
            System.out.println();
        }

        if (violations.isEmpty() && warnings.isEmpty()) {
            System.out.println("✓ ALL CHECKS PASSED");
            System.out.println();
        }

        System.out.println("=".repeat(60));
        return violations.isEmpty();
    }

    private class ServiceAnalysis {
        String className;
        Set<String> serviceDependencies;
        Set<String> repositoryDependencies;
        int serviceDependencyCount;
        int repositoryDependencyCount;

        ServiceAnalysis(String className, Set<String> serviceDependencies, Set<String> repositoryDependencies) {
            this.className = className;
            this.serviceDependencies = serviceDependencies;
            this.repositoryDependencies = repositoryDependencies;
            this.serviceDependencyCount = serviceDependencies.size();
            this.repositoryDependencyCount = repositoryDependencies.size();
        }

        boolean hasServiceViolation() {
            return serviceDependencyCount > MAX_SERVICE_DEPENDENCIES;
        }

        boolean hasRepositoryWarning() {
            return repositoryDependencyCount > MAX_REPOSITORY_DEPENDENCIES;
        }
    }
}
