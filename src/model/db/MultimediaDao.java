package model.db;

import model.Multimedia;
import model.Post;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class MultimediaDao {
    private static MultimediaDao instance;

    private MultimediaDao(){}

    public static MultimediaDao getInstance(){
        if(instance==null){
            instance=new MultimediaDao();
        }
        return instance;
    }

    public void insertMultimedia(Post post, Multimedia multimedia) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into multimedia(file_dir,is_video, post_id) value (?,?,?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, multimedia.getUrl());
        ps.setBoolean(2,multimedia.isVideo());
        ps.setLong(3,multimedia.getPostId());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        multimedia.setId(rs.getLong(1));
    }

    public void deleteMultimedia(long postId) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "delete from multimedia where post_id= ? ;");
        ps.setLong(1, postId);
        ps.executeUpdate();
    }

    public ArrayList<Long> getMultimediaIds(long postId) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "select multimedia_id from multimedia where post_id= ?  ;");
        ps.setLong(1, postId);
        ResultSet rs=ps.executeQuery();
        ArrayList<Long> multimediaIds=new ArrayList<>();
        while (rs.next()){
            multimediaIds.add(rs.getLong("multimedia_id"));
        }
        return multimediaIds;
    }

    public Multimedia getMultimediaById(long multimediaId) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "select * from multimedia where multimedia_id= ?   ;");
        ps.setLong(1,multimediaId );
        ResultSet rs=ps.executeQuery();
        rs.next();
        Multimedia multimedia=new Multimedia(multimediaId,
                rs.getString("file_dir"), rs.getBoolean("is_video"),rs.getLong("post_id"));
        return multimedia;
    }
}
