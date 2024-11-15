package com.adobe.components;

import com.adobe.entities.Lead;
import com.adobe.entities.LeadData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * The {@code JsonProcessor} class is responsible for reading and writing JSON files
 * containing {@link LeadData} objects. It uses Jackson's {@link ObjectMapper} for
 * JSON processing and handles {@link LocalDateTime} serialization and deserialization.
 * <p>
 * This class provides methods to read JSON data from a file and write JSON data to a file,
 * while ensuring the correct handling of dates as {@link LocalDateTime} objects.
 */
@Component
public class JsonProcessor {
    private final ObjectReader objectReader;
    private final ObjectWriter objectWriter;

    /**
     * Constructs a new {@code JsonProcessor} object, initializing the {@link ObjectMapper} with
     * the necessary configuration to handle {@link LocalDateTime} and pretty print JSON output.
     * <p>
     * The {@link JavaTimeModule} is registered to support {@link LocalDateTime} serialization,
     * and timestamps for dates are disabled to ensure the dates are written in ISO format.
     */
    public JsonProcessor() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDateTime support
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Disable timestamps for dates

        // Set up ObjectReader and ObjectWriter
        this.objectReader = objectMapper.readerFor(LeadData.class);
        this.objectWriter = objectMapper.writerWithDefaultPrettyPrinter();  // Pretty print for better readability
    }


    /**
     * Reads a JSON file and deserializes its contents into a {@link LeadData} object.
     * <p>
     * The JSON file must conform to the expected structure for {@link LeadData}. This method
     * will convert the JSON into an instance of {@link LeadData}, which contains a list of {@link Lead} objects.
     *
     * @param file the JSON file containing lead data to be read
     * @return a {@link LeadData} object representing the contents of the JSON file
     * @throws IOException if there is an error reading the file or parsing the JSON data
     */
    public LeadData readLeadsFromFile(File file) throws IOException {

        if (file == null || !file.exists() || file.length() == 0) {
            throw new IOException("The input file is empty or does not exist.");
        }

        LeadData leadData = objectReader.readValue(file);
        if (leadData == null || leadData.getLeads() == null || leadData.getLeads().isEmpty()) {
            throw new IOException("The input JSON file contains no leads to process.");
        }

        return leadData;
    }

    /**
     * Writes a {@link Map} of leads to a JSON file.
     * <p>
     * The method converts the provided map of leads to JSON format and writes it to the specified file.
     * The JSON is pretty printed for better readability.
     *
     * @param file the destination file where the JSON data will be written
     * @param leads a {@link Map} containing lists of {@link Lead} objects to be written to the file
     * @throws IOException if there is an error writing to the file or serializing the data
     */
    public void writeLeadsToFile(File file, Map<String, List<Lead>> leads) throws IOException {
        objectWriter.writeValue(file, leads);
    }
}

