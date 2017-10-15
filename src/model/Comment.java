package model;

import java.sql.Timestamp;

import model.exceptions.CommentException;

public final class Comment {
	// object data
		private long id = 0;
		private String content = null;
		private int likesCount = 0;
		private int dislikesCount = 0;
		private long postId = 0;
		private long userId = 0;
		private Timestamp datetime = null;
		// constants
		private static final int MAX_CONTENT_LENGTH = 500;
		
		// constructor to be used when posting a new comment
		public Comment(String content, long postId, long userId, Timestamp datetime) throws CommentException {
			this.setContent(content);
			this.postId = postId;
			this.userId = userId;
			this.setDatetime(datetime);
		}

		// constructor to be used when loading an existing comment from db
		public Comment(long id, String content, int likesCount, int dislikesCount, long postId, long userId, Timestamp datetime) throws CommentException {
			this(content, postId, userId, datetime);
			this.setId(userId);
			this.setLikesCount(likesCount);
			this.setDislikesCount(dislikesCount);	
		}

		//accessors
		public long getId() {
			return this.id;
		}
		
		public String getContent() {
			return this.content;
		}

		public int getLikesCount() {
			return this.likesCount;
		}

		public int getDislikesCount() {
			return this.dislikesCount;
		}

		public long getPostId() {
			return this.postId;
		}

		public long getUserId() {
			return this.userId;
		}

		public Timestamp getDatetime() {
			return this.datetime;
		}

		//mutators
		public void setId(long id) {
			this.id = id;
		}
		
		private void setContent(String content) throws CommentException {
			if(content!=null) {
				if(content.length()<=MAX_CONTENT_LENGTH) {
				this.content = content;
				} else {
					throw new CommentException("Comment too long!");
				}
			} else {
				throw new CommentException("Invalid comment content!");
			}
		}

		public void setLikesCount(int likesCount) throws CommentException {
			if(likesCount>=0) {
				this.likesCount = likesCount;
			} else {
				throw new CommentException("Invalid number of likes!");
			}
		}
		
		public void setDislikesCount(int dislikesCount) throws CommentException {
			if(dislikesCount>=0) {
				this.dislikesCount = dislikesCount;
			} else {
				throw new CommentException("Invalid number of dislikes!");
			}
		}
		
		private void setDatetime(Timestamp datetime) throws CommentException {
			if(datetime!=null) {
				this.datetime = datetime;
			} else {
				throw new CommentException("Invalid date/time!");
			}
		}
		
}