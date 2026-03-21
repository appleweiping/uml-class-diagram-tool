UML Class Diagram Tool

A lightweight UML (Unified Modeling Language) class diagram editor designed to create, edit, validate, and export class diagrams through an intuitive user interface.

Overview

This tool enables users to design UML class diagrams efficiently while ensuring correctness through validation rules. It supports saving diagrams, exporting them as images, and verifying structural and syntactic correctness based on UML standards and Java constraints.

Features
Interactive Diagram Editor
Create and modify UML class diagrams
Add classes, attributes, operations, and relationships
Validation System
Ensures compliance with UML specifications:
Class names
Attributes
Operations
Applies Java-based constraints on relationships:
Example: a class can have at most one inheritance relationship
File Support
Save diagrams to file
Load previously saved diagrams
Export Functionality
Export diagrams as images for use in external applications
Undo/Redo Support
Full history tracking of user actions
Architecture & Design Patterns

The system is built using several well-established design patterns:

Command Pattern
Enables undo/redo functionality by encapsulating actions
Observer Pattern
Automatically triggers revalidation when the diagram structure changes
Strategy Pattern
Provides flexible serialization for supporting multiple file formats
Use Cases

The system supports multiple interaction scenarios, including:

Creating and editing class diagrams
Validating diagrams
Saving and exporting diagrams

Additionally, the system includes:

Alternative flows for different user actions
Exception handling for invalid operations
Validation Rules

The validation system combines:

UML rules for structural correctness
Java constraints for relationship restrictions

Examples:

Valid class naming conventions
Proper definition of attributes and methods
Restrictions on inheritance relationships
Getting Started
Prerequisites
Java (recommended version: Java 8 or higher)
Running the Application
Clone the repository
Build the project
Run the main application
git clone <repository-url>
cd <project-folder>
# build & run depending on your setup
Future Improvements
Support for additional UML diagram types
Enhanced UI/UX
More advanced validation rules
Plugin system for extensibility