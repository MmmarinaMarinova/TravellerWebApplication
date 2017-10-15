package model.db;

import model.Location;

import java.sql.*;

/**
 * Created by Marina on 15.10.2017 г..
 */
public class LocationDao {
    private static LocationDao instance;

    private LocationDao() {}

    public static synchronized LocationDao getInstance(){
        if(instance!=null){
            instance=new LocationDao();
        }
        return instance;
    }

    public void insertLocation(Location location) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "insert into locations( latitude,longtitude, description, location_name) value (?,?,?,?);",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, location.getLatitude());
        ps.setString(2,location.getLongtitude());
        ps.setString(3,location.getDescription());
        ps.setString(4,location.getLocationName());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
        location.setId(rs.getLong(1));
    }

    public Location getLocation(long id) throws SQLException {
        Connection con = DBManager.getInstance().getConnection();
        PreparedStatement ps = con.prepareStatement(
                "SELECT latitude, longtitude, description, location_name FROM locations where location_id=?;",
                Statement.RETURN_GENERATED_KEYS);
        ps.setLong(1, id);
        ResultSet rs=ps.executeQuery();
        Location location=new Location(id,rs.getString("latitude"),
                rs.getString("longtitude"), rs.getString("description"), rs.getString("location_name"));
        return location;
    }
}
