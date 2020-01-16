package pl.dsamsel.mp1.Models;

public class Shop {

    private String id;
    private String name;
    private String description;
    private double range;
    private double latitude;
    private double longitude;

    public Shop(String id, String name, String description, double range, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.range = range;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
