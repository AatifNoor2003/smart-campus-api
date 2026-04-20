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

public class DataStore {
    private static DataStore instance;
    private final Map<String, Room> rooms;

    private DataStore() {
        rooms = new ConcurrentHashMap<>();
        
        // Sample data for Part 2
        Room room1 = new Room("R001", "Lecture Hall A", "Main Building", 1);
         room1.addSensorId("S001");
        rooms.put("R001", room1);
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public Map<String, Room> getRooms() { return rooms; }
}