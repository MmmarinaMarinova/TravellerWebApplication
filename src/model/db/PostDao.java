package model.db;
import model.*;
import model.exceptions.*;

import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class PostDao {
    private static PostDao instance;

    private PostDao() {}

    public static synchronized PostDao getInstance(){
        if(instance==null){
            instance=new PostDao();
        }
        return instance;
    }

    public void insertNewPost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into posts(user_id, description, date_time, location_id) value (?,?,?,?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, post.getUser().getUserId());
        ps.setString(2,post.getDescription());
        ps.setTimestamp(3,post.getDateTime());
        ps.setLong(4,post.getLocation().getId());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        post.setId(rs.getLong(1));
        CategoryDao.getInstance().addAllCategoriesToPost(post.getCategories());
        MultimediaDao.getInstance().addAllMultimediaToPost(post, post.getMultimedia());
        for (User user : post.getTaggedPeople()) {
            this.tagUser(post,user);
        }
        this.tagAllUsers(post, post.getTaggedPeople());
    }

    private void tagAllUsers(Post post, HashSet<User> taggedPeople) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into tagged_users(post_id, user_id) values(?,?);");
        for (User user : taggedPeople) {
            ps.setLong(1,post.getId());
            ps.setLong(2,user.getUserId());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void tagUser(Post post, User user) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into tagged_users(post_id, user_id) values(?,?);");
        ps.setLong(1,post.getId());
        ps.setLong(2,user.getUserId());
        ps.executeUpdate();
    }

    private void addCategoryToPost(Post post, Category category) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into posts_categories(post_id, category_id) values(?,?);");
        ps.setLong(1, post.getId());
        ps.setLong(2,category.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO show popup info
        }
    }

    public void deletePost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "delete from posts where post_id=?;");
        ps.setLong(1, post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    public void updateLocation(Post post, Location newLocation) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "update posts set location_id= ?  where post_id= ?;");
        ps.setLong(1, newLocation.getId());
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    public void updateLikes(Post post,String choice) throws SQLException {
        int count=0;
        //choose if incrementing or decrementing and make likes counter one more or less
        count = choice.equals("increment") ? 1 : -1;
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "update posts set likes_count= ?  where post_id= ?;");
        ps.setInt(1, post.getLikesCount()+count);
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    public void updateDislikes(Post post,String choice) throws SQLException {
        int count=0;
        //choose if incrementing or decrementing and make likes counter one more or less
        count = choice.equals("increment") ? 1 : -1;
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "update posts set dislikes_count= ?  where posts.post_id= ?;");
        ps.setInt(1, post.getDislikesCount()+count);
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    public void updateDescription(Post post, String newDescription) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "update posts set description= ?  where posts.post_id= ?;");
        ps.setString(1, newDescription);
        ps.setLong(2,post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    public void getPostsForUser(User user) throws SQLException, VisitedLocationException, UserException, PostException, CategoryException, MultimediaException, LocationException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select * from posts where user_id= ?;");
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
            post.setLocation(LocationDao.getInstance().getLocationByPost(post));
            post.setMultimedia(MultimediaDao.getInstance().getMultimediaForPost(post));
            posts.add(post);
        }
    }




}
