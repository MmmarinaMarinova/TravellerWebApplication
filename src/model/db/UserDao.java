package model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import model.User;
import model.VisitedLocation;
import model.exceptions.UserException;
import model.exceptions.VisitedLocationException;

public class UserDao { //used to operate with the following tables: 'users', 'users_followers', 'wishlists'
	private static UserDao instance;

	private UserDao() {
	}

	public static synchronized UserDao getInstance() {
		if (instance == null) {
			instance = new UserDao();
		}
		return instance;
	}

	public void insertUser(User u) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement("insert into users (username, password) VALUES (?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, u.getUsername());
		ps.setString(2, u.getPassword()); // hashing required
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		u.setUserId(rs.getLong(1));
	}

	public boolean existsUser(String username, String password) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con
				.prepareStatement("select count(*) as count from users where username = ? and password = ?");
		ps.setString(1, username);
		ps.setString(2, password); // hashing required
		ResultSet rs = ps.executeQuery();
		rs.next();
		return rs.getInt("count") > 0;
	}

	public User getUserByUsername(String username) throws SQLException, UserException, VisitedLocationException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con
				.prepareStatement("select id, username, password, profile_pic_id, description from users where username = ?");
		ps.setString(1, username);
		ResultSet rs = ps.executeQuery();
		rs.next();
		HashSet<User> followers = this.getFollowers(rs.getLong("id"));
		HashSet<User> following = this.getFollowing(rs.getLong("id"));
		HashSet<VisitedLocation> visitedLocations = VisitedLocationDao.getInstance().getVisitedLocationsForUser(rs.getLong("id"));
		return new User(rs.getLong("id"), username, rs.getString("password"), rs.getLong("profile_pic_id"),
				rs.getString("description"), followers, following, visitedLocations);
	}
	
	public User getUserById(long user_id) throws SQLException, UserException, VisitedLocationException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con
				.prepareStatement("select username, password, profile_pic_id, description from users where id = ?");
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		rs.next();
		HashSet<User> followers = this.getFollowers(user_id);
		HashSet<User> following = this.getFollowing(user_id);
		HashSet<VisitedLocation> visitedLocations = VisitedLocationDao.getInstance().getVisitedLocationsForUser(user_id);
		return new User(user_id, rs.getString("username"), rs.getString("password"), rs.getLong("profile_pic_id"),
				rs.getString("description"), followers, following, visitedLocations);
	}
	
	public void changePassword(User u, String newPassword) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"update users set password = ? where user_id = ?");
		ps.setString(1, newPassword); //hashing required
		ps.setLong(2, u.getUserId());
		ps.executeUpdate();
	}
	
	public void changeProfilePicId(User u, long profilePicId) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"update users set profile_pic_id = ? where user_id = ?");
		ps.setLong(1, profilePicId); 
		ps.setLong(2, u.getUserId());
		ps.executeUpdate();
	}
	
	public void changeDescription(User u, String description) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"update users set description = ? where user_id = ?");
		ps.setString(1, description); 
		ps.setLong(2, u.getUserId());
		ps.executeUpdate();
	}
	
	public void follow(User follower, User followed) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"insert into users_followers (follower_id, followed_id) value (?, ?);");
		ps.setLong(1,follower.getUserId()); 
		ps.setLong(2, followed.getUserId());
		ps.executeUpdate();
	}
	
	public void unfollow(User follower, User followed) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"delete from users_followers where follower_id = ? and followed_id = ?");
		ps.setLong(1,follower.getUserId()); 
		ps.setLong(2, followed.getUserId());
		ps.executeUpdate();
	}
	
	public HashSet<User> getFollowers(long followed_id) throws SQLException, UserException, VisitedLocationException {
		HashSet<User> followers = new HashSet<User>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"select follower_id from users_followers where followed_id = ?");
		ps.setLong(1, followed_id);
		ps.executeUpdate();
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			followers.add(this.getUserById(rs.getLong("follower_id")));
		}
		return followers;
	}
	
	public HashSet<User> getFollowing(long follower_id) throws SQLException, UserException, VisitedLocationException {
		HashSet<User> following = new HashSet<User>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"select followed_id from users_followers where follower_id = ?");
		ps.setLong(1, follower_id);
		ps.executeUpdate();
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			following.add(this.getUserById(rs.getLong("followed_id")));
		}
		return following;
	}
	
	public void addToWishlist(User u, Location l) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"insert into wishlists (user_id, location_id) value (?, ?);");
		ps.setLong(1,u.getUserId()); 
		ps.setLong(2, l.getLocationId());
		ps.executeUpdate();
	}
	
	public void removeFromWishlist(User u, Location l) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"delete from wishlists (user_id, location_id) value (?, ?);");
		ps.setLong(1,u.getUserId()); 
		ps.setLong(2, l.getLocationId());
		ps.executeUpdate();
	}
	
}