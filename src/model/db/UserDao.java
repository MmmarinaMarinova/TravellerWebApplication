package model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import model.Location;
import model.User;
import model.exceptions.UserException;
import model.exceptions.VisitedLocationException;

public class UserDao { // operates with the following tables: 'users', 'users_followers',
						// 'visited_locations', 'wishlists', 'posts'

	private static UserDao instance;

	private UserDao() {
	}

	public static synchronized UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	// insert user in db
	public void insertUser(User u) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("insert into users (username, password,email) VALUES (?, ?,?);",
				Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getPassword()); // hashing required
		ps.setString(3,u.getEmail());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		u.setUserId(rs.getLong(1));
	}

	// check if user exists ( to be used when users log in )
	//tested
	public boolean existsUser(String username, String password) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con
				.prepareStatement("select count(*) as count from users where username = ? and password = ?;");
		ps.setString(1, username);
		ps.setString(2, password); // hashing required
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("count") > 0;
	}

	// check if username is taken ( to be used when users register)
	public boolean existsUsername(String username) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("select count(*) as count from users where username = ?;");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("count") > 0;
	}

	// loading user from db
	public User getUserByUsername(String username) throws SQLException, UserException, VisitedLocationException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"select user_id, username, password, profile_pic_id, description from users where username = ?;");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		ArrayList<Long> followersIds = this.getFollowersIds(rs.getLong("user_id"));
		ArrayList<Long> followingIds = this.getFollowingIds(rs.getLong("user_id"));
		ArrayList<Long> visitedLocationsIds = this.getVisitedLocationsIds(rs.getLong("user_id"));
		ArrayList<Long> locationsFromWishlistIds = this.getLocationsFromWishlistIds(rs.getLong("user_id"));
		ArrayList<Long> postsIds = this.getPostsIds(rs.getLong("user_id"));
		return new User(rs.getLong("id"), username, rs.getString("password"), rs.getLong("profile_pic_id"),
				rs.getString("description"), followersIds, followingIds, visitedLocationsIds, locationsFromWishlistIds,
				postsIds);
	}

	//tested
	public User getUserById(long user_id) throws SQLException, UserException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con
				.prepareStatement("select username, password, email from users where user_id = ?;");
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		//ArrayList<Long> followersIds = this.getFollowersIds(rs.getLong("user_id"));
		//ArrayList<Long> followingIds = this.getFollowingIds(rs.getLong("user_id"));
		//ArrayList<Long> visitedLocationsIds = this.getVisitedLocationsIds(rs.getLong("user_id"));
		//ArrayList<Long> locationsFromWishlistIds = this.getLocationsFromWishlistIds(rs.getLong("user_id"));
		//ArrayList<Long> postsIds = this.getPostsIds(rs.getLong("user_id"));
		//return new User(user_id, rs.getString("username"), rs.getString("password"), rs.getLong("profile_pic_id"),
		//		rs.getString("description"), followersIds, followingIds, visitedLocationsIds, locationsFromWishlistIds,
		//		postsIds);
		return new User(rs.getString("username"),rs.getString("password"),rs.getString("email"));
	}

	// getting visited locations ids
	private ArrayList<Long> getVisitedLocationsIds(long user_id) throws SQLException {
		ArrayList<Long> visitedLocationsIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("select location_id from visited_locations where user_id = ?;");
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			visitedLocationsIds.add(rs.getLong("location_id"));
		}
		return visitedLocationsIds;
	}

	// getting wishlist locations ids
	private ArrayList<Long> getLocationsFromWishlistIds(long user_id) throws SQLException {
		ArrayList<Long> locationsFromWishlistIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("select location_id from wishlists where user_id = ?;");
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			locationsFromWishlistIds.add(rs.getLong("location_id"));
		}
		return locationsFromWishlistIds;
	}

	// getting posts ids
	private ArrayList<Long> getPostsIds(long user_id) throws SQLException {
		ArrayList<Long> postsIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("select post_id from posts where user_id = ?;");
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			postsIds.add(rs.getLong("post_id"));
		}
		return postsIds;
	}

	// methods for updating user data
	public void changePassword(User u, String newPassword) throws SQLException, UserException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("update users set password = ? where user_id = ?;");
		ps.setString(1, newPassword); // hashing required
		ps.setLong(2, u.getUserId());
		ps.executeUpdate();
		u.setPassword(newPassword); // hashing required
	}

	public void changeProfilePicId(User u, long profilePicId) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("update users set profile_pic_id = ? where user_id = ?;");
		ps.setLong(1, profilePicId);
		ps.setLong(2, u.getUserId());
		ps.executeUpdate();
		u.setProfilePicId(profilePicId);
	}

	public void changeDescription(User u, String description) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("update users set description = ? where user_id = ?;");
		ps.setString(1, description);
		ps.setLong(2, u.getUserId());
		ps.executeUpdate();
		u.setDescription(description);
	}

	// follow/unfollow
	public void follow(User follower, User followed) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con
				.prepareStatement("insert into users_followers (follower_id, followed_id) value (?, ?);");
		ps.setLong(1, follower.getUserId());
		ps.setLong(2, followed.getUserId());
		ps.executeUpdate();
		ArrayList<Long> followingIds = follower.getFollowingIds();
		followingIds.add(followed.getUserId());
		follower.setFollowingIds(followingIds);
	}

	public void unfollow(User follower, User followed) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con
				.prepareStatement("delete from users_followers where follower_id = ? and followed_id = ?;");
		ps.setLong(1, follower.getUserId());
		ps.setLong(2, followed.getUserId());
		ps.executeUpdate();
		ArrayList<Long> followingIds = follower.getFollowingIds();
		followingIds.remove(followed.getUserId());
		follower.setFollowingIds(followingIds);
	}

	// get followers
	public ArrayList<Long> getFollowersIds(long followed_id) throws SQLException {
		ArrayList<Long> followersIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("select follower_id from users_followers where followed_id = ?;");
		ps.setLong(1, followed_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			followersIds.add(rs.getLong("follower_id"));
		}
		return followersIds;
	}

	public HashSet<User> getFollowers(User u) throws SQLException, UserException {
		HashSet<User> followers = new HashSet<User>();
		for (Long followerId : u.getFollowersIds()) {
			followers.add(this.getUserById(followerId));
		}
		return followers;
	}

	// get followed users
	public ArrayList<Long> getFollowingIds(long follower_id) throws SQLException {
		ArrayList<Long> followingIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("select followed_id from users_followers where follower_id = ?;");
		ps.setLong(1, follower_id);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			followingIds.add(rs.getLong("followed_id"));
		}
		return followingIds;
	}

	public HashSet<User> getFollowing(User u) throws SQLException, UserException {
		HashSet<User> following = new HashSet<User>();
		for (Long followingId : u.getFollowingIds()) {
			following.add(this.getUserById(followingId));
		}
		return following;
	}

	// add/remove location from wishlist
	public void addToWishlist(User u, Location l) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("insert into wishlists (user_id, location_id) value (?, ?);");
		ps.setLong(1, u.getUserId());
		ps.setLong(2, l.getId());
		ps.executeUpdate();
		ArrayList<Long> locationsFromWishlistIds = u.getLocationsFromWishlistIds();
		locationsFromWishlistIds.add(l.getId());
		u.setLocationsFromWishlistIds(locationsFromWishlistIds);
	}

	public void removeFromWishlist(User u, Location l) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("delete from wishlists (user_id, location_id) value (?, ?);");
		ps.setLong(1, u.getUserId());
		ps.setLong(2, l.getId());
		ps.executeUpdate();
		ArrayList<Long> locationsFromWishlistIds = u.getLocationsFromWishlistIds();
		locationsFromWishlistIds.remove(l.getId());
		u.setLocationsFromWishlistIds(locationsFromWishlistIds);
	}

}
