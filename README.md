# sboot-payroll-query-service

## Project Overview
This project is a Spring Boot application designed for querying payroll information. It provides a RESTful API for interacting with payroll data, suitable for integration with other services.

## Features
- Retrieve employee payroll details
- Filter payroll data based on various parameters
- Secure endpoints with JWT authentication.

## Architecture
The application follows a microservices architecture, utilizing Spring Boot for service implementation and Spring Data JPA for database interactions.

## API Endpoints
| Method | Endpoint                  | Description                       |
|--------|---------------------------|-----------------------------------|
| GET    | /api/payroll/{employeeId}?year=2026&month=7 | Retrieve one payroll in the authenticated tenant. Employees may only read themselves; admins may read employees in their tenant. |
| GET    | /api/payroll              | List payrolls in the authenticated tenant (admin only). |
| GET    | /payroll                  | Compatibility alias for the tenant-scoped admin list. |

## Multi-tenant security

The service validates the bearer JWT with the same HMAC secret and `companyId`,
`employeeId`, and `roles` claims issued by the authentication service. Tenant
scope is never accepted from a path, query parameter, or request body. Missing
and out-of-scope payrolls do not disclose data from another company.

`company_id` is introduced as a nullable expand migration so existing rows are
preserved without inventing ownership. Rows without a verified tenant mapping
remain quarantined and are not returned by tenant-scoped queries. A later
contract migration may make the column non-null only after the owner mapping is
verified and the unmapped-row count is zero.

Required runtime configuration:

- `JWT_SECRET`: at least 64 UTF-8 bytes, supplied through environment/secret management.
- `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`: required shared-environment PostgreSQL values.
- `DB_DRIVER`: optional JDBC driver override; defaults to `org.postgresql.Driver`.
- shared environments must use `ddl-auto=validate`; Flyway owns schema evolution.

## Installation Guide
1. Clone the repository:
   ```
   git clone https://github.com/username/sboot-payroll-query-service.git
   cd sboot-payroll-query-service
   ```
2. Ensure you have JDK 11+ and Maven installed.
3. Run the application:
   ```
   mvn spring-boot:run
   ```

## Testing Instructions
To run tests:
```bash
mvn test
```

## Configuration
Environment settings can be modified in `application.properties`. Ensure that the database configuration is correct.

## Design Patterns
- **Repository Pattern**: Used for data access layers.
- **Service Layer Pattern**: Encapsulates business logic.

## Technologies
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Troubleshooting
- If you encounter a `java.lang.NoClassDefFoundError`, make sure all dependencies are correctly specified in `pom.xml`.
- Check database connection settings in `application.properties` if you have connectivity issues.

## Contribution Guidelines
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/your-feature`).
3. Commit your changes (`git commit -am 'Add some feature'`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Create a new Pull Request.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
