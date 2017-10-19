package model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.TreeSet;

import model.Comment;
import model.exceptions.CommentException;

public final class CommentDao extends AbstractDao{ //used to operate with table 'comments' from db
	private static CommentDao instance;

	private CommentDao() {
	}

	public static synchronized CommentDao getInstance() {
		if (instance == null) {
			instance = new CommentDao();
		}
		return instance;
	}

	public void insertComment(Comment c) throws SQLException {
		PreparedStatement ps = this.getCon().prepareStatement(
				"insert into comments (content, post_id, user_id, date_time) values (?, ?, ?, ?);",
				Statement.RETURN_GENERATED_KEYS);
		ps.setString(1, c.getContent());
		ps.setLong(2, c.getPostId());
		ps.setLong(3, c.getUserId());
		ps.setTimestamp(4, c.getDatetime());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		c.setId(rs.getLong(1));
	}
	
	public void deleteComment(Comment c) throws SQLException {
		PreparedStatement ps = this.getCon().prepareStatement(
				"delete from comments where id = ? and content = ? and post_id = ? and user_id = ? and date_time = ?;",
				Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, c.getId());
		ps.setString(2, c.getContent());
		ps.setLong(3, c.getPostId());
		ps.setLong(4, c.getUserId());
		ps.setTimestamp(5, c.getDatetime());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		c.setId(rs.getLong(1));
	}

	public Comment getComment(String content, int likesCount, int dislikesCount, long postId, long userId,
			Timestamp datetime) throws SQLException, CommentException {
		PreparedStatement ps = this.getCon().prepareStatement(
				" select id, content, likes_counter, dislikes_counter, post_id, user_id, date_time from comments where content = ? and likes_counter = ? and dislikes_counter = ? and post_id = ? and user_id = ? and date_time = ?;");
		ps.setString(1, content);
		ps.setInt(2, likesCount);
		ps.setInt(3, dislikesCount);
		ps.setLong(4, postId);
		ps.setLong(5, userId);
		ps.setTimestamp(6, datetime);
		ResultSet rs = ps.executeQuery();
		rs.next();
		return new Comment(rs.getLong("id"), rs.getString("content"), rs.getInt("likes_counter"),
				rs.getInt("dislikes_counter"), postId, rs.getLong("user_id"), rs.getTimestamp("date_time"));
	}
	
	public void incrementLikes(Comment c) throws SQLException {
		PreparedStatement ps = this.getCon().prepareStatement(
				"update comments set likes_counter = ? where id = ?;");
		ps.setInt(1, c.getLikesCount()+1);
		ps.setLong(2, c.getId());
		ps.executeUpdate();
	}

	public void incrementDislikes(Comment c) throws SQLException {
		PreparedStatement ps = this.getCon().prepareStatement(
				"update comments set dislikes_counter = ? where id = ?;");
		ps.setInt(1, c.getDislikesCount()+1);
		ps.setLong(2, c.getId());
		ps.executeUpdate();
	}
	
	public TreeSet<Comment> getCommentsForPost(long postId) throws SQLException, CommentException {
		TreeSet<Comment> comments = new TreeSet<>((c1, c2) -> c1.getDatetime().compareTo(c2.getDatetime()));
		PreparedStatement ps = this.getCon().prepareStatement(
				"select id, content, likes_counter, dislikes_counter, post_id, user_id, date_time from comments where post_id = ?;");
		ps.setLong(1, postId);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			comments.add(new Comment(rs.getLong("id"), rs.getString("content"), rs.getInt("likes_counter"),
					rs.getInt("dislikes_counter"), postId, rs.getLong("user_id"), rs.getTimestamp("date_time")));
		}
		return comments;
	}

}