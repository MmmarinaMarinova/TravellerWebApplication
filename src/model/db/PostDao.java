package model.db;
import model.*;
import model.exceptions.UserException;
import model.exceptions.VisitedLocationException;

import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class PostDao {
    private static PostDao instance;

    private PostDao() {}

    public static synchronized PostDao getInstance(){
        if(instance!=null){
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
        for (Category cat:post.getCategories()) {
            this.addCategoryToPost(post, cat);
        }
        for (Multimedia m: post.getMultimedia()) {
            this.addMultimediaToPost(post, m);
        }
        for (User user:post.getTaggedPeople()) {
            this.tagUser(post,user);
        }
    }

    private void tagUser(Post post, User user) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into tagged_users(post_id, user_id) values(?,?);");
        ps.setLong(1,post.getId());
        ps.setLong(2,user.getUserId());
        ps.executeUpdate();
    }

    private void addMultimediaToPost(Post post, Multimedia m) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into multimedia(file_dir, is_video, post_id) values(?,?,?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,m.getUrl());
        ps.setBoolean(2,m.isVideo());
        ps.setLong(3,post.getId());
        ps.executeUpdate();
        ResultSet rs=ps.getGeneratedKeys();
        m.setId(rs.getLong(1));
    }

    private void addCategoryToPost(Post post, Category cat) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into posts_categories(post_id, category_id) values(?,?);");
        ps.setLong(1, post.getId());
        ps.setLong(2,cat.getId());
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

    public HashSet<Category> getCategoriesForPost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select cat.category_id,cat.category_name from categories as cat join posts_categories as pc on pc.category_id=cat.category_id where pc.post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Category> categories=new HashSet<>();
        while (rs.next()){
            categories.add(new Category(rs.getLong("cat.category_id"), rs.getString("category_name")));
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
            post.setUser(UserDao.getUserById(userId));
            post.setCategories(this.getCategoriesForPost(post));
            post.setLocation(this.getLocationByUserId(post.getId()));
            post.setMultimedia(this.getMultimediaForPost(post));
            posts.add(post);

        }
    }

    private HashSet<Multimedia> getMultimediaForPost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select * from multimedia where post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Multimedia> multimedia=new HashSet<>();
        while (rs.next()){
            multimedia.add(new Multimedia(rs.getLong("multimedia_id"),
                    rs.getString("file_dir"), rs.getBoolean("is_video"), post));
        }
        return multimedia;
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
