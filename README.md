# About
This is an application that provides online use of a library by readers and employees. The main functions of the system intended for readers are browsing, borrowing and reserving books. For the employee, these include managing users, their borrowings and reservations, and the collection of books. The application provides statistics for users involving popularity of the books among readers.

# Stack
Java, Spring Boot, Spring Data JPA, JUnit, Testcontainers, Docker, HTML, CSS, JavaScript, Vue.js, PrimeVue, MySQL, Liquibase, Git.

# How to get started
## What do you need
- Java SDK v1.6+
- WSL
- Docker Desktop
- Your favorite backend, frontend and database environment.

## Running the app
### Backend
1. Run Docker Desktop.
2. Open library project in your environment.
3. Open library-backend\docker-compose.yml file.
4. Click the green button in line 3.
5. Create the database named "library" (all needed data is located in library-backend\src\main\resources\application.properties).
6. Uncomment lines 50-58 in library-backend/src/main/java/com/adrian/library/config/Faker.java to fill the database with random data upon starting application (comment them before running app again).
7. Run the project.

### Frontend
```
npm install
npm run serve
```

## System accounts (the password is 1234)
- reader@localhost
- librarian@localhost
- admin@localhost
