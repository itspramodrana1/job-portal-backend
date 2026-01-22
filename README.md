# Job Portal Backend Application

A secure Job Portal backend built using Spring Boot with JWT-based authentication and role-based authorization.
This project provides REST APIs for user authentication, job posting, job searching, and role-based access using JWT Security.

# Job Portal Backend Application

 ### Features
- User Authentication (Signup & Login)
- JWT-based Security
- Role-based Authorization (ADMIN, RECRUITER, JOB_SEEKER)
- Job Posting (Recruiter/Admin)
- Job Search  (title / Location)
- Pagination & Sorting
- Global Exception Handling
- Swagger API Documentation
- MySQL Database Integration

##  Tech Stack
- Java 17
- Spring Boot
- Spring Security + JWT(JSON WEB Token)
- Spring Data JPA (Hibernate)
- MySQL
- Swagger (OpenAPI)
- Meaven

## Project Structure
src/main/java/com/pramod/jobportal
│
├── controller        # REST Controllers
├── service           # Business Logic
├── repository        # JPA Repositories
├── model             # Entity Classes
├── dto               # Request/Response DTOs
├── security          # JWT & Security Config
├── exception         # Global Exception Handling
└── JobPortalBackendApplication.java


🔐 Authentication Flow
1. User registers using /api/auth/signup
2. User logs in using /api/auth/login
3. Backend returns a JWT Token
4. Token is sent in request header :
   Authorization: Bearer <JWT_TOKEN>
5. Access is granted based on user role


### Roles & Permissions

### Admin
- Full access to all APIs
- Manage jobs and users

### Recruiter
- Create, update, delete jobs
- View applicants for posted jobs

### Job Seeker
- View jobs
- Search jobs by title/location
- Apply for jobs


##  API Documentation
Swagger UI: http://localhost:8080/swagger-ui/index.html

## Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/job_portal_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080

##  Run the Project
1. Configure MySQL in application.properties
2. Run the application
3. Test APIs using Swagger or Postman
