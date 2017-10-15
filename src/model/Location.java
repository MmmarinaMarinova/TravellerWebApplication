package model;

import model.exceptions.LocationException;

import java.util.ArrayList;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class Location {
    /*location_id INT(11)
latitude VARCHAR(45)
longtitude VARCHAR(45)
description VARCHAR(1000)
location_name VARCHAR(45)*/
    private final static int MAX_LENGTH=255;
    private final static int MIN_LENGTH=5;

    private long id;
    private String latitude;
    private String longtitude;
    private String description;
    private String locationName;
    private ArrayList<Long> visitedLocationsIDs;

    //constructor to be used when putting object in database
    public Location(String latitude, String longtitute, String description, String locationName) throws LocationException {
        this.setLatitude(latitude);
        this.setLongtitude(longtitute);
        this.setDescription(description);
        this.setLocationName(locationName);
        this.visitedLocationsIDs=new ArrayList<>();
    }

    //constructor to be used when fetching from database
    public Location(long id, String latitude, String longtitute, String description, String locationName) throws LocationException {
        this(latitude, longtitute, description, locationName);
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public void setLatitude(String latitude) {
        //TODO validations for latitude
        this.latitude = latitude;
    }

    public String getLongtitude() {
        return this.longtitude;
    }

    public void setLongtitude(String longtitude) {
        //TODO validations for longtitude
        this.longtitude = longtitude;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public void setLocationName(String locationName) throws LocationException {
        if(!locationName.isEmpty()){
            if(locationName.length()<MIN_LENGTH){
                throw new LocationException("Name of the category is too short. It should be more than "+MIN_LENGTH+" symbols.");
            }else if(locationName.length()>MAX_LENGTH){
                throw new LocationException("Name of the category is too long. It should be less than"+MAX_LENGTH+" symbols");
            }
        }else{
            throw new LocationException("Name of the category should not be empty!");
        }
        this.locationName = locationName;
    }

    public ArrayList<Long> getVisitedLocationsIDs() {
        return this.visitedLocationsIDs;
    }

    public void setVisitedLocationsIDs(ArrayList<Long> visitedLocationsIDs) {
        this.visitedLocationsIDs = visitedLocationsIDs;
    }
}
