# Job Portal Backend Application

A secure Job Portal backend built using Spring Boot with JWT-based authentication and role-based authorization.
This project provides REST APIs for user authentication, job posting, job searching, and role-based access using JWT Security.


🚀  Features
- User Authentication (Signup & Login)
- JWT-based Security
- Role-based Authorization (ADMIN, RECRUITER, JOB_SEEKER)
- Job Posting (Recruiter/Admin)
- Job Search  (title / Location)
- Pagination & Sorting
- Global Exception Handling
- Swagger API Documentation
- MySQL Database Integration


🛠️ Tech Stack
- Java 17
- Spring Boot
- Spring Security + JWT(JSON WEB Token)
- Spring Data JPA (Hibernate)
- MySQL
- Swagger (OpenAPI)
- Meaven


📂 Project Structure
src/main/java/com/pramod/jobportal
├── controller        # REST Controllers
├── service           # Business Logic
├── repository        # JPA Repositories
├── model             # Entity Classes
├──Enum               # Enum Constant
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


👥 Roles & Permissions

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


📘 API Documentation
Swagger UI : http://localhost:8080/swagger-ui/index.html   


▶️ How to Run the Project
Follow the steps below to run this project locally.

1️⃣ Clone the Repository
clone  https://github.com/itspramodrana1/job-portal-backend.git
cd job-portal-backend

2️⃣ Configure MySQL Database
Make sure MySQL is running on your system.

Create the database:
CREATE DATABASE job_portal_db;

3️⃣ Update application.properties
Go to:
src/main/resources/application.properties

Update the database configuration:
spring.datasource.url=jdbc:mysql://localhost:3306/job_portal_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080

4️⃣ Run the Spring Boot Application
Option 1: IntelliJ IDEA

Open the project in IntelliJ

Run JobPortalBackendApplication.java

5️⃣ Open Swagger UI : 
Once the application starts successfully, open your browser and visit:
http://localhost:8080/swagger-ui/index.html


🔐 Authentication (JWT)
Some APIs are secured using JWT Authentication.

Steps to Access Secured APIs
1. Call the login API:

POST /auth/login

2. Copy the JWT token from the response

3. Open Swagger UI and click Authorize

4. Paste the token in this format:
   Bearer <your_token>

5. Click Authorize and close the popup

You can now access secured APIs.


🔑 Sample Credentials (For Testing)
Admin User
email: admin@gmail.com
password: admin123

Normal User
email: user@gmail.com
password: user123

Note: This is a backend-only project. Swagger UI is used to test APIs after running the application locally.
