/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

/**
 *
 * @author Aatif Noor
 */
import com.smartcampus.models.Room;
import com.smartcampus.storage.DataStore;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoom {

    private final DataStore dataStore = DataStore.getInstance();

    // GET /api/v1/rooms - Listing all the rooms
    @GET
    public Response getAllRooms() {
        List<Room> rooms = new ArrayList<>(dataStore.getRooms().values());
        return Response.ok(rooms).build();
    }

    // GET /api/v1/rooms/{roomId} - Getting all the rooms by ID
    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }
        return Response.ok(room).build();
    }

    // POST /api/v1/rooms - Creating a new room
    @POST
    public Response createRoom(Room room) {
        String id = UUID.randomUUID().toString();
        room.setId(id);
        dataStore.getRooms().put(id, room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // DELETE /api/v1/rooms/{roomId} - Deleting a room (only if no sensors)
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = dataStore.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found"))
                    .build();
        }
        
        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Cannot delete room with active sensors"))
                    .build();
        }
        
        dataStore.getRooms().remove(roomId);
        return Response.noContent().build();
    }
}
