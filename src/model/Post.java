package model;

import model.exceptions.PostException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;


/**
 * Created by Marina on 15.10.2017 ?..
 */
public class Post implements Comparable {
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
    private HashSet<Comment> comments;


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
        if (description!=null && !description.isEmpty()) {
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

    HashSet<Category> getCategories() {
        return (HashSet<Category>) Collections.unmodifiableSet(this.categories);
    }

    void setCategories(HashSet<Category> categories) {
        this.categories = categories;
    }

    HashSet<Multimedia> getMultimedia() {
        return (HashSet<Multimedia>) Collections.unmodifiableSet(this.multimedia);
    }

    void setMultimedia(HashSet<Multimedia> multimedia) {
        this.multimedia = multimedia;
    }

    HashSet<User> getTaggedPeople() {
        return (HashSet<User>) Collections.unmodifiableSet(this.taggedPeople);
    }

    void setTaggedPeople(HashSet<User> taggedPeople) {
        this.taggedPeople = taggedPeople;
    }

    HashSet<Comment> getComments() {
        return (HashSet<Comment>) Collections.unmodifiableSet(this.comments);
    }

    void setComments(HashSet<Comment> comments) {
        this.comments = comments;
    }


    /**
     *
     * @param o
     * @return 0 if equal, -1 if current date is before the argument date, 1 if current date is after the argument date
     */
    @Override
    public int compareTo(Object o) {
        return this.dateTime.compareTo((Timestamp) o);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return id == post.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }
}

