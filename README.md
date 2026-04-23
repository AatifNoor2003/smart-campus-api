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

JAX-RS by default generates a new instance of the resource class with each incoming HTTP request. It is not considered a singleton in the runtime. Since a new instance is created each time there is a request, the data that is stored in the instance variables would be lost when the request is terminated. All shared states are stored in a static singleton DataStore to avoid losing data. A ConcurrentHashMap is used in place of a regular HashMap in the DataStore. This ensures that the data is secure when there are several requests to the API at the same time. The ConcurrentHashMap automatically supports concurrent access, thus there is no loss or corruption of data.

**2. The “Discovery” Endpoint**

The API responses contain links to the related resources, and that is what the term HATEOAS implies. This is regarded as a hallmark as it renders APIs self-descriptive and discoverable thus assisting clients to navigate it without prior knowledge of its URL structure. This is advantageous to client developers as they do not have to hard code URLs or rely wholly on external documentation. Rather, the API itself directs them to the subsequent actions with the links contained in the responses.

### Part 2: Room Management
**1. Room Resource Implementation**

The use of IDs alone saves bandwidth and response size in the network, a plus when dealing with large collections. Due to this reason, the client will have to send extra GET requests to obtain details on each room. This is not only time consuming but also complicates the process to the client. The size of response is larger when you give the full list of rooms, but it gives all data to the client in one request. This is far simpler and quicker, but the right decision will hinge on the circumstance. When it is a dashboard, it is more appropriate to show the entire list, whereas IDs might be more suitable when the client requires only references. 

**2. Room Deletion & Safety Logic**

Yes, the DELETE operation is idempotent. The initial DELETE will erase the room in the DataStore and will send a 204 No Content success response. The second DELETE on the same room will result in a 404 Not Found error since the room does not exist anymore. The status codes of the HTTP can vary (204 on the first, 404 on the second), but the state of the resource remains the same. There are no side effects or data corruption with repeating the DELETE. So this is to show that the DELETE operation is idempotent.

### Part 3: Sensor Operations & Linking
**1. Sensor Resource & Integrity**

The @Consumes(MediaType.APPLICATION_JSON) annotation informs JAX-RS that the endpoint accepts only the data in the form of JSON. In case a client submits data in another format, e.g. text/plain or application/xml, JAX-RS is not aware of how to process it. It will automatically resend a 415 Unsupported Media Type error before even the code runs. This is desirable since it prevents the transmission of erroneous data formats to the server.

**2. Filtered Retrieval & Search**

Filtering with query parameters such as “?type=CO2 is more appropriate since it leaves the URL clean and filtering optional. Path such as /sensors/type/CO2 would appear that CO2 is a resource, which is not the case. Additional filters can also be easily added using query parameters such as:?type=CO2&status=ACTIVE. The developers expect query parameters to be used in searching and filtering, which is the case with most REST APIs.

### Part 4: Deep Nesting with Sub – Resources
**1. The Sub-Resource Locator Pattern**

In the sub-resource locator pattern, nested path requests are sent to another class rather than being dealt with in a single controller. SensorResource in this project has a locator method which returns a SensorReadingResource instance when called by /sensors/{id}/readings. This simplifies the upkeep of the code since every class is devoted to one task. With all the nested paths in a single large controller class, the code would be difficult to read and test as the API expands.

### Part 5: Advanced Error Handling, Exception Mapping & Logging
**2. Dependency Validation (422 Unprocessable Entity)**

In case the URL is not available, a 404 Not Found error is issued. In the context of a sensor, the endpoint of the API is present, hence no 404 error can be possible. The actual problem is that the roomId within the JSON does not correspond with any real room. That is why 422 is better. It informs the client that it knows the request and the JSON is valid, but one of the values fail. This assists the client to repair the data, but not the URL.

**4. The Global Safety Net (500)**

In case Java stack traces are leaked to clients, attackers can view the version and libraries being used in Java. They are also able to locate file paths, class names and line numbers. This assists them in determining the known security holes in those particular versions or determining where to attack.

**5. API Request & Response Logging Filters**

The logging logic is coded once and applied to all requests with a filter. The repeated code would be to manually add the Logger.info() to every method and new endpoints would not see the logging statements. The filters also maintain cleanliness of resource classes by concentrating on the logic. In case the logging format should be altered, then it is only the filter class that should be changed, not all the resource methods.

---

## Author Details
**Name - Aatif Noor**  
**IIT Student ID - 20232258**  
**UOW Student ID - w2120211**