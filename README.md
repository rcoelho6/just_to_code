# Just to Code - POC & Experimentation Playground

A Spring Boot project designed as a **sandbox and reference implementation** for Proofs of Concepts (POCs), architectural experiments, and design pattern exploration.

## 🏛️ Architecture (Clean Architecture Version)

### Clean Architecture Implementation

The current implementation follows a **Clean Architecture** approach (this **`feat/clean-arch`** branch) featuring:

- **Independent Layer Structure** - Business logic separated from frameworks
- **Use Cases** - Core application workflows isolated
- **Domain Entities** - Pure business rules without framework dependencies
- **Adapters** - Explicit boundaries for external systems
- **Ports & Adapters Pattern** - Framework-agnostic interfaces
- **Dependency Inversion** - Dependencies point inward to business logic

**Key Directories in `feat/clean-arch`:**
```
├── usecases/          # Application business rules
├── domains/           # Pure business entities
├── adapters/          # Framework implementations
├── gateways/          # External system boundaries
└── presenters/        # Response formatting
```

### Other Implementation (Main Branch)

For simpler Architecture implementation use main branch:

```bash
git checkout main
```

This branch implements the same API using **layered architecture** principles:

- **Controller Layer** - HTTP request handling
- **Service Layer** - Business logic and orchestration
- **Repository Layer** - Data persistence (Spring Data JPA)
- **Model Layer** - Domain objects and entities

## 🛠️ Technology Stack

- **Spring Boot 3.5.16** - Framework
- **Spring Data JPA** - Data access
- **H2 Database** - In-memory database
- **JUnit 5** - Testing framework
- **Mockito** - Mocking library
- **Maven** - Build tool
- **Java 21** - Programming language

## 📝 What This Repository Demonstrates

This POC playground includes examples of:

- ✅ RESTful API design patterns
- ✅ Unit testing with mocks and Mockito
- ✅ Spring Boot configuration and best practices
- ✅ JPA/Hibernate integration
- ✅ Error handling and validation strategies
- ✅ Layered architecture pattern
- ✅ SOLID principles application in practice
- ✅ Dependency Injection patterns
- ✅ Clean code principles

## 🎓 Learning Resources for References

### Architecture Patterns Explored

- [Layered Architecture](https://en.wikipedia.org/wiki/Multitier_architecture) - Current implementation
- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) - `feat/clean-arch` branch
- Hexagonal Architecture (Ports & Adapters)
- Domain-Driven Design (DDD) concepts

### Best Practices Demonstrated

- Separation of Concerns
- Dependency Injection
- Interface-based programming
- Test-Driven Development (TDD)
- Mock-based unit testing

## 📋 Overview

This project serves as a **practical playground** for:

- 🏗️ **Architectural Pattern Exploration** - Testing different design patterns and architectures
- 🧪 **POC Development**
- 📚 **Learning Reference** - Clean examples of Spring Boot best practices
- 🔄 **Pattern Comparison** - Side-by-side implementations of different approaches
- 💡 **Technical Experimentation** - Validating new frameworks, libraries, and techniques

## 🎯 Project Aim

This repository is meant to be a **sandbox environment** where developers can:

1. **Experiment with POCs** - Build proofs of concepts without production constraints
2. **Test architectural patterns** - Implement solutions using different architectural approaches
3. **Apply SOLID principles** - Practice clean code and design principles in real-world scenarios
4. **Explore design patterns** - Reference implementations of common and advanced patterns
5. **Prototype new features** - Validate technical approaches before production implementation
6. **Document learnings** - Create working examples for team knowledge sharing

## 🏃 Quick Start

### Prerequisites

- **Java 21+**
- **Maven 3.9+**
- **Spring Boot 3.5.16**

### Build & Run

```bash
# Clone the repository
git clone https://github.com/rcoelho6/just_to_code.git
cd just-to-code

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## 📚 Current POCs & Modules

### Example: Task Management Module

This project includes a **Task Management module** as an example implementation demonstrating RESTful API design and CRUD operations.

**Endpoints:**
- **POST** `/tasks` - Create a new task
- **PUT** `/tasks/{id}` - Update an existing task
- 🔨`[To Be Implemented]`**GET** `/tasks/{id}` - Retrieve a specific task
- 🔨`[To Be Implemented]`**DELETE** `/tasks/{id}` - Delete a task

**Request Body Example:**
```json
{
  "description": "Hey, look this! It has a branch for clean arch impl!",
  "priority": 9999
}
```

**Error Response Example:**
```json
{
  "message": "Oh God! No! No! Nooo!",
  "status": 404
}
```

Feel free to add more modules and POCs to this project!

## 🧪 Testing

Run the test suite:

```bash
mvn test
```

Tests are written using:
- **JUnit 5** - Test framework
- **Mockito** - Mocking framework
- **Spring Test** - Spring Boot testing utilities

## 🔄 Branching Strategy

| Branch | Purpose | Architecture |
|--------|---------|--------------|
| `main` | Primary implementation and POCs | Layered Architecture |
| `feat/clean-arch` | Clean Architecture reference | Clean/Hexagonal Architecture |
| `feat/*` | Feature branches and experiments | Various patterns |

## 🚀 Contributing & Adding POCs

Feel free to:

1. **Create feature branches** - `feat/new-poc-name` for new proofs of concepts
2. **Add new modules** - Implement different POCs within this sandbox
3. **Experiment with patterns** - Try different architectural approaches
4. **Test frameworks/libraries** - Validate new technologies in isolation
5. **Document learnings** - Add comments and examples of what you discovered
6. **Submit pull requests** - Share your POCs and experiments with the team

### Adding a New POC

```bash
# 1. Create a feature branch
git checkout -b feat/your-poc-name

# 2. Create a new module/package
mkdir -p src/main/java/com/example/test/just_to_code/yourpoc

# 3. Implement your POC
# 4. Add tests
# 5. Document your findings
# 6. Commit and push
git commit -m "feat: Add [Your POC Name] proof of concept"
git push origin feat/your-poc-name
```

## 📖 Git Workflow

```bash
# Create a new feature branch
git checkout -b feat/new-concept

# Make your changes and commit
git add .
git commit -m "Add new POC or example"

# Push to repository
git push origin feat/new-concept

# Compare with other branches
git diff main feat/new-concept
```

## ❓ FAQ

**Q: Can I use this in production?**  
A: This is a learning/POC project. For production use, ensure proper security, validation, and error handling are implemented.

**Q: How do I compare different architectures?**  
A: Use `git diff` to compare the `main` branch with `feat/clean-arch` to see different implementation approaches for the same API.

**Q: Where should I add new experiments?**  
A: Create a feature branch (`feat/your-experiment-name`) to keep experiments organized and isolated.

## 📄 License

This project is open source and available under the MIT License.

## 👤 Author

Created as a reference implementation and POC playground by the development team.

---

**Happy coding! This repository is your sandbox for learning and experimentation. 🎉**
