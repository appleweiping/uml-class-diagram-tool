# UML Class Diagram Tool

A desktop-based UML (Unified Modeling Language) class diagram editor that supports creation, validation, and export of diagrams with a user-friendly interface.

## ✨ Features

* 🧩 Create and edit UML class diagrams
* ✅ Automatic validation based on UML and Java constraints
* 🔄 Undo / Redo functionality
* 💾 Save and load diagrams
* 🖼 Export diagrams as images

## 🧠 Validation Rules

The tool ensures correctness using:

* UML standards:

  * Class naming
  * Attributes
  * Operations
* Java constraints:

  * Only one inheritance relationship per class

## 🏗 Design Patterns Used

* **Command Pattern**

  * Enables undo/redo functionality

* **Observer Pattern**

  * Automatically revalidates diagrams when changes occur

* **Strategy Pattern**

  * Supports multiple serialization formats

## 🚀 Getting Started

### Requirements

* Java 8+

### Run the project

```bash
# compile & run (depending on your setup)
```

## 📁 Project Structure

```
src/
 ├── model/
 ├── view/
 ├── controller/
 ├── commands/
 ├── validation/
```

## 📌 Future Improvements

* Support more UML diagram types
* Improve UI/UX
* Add more validation rules
* Plugin system for extensibility
