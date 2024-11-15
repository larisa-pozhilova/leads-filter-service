package com.adobe;

import com.adobe.components.LeadDataProcessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Spring Boot application. This class initializes the application
 * and triggers the lead data processing logic once the application starts.
 * <p>
 * This class implements {@link CommandLineRunner} to run the lead data processing logic
 * when the application starts.
 * </p>
 */
@SpringBootApplication
public class Application implements CommandLineRunner {

    /**
     * The {@link LeadDataProcessor} instance used to process the lead data.
     */
    private final LeadDataProcessor leadDataProcessor;

    public Application(LeadDataProcessor leadDataProcessor) {
        this.leadDataProcessor = leadDataProcessor;
    }

    /**
     * The main entry point for the Spring Boot application.
     * <p>
     * It launches the Spring Boot application by invoking {@link SpringApplication#run(Class, String...)}.
     * </p>
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * Executes the lead data processing logic after the Spring Boot application has started.
     * <p>
     * The method processes the leads from the input file and writes the filtered leads to the output file.
     * The input and output file paths are specified as arguments to the {@link LeadDataProcessor#processLeads(String, String)} method.
     * </p>
     *
     * @param args command-line arguments (not used in this method)
     * @throws Exception if any error occurs during lead data processing
     */
    @Override
    public void run(String... args) throws Exception {
        // Example of running the processing logic
        leadDataProcessor.processLeads("leads.json", "filtered_leads_output.json");
    }
}
