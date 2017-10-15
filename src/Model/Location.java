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

    private long id;
    private String latitude;
    private String longtitute;
    private String description;
    private String locationName;

    //constructor to be used when putting object in database
    public Location(String latitude, String longtitute, String description, String locationName) {
        this.latitude = latitude;
        this.longtitute = longtitute;
        this.description = description;
        this.locationName = locationName;
    }

    //constructor to be used when fetching from database
    public Location(long id, String latitude, String longtitute, String description, String locationName) {
        this(latitude, longtitute, description, locationName);
        this.id = id;
    }


}
