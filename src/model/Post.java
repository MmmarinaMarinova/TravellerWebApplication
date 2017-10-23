package model;

import model.exceptions.PostException;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Marina on 15.10.2017 ?..
 */
public class Post implements Comparable<Post> {
	private static final int MIN_LENGTH = 5;
	private static final int MAX_LENGTH = 255;
	/*
	 * post_id INT(11) user_id INT(11) description VARCHAR(1000) likes_count INT(11)
	 * dislikes_count INT(11) date_time TIMESTAMP location_id INT(11)
	 */

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
	private TreeSet<Comment> comments;

	// constructor to be used when putting object in database
	Post(User user, String description, Timestamp dateTime, Location location, HashSet<Category> categories,
			HashSet<Multimedia> multimedia, HashSet<User> taggedPeople) throws PostException {
		this.user = user;
		this.setDescription(description);
		this.dateTime = dateTime;
		this.location = location;
		this.categories = categories;
		this.multimedia = multimedia;
		this.taggedPeople = taggedPeople;
		this.likesCount = 0;
		this.dislikesCount = 0;
	}

	Post(User user, HashSet<Category> categories, HashSet<User> taggedPeople) throws PostException {
		this.user = user;
		this.categories = categories;
		this.taggedPeople = taggedPeople;
		this.likesCount = 0;
		this.dislikesCount = 0;
		this.description = "trial_description";
	}

	// constructor to be used when fetching from database
	Post(long id, String description, int likesCount, int dislikesCount, Timestamp dateTime) throws PostException {
		this.id = id;
		this.likesCount = likesCount;
		this.dislikesCount = dislikesCount;
		this.setDescription(description);
		this.dateTime = dateTime;
		// have to make methods in post dao for:
		// HashSet<Category> categories, HashSet<Multimedia> multimedia, HashSet<User>
		// taggedPeople
	}

	public Post(User user, long user_id, String description, long likes_count, long dislikes_count, Timestamp date_time,
			long location_id) {
	}

	public long getId() {
		return this.id;
	}

	void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return this.user;
	}

	void setUser(User user) {
		this.user = user;
	}

	public String getDescription() {
		return this.description;
	}

	void setDescription(String description) throws PostException {
		if (description != null && !description.isEmpty()) {
			if (description.length() < MIN_LENGTH) {
				throw new PostException(
						"Name of the category is too short. It should be more than " + MIN_LENGTH + " symbols.");
			} else if (description.length() > MAX_LENGTH) {
				throw new PostException(
						"Name of the category is too long. It should be less than" + MAX_LENGTH + " symbols");
			}
		} else {
			throw new PostException("Name of the category should not be empty!");
		}
		this.description = description;
	}

	public int getLikesCount() {
		return this.likesCount;
	}

	void setLikesCount(int likesCount) {
		this.likesCount = likesCount;
	}

	public int getDislikesCount() {
		return this.dislikesCount;
	}

	void setDislikesCount(int dislikesCount) {
		this.dislikesCount = dislikesCount;
	}

	public Timestamp getDateTime() {
		return this.dateTime;
	}

	void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public Location getLocation() {
		return this.location;
	}

	void setLocation(Location location) {
		this.location = location;
	}

	public Set<Category> getCategories() {
		return Collections.unmodifiableSet(this.categories);
	}

	void setCategories(HashSet<Category> categories) {
		this.categories = categories;
	}

	public HashSet<Multimedia> getMultimedia() {
		return (HashSet<Multimedia>) Collections.unmodifiableSet(this.multimedia);
	}

	void setMultimedia(HashSet<Multimedia> multimedia) {
		this.multimedia = multimedia;
	}

	public Set<User> getTaggedPeople() {
		return Collections.unmodifiableSet(this.taggedPeople);
	}

	void setTaggedPeople(HashSet<User> taggedPeople) {
		this.taggedPeople = taggedPeople;
	}

	public Set<Comment> getComments() {
		return Collections.unmodifiableSet(this.comments);
	}

	void setComments(TreeSet<Comment> treeSet) {
		this.comments = treeSet;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
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

	void deleteMultimedia(Multimedia multimedia) {
		if (multimedia != null) {
			this.multimedia.remove(multimedia);
		}
	}

	void tagUser(User user) {
		if (user != null && !this.taggedPeople.contains(user)) {
			this.taggedPeople.add(user);
		}
	}

	void addCategory(Category category) {
		if (category != null && !this.categories.contains(category)) {
			this.categories.add(category);
		}
	}

	void addComment(Comment c) {
		if (c != null) {
			this.comments.add(c);
		}
	}

	void deleteComment(Comment c) {
		if (c != null && this.comments.contains(c)) {
			this.comments.remove(c);
		}
	}

	@Override
	public int compareTo(Post o) {
		return this.dateTime.compareTo(o.dateTime);

	}
}
