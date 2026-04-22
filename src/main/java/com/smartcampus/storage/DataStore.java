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
import com.smartcampus.models.SensorReading;
import java.util.List;
import java.util.ArrayList;

public class DataStore {
    private static final DataStore INSTANCE = new DataStore();
    private final Map<String, Room> rooms;
    private final Map<String, Sensor> sensors;
    private final Map<String, List<SensorReading>> sensorReadings;

    private DataStore() {
        rooms = new ConcurrentHashMap<>();
        sensors = new ConcurrentHashMap<>();
        sensorReadings = new ConcurrentHashMap<>();
        
        // Sample room
        Room room1 = new Room("R001", "Lecture Hall A", 100);
        room1.addSensorId("S001");
        rooms.put("R001", room1);
        
        // Sample sensor
        Sensor sensor1 = new Sensor("S001", "CO2", "R001");
        sensors.put("S001", sensor1);
        
        // Sample reading for sensor S001
        List<SensorReading> readings = new ArrayList<>();
        SensorReading reading1 = new SensorReading();
        reading1.setValue(425.5);
        reading1.setTimestamp(System.currentTimeMillis());
        readings.add(reading1);
        sensorReadings.put("S001", readings);
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, Sensor> getSensors() { return sensors; } 
    public Map<String, List<SensorReading>> getSensorReadings() { return sensorReadings; }
}