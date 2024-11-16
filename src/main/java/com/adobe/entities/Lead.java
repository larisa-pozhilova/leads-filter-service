package com.adobe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a {@code Lead} object containing personal and contact information.
 * <p>
 * The {@code Lead} class includes fields such as {@code id}, {@code email}, {@code firstName},
 * {@code lastName}, {@code address}, and {@code entryDate}, which are serialized and deserialized
 * from JSON using Jackson annotations. The class includes getter and setter methods for these fields
 * and handles the conversion of the {@code entryDate} field to a {@link LocalDateTime} object.
 * <p>
 * The {@link #getEntryDateTime()} method parses the entry date string into a {@link LocalDateTime}
 * object for easier manipulation and comparison.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(
        {"id", "email", "firstName", "lastName", "address", "entryDate"}
)
public class Lead {
    @JsonProperty("_id")
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String entryDate;

    /**
     * Retrieves the entry date as a {@link LocalDateTime} object.
     * <p>
     * This method parses the {@code entryDate} string, which is expected to be in ISO_OFFSET_DATE_TIME
     * format, and returns it as a {@link LocalDateTime}.
     *
     * @return the {@link LocalDateTime} representation of the entry date
     */
    @JsonIgnore
    public LocalDateTime getEntryDateTime() {
        return LocalDateTime.parse(entryDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * Returns a string representation of the {@code Lead} object.
     * <p>
     * The returned string includes the values of all the fields of the {@code Lead} object
     * in the following format:
     * <pre>
     * Lead{id='id', email='email', firstName='firstName', lastName='lastName', address='address', entryDate='entryDate'}
     * </pre>
     *
     * @return a string representation of the {@code Lead} object
     */
    @Override
    public String toString() {
        return "Lead{id='" + id + "', email='" + email + "', firstName='" + firstName +
                "', lastName='" + lastName + "', address='" + address +
                "', entryDate='" + entryDate + "'}";
    }
}

