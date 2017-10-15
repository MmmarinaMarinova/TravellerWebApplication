package model;

import java.sql.Timestamp;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class Post {
   /* post_id INT(11)
    user_id INT(11)
    description VARCHAR(1000)
    likes_count INT(11)
    dislikes_count INT(11)
    date_time TIMESTAMP
    location_id INT(11)*/

    private long id;
    private User user;
    private String description;
    private int likesCount;
    private int dislikesCount;
    private Timestamp dateTime;
    private Location location;
    private HashSet<Category> categories;

    //constructor to be used when putting object in database
    public Post(User user, String description, Timestamp dateTime, Location location, HashSet<Category> categories) {
        this.user = user;
        this.description = description;
        this.dateTime = dateTime;
        this.location = location;
        this.categories = categories;
    }

    //constructor to be used when fetching from database
    public Post(long id, User user, String description, int likesCount, int dislikesCount, Timestamp dateTime, Location location, HashSet<Category> categories) {
        this(user, description, dateTime,location,categories);
        this.id = id;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
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

    public HashSet<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(HashSet<Category> categories) {
        this.categories = categories;
    }
}
