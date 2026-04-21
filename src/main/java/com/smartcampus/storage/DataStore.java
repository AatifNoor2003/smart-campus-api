/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.storage;

/**
 *
 * @author Aatif Noor
 */
import com.smartcampus.models.Room;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.smartcampus.models.Sensor;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();
    private final Map<String, Room> rooms;
    private final Map<String, Sensor> sensors;

    private DataStore() {
        rooms = new ConcurrentHashMap<>();
        sensors = new ConcurrentHashMap<>(); 
        
        // Sample room
        Room room1 = new Room("R001", "Lecture Hall A", 100, "Main Building", 1);
         room1.addSensorId("S001");
        rooms.put("R001", room1);
        
        // Sample sensor
        Sensor sensor1 = new Sensor("S001", "CO2", "R001");
        sensors.put("S001", sensor1);
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, Sensor> getSensors() { return sensors; } 
}