package model;

import model.AbstractDao;
import model.Location;
import model.Post;
import model.exceptions.LocationException;

import java.sql.*;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class LocationDao extends AbstractDao {
    private static LocationDao instance;

    private LocationDao() {}

    public static synchronized LocationDao getInstance(){
        if(instance==null){
            instance=new LocationDao();
        }
        return instance;
    }

    //tested
    public void insertLocation(Location location) throws SQLException, LocationException {
        try{
            PreparedStatement ps= this.getCon().prepareStatement(
                    "insert into locations( latitude,longtitude, description, location_name) values (?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, location.getLatitude());
            ps.setString(2,location.getLongtitude());
            ps.setString(3,location.getDescription());
            ps.setString(4,location.getLocationName());
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            location.setId(rs.getLong(1));
        }catch (SQLException e){
            throw new LocationException("Location could not be added. Reason: "+e.getMessage());
        }
    }

    //tested
    public Location getLocationById(long id) throws SQLException, LocationException {
        Location location;
        try {
            PreparedStatement ps = this.getCon().prepareStatement(
                    "SELECT latitude, longtitude, description, location_name FROM locations where location_id=?;");
            ps.setLong(1, id);
            ResultSet rs=ps.executeQuery();
            rs.next();
            location=new Location(id,rs.getString("latitude"),
                    rs.getString("longtitude"), rs.getString("description"), rs.getString("location_name"));
        }catch (SQLException e){
            throw new LocationException("Location could not be loaded. Reason: "+e.getMessage());
        }
        return location;
    }

    //tested
    public Location getLocationByPost(Post post) throws SQLException, LocationException {
        //Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = this.getCon().prepareStatement("select l.location_id, l.latitude, l.longtitude, l.description, l.location_name" +
                " from locations as l join posts " +
                "on posts.location_id=l.location_id" +
                " where post_id=?;");
        ps.setLong(1,post.getId());
        ResultSet rs=ps.executeQuery();
        rs.next();
        Location location=new Location(rs.getLong("l.location_id"),
                rs.getString("l.latitude"), rs.getString("l.longtitude"),
                rs.getString("l.description"), rs.getString("l.location_name"));
        return location;
    }
}
