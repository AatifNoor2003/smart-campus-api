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
- Appropriate HTTP status codes (200, 201, 404, 409, 422, 403, 500)
- In-memory storage using `ConcurrentHashMap` for thread safety
- Sub-resource locator pattern for sensor readings

## Build & Run Instructions

### Run the Project

#### Method 1 - Using NetBeans (Recommended)
1. Open the project in NetBeans
2. Right-click the project → **Clean and Build**
3. Right-click the project → **Run**
4. Tomcat will start and deploy automatically
5. Access the API at `http://localhost:8080/smart-campus-api/api/v1/`

#### Method 2 - Using Command Line
1. Build the project: `mvn clean package`
2. Copy `target/smart-campus-api.war` to Tomcat's `webapps/` folder
3. Start Tomcat
4. Access the API at `http://localhost:8080/smart-campus-api/api/v1/`

## API Endpoints

### Part 1: Discovery
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/` | Returns API metadata and resource links |

### Part 2: Room Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/rooms` | List all rooms |
| POST | `/api/v1/rooms` | Create a new room |
| GET | `/api/v1/rooms/{id}` | Get room by ID |
| DELETE | `/api/v1/rooms/{id}` | Delete room (fails if room has sensors) |

### Part 3: Sensor Operations
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/sensors` | List all sensors |
| GET | `/api/v1/sensors/{sensorId}` | Get sensor by ID |
| POST | `/api/v1/sensors` | Register a new sensor (validates roomId exists) |

### Part 4: Sensor Readings
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

## Error Handling

The API uses standard HTTP status codes and returns meaningful JSON error messages.

| Status Code | Scenario | Example Response |
|-------------|----------|------------------|
| 409 Conflict | Deleting a room that still has sensors | `{"error":"Cannot delete room 'R001' - it has 1 sensor(s) still assigned"}` |
| 422 Unprocessable Entity | Creating a sensor with non-existent roomId | `{"error":"Room not found with ID: XXXX"}` |
| 403 Forbidden | Adding a reading to a sensor in MAINTENANCE mode | `{"error":"Sensor 'xxx' is currently in maintenance mode and cannot accept new readings"}` |
| 500 Internal Server Error | Unexpected runtime error | `{"error":"An unexpected internal server error occurred"}` |

## Logging

All API requests and responses are logged to the Tomcat console with
- HTTP method (GET, POST, DELETE)
- Request URI
- Response status code

Example log output:

```text
--- Incoming Request ---
Method: DELETE
URI: http://localhost:8080/smart-campus-api/api/v1/rooms/R001
--- Outgoing Response ---
Status: 409
```

## Author
Aatif Noor
