package model.db;

import model.Multimedia;
import model.Post;

import java.sql.*;

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
}
