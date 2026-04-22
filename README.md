# Smart Campus API

A JAX-RS RESTful API for the Smart Campus coursework. This API manages Rooms and Sensors for a smart campus system.

## API Design Overview

The Smart Campus API is built using JAX-RS (Jersey 2.41) deployed on Apache Tomcat 9.0.100. 
It follows RESTful principles with a versioned base path `/api/v1`.

Key design decisions:
- Resource-based URLs with logical nesting (e.g. `/sensors/{id}/readings`)
- Standard HTTP methods — GET, POST, DELETE
- JSON for all requests and responses
- Proper HTTP status codes (200, 201, 204, 400, 403, 404, 409, 422, 500)
- In-memory storage using `ConcurrentHashMap` (thread-safe, no database)
- Sub-resource locator pattern to keep sensor readings separate
- Custom exception mappers to hide internal stack traces from users
- Request and response logging via JAX-RS filters

## Technology Stack

| Technology | Version |
|---|---|
| Java | 17 |
| JAX-RS (Jersey) | 2.41 |
| Apache Tomcat | 9.0.100 |
| Maven | 3.x (bundled with NetBeans) |
| Jackson | via jersey-media-json-jackson |

## Project Structure
```
src/main/java/
├── com/smartcampus/
│   └── SmartCampusApplication.java           # JAX-RS Application entry point     
├── com/smartcampus/storage/
│   └── DataStore.java                        # Singleton in-memory data store
├── com/smartcampus/models/
│   ├── Room.java                             # Room POJO
│   ├── Sensor.java                           # Sensor POJO
│   └── SensorReading.java                    # SensorReading POJO
├── com/smartcampus/resources/
│   ├── DiscoveryResource.java                # GET /api/v1/ discovery endpoint
│   ├── SensorRoom.java                       # Room CRUD endpoints
│   ├── SensorResource.java                   # Sensor CRUD endpoints with filtering
│   └── SensorReadingResource.java            # Nested reading endpoints (sub-resource)
├── com/smartcampus/exceptions/
│   ├── RoomNotEmptyException.java            # Thrown when deleting a room with sensors
│   ├── LinkedResourceNotFoundException.java  # Thrown when roomId doesn't exist
│   └── SensorUnavailableException.java       # Thrown when the sensor is in maintenance
├── com/smartcampus/mappers/
│   ├── RoomNotEmptyExceptionMapper.java      # Maps to 409 Conflict
│   ├── LinkedResourceNotFoundExceptionMapper.java   # Maps to 422 Unprocessable Entity
│   ├── SensorUnavailableExceptionMapper.java # Maps to 403 Forbidden
│   └── GlobalExceptionMapper.java            # Catch-all for 500 errors
└── com/smartcampus/filters/
    └── LoggingFilter.java                    # Logs all requests and responses
```

## Build & Run Instructions

### Prerequisites

Before starting, make sure that the following are installed:

1. **JDK 17** — Download from https://adoptium.net
   - After installing, verify with: `java -version`
2. **Apache Tomcat 9** — Download from https://tomcat.apache.org/download-90.cgi
   - Extract to a folder, e.g. `C:\tomcat9` or `/opt/tomcat9`
3. **Maven** — Either installed separately or use the one bundled with NetBeans
   - Verify with: `mvn -version`
4. **NetBeans IDE** (recommended) or any Java IDE

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/AatifNoor2003/smart-campus-api.git
cd smart-campus-api
```

---

### Step 2 — Build the Project

Using the terminal or command prompt inside the project folder:

```bash
mvn clean package
```

This will compile the project and generate a WAR file at:
target/smart-campus-api.war

If using NetBeans:
1. Open the project in NetBeans
2. Right-click the project and click **Clean and Build**
3. The WAR file will appear in the `target/` folder

---

### Step 3 — Deploy to Tomcat

**Option A — Manual deployment:**
1. Copy `target/smart-campus-api.war` into the Tomcat `webapps/` folder
2. Start Tomcat:
   - Windows: run `bin\startup.bat`
   - Mac/Linux: run `bin/startup.sh`
3. Wait for Tomcat to start (usually 5–10 seconds)

**Option B — Deploy via NetBeans:**
1. In NetBeans, make sure Tomcat 9 is configured as the server
2. Right-click the project and click **Run**
3. NetBeans will build, deploy, and start Tomcat automatically

---

### Step 4 — Verify the Server is Running

Open in browser or use curl:

```bash
curl http://localhost:8080/smart-campus-api/api/v1/
```

Output visible should be:
```json
{
  "name": "Smart Campus API",
  "description": "A RESTful API for managing campus rooms and sensors",
  "version": "1.0",
  "contact": "noor.20232258@iit.ac.lk",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

---

## API Endpoints

### Discovery Endpoint
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/` | API discovery - returns metadata and resource links |

### Room Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/rooms` | List all rooms |
| POST | `/api/v1/rooms` | Create a new room |
| GET | `/api/v1/rooms/{roomId}` | Get a specific room by ID |
| DELETE | `/api/v1/rooms/{roomId}` | Delete a room |

### Sensor Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/sensors` | List all sensors |
| GET | `/api/v1/sensors?type=CO2` | List sensors filtered by type |
| GET | `/api/v1/sensors/{sensorId}` | Get a specific sensor by ID |
| POST | `/api/v1/sensors` | Register a new sensor |

### Reading Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/sensors/{sensorId}/readings` | Get all readings for a sensor |
| POST | `/api/v1/sensors/{sensorId}/readings` | Add a new reading |

---

## HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 OK | Success |
| 201 Created | Resource created |
| 204 No Content | Resource deleted |
| 400 Bad Request | Missing required field |
| 403 Forbidden | Sensor in maintenance mode |
| 404 Not Found | Resource does not exist |
| 409 Conflict | Cannot delete room with sensors |
| 422 Unprocessable Entity | Invalid roomId reference |
| 500 Internal Server Error | Unexpected error |

---

## Sample curl Commands

### 1. Get API discovery info
```bash
curl http://localhost:8080/smart-campus-api/api/v1/
```

Expected output:
```json
{
  "name": "Smart Campus API",
  "description": "A RESTful API for managing campus rooms and sensors",
  "version": "1.0",
  "contact": "noor.20232258@iit.ac.lk",
  "resources": {
    "rooms": "/api/v1/rooms",
    "sensors": "/api/v1/sensors"
  }
}
```

### 2. Create a new room
```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"LIB-301\",\"name\":\"Library Quiet Study\",\"capacity\":50}"
```

Expected output:
```json
{"id":"LIB-301","name":"Library Quiet Study","capacity":50,"sensorIds":[]}
```

### 3. Get all rooms
```bash
curl http://localhost:8080/smart-campus-api/api/v1/rooms
```
Expected output:
```json
[
  {"id":"R001","name":"Lecture Hall A","capacity":100,"sensorIds":["S001"]},
  {"id":"LIB-301","name":"Library Quiet Study","capacity":50,"sensorIds":[]}
]
```

### 4. Get a specific room by ID
```bash
curl http://localhost:8080/smart-campus-api/api/v1/rooms/R001
```

Expected output:
```json
{"id":"R001","name":"Lecture Hall A","capacity":100,"sensorIds":["S001"]}
```

### 5. Delete a room (succeeds only if no sensors assigned)
```bash
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/LIB-301
```

Expected output: Empty response

### 6. Register a new sensor
```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"TEMP-001\",\"type\":\"Temperature\",\"roomId\":\"R001\"}"
```

Expected output:
```json
{"id":"TEMP-001","type":"Temperature","roomId":"R001","status":"ACTIVE","currentValue":0.0}
```

### 7. Get all sensors filtered by type
```bash
curl http://localhost:8080/smart-campus-api/api/v1/sensors?type=CO2
```

Expected output:
```json
[{"id":"S001","type":"CO2","roomId":"R001","status":"ACTIVE","currentValue":0.0}]
```

### 8. Add a reading to a sensor
```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/S001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":425.5}"
```

Expected output:
```json
{"id":"some-uuid","value":425.5,"timestamp":1734567890123}
```

### 9. Get all readings for a sensor
```bash
curl http://localhost:8080/smart-campus-api/api/v1/sensors/S001/readings
```

Expected output:
```json
[{"id":"some-uuid","value":425.5,"timestamp":1734567890123}]
```

### 10. Try to delete a room with sensors (409 Conflict)
```bash
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/R001
```

Expected output:
```json
{"error":"Cannot delete room 'R001' - it has 1 sensor(s) still assigned"}
```

### 11. Try to register a sensor with invalid roomId (422 Unprocessable Entity)
```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors \
  -H "Content-Type: application/json" \
  -d "{\"id\":\"TEST-001\",\"type\":\"CO2\",\"roomId\":\"INVALID\"}"
```

Expected output:
```json
{"error":"Room not found with ID: INVALID"}
```

### 12. Try to post a reading to a MAINTENANCE sensor (403 Forbidden)
```bash
curl -X POST http://localhost:8080/smart-campus-api/api/v1/sensors/MAINT-001/readings \
  -H "Content-Type: application/json" \
  -d "{\"value\":25.5}"
```

Expected output:
```json
{"error":"Sensor 'MAINT-001' is currently in maintenance mode and cannot accept new readings"}
```
---

## Report — Question Answers

### Part 1: Service Architecture & Setup
**1. Project & Application Configuration**

By default, JAX-RS creates a new instance of the resource class for every incoming HTTP request. The runtime does NOT treat it as a singleton. Because a new instance is created per request, any data stored in the instance variables would be lost after the request ends. To prevent data loss, all shared states are stored in a static singleton DataStore class. The DataStore uses ConcurrentHashMap instead of a regular HashMap. This keeps the data safe when multiple requests hit the API at the same time. The ConcurrentHashMap handles concurrent access automatically, so no data is lost or corrupted.

**2. The “Discovery” Endpoint**

API responses include hyperlinks to related resources and available actions, which is what HATEOAS means. It is considered a hallmark of advanced RESTful design because it makes the API self-descriptive and discoverable. This helps clients navigate the API dynamically without needing any prior knowledge of its URL structure. This benefits client developers because they do not need to hardcode URLs or depend entirely on external documentation. Instead, the API itself guides them to the next available actions through the links embedded in the responses.

### Part 2: Room Management
**1. Room Resource Implementation**

Returning only IDs reduces network bandwidth and response size, which is beneficial for large collections. Because of this, the client must make additional GET requests to get details for each room. This increases the time taken and also makes the process more complex for the client. On the other hand, returning the complete list of rooms increases the response size but provides the client with all data in a single request. This is much easier and faster. The correct choice depends on the situation. If it’s a dashboard, displaying the complete list is better, but IDs alone may be better when the client only needs references. 

**2. Room Deletion & Safety Logic**

Yes, the DELETE operation is idempotent. The first DELETE removes the room from the DataStore and returns a “204 No Content” success response. The second DELETE on the same room will return a “404 Not Found” error because the room no longer exists. The HTTP status codes may differ (204 for the first, 404 for the second), but the resource state doesn’t change. Repeating the DELETE doesn’t cause any side effects or data corruption. Therefore, this proves that the DELETE operation is idempotent.

### Part 3: Sensor Operations & Linking
**1. Sensor Resource & Integrity**

The @Consumes(MediaType.APPLICATION_JSON) annotation tells JAX-RS that the endpoint only accepts JSON data. If a client sends data in a different format, such as text/plain or application/xml, JAX-RS won't know how to handle it. It will automatically send back a 415 Unsupported Media Type error before the code even runs. This is good because it stops wrong data formats from reaching the server.

**2. Filtered Retrieval & Search**

Using query parameters like “?type=CO2” is better for filtering because it keeps the URL clean and makes filtering optional. Using a path like “/sensors/type/CO2” would make it look like CO2 is a resource, which is not true. Query parameters also make it easy to add additional filters, like “?type=CO2&status=ACTIVE”. Most REST APIs use query parameters for searching and filtering, so this is what the developers expect.

### Part 4: Deep Nesting with Sub – Resources
**1. The Sub-Resource Locator Pattern**

The sub-resource locator pattern sends nested path requests to a separate class instead of handling everything in one controller. In this project, SensorResource has a locator method that gives back a SensorReadingResource instance when “/sensors/{id}/readings” is called. This makes the code easier to maintain because each class focuses on a single task. If all the nested paths were placed in one large controller class, the code would become hard to read and test as the API grows.

### Part 5: Advanced Error Handling, Exception Mapping & Logging
**2. Dependency Validation (422 Unprocessable Entity)**

An HTTP 404 Not Found error means the URL does not exist. When creating a sensor, the URL “/api/v1/sensors” exists, so a 404 error would be wrong. The real issue is that the roomId inside the JSON doesn't match any real room. That's why 422 is better. It tells the client that it understands the request and the JSON is valid, but one of the values doesn’t work. This helps the client fix the data, not change the URL.

**4. The Global Safety Net (500)**

If Java stack traces leak to clients, attackers can see which Java version and libraries are being used. They can also find file paths, class names, and line numbers. This helps them identify known security holes in those specific versions or figure out where to attack.

**5. API Request & Response Logging Filters**

With a JAX-RS filter, the logging logic is written once and applied automatically to every request. Manually adding “Logger.info()” to each method would repeat the same code many times and could cause new endpoints to miss logging statements. Filters also keep resource classes clean, as they focus only on business logic. If the logging format needs to change, only the filter class needs to be updated, not every resource method.

---

## Author Details
**Name - Aatif Noor**  
**IIT Student ID - 20232258**  
**UOW Student ID - w2120211**