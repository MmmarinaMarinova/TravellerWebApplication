package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import model.exceptions.CategoryException;
import model.exceptions.CommentException;
import model.exceptions.LocationException;
import model.exceptions.PostException;
import model.exceptions.UserException;

public class UserDao extends AbstractDao { // operates with the following tables: 'users', 'users_followers',
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
		try (PreparedStatement ps = this.getCon().prepareStatement(
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
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("select count(*) as count from users where username = ? and password = ?;");) {
			ps.setString(1, username);
			ps.setString(2, password); // hashing required
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("count") > 0;
		}
	}

	// ::::::::: check if username is taken :::::::::
	// * TESTED *
	public boolean existsUsername(String username) throws SQLException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("select count(*) as count from users where username = ?;");) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("count") > 0;
		}
	}

	// ::::::::: check if email is taken :::::::::
	// * TESTED *
	public boolean existsEmail(String email) throws SQLException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("select count(*) as count from users where email = ?;");) {
			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt("count") > 0;
		}
	}

	// ::::::::: loading user from db :::::::::
	// * TESTED *
	public User getUserByUsername(String username)
			throws SQLException, UserException, PostException, LocationException, CategoryException, CommentException {
		User fetched = null;
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select user_id, username, password, email, profile_pic_id, description from users where username = ?;");) {
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fetched = new User(rs.getLong("user_id"), username, rs.getString("password"), rs.getString("email"),
						rs.getLong("profile_pic_id"), rs.getString("description"));
				fetched.setProfilePic(MultimediaDao.getInstance().getMultimediaById(rs.getLong("profile_pic_id")));
			}
			UserDao.getInstance().setPosts(fetched);
			return fetched;
		}
	}

	// * TESTED *
	public User getUserById(long user_id) throws SQLException, UserException {
		User fetched = null;
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select username, password, email, profile_pic_id, description from users where user_id = ?;");) {
			ps.setLong(1, user_id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fetched = new User(user_id, rs.getString("username"), rs.getString("password"), rs.getString("email"),
						rs.getLong("profile_pic_id"), rs.getString("description"));
			}
			return fetched;
		}
	}

	// ::::::::: loading user data from db :::::::::
	// get followers
	public HashSet<User> getFollowers(User u) throws SQLException, UserException {
		HashSet<User> followers = new HashSet<User>();
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select u.user_id, u.username, u.password, u.email, u.profile_pic_id, u.description from users as u join users_followers as uf on(u.user_id = uf.follower_id) where uf.followed_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				followers.add(new User(rs.getLong("user_id"), rs.getString("username"), rs.getString("password"),
						rs.getString("email"), rs.getLong("profile_pic_id"), rs.getString("description")));
			}
		}
		return followers;
	}

	// get following
	public HashSet<User> getFollowing(User u) throws SQLException, UserException {
		HashSet<User> following = new HashSet<User>();
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select u.user_id, u.username, u.password, u.email, u.profile_pic_id, u.description from users as u join users_followers as uf on(u.user_id = uf.followed_id) where uf.follower_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				following.add(new User(rs.getLong("user_id"), rs.getString("username"), rs.getString("password"),
						rs.getString("email"), rs.getLong("profile_pic_id"), rs.getString("description")));
			}
		}
		return following;
	}

	// get visited locations
	public TreeMap<Timestamp, Location> getVisitedLocations(User u) throws SQLException, LocationException {
		TreeMap<Timestamp, Location> visitedLocations = new TreeMap<Timestamp, Location>();
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select vl.date_time, l.location_id, l.latitude, l.longtitude, l.description, l.location_name from locations as l join visited_locations as vl on(l.location_id = vl.location_id) where user_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				visitedLocations.put(rs.getTimestamp("date_time"),
						new Location(rs.getLong("location_id"), rs.getString("latitude"), rs.getString("longtitude"),
								rs.getString("description"), rs.getString("location_name")));
			}
		}
		return visitedLocations;
	}

	// get wishlist locations
	public HashSet<Location> getWishlistLocations(User u) throws SQLException, LocationException {
		HashSet<Location> wishlistLocations = new HashSet<Location>();
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select l.location_id, l.latitude, l.longtitude, l.description, l.location_name from locations as l join wishlists w on(l.location_id = w.location_id) where w.user_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				wishlistLocations.add(new Location(rs.getLong("location_id"), rs.getString("latitude"),
						rs.getString("longtitude"), rs.getString("description"), rs.getString("location_name")));
			}
		}
		return wishlistLocations;
	}

	// get posts
	public TreeSet<Post> getPosts(User u) throws SQLException, PostException, LocationException, CategoryException, UserException, CommentException {
		TreeSet<Post> posts = new TreeSet<Post>(); // posts should be compared by datetime by default
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select post_id, user_id, description, likes_count, dislikes_count, date_time, location_id from posts where user_id = ?;");) {
			ps.setLong(1, u.getUserId());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Post post = new Post(rs.getLong("post_id"), rs.getString("description"), rs.getInt("likes_count"),
						rs.getInt("dislikes_count"), rs.getTimestamp("date_time"));
				post.setUser(u);
				post.setLocation(LocationDao.getInstance().getLocationByPost(post));
				post.setCategories(CategoryDao.getInstance().getCategoriesForPost(post));
				post.setMultimedia(MultimediaDao.getInstance().getAllMultimediaForPost(post));
				post.setTaggedPeople(UserDao.getInstance().getAllTaggedUsersForPost(post));
				post.setComments(CommentDao.getInstance().getCommentsForPost(post));
				posts.add(post);
			}
		}
		return posts;
	}

	private HashSet<User> getAllTaggedUsersForPost(Post post) throws SQLException, UserException {
		PreparedStatement ps = this.getCon().prepareStatement(
				"select u.user_id, u.username, u.password, u.email, u.profile_pic_id, u.description from users as u join tagged_users as tu on(u.user_id = tu.user_id) where post_id = ?;");
		ps.setLong(1, post.getId());
		ResultSet rs = ps.executeQuery();
		HashSet<User> taggedUsers = new HashSet<User>();
		while (rs.next()) {
			taggedUsers.add(new User(rs.getLong("user_id"), rs.getString("username"), rs.getString("password"),
					rs.getString("email"), rs.getLong("profile_pic_id"), rs.getString("description")));
		}
		return taggedUsers;
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
	public void setVisitedLocations(User u) throws SQLException, UserException, LocationException {
		u.setVisitedLocations(UserDao.getInstance().getVisitedLocations(u));
	}

	// set wishlit
	public void setWishlistLocations(User u) throws SQLException, UserException, LocationException {
		u.setWishlist(UserDao.getInstance().getWishlistLocations(u));
	}

	// set posts
	public void setPosts(User u)
			throws SQLException, UserException, PostException, LocationException, CategoryException, CommentException {
		u.setPosts(UserDao.getInstance().getPosts(u));
	}

	// ::::::::: methods for updating user data :::::::::
	// * TESTED *
	public void changePassword(User u, String newPassword) throws SQLException, UserException {
		if (u.setPassword(newPassword)) {
			try (PreparedStatement ps = this.getCon()
					.prepareStatement("update users set password = ? where user_id = ?;");) {
				ps.setString(1, u.getPassword());
				ps.setLong(2, u.getUserId());
				ps.executeUpdate();
			}
		}
	}

	// * TESTED *
	public void changeEmail(User u, String newEmail) throws SQLException, UserException {
		if (u.setEmail(newEmail)) {
			try (PreparedStatement ps = this.getCon()
					.prepareStatement("update users set email = ? where user_id = ?;");) {
				ps.setString(1, u.getEmail());
				ps.setLong(2, u.getUserId());
				ps.executeUpdate();
			}
		}
	}

	// !!! TO BE DISCUSSED !!!
	public void changeProfilePicId(User u, Multimedia profilePic) throws SQLException, UserException {
		if (u.setProfilePicId(profilePic.getId())) {
			try (PreparedStatement ps = this.getCon()
					.prepareStatement("update users set profile_pic_id = ? where user_id = ?;");) {
				ps.setLong(1, u.getProfilePicId());
				ps.setLong(2, u.getUserId());
				ps.executeUpdate();
			}
		}
	}

	// * TESTED *
	public void changeDescription(User u, String description) throws SQLException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("update users set description = ? where user_id = ?;");) {
			u.setDescription(description);
			ps.setString(1, u.getDescription());
			ps.setLong(2, u.getUserId());
			ps.executeUpdate();
		}
	}

	// ::::::::: methods for follow/unfollow operations :::::::::
	public void follow(User follower, User followed) throws SQLException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("insert into users_followers (follower_id, followed_id) value (?, ?);");) {
			ps.setLong(1, follower.getUserId());
			ps.setLong(2, followed.getUserId());
			ps.executeUpdate();
			follower.follow(followed);
		}
	}

	public void unfollow(User follower, User followed) throws SQLException {
		try (PreparedStatement ps = this.getCon()
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
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"insert into visited_locations (user_id, location_id, date_time) value (?, ?, ?);");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getId());
			ps.setTimestamp(3, t);
		}
	}

	public void removeFromVisitedLocations(User u, Location l, Timestamp t) throws SQLException {
		u.removeVisitedLocation(t, l);
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"delete from visited_locations where user_id = ? and location_id = ? and date_time = ?;");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getId());
			ps.setTimestamp(3, t);
		}
	}

	// ::::::::: add/remove from wishlist :::::::::
	public void addToWishlist(User u, Location l) throws SQLException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("insert into wishlists (user_id, location_id) value (?, ?);");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getId());
			ps.executeUpdate();
			u.addToWishlist(l);
		}
	}

	public void removeFromWishlist(User u, Location l) throws SQLException {
		try (PreparedStatement ps = this.getCon()
				.prepareStatement("delete from wishlists (user_id, location_id) value (?, ?);");) {
			ps.setLong(1, u.getUserId());
			ps.setLong(2, l.getId());
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