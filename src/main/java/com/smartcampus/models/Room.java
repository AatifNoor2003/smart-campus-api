/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.models;

/**
 *
 * @author Aatif Noor
 */
import java.util.ArrayList;
import java.util.List;

public class Room {
    private String id;
    private String name;
    private int capacity;
    private String building;
    private int floor;
    private List<String> sensorIds;

    public Room() {
        this.sensorIds = new ArrayList<>();
    }

    public Room(String id, String name, int capacity, String building, int floor) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.building = building;
        this.floor = floor;
        this.sensorIds = new ArrayList<>();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getBuilding() { return building; }
    public void setBuilding(String building) { this.building = building; }

    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }

    public List<String> getSensorIds() { return sensorIds; }
    public void setSensorIds(List<String> sensorIds) { this.sensorIds = sensorIds; }

    public void addSensorId(String sensorId) {
        this.sensorIds.add(sensorId);
    }

    public void removeSensorId(String sensorId) {
        this.sensorIds.remove(sensorId);
    }
}
