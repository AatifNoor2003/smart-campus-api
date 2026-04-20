/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.resources;

/**
 *
 * @author Aatif Noor
 */
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getApiInfo() {
        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");

        Map<String, Object> discovery = new LinkedHashMap<>();
        discovery.put("name", "Smart Campus API");
        discovery.put("description", "A RESTful API for managing campus rooms and sensors");
        discovery.put("version", "1.0");
        discovery.put("admin_contact", "admin@smartcampus.edu");
        discovery.put("resources", resources);
        
        return Response.ok(discovery).build();
    }
}
