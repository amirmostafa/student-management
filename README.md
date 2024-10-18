# Student Management

- Students, Courses, Schedules data are inserted by liquibase on startup
- Default jwt token time is 5 minutes, value can be overriden by adding this property jwtToken.timeout
- Default refresh token time is 10 minutes, value can be overriden by adding this property jwt.refresh.timeout
- If token expired call refresh-token api and send refresh token value in body to generate new tokens
- Courses APIs are cached using redis, redis service should be provided.
- Postman collection included in project root
