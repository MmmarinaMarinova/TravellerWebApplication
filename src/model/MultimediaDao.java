package model;

import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class MultimediaDao extends AbstractDao {
    private static MultimediaDao instance;

    private MultimediaDao(){}

    public static MultimediaDao getInstance(){
        if(instance==null){
            instance=new MultimediaDao();
        }
        return instance;
    }

    //tested
    public Multimedia insertMultimedia(Post post, Multimedia multimedia) throws SQLException {
        try {
            this.getCon().setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("oops");
        }
        PreparedStatement ps = null;
        try {
            ps = this.getCon().prepareStatement(
                    "insert into multimedia(file_url,is_video, post_id) value (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, multimedia.getUrl());
            ps.setBoolean(2,multimedia.isVideo());
            ps.setLong(3,post.getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            multimedia.setId(rs.getLong(1));
            this.getCon().commit();
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
        }

        return multimedia;
    }


    //TODO not working
    public void deleteMultimedia(Multimedia multimedia) throws SQLException {
        this.getCon().setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            ps = this.getCon().prepareStatement(
                    "delete from multimedia where multimedia_id= ? ;");
            ps.setLong(1, multimedia.getId());
            this.getCon().commit();
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
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
            multimedia.add(new Multimedia(rs.getLong("multimedia_id"), rs.getString("file_dir"), rs.getBoolean("is_video"),post));
        }
        return multimedia;
    }

    /*public Multimedia getMultimediaById(long multimediaId) throws MultimediaException, SQLException, UserException, PostException {
        PreparedStatement ps = this.getCon().prepareStatement(
                "select file_dir, is_video, post_id from multimedia where multimedia_id= ?   ;");
        ps.setLong(1,multimediaId );
        ResultSet rs=ps.executeQuery();
        rs.next();
        Multimedia multimedia=new Multimedia(multimediaId,
                rs.getString("file_dir"), rs.getBoolean("is_video"),
                PostDao.getInstance().getPostById(rs.getLong("post_id")));
        return multimedia;
    }*/



    public void addMultimediaToPost(Post post, Multimedia multimedia) throws SQLException {
        PreparedStatement ps = this.getCon().prepareStatement(
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
        this.getCon().setAutoCommit(false);
        PreparedStatement ps = null;
        try {
            for (Multimedia m : multimedia) {
                //TODO SET IDS OF ALL  MULTIMEDIA
                ps = this.getCon().prepareStatement(
                        "insert into multimedia(file_dir,is_video, post_id) values (?,?,?);", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,m.getUrl());
                ps.setBoolean(2,m.isVideo());
                ps.setLong(3,post.getId());
                ps.executeUpdate();
                ResultSet resultSet=ps.getGeneratedKeys();
                m.setId(resultSet.getLong(1));
                this.getCon().commit();
            }
        } catch (SQLException e) {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
            this.getCon().close();
            throw e;
        }


    }
}
