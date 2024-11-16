package com.adobe.controllers;

import com.adobe.components.LeadDataProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST controller for handling lead data processing requests.
 * <p>
 * This controller provides an API endpoint for processing lead data from an input JSON file,
 * filtering the leads, and writing the filtered data to an output JSON file.
 * </p>
 */
@RestController
@RequestMapping("/api")
public class LeadsController {

    private final LeadDataProcessor leadDataProcessor;

    /**
     * Constructs a new {@code LeadsController} with the provided {@link LeadDataProcessor}.
     *
     * @param leadDataProcessor the {@link LeadDataProcessor} used for processing leads
     */
    @Autowired
    public LeadsController(LeadDataProcessor leadDataProcessor) {
        this.leadDataProcessor = leadDataProcessor;
    }

    /**
     * Processes lead data from an input JSON file and writes the filtered data to an output JSON file.
     * <p>
     * This method accepts the paths to the input and output files as request parameters, invokes the
     * {@link LeadDataProcessor} to process the leads, and returns a response indicating success or failure.
     * </p>
     *
     * @param input  the path to the input JSON file containing the lead data
     * @param output the path to the output JSON file where the filtered lead data will be written
     * @return a {@link ResponseEntity} containing a success message if the operation completes successfully,
     * or an error message if an exception occurs
     */
    @PostMapping("/process-leads")
    public ResponseEntity<String> getLeads(@RequestParam final String input,
                                           @RequestParam final String output) {
        try {
            leadDataProcessor.processLeads(input, output);
            return ResponseEntity.ok("Filtered leads have been written");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(e.getLocalizedMessage());
        }
    }

}
