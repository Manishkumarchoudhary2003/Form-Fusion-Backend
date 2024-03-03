# Form Fusion Backend

## Description
Form Fusion Backend is the server-side application built using Spring Boot. It manages forms, responses, and users effectively by providing RESTful APIs.

## Features
- Create, update, and delete forms.
- Retrieve responses for specific forms.
- User management including authentication and authorization.

## Technologies Used
- Java
- Spring Boot
- Spring Security
- Hibernate
- MySql
- Maven for dependency management

# Installation

To run Form Fusion locally, follow these steps:

1. Clone the repository: `git clone https://github.com/example/repository.git`
2. Navigate to the project directory: `cd project-directory`
3. Build the project: `./gradlew build`

### MySQL Configuration

Before running the project, ensure MySQL is installed and configured. 
Follow these steps to set up the database:

1. Install MySQL if not already installed.
2. Create a new database for Form Fusion.
3. Update the application.properties file with the database configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
```


## Usage
- Access the application through the provided endpoints.
- Perform CRUD operations on forms, responses, and users using RESTful APIs.
- Ensure proper authentication and authorization for secure access to sensitive functionalities.
