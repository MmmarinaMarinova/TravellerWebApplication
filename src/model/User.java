package model;

import java.util.ArrayList;

import model.exceptions.UserException;

public final class User {
	// object data
	private long userId = 0;
	private String username = null;
	private String password = null;
	private long profilePicId = 0; // default profile pic id must be 0
	private String description = "";
	private ArrayList<Long> followersIds = null;
	private ArrayList<Long> followingIds = null;
	private ArrayList<Long> visitedLocationsIds = null;
	private ArrayList<Long> locationsFromWishlistIds = null;
	private ArrayList<Long> postsIds = null;
	// constants
	private static final int MIN_USERNAME_LENGTH = 5;
	private static final int MAX_USERNAME_LENGTH = 45;
	private static final int MIN_PASSWORD_LENGTH = 6;
	private static final int MAX_PASSWORD_LENGTH = 255;

	// constructor to be used when registering new user
	public User(String username, String password) throws UserException {
		this.setUsername(username);
		this.setPassword(password);
	}

	// constructor to be used when loading an existing user from db
	public User(long userId, String username, String password, long profilePicId, String description,
			ArrayList<Long> followersIds, ArrayList<Long> followingIds, ArrayList<Long> visitedLocationsIds,
			ArrayList<Long> locationsFromWishlistIds, ArrayList<Long> postsIds) throws UserException {
		this(username, password);
		this.userId = userId;
		this.setProfilePicId(profilePicId);
		this.setDescription(description);
		this.followersIds = followersIds;
		this.followingIds = followingIds;
		this.visitedLocationsIds = visitedLocationsIds;
		this.locationsFromWishlistIds = locationsFromWishlistIds;
		this.postsIds = postsIds;
	}

	// accessors
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

	public ArrayList<Long> getFollowersIds() {
		return this.followersIds;
	}

	public ArrayList<Long> getFollowingIds() {
		return this.followingIds;
	}

	public ArrayList<Long> getVisitedLocationsIds() {
		return this.visitedLocationsIds;
	}
	
	public ArrayList<Long> getLocationsFromWishlistIds() {
		return this.locationsFromWishlistIds;
	}

	public ArrayList<Long> getPostsIds() {
		return this.postsIds;
	}

	// mutators
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setUsername(String username) throws UserException {
		if (username.length() >= MIN_USERNAME_LENGTH && username.matches("^(?=\\S+$)$")) {
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
	public void setPassword(String password) throws UserException {
		if (password.length() >= MIN_PASSWORD_LENGTH && (password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])$")
				|| password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])$"))) {
			if (password.length() <= MAX_PASSWORD_LENGTH) {
				this.password = password; // hashing required
			} else {
				throw new UserException("Password too long!");
			}
		} else {
			throw new UserException("Password must be at least " + MIN_PASSWORD_LENGTH
					+ " characters long and must contain at least one lowercase character, at least one uppercase character and at least one non-alphabetic character!");
		}
	}

	public void setProfilePicId(long profilePicId) {
		this.profilePicId = profilePicId;
	}

	public void setDescription(String description) {
		this.description = description != null ? description : "";
	}

	public void setFollowersIds(ArrayList<Long> followersIds) {
		this.followersIds = followersIds;
	}

	public void setFollowingIds(ArrayList<Long> followingIds) {
		this.followingIds = followingIds;
	}

	public void setVisitedLocationsIds(ArrayList<Long> visitedLocationsIds) {
		this.visitedLocationsIds = visitedLocationsIds;
	}

	public void setLocationsFromWishlistIds(ArrayList<Long> locationsFromWishlistIds) {
		this.locationsFromWishlistIds = locationsFromWishlistIds;
	}

	public void setPostsIds(ArrayList<Long> postsIds) {
		this.postsIds = postsIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

}
