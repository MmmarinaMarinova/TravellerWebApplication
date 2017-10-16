package model.db;

import model.Multimedia;
import model.Post;
import model.exceptions.MultimediaException;

import java.sql.*;
import java.util.HashSet;

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
        ps.setLong(3,multimedia.getPost().getId());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        multimedia.setId(rs.getLong(1));
    }

    public void deleteMultimedia(Multimedia multimedia) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "delete from multimedia where multimedia_id= ? ;");
        ps.setLong(1, multimedia.getId());
        int affectedRows=ps.executeUpdate();
        if(affectedRows>0){
            //TODO show popup info here
        }
    }

    public HashSet<Multimedia> getAllMultimediaForPost(Post post) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "select multimedia_id from multimedia where post_id= ?  ;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Multimedia> multimedia=new HashSet<>();
        while (rs.next()){
            multimedia.add(new Multimedia(rs.getLong("multimedia_id"), rs.getString("file_dir"), rs.getBoolean("is_video"),post.getId()));
        }
        return multimedia;
    }

    public Multimedia getMultimediaById(long multimediaId) throws MultimediaException,SQLException {
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

    public HashSet<Multimedia> getMultimediaForPost(Post post) throws SQLException, MultimediaException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement("select multimedia_id from multimedia where post_id= ?;");
        ps.setLong(1, post.getId());
        ResultSet rs=ps.executeQuery();
        HashSet<Multimedia> multimedia=new HashSet<>();
        while (rs.next()){
            multimedia.add(MultimediaDao.getInstance().getMultimediaById(rs.getLong("multimedia_id")));
        }
        return multimedia;
    }

    public void addMultimediaToPost(Post post, Multimedia multimedia) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into multimedia(file_dir, is_video, post_id) values(?,?,?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1,multimedia.getUrl());
        ps.setBoolean(2,multimedia.isVideo());
        ps.setLong(3,post.getId());
        ps.executeUpdate();
        ResultSet rs=ps.getGeneratedKeys();
        multimedia.setId(rs.getLong(1));
    }

    public void addAllMultimediaToPost(Post post, HashSet<Multimedia> multimedia) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into multimedia(file_dir,is_video, post_id) value (?,?,?);");
        for (Multimedia m : multimedia) {
            ps.setString(1,m.getUrl());
            ps.setBoolean(2,m.isVideo());
            ps.setLong(3,post.getId());
            ps.addBatch();
        }
        ps.executeBatch();

    }
}
