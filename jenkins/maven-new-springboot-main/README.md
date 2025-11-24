Springdemo — Small Spring Boot app with Jenkins CI/CD

Overview
- Minimal Spring Boot application packaged as a WAR (see `pom.xml`) and suitable for deployment to Tomcat.
- Uses Thymeleaf for UI templates and includes Actuator for health/info endpoints.
- Static assets are under `src/main/resources/static` and templates in `src/main/resources/templates`.

Run locally
1. Build and run with Maven wrapper:

```bash
chmod +x ./mvnw
./mvnw -DskipTests clean package
./mvnw spring-boot:run
```

2. Open the app at `http://localhost:8090/springdemo` (context path from `application.properties`).

Production (Tomcat on EC2 - recommended)
- This project is packaged as a WAR and can be deployed to Tomcat's `webapps/` directory or via the Tomcat Manager.

Prerequisites on EC2 (Amazon Linux 2023)
- OpenJDK 17
- Apache Tomcat 9/10 (installed and configured)
- Jenkins (optional) running on a CI server to build and deploy

Quick EC2 setup (example)

```bash
# Install Java 17
sudo yum install -y java-17-amazon-corretto-headless
# Create a tomcat user and install Tomcat (or use package)
# Deploy the WAR to Tomcat's webapps/ by copying target/*.war
sudo cp target/*.war /opt/tomcat/webapps/springdemo.war
# Start/restart Tomcat
sudo systemctl restart tomcat
```

Jenkins notes for CI/CD
- Build step: `./mvnw -DskipTests clean package`
- Post-build (Deploy war/ear to a container): set `WAR/EAR files` to `**/target/*.war` (avoid trailing commas)
- If using the "Deploy to container" plugin for Tomcat, provide correct manager URL and credentials.
- The project contains a small build-time fix copying image names to lowercase in the WAR to avoid case-sensitivity problems on Linux/Tomcat.

Actuator
- Production profile file `src/main/resources/application-prod.properties` exposes `health` and `info` endpoints.
- Start with profile: `--spring.profiles.active=prod` or set `SPRING_PROFILES_ACTIVE=prod` in your environment.

Recommended improvements (optional)
- Secure actuator endpoints behind an auth mechanism in production.
- Add automated tests and integrate with Jenkins test reporting.
- Add CDN or S3-hosted static assets for better scaling.

Contact
- For help adapting the deployment or securing the endpoints, open an issue or message the maintainer.
