package com.adobe.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

/**
 * Represents a container for a list of {@link Lead} objects.
 * <p>
 * The {@code LeadData} class holds a collection of {@link Lead} instances. It provides getter and setter
 * methods for accessing and modifying the list of leads.
 * <p>
 * This class can be used to manage and manipulate a collection of leads, such as when deserializing from
 * or serializing to JSON. The list of leads is encapsulated as a {@link List} of {@link Lead} objects.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LeadData {

    /**
     * A list of {@link Lead} objects.
     * <p>
     * This field holds all the {@link Lead} objects associated with the lead data.
     *
     * @see Lead
     */
    private List<Lead> leads;

}
