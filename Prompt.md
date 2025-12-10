# AI Collaboration Challenge - Bulk Operations Feature

## Feature Request
Add a product update feature that allows users to perform basic update actions (price updates, category changes, deletion) with appropriate confirmation responses and error handling.

## 1. AI Tool Selection

**Which AI tool would you choose and why?**

Copilot would be where my comfort level and experience lies. At my prior company we had to use it exclusively, therefore it would be my initial goto for no other reason than that. I would be open to and welcome leveraging other ai tools (Cursor would be interesting) as need and curiosity dictate.

## 2. Comprehensive Prompt

**Write your complete prompt including context about the codebase architecture and any constraints:**

NOTE: This text is NOT part of my prompt but to indicate I'm making the assumption that I'm leveraging the codebase to add additional methods to support bulk updates and not introducing the ability to do so via REST/with a file for example. So the changes begin within the existing code at the service layer and not with the addition of new code to support a bulk update REST endpoint for file uploads for example.

Here's where my prompt begins:

You are a senior Java developer asked to implement a Bulk Product Update feature within an existing Product Inventory Tracker application. The app is built with Java 21, Spring Boot, Spring Data JPA, Jakarata Validation, and an H2 in memory database as well as JUnit for unit tests.

The goal is to add service and repository methods that allow users to perform bulk updates and bulk deletes within a single request. The product attributes include id, name, price, category, and available. The feature should allow users to perform bulk udpates to name, category, price, and available, in addition to bulk deletes of products by id. It should also include proper validation, confirmation responses, and error handling.

The generated output should include:

* ProductService and ProductRepository Methods:
    * updateProducts - accepts a list of products by ID and updated fields (name, price, category, available).
    * deleteProducts - accepts a list of product IDs to delete.

* Validation:
    * Product IDs must already exist.
    * Price cannot be negative.
    * Category should be validated against a fixed/enumerated list (Electronics, Home Appliances, Apparel, Accessories, Home Decor, Office Supplies, Outdoor).
        * NOTE: this text is not part of prompt but a comment to indicate Category would ideally be its own managed Entity/Db Table/Data and validation would be done against the values in the Db, but I have referenced the above mock data list for simplicity as an example.

* Behavior:
    * Bulk update and delete should support both "all or nothing" and partial successes options.
    * In the case of "all or nothing", @Transactional annotation should be used to ensure atomicity.
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
    * Include proper error handling and validation feedback for each unsuccessful item.

* Implementation
    * Generate code that follows the existing code structure: Service -> Repository -> DTO -> Entity
    * Use modern Java 21 syntax (records, streams, etc.).
    * Demonstrate separation of concerns, readability, and maintainability.
* What to include:
    * Comprehensive Javadoc describing the methods, usage, and purpose
    * Validation and error-handling examples.
    * Unit tests for the methods covering successes and failures including all or nothing and partial success/failure approaches.


## 3. Collaboration Approach

**How would you iterate and collaborate with the AI tool to implement this feature?**

I'm making the assumption that this portion is being completed within an IDE using the integrated AI tool (Copilot, etc.).

1. Seed the ai tool with the requirements and provide it context about expectations:

Implement a Bulk Product Update feature for the Product Inventory Tracker. Requirements: bulk update and delete service methods, validation, and error handling with structured responses following the existing project structure.

2. Incrementally generate each layer.

In ProductService implement updateProducts and deleteProducts. Use transactions as necessary (all or nothing), handle failures gracefully, and return a structured summary of results.

// NOTE: This is where there could be some repeated back and forth with the ai tool to refine based on how it's responding, example:
    * Refine response object to include counts of success or failures for partial updates/deletes.
    * Use Streams to map update results and produce summary object.
    * Add validation.
    * Add error aggregation per product.

3. Add tests

Create ProductService tests for the update and delete methods for both successes and failures. Cover both all or nothing and partial success paths.

Add tests verifying that invalid data (incorrect categories for example) causes full and partial failures.
    
    * Run tests, inspect coverage, regenerate or tweak as necessary.

4. Add Sample requests and documentation

Generate documentation for the methods including sample requests.



