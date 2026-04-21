/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.models;

/**
 *
 * @author Aatif Noor
 */
import java.util.UUID;

public class SensorReading {
    private String id;
    private double value;
    private long timestamp;

    public SensorReading() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getValue() { return value; }
    public void setValue(double value) { this.value = value; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
