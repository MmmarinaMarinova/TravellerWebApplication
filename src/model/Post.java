package model;

import model.exceptions.PostException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;


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
    private User user;
    private String description;
    private int likesCount;
    private int dislikesCount;
    private Timestamp dateTime;
    private Location location;
    private HashSet<Category> categories;
    private HashSet<Multimedia> multimedia;
    private HashSet<User> taggedPeople;

    //constructor to be used when putting object in database
    Post(User user, String description, Timestamp dateTime, Location location, HashSet<Category> categories, HashSet<Multimedia> multimedia, HashSet<User> taggedPeople) throws PostException {
        this.user = user;
        this.setDescription(description);
        this.dateTime = dateTime;
        this.location = location;
        this.categories = categories;
        this.multimedia = multimedia;
        this.taggedPeople = taggedPeople;
        this.likesCount=0;
        this.dislikesCount=0;
    }

    Post(User user, HashSet<Category> categories, HashSet<User> taggedPeople) throws PostException {
        this.user = user;
        this.categories = categories;
        this.taggedPeople = taggedPeople;
        this.likesCount=0;
        this.dislikesCount=0;
        this.description="trial_description";
    }


    //constructor to be used when fetching from database
    Post(long id, String description, int likesCount, int dislikesCount, Timestamp dateTime) throws PostException {
        this.id = id;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
        this.setDescription(description);
        this.dateTime=dateTime;
        //have to make methods in post dao for:
        //HashSet<Category> categories, HashSet<Multimedia> multimedia, HashSet<User> taggedPeople
    }

    long getId() {
        return this.id;
    }

    void setId(long id) {
        this.id = id;
    }

    User getUser() {
        return this.user;
    }

    void setUser(User user) {
        this.user = user;
    }

    String getDescription() {
        return this.description;
    }

    void setDescription(String description) throws PostException {
        if (!description.isEmpty()) {
            if (description.length() < MIN_LENGTH) {
                throw new PostException("Name of the category is too short. It should be more than " + MIN_LENGTH + " symbols.");
            } else if (description.length() > MAX_LENGTH) {
                throw new PostException("Name of the category is too long. It should be less than" + MAX_LENGTH + " symbols");
            }
        } else {
            throw new PostException("Name of the category should not be empty!");
        }
        this.description = description;
    }

    int getLikesCount() {
        return this.likesCount;
    }

    void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    int getDislikesCount() {
        return this.dislikesCount;
    }

    void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    Timestamp getDateTime() {
        return this.dateTime;
    }

    void setDateTime(Timestamp dateTime) {
        this.dateTime = dateTime;
    }

    Location getLocation() {
        return this.location;
    }

    void setLocation(Location location) {
        this.location = location;
    }

<<<<<<< HEAD
    public Set<Category> getCategories() {
        return Collections.unmodifiableSet(this.categories);
=======
    HashSet<Category> getCategories() {
        return this.categories;
>>>>>>> 4b088f946734be909f85cfb4030bc49f0c9f0099
    }

    void setCategories(HashSet<Category> categories) {
        this.categories = categories;
    }

<<<<<<< HEAD
    public Set<Multimedia> getMultimedia() {
        return Collections.unmodifiableSet(this.multimedia);
=======
    HashSet<Multimedia> getMultimedia() {
        return this.multimedia;
>>>>>>> 4b088f946734be909f85cfb4030bc49f0c9f0099
    }

    void setMultimedia(HashSet<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }

<<<<<<< HEAD
    public Set<User> getTaggedPeople() {
        return Collections.unmodifiableSet(this.taggedPeople);
=======
    HashSet<User> getTaggedPeople() {
        return this.taggedPeople;
>>>>>>> 4b088f946734be909f85cfb4030bc49f0c9f0099
    }

    void setTaggedPeople(HashSet<User> taggedPeople) {
        this.taggedPeople = taggedPeople;
    }
}

