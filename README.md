# Smart Campus API

JAX-RS RESTful API for university Smart Campus coursework. This API manages rooms and sensors for a smart campus system.

## Technology Stack
- **JAX-RS** (Jersey 2.41)
- **Apache Tomcat 9**
- **Java 17**
- **Maven**

## API Design Overview
The API follows RESTful principles with a versioned base path `/api/v1`. It uses:
- Resource-based URLs with logical nesting (e.g. `/sensors/{id}/readings`)
- Standard HTTP methods (GET, POST, DELETE)
- JSON request and response bodies
- Appropriate HTTP status codes (200, 201, 404, 409, 422)
- In-memory storage using `ConcurrentHashMap` for thread safety
- Sub-resource locator pattern for sensor readings

## Build & Run Instructions

### Prerequisites
- JDK 17 or later
- Apache Tomcat 9 installed
- Maven (included with NetBeans)

### Build the Project
```bash
mvn clean package
```

### Deploy to Tomcat
1. Copy `target/smart-campus-api.war` to Tomcat's `webapps/` folder
2. Start Tomcat
3. Access the API at `http://localhost:8080/smart-campus-api/api/v1/`

### Run in NetBeans
1. Open the project in NetBeans
2. Right-click project → **Clean and Build**
3. Right-click project → **Run**
4. Tomcat will start and deploy automatically
5. Access the API at `http://localhost:8080/smart-campus-api/api/v1/`

## API Endpoints

### Part 1: Discovery
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/` | Returns API metadata and resource links |

### Part 2: Room Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/rooms` | List all rooms |
| POST | `/api/v1/rooms` | Create a new room (auto-generates UUID) |
| GET | `/api/v1/rooms/{id}` | Get room by ID |
| DELETE | `/api/v1/rooms/{id}` | Delete room (fails if room has sensors) |

### Part 3: Sensor Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/sensors` | List all sensors (optional `?type=` filter) |
| GET | `/api/v1/sensors/{sensorId}` | Get sensor by ID |
| POST | `/api/v1/sensors` | Register a new sensor (validates roomId exists) |

### Part 4: Sensor Readings (Sub-Resource)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/sensors/{sensorId}/readings` | Get all readings for a sensor |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add a new reading (updates sensor's currentValue) |

## Sample Curl Commands

```bash
# 1. The discovery endpoint
curl http://localhost:8080/smart-campus-api/api/v1/

# 2. Getting all the rooms
curl http://localhost:8080/smart-campus-api/api/v1/rooms

# 3. Getting a room by the ID
curl http://localhost:8080/smart-campus-api/api/v1/rooms/R001

# 4. Creating a new room
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"name":"Lab B","capacity":40,"building":"Science Block","floor":2}'

# 5. Deleting a room (only if no sensors are assigned)
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/{roomId}

# 6. Getting all the sensors
curl http://localhost:8080/smart-campus-api/api/v1/sensors

# 7. Getting the sensors filtered by type
curl http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2

# 8. Registering a new sensor
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d '{"type":"Temperature","status":"ACTIVE","roomId":"R001"}'

# 9. Getting all the readings for a sensor
curl http://localhost:8080/smart-campus-api/api/v1/sensors/S001/readings

# 10. Adding a new reading to a sensor
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/S001/readings \
  -H "Content-Type: application/json" \
  -d '{"value":23.5}'
```

## Author
Aatif Noor
