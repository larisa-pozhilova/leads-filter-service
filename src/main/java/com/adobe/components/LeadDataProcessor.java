package com.adobe.components;

import com.adobe.FieldSelector;
import com.adobe.entities.Lead;
import com.adobe.entities.LeadData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * The {@code LeadDataProcessor} class is responsible for processing lead data from input JSON files,
 * filtering leads based on unique identifiers and email addresses, and writing the filtered results
 * to an output JSON file.
 * <p>
 * The main functionality involves reading a JSON file containing lead data, filtering the leads by
 * unique identifiers (such as ID and email), and saving the filtered leads back to a JSON file.
 * The filtering process ensures that for each identifier (ID or email), only the most recent lead
 * based on the {@code entryDate} is kept.
 * <p>
 * The class uses a {@link JsonProcessor} for reading from and writing to JSON files, and a {@link FieldSelector}
 * functional interface is used to allow flexible filtering by different fields.
 */
@Component
public class LeadDataProcessor {

    private static final Logger logger = LoggerFactory.getLogger(LeadDataProcessor.class);

    private final JsonProcessor jsonProcessor;

    /**
     * Constructs a {@code LeadDataProcessor} instance and initializes the {@code JsonProcessor}.
     * The {@code JsonProcessor} is responsible for handling JSON operations, including reading and writing
     * lead data to and from files.
     */
    public LeadDataProcessor() {
        this.jsonProcessor = new JsonProcessor();
    }

    /**
     * Constructs a {@code LeadDataProcessor} instance and initializes the {@code JsonProcessor}.
     * The {@code JsonProcessor} is responsible for handling JSON operations, including reading and writing
     * lead data to and from files.
     */
    public LeadDataProcessor(final JsonProcessor jsonProcessor) {
        this.jsonProcessor = jsonProcessor;
    }

    /**
     * Filters the collection of {@link Lead} objects based on the selected field and entry date.
     * <p>
     * This method ensures that for each unique value of the selected field (e.g., email, id), only the most
     * recent lead (based on the {@code entryDate}) is kept in the result. The comparison between leads is
     * done using the {@link Lead#getEntryDateTime()} method.
     *
     * @param leads the collection of {@link Lead} objects to filter
     * @param fieldSelector the field selector used to extract the field value for comparison (e.g., {@link Lead::getId})
     * @return a map of filtered leads, with the field value as the key and the most recent lead as the value
     */
    public Map<String, Lead> filterBy(Collection<Lead> leads, FieldSelector fieldSelector) {
        Map<String, Lead> latestLeads = new HashMap<>();

        // Iterate through the leads and update the map with the most recent lead for the selected field
        for (Lead lead : leads) {
            String fieldValue = fieldSelector.select(lead);  // Use FieldSelector to get the field value (email, id, etc.)
            latestLeads.merge(fieldValue, lead, (existing, newLead) -> {
                if (newLead.getEntryDateTime().isAfter(existing.getEntryDateTime())) {
                    logChanges(fieldValue, existing, newLead); // Log the detailed changes
                    return newLead; // Keep the new lead
                } else {
                    logger.info("No update for {}: Existing record is newer or the same.", fieldValue);
                    return existing; // Keep the existing lead
                }
            });
        }
        return latestLeads;
    }

    private void logChanges(String fieldValue, Lead existing, Lead newLead) {
        StringBuilder logMessage = new StringBuilder();
        logMessage.append("Updating record for field: ").append(fieldValue).append("\n")
                .append("Existing Record: ").append(existing).append("\n")
                .append("New Record: ").append(newLead).append("\n")
                .append("Field Changes:\n");

        if (!existing.getEntryDateTime().equals(newLead.getEntryDateTime())) {
            logMessage.append("    - Entry DateTime: ").append(existing.getEntryDateTime())
                    .append(" -> ").append(newLead.getEntryDateTime()).append("\n");
        }

        logger.info(logMessage.toString());
    }

    /**
     * Filters the list of {@link Lead} objects first by unique ID and then by unique email, retaining
     * the most recent lead for each field based on the entry date.
     * <p>
     * This method applies two levels of filtering:
     * <ul>
     *     <li>First, it filters by unique ID, keeping only the most recent lead for each ID.</li>
     *     <li>Then, it filters by unique email, retaining the most recent lead for each email.</li>
     * </ul>
     *
     * @param leads the list of {@link Lead} objects to filter
     * @return a list of filtered {@link Lead} objects, with unique IDs and emails, retaining the most recent records
     */
    List<Lead> filterLeadsByIdAndEmail(List<Lead> leads) {

        // Step 1: Filter by unique _id, keeping the record with the newest entryDate
        Map<String, Lead> filteredById = filterBy(leads, Lead::getId);

        // Step 2: Filter by unique email, retaining the most recent one based on entryDate
        Map<String, Lead> filteredByEmail = filterBy(filteredById.values(), Lead::getEmail);

        // Log the final filtered map size
        logger.info("Filtering complete. Total unique leads by ID: {}", filteredByEmail.size());

        // Print filtered results
        return new ArrayList<>(filteredByEmail.values());
    }

    /**
     * Processes lead data from an input JSON file, filters the leads, and writes the filtered data to an output file.
     * <p>
     * This method reads the lead data from a specified input file, applies the filtering process to retain only
     * the most recent leads by ID and email, and writes the filtered lead data to the specified output file.
     *
     * @param inputFilePath the path to the input JSON file containing the lead data
     * @param outputFilePath the path to the output JSON file where the filtered lead data will be written
     */
    public void processLeads(final String inputFilePath, final String outputFilePath) throws IOException {
        try {
            // Step 1: Read the input JSON file into a list of Lead objects
            File inputFile = new File(inputFilePath);
            LeadData leadData = jsonProcessor.readLeadsFromFile(inputFile);

            if (leadData == null) {
                logger.error("No leads to process.");
                throw new IOException("No leads to process.");
            }

            List<Lead> leads = leadData.getLeads();

            // Step 2: Apply filtering
            List<Lead> filteredLeads = filterLeadsByIdAndEmail(leads);

            // Step 3: Write the filtered leads to the output JSON file
            File outputFile = new File(outputFilePath);
            jsonProcessor.writeLeadsToFile(outputFile, Collections.singletonMap("leads", filteredLeads));

            logger.info("Filtered leads have been written to '" + outputFilePath + "'");

        } catch (IOException e) {
            logger.error("An error occurred while processing leads: " + e.getLocalizedMessage());
            throw e;
        }
    }
}
