# UML Class Diagram Tool

JavaFX desktop application for creating, editing, validating, and exporting UML class diagrams. Built with Maven, 98 Java source files across model, view, controller, command, validation, and serialization layers.

---

## Features

- Create and edit UML class diagrams (classes, interfaces, abstract classes, enumerations)
- Draw relationships: association, aggregation, composition, inheritance, realization, dependency
- Automatic validation against UML standards and Java constraints
- Undo / redo via Command pattern
- Save and load diagrams (custom serialization format)
- Export to PNG and JPG

---

## Architecture

The project follows MVC with three supporting patterns:

| Pattern | Where | Purpose |
|---------|-------|---------|
| Command | `gui/commands/` | Undo/redo for create, delete, move, resize operations |
| Observer | `listeners/` | Revalidation on diagram change |
| Strategy | `serialization/` | Pluggable serializers (diagram format, PNG, JPG) |

**Package structure:**

```
com.mycompany.irr00_group_project/
  App.java                    Entry point
  representation/             Domain model (ClassNode, UMLConnection, DiagramData)
  gui/                        JavaFX views and controllers
    commands/                 Command objects for undo/redo
  listeners/                  Observer/Subject interfaces and EventListener
  serialization/              Diagram and image serializers
  validation/                 UML and Java constraint validators
```

---

## Validation Rules

**UML constraints:**
- Class names must be valid identifiers
- Attribute and operation syntax checked

**Java constraints:**
- Single inheritance per class (no multiple `extends`)

---

## Build and Run

Requires Java 11+ and Maven.

```bash
cd irr00_group_Project
mvn clean package
mvn exec:java
```

Or open in NetBeans / IntelliJ and run `App.java`.

---

## License

MIT
