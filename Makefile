# Makefile for building and managing the Shirt Shop Backend

# Variable for project directory
PROJECT_DIR=./backend

# Main targets
all: build

# Build the application using Maven
build:
	@echo "Building the application..."
	cd $(PROJECT_DIR) && mvn clean install

# Run the application
run:
	@echo "Running the application..."
	cd $(PROJECT_DIR) && mvn spring-boot:run

# Start the PostgreSQL database using Docker
postgres:
	@echo "Starting PostgreSQL database..."
	docker run --name postgress16 -p 5432:5432 -e POSTGRES_USER=root -e POSTGRES_PASSWORD=secret -d postgres:16.3

# Create the database
createdb:
	@echo "Creating the database..."
	docker exec -it postgress16 createdb --username=root --owner=root shortstop

# Drop the database
dropdb:
	@echo "Dropping the database..."
	docker exec -it postgress16 dropdb shortstop

# Stop the PostgreSQL container
stopdb:
	@echo "Stopping the PostgreSQL database..."
	docker stop postgress16

# Remove the PostgreSQL container
removedb:
	@echo "Removing the PostgreSQL container..."
	docker rm postgress16

# Clean the project
clean:
	@echo "Cleaning up..."
	cd $(PROJECT_DIR) && mvn clean
	docker system prune -f

.PHONY: all build run postgres createdb dropdb stopdb removedb clean
