# Smart Campus API

JAX-RS RESTful API for university Smart Campus coursework. This API manages rooms and sensors for a smart campus system.

## Technology Stack
- **JAX-RS** (Jersey 2.41)
- **Apache Tomcat 9**
- **Java 17**
- **Maven**

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

## Sample Curl Commands

```bash
# 1. Discovery endpoint
curl http://localhost:8080/smart-campus-api/api/v1/

# 2. Get all rooms
curl http://localhost:8080/smart-campus-api/api/v1/rooms

# 3. Get room by ID
curl http://localhost:8080/smart-campus-api/api/v1/rooms/R001

# 4. Create a new room
curl -X POST http://localhost:8080/smart-campus-api/api/v1/rooms \
  -H "Content-Type: application/json" \
  -d '{"name":"Lab B","building":"Science Block","floor":2}'

# 5. Delete a room (only if no sensors)
curl -X DELETE http://localhost:8080/smart-campus-api/api/v1/rooms/{room-id}
```

## Author
Aatif Noor
