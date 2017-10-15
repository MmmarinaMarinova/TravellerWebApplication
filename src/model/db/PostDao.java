package model.db;
import model.Category;
import model.Location;
import model.Post;

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
}
