package model;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import model.exceptions.UserException;

public final class User {
	// ::::::::: main object characteristics :::::::::
	private long userId = 0;
	private String username = null;
	private String password = null;
	private String email = null;
	private long profilePicId = 0; // default profile pic id must be 0
	private String description = "";
	// private Multimedia profilePic = null;
	private HashSet<User> followers = null;
	private HashSet<User> following = null;
	private TreeMap<Timestamp, Location> visitedLocations = null; // order by date and time of visit required
	private HashSet<Location> wishlist = null;
	private TreeSet<Post> posts = null; // order by date and time of post submition required
	// !!! overriding of compareTo() in 'Post' required !!!

	// ::::::::: additional object characteristics :::::::::
	private static final int MIN_USERNAME_LENGTH = 5;
	private static final int MAX_USERNAME_LENGTH = 45;
	private static final int MIN_PASSWORD_LENGTH = 6;
	private static final int MAX_PASSWORD_LENGTH = 255;
	private static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*([0-9]|[\\W])).+$";
	private static final String EMAIL_VALIDATION_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	private static final String USERNAME_VALIDATION_REGEX = "([A-Za-z0-9-_]+)";

	// ::::::::: constructor to be used for user registration :::::::::
	// public modifier required - constructor will be used in 'controller' package
	public User(String username, String password, String email) throws UserException {
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
	}

	// ::::::::: constructor to be used when loading an existing user from db
	// :::::::::
	User(long userId, String username, String password, String email, long profilePicId, String description
	/* ,Multimedia profilePic */) throws UserException {
		this(username, password, email);
		this.setUserId(userId);
		this.setProfilePicId(profilePicId);
		this.setDescription(description);
		// this.setProfilePic(profilePic);
	}

	// ::::::::: accessors :::::::::
	public long getUserId() {
		return this.userId;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public long getProfilePicId() {
		return this.profilePicId;
	}

	public String getDescription() {
		return this.description;
	}

	public String getEmail() {
		return this.email;
	}

	/*
	 * public Multimedia getProfilePic() { return this.profilePic; }
	 */

	public Set<User> getFollowers() {
		return Collections.unmodifiableSet(this.followers);
	}

	public Set<User> getFollowing() {
		return Collections.unmodifiableSet(this.following);
	}

	public SortedMap<Timestamp, Location> getVisitedLocations() {
		return Collections.unmodifiableSortedMap(this.visitedLocations);
	}

	public Set<Location> getWishlist() {
		return Collections.unmodifiableSet(this.wishlist);
	}

	public SortedSet<Post> getPosts() {
		return Collections.unmodifiableSortedSet(this.posts);
	}

	// ::::::::: mutators :::::::::
	void setUserId(long userId) throws UserException {
		if (userId > 0) {
			this.userId = userId;
		} else {
			throw new UserException("Invalid user id!");
		}
	}

	private void setUsername(String username) throws UserException {
		if (username.length() >= MIN_USERNAME_LENGTH && username.matches(USERNAME_VALIDATION_REGEX)) {
			if (username.length() <= MAX_USERNAME_LENGTH) {
				this.username = username;
			} else {
				throw new UserException("Username too long!");
			}
		} else {
			throw new UserException("Username must be at least " + MIN_USERNAME_LENGTH
					+ " characters long and must contain only letters, digits, hyphens and underscores! ");
		}
	}

	/*
	 * public void setPassword(String password) throws UserException { if
	 * (password.length() >= MIN_PASWORD_LENGTH) { if
	 * (password.matches("^(?=.*[a-z])(?=.*[A-Z])$")) { if
	 * (password.matches("^(?=.*[0-9])$") || password.matches("^(?=.*[@#$%^&+=])$"))
	 * { this.password = password; // hashing required } else { throw new
	 * UserException("Password must contain at least one non-alphabetic character!"
	 * ); } } else { throw new UserException(
	 * "Password must contain at least one uppercase and at least one lowercase character!"
	 * ); } } else { throw new UserException("Password must be at least " +
	 * MIN_PASWORD_LENGTH + " characters long!"); } }
	 */

	boolean setPassword(String password) throws UserException {
		if (password != null && password.length() >= MIN_PASSWORD_LENGTH
				&& (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])$")
						|| password.matches(PASSWORD_VALIDATION_REGEX))) {
			if (password.length() <= MAX_PASSWORD_LENGTH) {
				this.password = password; // hashing required
				return true;
			} else {
				throw new UserException("Password too long!");
			}
		} else {
			throw new UserException("Password must be at least " + MIN_PASSWORD_LENGTH
					+ " characters long and must contain at least one lowercase character, at least one uppercase character and at least one non-alphabetic character!");
		}
	}

	boolean setEmail(String email) throws UserException {
		if (email != null && email.matches(EMAIL_VALIDATION_REGEX)) {
			this.email = email;
			return true;
		} else {
			throw new UserException("Invalid e-mail address!");
		}
	}

	boolean setProfilePicId(long profilePicId) throws UserException {
		if (profilePicId >= 0) {
			this.profilePicId = profilePicId;
			return true;
		} else {
			throw new UserException("Invalid profile picture id!");
		}
	}

	void setDescription(String description) {
		this.description = description != null ? description : "";
	}

	/*
	 * void setProfilePic(Multimedia profilePic) throws UserException { if
	 * (profilePic != null) { this.profilePic = profilePic; } else { throw new
	 * UserException("Invalid profile picture!"); } }
	 */

	void setFollowers(HashSet<User> followers) {
		this.followers = followers;
	}

	void setFollowing(HashSet<User> following) {
		this.following = following;
	}

	void setVisitedLocations(TreeMap<Timestamp, Location> visitedLocations) {
		this.visitedLocations = visitedLocations;
	}

	void setWishlist(HashSet<Location> wishlist) {
		this.wishlist = wishlist;
	}

	void setPosts(TreeSet<Post> posts) {
		this.posts = posts;
	}

	// ::::::::: follow/unfollow :::::::::
	void follow(User followed) {
		if (this.following == null) {
			this.following = new HashSet<User>();
		}
		this.following.add(followed);
		if (followed.followers == null) {
			followed.followers = new HashSet<User>();
		}
		followed.followers.add(this);
	}

	void unfollow(User followed) {
		this.following.remove(followed);
		followed.followers.remove(this);
	}

	// ::::::::: add/remove from visited_locations :::::::::
	void addVisitedLocation(Timestamp datetime, Location location) {
		if (this.visitedLocations == null) {
			this.visitedLocations = new TreeMap<Timestamp, Location>();
		}
		this.visitedLocations.put(datetime, location);
	}

	void removeVisitedLocation(Timestamp datetime, Location location) {
		this.visitedLocations.remove(datetime, location);
	}

	// ::::::::: add/remove from wishlit :::::::::
	void addToWishlist(Location l) {
		if (this.wishlist == null) {
			this.wishlist = new HashSet<Location>();
		}
		this.wishlist.add(l);
	}

	void removeFromWihslist(Location l) {
		this.wishlist.remove(l);
	}

	// ::::::::: add/remove from posts :::::::::
	void addPost(Post p) {
		if (this.posts == null) {
			this.posts = new TreeSet<Post>();
		}
		this.posts.add(p);
	}

	void removePost(Post p) {
		this.posts.remove(p);
	}

	// ::::::::: overriding of 'hashCode()' and 'equals()' methods :::::::::
	// only 'userId' field is used for user distinction
	// (duplicate usernames and emails must not be assigned)
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userId != other.userId)
			return false;
		return true;
	}

}