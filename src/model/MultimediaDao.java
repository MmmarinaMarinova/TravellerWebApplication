﻿package model;

import model.exceptions.MultimediaException;
import model.exceptions.PostException;

import java.sql.*;
import java.util.HashSet;

/**
 * Created by Marina on 15.10.2017 Рі..
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
    public void insertMultimedia(Post post, Multimedia multimedia) throws SQLException, MultimediaException {
        try {
            PreparedStatement ps = this.getCon().prepareStatement(
                    "insert into multimedia(file_url,is_video, post_id) value (?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, multimedia.getUrl());
            ps.setBoolean(2,multimedia.isVideo());
            ps.setLong(3,post.getId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            multimedia.setId(rs.getLong(1));
        } catch (SQLException e) {
            throw new MultimediaException("Multimedia could not be inserted. Reason: "+e.getMessage());
        }
    }


    //TODO not working
    public void deleteMultimedia(Multimedia multimedia) throws SQLException, PostException {
        try {
            Connection connection=this.getCon();
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(
                    "delete from multimedia where multimedia_id= ? ;");
            ps.setLong(1, multimedia.getId());
            ps.executeUpdate();
            this.deleteMultimediaFromPost(multimedia);
            this.getCon().commit();
        } catch (SQLException e) {
            throw new PostException("Error deleting multimedia. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }

    public void deleteMultimediaFromPost(Multimedia multimedia) throws SQLException, PostException {
        PostDao.getInstance().getPostById(multimedia.getPost().getId()).deleteMultimedia(multimedia);
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



    public void addMultimediaToPost(Post post, Multimedia multimedia) throws SQLException, MultimediaException {
        try{
            PreparedStatement ps = this.getCon().prepareStatement(
                    "insert into multimedia(file_dir, is_video, post_id) values(?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,multimedia.getUrl());
            ps.setBoolean(2,multimedia.isVideo());
            ps.setLong(3,post.getId());
            ps.executeUpdate();
            ResultSet rs=ps.getGeneratedKeys();
            multimedia.setId(rs.getLong(1));
        }catch (SQLException e){
            throw new MultimediaException("multimedia could not be put to post. Reason: "+e.getMessage());
        }

    }

    public void addAllMultimediaToPost(Post post, HashSet<Multimedia> multimedia) throws SQLException, MultimediaException {
        PreparedStatement ps = null;
        try {
            for (Multimedia m : multimedia) {
                this.getCon().setAutoCommit(false);
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
            throw new MultimediaException("Multimedia could not be inserted. Reason: "+e.getMessage());
        }finally {
            this.getCon().rollback();
            this.getCon().setAutoCommit(true);
        }
    }
    
    public Multimedia getMultimediaById(long id) throws SQLException, PostException {
		Multimedia fetched = null;
		try (PreparedStatement ps = this.getCon().prepareStatement(
				"select file_url, is_video, post_id from multimedia where multimedia_id = ?;");) {
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				fetched = new Multimedia(id, rs.getString("file_url"), rs.getBoolean("is_video"), PostDao.getInstance().getPostById(rs.getLong("post_id")));
			}
			return fetched;
		}
	}
    
}