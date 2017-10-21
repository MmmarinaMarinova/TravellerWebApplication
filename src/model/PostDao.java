package model;
import model.exceptions.*;

import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class PostDao extends AbstractDao{
    private static PostDao instance;

    private PostDao() {}

    public static synchronized PostDao getInstance(){
        if(instance==null){
            instance=new PostDao();
        }
        return instance;
    }

    //tested
    public void insertNewPost(Post post) throws SQLException {
        this.getCon().setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = this.getCon().prepareStatement(
                    "insert into posts(user_id, description, date_time) value (?,?,now());",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, post.getUser().getUserId());
            ps.setString(2,post.getDescription());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            post.setId(rs.getLong(1));
            CategoryDao.getInstance().addAllCategoriesToPost(post, post.getCategories()); //not sure if it is correct this way
            //MultimediaDao.getInstance().addAllMultimediaToPost(post, post.getMultimedia());
            this.tagAllUsers(post, post.getTaggedPeople());
            this.getCon().commit();
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
        }

    }

    //tested
    private void tagAllUsers(Post post, HashSet<User> taggedPeople) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "insert into tagged_users(post_id, user_id) values(?,?);");
        for (User user : taggedPeople) {
            ps.setLong(1,post.getId());
            ps.setLong(2,user.getUserId());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    //tested
    public void tagUser(Post post, User user) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "insert into tagged_users(post_id, user_id) values(?,?);");
        ps.setLong(1,post.getId());
        ps.setLong(2,user.getUserId());
        ps.executeUpdate();
    }

    //tested
    public void addCategoryToPost(Post post, Category category) throws SQLException {
        this.getCon().setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = this.getCon().prepareStatement(
                    "insert into posts_categories(post_id, category_id) values(?,?);");
            ps.setLong(1, post.getId());
            ps.setLong(2,category.getId());
            ps.executeUpdate();
            this.getCon().commit();
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
        }
    }

    //tested
    public void deletePost(Post post) throws SQLException {
        this.getCon().setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = this.getCon().prepareStatement(
                    "delete from posts where post_id=?;");
            ps.setLong(1, post.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
        }
    }

    //tested
    public void updateLocation(Post post, Location newLocation) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "update posts set location_id= ?  where post_id= ?;");
        ps.setLong(1, newLocation.getId());
        ps.setLong(2,post.getId());
        ps.executeUpdate();
    }

    //tested
    public void incrementLikes(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "update posts set likes_count= ?  where post_id= ?;");
        ps.setInt(1, post.getLikesCount()+1);
        ps.setLong(2,post.getId());
        ps.executeUpdate();
    }

    //tested
    public void decrementLikes(Post post) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "update posts set likes_count= ?  where post_id= ?;");
        ps.setInt(1, post.getLikesCount()-1);
        ps.setLong(2,post.getId());
        ps.executeUpdate();
    }


    //tested
    public void incrementDislikes(Post post) throws SQLException {
        //TODO dislikes should never become less than 0
        PreparedStatement ps = this.getCon().prepareStatement(
                "update posts set dislikes_count= ?  where posts.post_id= ?;");
        ps.setInt(1, post.getDislikesCount()+1);
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    //tested
    public void decrementDislikes(Post post) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "update posts set dislikes_count= ?  where posts.post_id= ?;");
        ps.setInt(1, post.getDislikesCount()-1);
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    //tested
    public void updateDescription(Post post, String newDescription) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "update posts set description= ?  where posts.post_id= ?;");
        ps.setString(1, newDescription);
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    //tested
    public HashSet<Post> getPostsForUser(User user) throws SQLException, VisitedLocationException, UserException, PostException, CategoryException, MultimediaException, LocationException {
        PreparedStatement ps = this.getCon().prepareStatement("select post_id, description, " +
                "likes_count, dislikes_count, date_time from posts where user_id= ?;");
        ps.setLong(1, user.getUserId());
        ResultSet rs = ps.executeQuery();
        HashSet<Post> posts=new HashSet<Post>();
        while(rs.next()){
            Post post=new Post(rs.getLong("post_id"),
                    rs.getString("description"), rs.getInt("likes_count"),
                    rs.getInt("dislikes_count"),rs.getTimestamp("date_time"));
            post.setUser(user);
            post.setLocation(LocationDao.getInstance().getLocationByPost(post));
            post.setCategories(CategoryDao.getInstance().getCategoriesForPost(post));
            post.setMultimedia(MultimediaDao.getInstance().getAllMultimediaForPost(post));
            posts.add(post);
        }
        return posts;
    }


    //tested
    public Post getPostById(long post_id) throws SQLException, PostException {
        PreparedStatement ps = this.getCon().prepareStatement("select description, likes_count, " +
                "dislikes_count, date_time from posts where post_id = ? ;");
        ps.setLong(1, post_id);
        ResultSet rs=ps.executeQuery();
        rs.next();
        Post post=new Post(post_id, rs.getString("description"),rs.getInt("likes_count"),rs.getInt("dislikes_count"),
                rs.getTimestamp("date_time"));
        return post;
    }

    public void addComment(Post postById, Comment c) throws SQLException {
        //TODO INSERT IN COLLECTION
    }

    public void deleteComment(Post postById, Comment c) throws SQLException {
        //TODO DELETE FROM COLLECTION

    }
    
}
