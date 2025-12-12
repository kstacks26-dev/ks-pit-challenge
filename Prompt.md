# AI Collaboration Challenge - Bulk Operations Feature

## Feature Request
Add a product update feature that allows users to perform basic update actions (price updates, category changes, deletion) with appropriate confirmation responses and error handling.

## 1. AI Tool Selection

**Which AI tool would you choose and why?**

Copilot is where my comfort level and experience lies. At my prior company we had to use it exclusively, therefore it would be my initial goto for no other reason than that. Copilot is great for existing project/IDE integration and inline code completion which provided the flexibility we needed for working on the numerous/different projects we aligned with. It may not be the right choice for more AI-forward initiatives and there are plenty of solutions available that are potentially better suited.

Overall the choice could be dictated by requirements such as those you've provided within for this challenge such as - does it excel at generating via comprehensive prompting or would a collaborative approach be better? Considering the context window may also be a factor given codebase size and how it will be used. An argument could also be made for using multiple tools leveraging strenghts of each for the desired purpose.

## 2. Comprehensive Prompt

**Write your complete prompt including context about the codebase architecture and any constraints:**

You are a senior Java developer asked to implement a Bulk Product Update feature within an existing Product Inventory Tracker application. The application is built with Java 21, Spring Boot, Spring Data JPA, Jakarata Validation, and an H2 in memory database as well as JUnit for unit tests.

The goal is to add service and repository methods that allow users to perform bulk updates and bulk deletes within a single request. The product attributes include id, name, price, category, and available. The feature should allow users to perform bulk udpates to name, category, price, and available, in addition to bulk deletes of products by id. It should also include proper validation, confirmation responses, and error handling.

The generated output should include appropriate changes to the existing project classes and interfaces following architecture principles that includes using DTOs and Service Layers, implement validation and exception handling and design for scalability.

* Existing Architecture:
    * Code is structured as follows: Service -> Repository -> DTO -> Entity
    * Existing classes and interfaces: ProductService, ProductRepository, ProductDTO, Product

* Requirements:
    * Bulk update - accepts a list of products by ID and updated fields (name, price, category, available).
    * Bulk delete - accepts a list of product IDs to delete.
    * Maximum number of records is unknown but expect to handle 500 - 1000 effeciently.
    * Each record should be validated.
    * Both bulk update and delete should support both "all or nothing" and partial success options.
    * In the case of "all or nothing", atomicity should be considered including the use of @Transactional annotation and rollback for failures.

* Validation:
    * Product IDs must already exist.
    * Price cannot be negative.
    * Category should be validated against a fixed/enumerated list (Electronics, Home Appliances, Apparel, Accessories, Home Decor, Office Supplies, Outdoor).
        * NOTE: this text is not part of prompt but a comment to indicate Category would ideally be its own managed Entity/Db Table/Data and validation would be done against the values in the Db, but I have referenced the above mock data list for simplicity as an example.

* Implementation
    * Generate code that follows the existing code structure: Service -> Repository -> DTO -> Entity
    * Use modern Java 21 syntax (records, streams, etc.).
    * Demonstrate separation of concerns, readability, and maintainability.
    * Include proper exception/error handling and validation feedback.
        * A structured response should be returned indicating what occurred. For example using the following JSON structure:
        ```
        {
            "recordsProcessed": 25,
            "successCount": 24,
            "failureCount": 1,
            "errors": [
                {"id": 104, "message": "Invalid category"}
            ]
        }
        ```

* What to include:
    * Documentation describing the methods, usage, and purpose
    * Validation and error-handling examples.
    * Unit tests for the methods covering successes, failures, and potential edge cases including all or nothing and partial success and failure approaches.


## 3. Collaboration Approach

**How would you iterate and collaborate with the AI tool to implement this feature?**

1. Seed the ai tool with the requirements and provide it context about expectations:

This project uses Java 21, Spring Boot, Spring Data JPA, Jakarata Validation, and an H2 in memory database as well as JUnit for unit tests. The following classes already exist: Product entity, ProductDTO, ProductService and ProductRepository. I want to add a bulk operations feature that allows updating and deleting multiple products at once with proper validation and error handling.

2. Incrementally generate each layer.

In ProductService, implement methods for bulk update and bulk delete that support both "all or nothing" and partial updates. Use transactions for "all or nothing", handle partial failures gracefully and return a structured summary of results.

// NOTE: This is where would be some iterative back and forth with the ai tool to refine based on how it's responding..

    * Add validation to support non-negative prices
    * Add category validation against a fixed/enumerated list (Electronics, Home Appliances, Apparel, Accessories, Home Decor, Office Supplies, Outdoor).
    * The structured response should be indicate what occurred. For example include a JSON response with the following attributes: recordsProcessed, successCount, failureCount, and an array of errors that include details about each of the failed records.

3. Add tests

Create tests for the update and delete methods for both successes and failures covering both all or nothing and partial success paths.

Add tests verifying that invalid data (incorrect categories for example) causes full and partial failures.

// NOTE: Iteratively run tests, inspect coverage, regenerate or tweak with ai as necessary.

4. Add Sample requests and documentation

Generate documentation for the service methods including sample requests as necessary.

5. Review and refactor

Review the service and suggest ways of simplifying or modernizing the code using Java 21 features.



