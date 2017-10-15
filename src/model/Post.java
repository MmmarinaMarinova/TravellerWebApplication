package model;

import model.exceptions.PostException;

import java.sql.Timestamp;
import java.util.ArrayList;


/**
 * Created by Marina on 15.10.2017 г..
 */
public class Post {
    private static final int MIN_LENGTH = 5;
    private static final int MAX_LENGTH = 255;
   /* post_id INT(11)
    user_id INT(11)
    description VARCHAR(1000)
    likes_count INT(11)
    dislikes_count INT(11)
    date_time TIMESTAMP
    location_id INT(11)*/

    private long id;
    private long userId;
    private String description;
    private int likesCount;
    private int dislikesCount;
    private Timestamp dateTime;
    private Location location;
    private ArrayList<Long> categoriesIds;
    private ArrayList<Long> multimediaIds;
    private ArrayList<Long> taggedPeopleIds;

    //constructor to be used when putting object in database
    public Post(long userId, String description, Timestamp dateTime, Location location, ArrayList<Long> categoriesIds, ArrayList<Long> multimediaIds,ArrayList<Long> taggedPeopleIds) throws PostException {
        this.userId = userId;
        this.setDescription(description);
        this.dateTime = dateTime;
        this.location = location;
        this.categoriesIds = categoriesIds;
        this.multimediaIds= multimediaIds;
        this.taggedPeopleIds=taggedPeopleIds;
    }

    //constructor to be used when fetching from database
    public Post(long id, long userId, String description, int likesCount, int dislikesCount, Timestamp dateTime, Location location, ArrayList<Long> categories,ArrayList<Long> multimedia, ArrayList<Long> taggedPeople) throws PostException {
        this(userId, description, dateTime,location,categories, multimedia,taggedPeople);
        this.id = id;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
    }

    public Post(long id, String description, int likesCount, int dislikesCount, Timestamp dateTime) throws PostException {
        this.id = id;
        this.setDescription(description);
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.dateTime = dateTime;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) throws PostException {
        if(!description.isEmpty()){
            if(description.length()<MIN_LENGTH){
                throw new PostException("Name of the category is too short. It should be more than "+MIN_LENGTH+" symbols.");
            }else if(description.length()>MAX_LENGTH){
                throw new PostException("Name of the category is too long. It should be less than"+MAX_LENGTH+" symbols");
            }
        }else{
            throw new PostException("Name of the category should not be empty!");
        }
        this.description = description;
    }

    public int getLikesCount() {
        return this.likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return this.dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    public Timestamp getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<Long> getCategoriesIds() {
        return this.categoriesIds;
    }

    public void setCategoriesIds(ArrayList<Long> categoriesIds) {
        this.categoriesIds = categoriesIds;
    }

    public ArrayList<Long> getMultimediaIds() {
        return this.multimediaIds;
    }

    public void setMultimediaIds(ArrayList<Long> multimediaIds) {
        this.multimediaIds = multimediaIds;
    }

    public ArrayList<Long> getTaggedPeopleIds() {
        return this.taggedPeopleIds;
    }

    public void setTaggedPeopleIds(ArrayList<Long> taggedPeopleIds) {
        this.taggedPeopleIds = taggedPeopleIds;
    }
}
