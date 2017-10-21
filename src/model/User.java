package model;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import model.exceptions.UserException;

// !!! SYNCHRONIZATION TO BE DISCUSSED !!!

public final class User {
	// ::::::::: main object characteristics :::::::::
	private long userId = 0;
	private String username = null;
	private String password = null;
	private String email = null;
	private long profilePicId = 0; // default profile pic id must be 0
	private String description = "";
	private HashSet<User> followers = null;
	private HashSet<User> following = null;
	private TreeMap<Timestamp, Location> visitedLocations = null; // order by date and time of visit required
	private HashSet<Location> wishlist = null;
	private TreeSet<Post> posts = null; // order by date and time of post submition required

	// ::::::::: additional object characteristics :::::::::
	private static final int MIN_USERNAME_LENGTH = 5;
	private static final int MAX_USERNAME_LENGTH = 45;
	private static final int MIN_PASSWORD_LENGTH = 6;
	private static final int MAX_PASSWORD_LENGTH = 255;
	private static final String PASSWORD_VALIDATION_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])$";
	private static final String EMAIL_VALIDATION_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
	private static final String USERNAME_VALIDATION_REGEX = "^(?=\\S+$)$";

	// ::::::::: constructor to be used for user registration :::::::::
	// public modifier required - constructor will be used in 'controller' package
	public User(String username, String password, String email) throws UserException {
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
	}

	// ::::::::: constructor to be used when loading an existing user from db
	// :::::::::
	 User(long userId, String username, String password, String email, long profilePicId, String description)
			throws UserException {
		this(username, password, email);
		this.setUserId(userId);
		this.setProfilePicId(profilePicId);
		this.setDescription(description);
	}

	// ::::::::: accessors :::::::::
	 long getUserId() {
		return this.userId;
	}

	 String getUsername() {
		return this.username;
	}

	 String getPassword() {
		return this.password;
	}

	 long getProfilePicId() {
		return this.profilePicId;
	}

	 String getDescription() {
		return this.description;
	}

	 String getEmail() {
		return this.email;
	}

	 HashSet<User> getFollowers() {
		return this.followers;
	}

	 HashSet<User> getFollowing() {
		return this.following;
	}

	 TreeMap<Timestamp, Location> getVisitedLocations() {
		return this.visitedLocations;
	}

	 HashSet<Location> getWishlist() {
		return this.wishlist;
	}

	 TreeSet<Post> getPosts() {
		return this.posts;
	}

	// ::::::::: mutators :::::::::
	 void setUserId(long userId) throws UserException {
		if (userId > 0) {
			this.userId = userId;
		} else {
			throw new UserException("Invalid user id!");
		}
	}

	 void setUsername(String username) throws UserException {
		if (username.length() >= MIN_USERNAME_LENGTH && username.matches(USERNAME_VALIDATION_REGEX)) {
			if (this.username.length() <= MAX_USERNAME_LENGTH) {
				this.username = username;
			} else {
				throw new UserException("Username too long!");
			}
		} else {
			throw new UserException(
					"Username must be at least " + " characters long and must not contain any whitespace characters!");
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
