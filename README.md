---

# Lead Data Processor

## Overview

The **Lead Data Processor** application processes lead data from an input JSON file, filters the leads to retain only the most recent records based on unique identifiers, and writes the filtered data to an output JSON file. It can run as a standalone application or as an application server providing REST API endpoints for lead processing.

---

## Features

- Reads lead data from a JSON file.
- Filters leads to keep the most recent entry for each unique ID and email combination.
- Writes the filtered lead data to an output JSON file.
- Provides a REST API to process lead data via HTTP requests.
- Detailed logging of the filtering process, including field changes for updated records.

---

## How It Works

1. **Input File**: The application takes a JSON file containing lead data as input.
2. **Filtering**: Leads are filtered to retain only the most recent records based on their unique identifiers (ID and email).
3. **Output File**: The filtered data is written to a specified JSON file.
4. **REST API**: The application can also run as a Spring Boot server, exposing an endpoint for lead data processing.

---

## Prerequisites

- **Java Development Kit (JDK)** 11 or later.
- **Build Tool**: Maven.
- **Spring Boot Framework**: Ensure dependencies for Spring Boot are included in your project.

---

## Running the Application

### As a Standalone Application

#### Using Maven

1. **Build the project:**

   ```bash
   mvn clean package
   ```

2. **Run the application:**

   ```bash
   java -jar target/lead-data-processor.jar --input-file=/path/to/input.json --output-file=/path/to/output.json
   ```

---

### As an Application Server

#### Running the Server

**Using Maven**

1. **Build the project:**

   ```bash
   mvn clean package
   ```

2. **Run the Spring Boot application:**

   ```bash
   mvn spring-boot:run
   ```

**Alternatively, run the packaged JAR:**

1. **Build the project (if not already built):**

   - **Maven:**

     ```bash
     mvn clean package
     ```

2. **Run the application:**

   ```bash
   java -jar target/lead-data-processor.jar
   ```

The server will start on the default port `8080` (or as configured in your `application.properties`).

#### Using the API Endpoint

You can send a POST request to process leads via the following endpoint:

- **Endpoint**: `/api/process-leads`
- **HTTP Method**: `POST`
- **Parameters**:
  - `input`: Path to the input JSON file containing lead data.
  - `output`: Path to the output JSON file where the filtered data will be written.

##### Example Request Using cURL

```bash
curl -X POST "http://localhost:8080/api/process-leads" \
     -d "input=/path/to/input.json" \
     -d "output=/path/to/output.json"
```

##### Example Response

- **Success**:

  ```plaintext
  HTTP 200 OK
  Filtered leads have been written
  ```

- **Error**:

  ```plaintext
  HTTP 500 Internal Server Error
  The input file does not exist or is invalid.
  ```

---

## Application Arguments

When running as a standalone application, the following arguments are used:

- **`--input-file`**: Path to the input JSON file containing the lead data.
- **`--output-file`**: Path to the output JSON file where the filtered data will be saved.

Example:

```bash
java -jar lead-data-processor.jar --input-file=/data/leads.json --output-file=/data/filtered_leads.json
```

---

## Example Input File

```json
{
  "leads": [
    {
      "id": "1",
      "email": "john.doe@example.com",
      "name": "John Doe",
      "address": "123 Elm Street",
      "entryDateTime": "2024-01-01T10:00:00Z"
    },
    {
      "id": "1",
      "email": "john.doe@example.com",
      "name": "Johnathan Doe",
      "address": "456 Maple Avenue",
      "entryDateTime": "2024-01-02T12:00:00Z"
    }
  ]
}
```

---

## Example Output File

```json
{
  "leads": [
    {
      "id": "1",
      "email": "john.doe@example.com",
      "name": "Johnathan Doe",
      "address": "456 Maple Avenue",
      "entryDateTime": "2024-01-02T12:00:00Z"
    }
  ]
}
```

---

## Logging

The application logs details of the filtering process, including:

- Records replaced with newer versions.
- Changes in fields between old and new records.
- Errors encountered during processing.

Example log output:

```plaintext
INFO: Updating record for field: john.doe@example.com
Existing Record: {id='1', email='john.doe@example.com', name='John Doe', ...}
New Record: {id='1', email='john.doe@example.com', name='Johnathan Doe', ...}
Field Changes:
    - Name: John Doe -> Johnathan Doe
    - Address: 123 Elm Street -> 456 Maple Avenue
    - Entry DateTime: 2024-01-01T10:00:00Z -> 2024-01-02T12:00:00Z
```

---

## Error Handling

- **Empty Input File**: Logs an error and throws an `IOException` if the input file contains no leads.
- **Invalid JSON Format**: Logs an error and throws an `IOException` for improperly formatted input files.
- **File I/O Errors**: Logs detailed errors and propagates exceptions for any file reading or writing issues.

---

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

---

## License

This project is licensed under the MIT License.

---

## Contact

For questions or support, please contact: **[Your Email Address]**

---
