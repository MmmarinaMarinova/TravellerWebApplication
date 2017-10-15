package model.db;
import model.*;
import model.exceptions.UserException;
import model.exceptions.VisitedLocationException;

import java.sql.*;
import java.util.ArrayList;
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
        ps.setLong(1, post.getUserId());
        ps.setString(2,post.getDescription());
        ps.setTimestamp(3,post.getDateTime());
        ps.setLong(4,post.getLocation().getId());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        post.setId(rs.getLong(1));
        /*for (Long id:post.getCategoriesIds()) {
            this.addCategoryToPost(post.getId(), id);
        }
        for (Multimedia: post.getMultimediaIds()) {
            this.addMultimediaToPost(post, id);
        }
        for (User user:post.getTaggedPeople()) {
            this.tagUser(post,user);
        }*/
    }

    private void tagUser(Post post, User user) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into tagged_users(post_id, user_id) values(?,?);");
        ps.setLong(1,post.getId());
        ps.setLong(2,user.getUserId());
        ps.executeUpdate();
    }

    private void addMultimediaToPost(long postId, Multimedia multimedia) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into multimedia(file_dir, is_video, post_id) values(?,?,?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,multimedia.getUrl());
        ps.setBoolean(2,multimedia.isVideo());
        ps.setLong(3,postId);
        ps.executeUpdate();
        ResultSet rs=ps.getGeneratedKeys();
        multimedia.setId(rs.getLong(1));
    }

    private void addCategoryToPost(long postId, long categoryId) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into posts_categories(post_id, category_id) values(?,?);");
        ps.setLong(1, postId);
        ps.setLong(2,categoryId);
        ps.executeUpdate();
    }

    public void deletePost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "delete from posts where posts.post_id=?;");
        ps.setLong(1, post.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO PUT SOME POPUP WITH INFO
        }
    }

    public void updateLocation(Post post, Location newLocation) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "update posts set location_id= ?  where posts.post_id= ?;");
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
                "update posts set likes_count= ?  where posts.post_id= ?;");
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

    public ArrayList<Long> getCategoriesIdsForPost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select category_id from posts_categories where post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        ArrayList<Long> categories=new ArrayList<>();
        while (rs.next()){
            categories.add(rs.getLong("category_id"));
        }
        return categories;
    }

    public void getPostsByUserId(long userId) throws SQLException, VisitedLocationException, UserException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select * from posts where user_id= ?;");
        ps.setLong(1, userId);
        ResultSet rs = ps.executeQuery();
        HashSet<Post> posts=new HashSet<Post>();
        while(rs.next()){
            Post post=new Post(
                    rs.getLong("post_id"), rs.getString("description"),
                    rs.getInt("likes_count"), rs.getInt("dislikes_count"),
                    rs.getTimestamp("date_time"));
            post.setUserId(userId);
            post.setCategoriesIds(this.getCategoriesIdsForPost(post));
            post.setLocation(this.getLocationByUserId(post.getId()));
            post.setMultimediaIds(this.getMultimediaIdsForPost(post));
            posts.add(post);

        }
    }

    private ArrayList<Long> getMultimediaIdsForPost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select multimedia_id from multimedia where post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        ArrayList<Long> multimediaIds=new ArrayList<>();
        while (rs.next()){
            multimediaIds.add(rs.getLong("multimedia_id"));
        }
        return multimediaIds;
    }

    private Location getLocationByUserId(long postId) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select * from locations where post_id=?;");
        ps.setLong(1,postId);
        ResultSet rs=ps.executeQuery();
        rs.next();
        Location location=new Location(rs.getLong("location_id"),
                rs.getString("latitude"), rs.getString("longtitude"),
                rs.getString("description"), rs.getString("location_name"));
        return location;
    }
}
