package Model;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class Location {
    /*location_id INT(11)
latitude VARCHAR(45)
longtitude VARCHAR(45)
description VARCHAR(1000)
location_name VARCHAR(45)*/

    private int id;
    private String latitude;
    private String longtitute;
    private String description;
    private String locationName;

    public Location(int id, String latitude, String longtitute, String description, String locationName) {
        this.id = id;
        this.latitude = latitude;
        this.longtitute = longtitute;
        this.description = description;
        this.locationName = locationName;
    }

    public Location(String latitude, String longtitute, String description, String locationName) {
        this.latitude = latitude;
        this.longtitute = longtitute;
        this.description = description;
        this.locationName = locationName;
    }
}
