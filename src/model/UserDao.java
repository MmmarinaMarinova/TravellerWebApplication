package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import model.exceptions.UserException;

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

	// ::::::::: inserting user in db :::::::::
	// * TESTED *
	public void insertUser(User u) throws SQLException, UserException {
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con.prepareStatement(
				"insert into users (username, password, email) value (?, ?, ?);", Statement.RETURN_GENERATED_KEYS);) {
			ps.setString(1, u.getUsername());
			ps.setString(2, u.getPassword()); // hashing required
			ps.setString(3, u.getEmail());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			u.setUserId(rs.getLong(1));
		}
	}

	// ::::::::: check if user exists ( to be used when users log in ) :::::::::
	// to be modified - should check for username OR email !!!
	// * TESTED *
	public boolean existsUser(String username, String password) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
<<<<<<< HEAD
		PreparedStatement ps = con
				.prepareStatement("select count(*) as count from users where username = ? and password = ?;");
		ps.setString(1, username);
		ps.setString(2, password); // hashing required
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("count") > 0;
=======
		try (PreparedStatement ps = con
				.prepareStatement("select count(*) as count from users where username = ? and password = ?;");) {
			ps.setString(1, username);
			ps.setString(2, password); // hashing required
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("count") > 0;
		}
>>>>>>> 36b216d3a2cff1657785b71b3f5b740913b4baf9
	}

	// ::::::::: check if username is taken :::::::::
	// * TESTED *
	public boolean existsUsername(String username) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
<<<<<<< HEAD
		PreparedStatement ps = con.prepareStatement("select count(*) as count from users where username = ?;");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("count") > 0;
=======
		try (PreparedStatement ps = con.prepareStatement("select count(*) as count from users where username = ?;");) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("count") > 0;
		}
>>>>>>> 36b216d3a2cff1657785b71b3f5b740913b4baf9
	}

	// ::::::::: check if email is taken :::::::::
	// * TESTED *
	public boolean existsEmail(String email) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con.prepareStatement("select count(*) as count from users where email = ?;");) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("count") > 0;
		}
	}

	// ::::::::: loading user from db :::::::::
	// * TESTED *
	public User getUserByUsername(String username) throws SQLException, UserException {
		Connection con = DBManager.getInstance().getConnection();
<<<<<<< HEAD
		PreparedStatement ps = con.prepareStatement(
				"select user_id, username, password, email, profile_pic_id, description from users where username = ?;");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return new User(rs.getLong("user_id"), username, rs.getString("password"), rs.getString("email"),
				rs.getLong("profile_pic_id"), rs.getString("description"));
=======
		User fetched = null;
		try (PreparedStatement ps = con.prepareStatement(
				"select user_id, username, password, email, profile_pic_id, description from users where username = ?;");) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fetched = new User(rs.getLong("user_id"), username, rs.getString("password"), rs.getString("email"),
						rs.getLong("profile_pic_id"), rs.getString("description"));
			}
			return fetched;
		}
>>>>>>> 36b216d3a2cff1657785b71b3f5b740913b4baf9
	}

	// * TESTED *
	public User getUserById(long user_id) throws SQLException, UserException {
		Connection con = DBManager.getInstance().getConnection();
<<<<<<< HEAD
		PreparedStatement ps = con.prepareStatement(
				"select username, password, email, profile_pic_id, description from users where user_id = ?;");
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		if (ps != null) {
			ps.close();
		}
		DBManager.getInstance().closeConnection();
		return new User(user_id, rs.getString("username"), rs.getString("password"), rs.getString("email"),
				rs.getLong("profile_pic_id"), rs.getString("description"));
=======
		User fetched = null;
		try (PreparedStatement ps = con.prepareStatement(
				"select username, password, email, profile_pic_id, description from users where user_id = ?;");) {
			ps.setLong(1, user_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fetched = new User(user_id, rs.getString("username"), rs.getString("password"), rs.getString("email"),
						rs.getLong("profile_pic_id"), rs.getString("description"));
			}
			return fetched;
		}
>>>>>>> 36b216d3a2cff1657785b71b3f5b740913b4baf9
	}

	// ::::::::: loading user data from db (helper methods) :::::::::
	// get followers ids
	public ArrayList<Long> getFollowersIds(User u) throws SQLException {
		ArrayList<Long> followersIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("select follower_id from users_followers where followed_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				followersIds.add(rs.getLong("follower_id"));
			}
			return followersIds;
		}
	}

	// get following ids
	public ArrayList<Long> getFollowingIds(User u) throws SQLException {
		ArrayList<Long> followingIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("select followed_id from users_followers where follower_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				followingIds.add(rs.getLong("followed_id"));
			}
			return followingIds;
		}
	}

	// get visited locations ids and datetimes
	private TreeMap<Timestamp, Long> getVisitedLocationsData(User u) throws SQLException {
		TreeMap<Timestamp, Long> visitedLocationsData = new TreeMap<Timestamp, Long>();
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("select location_id, date_time from visited_locations where user_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				visitedLocationsData.put(rs.getTimestamp("date_time"), rs.getLong("location_id"));
			}
			return visitedLocationsData;
		}
	}

	// get wishlist locations ids
	private ArrayList<Long> getWishlistLocationsIds(User u) throws SQLException {
		ArrayList<Long> wishlistLocationsIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con.prepareStatement("select location_id from wishlists where user_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				wishlistLocationsIds.add(rs.getLong("location_id"));
			}
			return wishlistLocationsIds;
		}
	}

	// get posts ids
	private ArrayList<Long> getPostsIds(User u) throws SQLException {
		ArrayList<Long> postsIds = new ArrayList<Long>();
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con.prepareStatement("select post_id from posts where user_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				postsIds.add(rs.getLong("post_id"));
			}
			return postsIds;
		}
	}

	// ::::::::: loading user data from db :::::::::
	// get followers
	public HashSet<User> getFollowers(User u) throws SQLException, UserException {
		HashSet<User> followers = new HashSet<User>();
		for (Long followerId : UserDao.getInstance().getFollowersIds(u)) {
			followers.add(UserDao.getInstance().getUserById(followerId));
		}
		return followers;
	}

	// get following
	public HashSet<User> getFollowing(User u) throws SQLException, UserException {
		HashSet<User> following = new HashSet<User>();
		for (Long followingId : UserDao.getInstance().getFollowersIds(u)) {
			following.add(UserDao.getInstance().getUserById(followingId));
		}
		return following;
	}

	// get visited locations
	public TreeMap<Timestamp, Location> getVisitedLocations(User u) throws SQLException {
		TreeMap<Timestamp, Location> visitedLocations = new TreeMap<Timestamp, Location>();
		TreeMap<Timestamp, Long> visitedLocationsData = UserDao.getInstance().getVisitedLocationsData(u);
		for (Iterator<Entry<Timestamp, Long>> it = visitedLocationsData.entrySet().iterator(); it.hasNext();) {
			Entry<Timestamp, Long> currentEntry = it.next();
			visitedLocations.put(currentEntry.getKey(),
					LocationDao.getInstance().getLocationById(currentEntry.getValue()));
		}
		return visitedLocations;
	}

	// get wishlist locations
	public HashSet<Location> getWishlistLocations(User u) throws SQLException {
		HashSet<Location> wishlistLocations = new HashSet<Location>();
		for (Long wishlistLocationId : UserDao.getInstance().getWishlistLocationsIds(u)) {
			wishlistLocations.add(LocationDao.getInstance().getLocationById(wishlistLocationId));
		}
		return wishlistLocations;
	}

	// get posts
	public TreeSet<Post> getPosts(User u) throws SQLException {
		TreeSet<Post> posts = new TreeSet<Post>((p1, p2) -> p1.getDateTime().compareTo(p2.getDateTime()));
		for (Long postId : UserDao.getInstance().getPostsIds(u)) {
			posts.add(PostDao.getInstance().getPostById(postId));
		}
		return posts;
	}

	// ::::::::: setting user data :::::::::
	// set followers
	public void setFollowers(User u) throws SQLException, UserException {
		u.setFollowers(UserDao.getInstance().getFollowers(u));
	}

	// set following
	public void setFollowing(User u) throws SQLException, UserException {
		u.setFollowing(UserDao.getInstance().getFollowing(u));
	}

	// set visited locations
	public void setVisitedLocations(User u) throws SQLException, UserException {
		u.setVisitedLocations(UserDao.getInstance().getVisitedLocations(u));
	}

	// set wishlit
	public void setWishlistLocations(User u) throws SQLException, UserException {
		u.setWishlist(UserDao.getInstance().getWishlistLocations(u));
	}

	// set posts
	public void setPosts(User u) throws SQLException, UserException {
		u.setPosts(UserDao.getInstance().getPosts(u));
	}

	// ::::::::: methods for updating user data :::::::::
	// * TESTED *
	public void changePassword(User u, String newPassword) throws SQLException, UserException {
		if (u.setPassword(newPassword)) {
			Connection con = DBManager.getInstance().getConnection();
			try (PreparedStatement ps = con.prepareStatement("update users set password = ? where user_id = ?;");) {
				ps.setString(1, u.getPassword());
				ps.setLong(2, u.getUserId());
				ps.executeUpdate();
			}
		}
	}
	
	// * TESTED *
	public void changeEmail(User u, String newEmail) throws SQLException, UserException {
		if (u.setEmail(newEmail)) {
			Connection con = DBManager.getInstance().getConnection();
			try (PreparedStatement ps = con.prepareStatement("update users set email = ? where user_id = ?;");) {
				ps.setString(1, u.getEmail());
				ps.setLong(2, u.getUserId());
				ps.executeUpdate();
			}
		}
	}

	// !!! TO BE DISCUSSED !!!
	public void changeProfilePicId(User u, Multimedia profilePic) throws SQLException, UserException {
		if (u.setProfilePicId(profilePic.getId())) {
			Connection con = DBManager.getInstance().getConnection();
			try (PreparedStatement ps = con
					.prepareStatement("update users set profile_pic_id = ? where user_id = ?;");) {
				ps.setLong(1, u.getProfilePicId());
				ps.setLong(2, u.getUserId());
				ps.executeUpdate();
			}
		}
	}

	// * TESTED *
	public void changeDescription(User u, String description) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con.prepareStatement("update users set description = ? where user_id = ?;");) {
			u.setDescription(description);
			ps.setString(1, u.getDescription());
			ps.setLong(2, u.getUserId());
			ps.executeUpdate();
		}
	}

	// ::::::::: methods for follow/unfollow operations :::::::::
	public void follow(User follower, User followed) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("insert into users_followers (follower_id, followed_id) value (?, ?);");) {
			ps.setLong(1, follower.getUserId());
			ps.setLong(2, followed.getUserId());
			ps.executeUpdate();
			follower.follow(followed);
		}
	}

	public void unfollow(User follower, User followed) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("delete from users_followers where follower_id = ? and followed_id = ?;");) {
			ps.setLong(1, follower.getUserId());
			ps.setLong(2, followed.getUserId());
			ps.executeUpdate();
			follower.unfollow(followed);
		}
	}

	// ::::::::: add/remove from visited locations :::::::::
	// both metods to be used by 'PostDao'
	public void addToVisitedLocations(User u, Location l, Timestamp t) throws SQLException {
		u.addVisitedLocation(t, l);
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con.prepareStatement(
				"insert into visited_locations (user_id, location_id, date_time) value (?, ?, ?);");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getLocationId());
			ps.setTimestamp(3, t);
		}
	}

	public void removeFromVisitedLocations(User u, Location l, Timestamp t) throws SQLException {
		u.removeVisitedLocation(t, l);
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con.prepareStatement(
				"delete from visited_locations where user_id = ? and location_id = ? and date_time = ?;");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getLocationId());
			ps.setTimestamp(3, t);
		}
	}

	// ::::::::: add/remove from wishlist :::::::::
	public void addToWishlist(User u, Location l) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("insert into wishlists (user_id, location_id) value (?, ?);");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getLocationId());
			ps.executeUpdate();
			u.addToWishlist(l);
		}
	}

	public void removeFromWishlist(User u, Location l) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		try (PreparedStatement ps = con
				.prepareStatement("delete from wishlists (user_id, location_id) value (?, ?);");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getLocationId());
			ps.executeUpdate();
			u.removeFromWihslist(l);
		}
	}

	// ::::::::: add/remove from posts :::::::::
	public void addPost(User u, Post p) {
		u.addPost(p);
	}
	
	public void removePost(User u, Post p) {
		u.removePost(p);
	}
	
}