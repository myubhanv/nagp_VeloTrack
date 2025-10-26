# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=builder /app/target/*.jar app.jar

# Install bash and netcat for wait-for-it to work (slim base often lacks them)
# Make apt-get robust: clean partial lists, retry update if it fails, and purge lists after install
RUN rm -rf /var/lib/apt/lists/* \
	&& apt-get update || (rm -rf /var/lib/apt/lists/* && apt-get update) \
	&& apt-get install -y --no-install-recommends --no-install-suggests \
		bash \
		netcat-openbsd \
		ca-certificates \
	|| (sleep 1 && apt-get update && apt-get install -y --no-install-recommends --no-install-suggests bash netcat-openbsd ca-certificates) \
	&& rm -rf /var/lib/apt/lists/*

# Copy wait-for-it script
COPY wait-for-it.sh /wait-for-it.sh
RUN chmod +x /wait-for-it.sh

# Copy an entrypoint wrapper that expands environment variables safely in sh
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# Use wait-for-it in strict mode with a timeout to ensure DB is accepting connections
# If the wait fails, container will exit instead of starting the app and failing with
# JDBC connection errors. The timeout is configurable via WAIT_FOR_DB_TIMEOUT env var
# (in seconds). Default to 120 seconds when not provided.
ENV WAIT_FOR_DB_TIMEOUT=120
ENTRYPOINT ["/docker-entrypoint.sh"]
