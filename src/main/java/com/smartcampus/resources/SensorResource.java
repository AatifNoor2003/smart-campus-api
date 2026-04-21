/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

/**
 *
 * @author atiff
 */
import com.smartcampus.models.Sensor;
import com.smartcampus.storage.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import com.smartcampus.models.Room;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore dataStore = DataStore.getInstance();

    // GET /api/v1/sensors - Get all sensors (with optional type filter)
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(dataStore.getSensors().values());
        
        // Filter by type if provided
        if (type != null && !type.trim().isEmpty()) {
            sensors = sensors.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        
        return Response.ok(sensors).build();
    }

    // GET /api/v1/sensors/{sensorId} - Get sensor by ID
    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = dataStore.getSensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found"))
                    .build();
        }
        return Response.ok(sensor).build();
    }

    // POST /api/v1/sensors - Register a new sensor
    @POST
    public Response createSensor(Sensor sensor) {
        // Validate roomId exists
        if (sensor.getRoomId() == null || !dataStore.getRooms().containsKey(sensor.getRoomId())) {
            return Response.status(422)
                    .entity(Map.of("error", "Room not found with ID: " + sensor.getRoomId()))
                    .build();
        }
        
        // Generate UUID if not provided
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            sensor.setId(UUID.randomUUID().toString());
        }
        
        // Set default status if not provided
        if (sensor.getStatus() == null) {
            sensor.setStatus("ACTIVE");
        }
        
        // Add sensor to DataStore
        dataStore.getSensors().put(sensor.getId(), sensor);
        
        // Add sensor ID to the room
        Room room = dataStore.getRooms().get(sensor.getRoomId());
        room.addSensorId(sensor.getId());
        
        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }
}
