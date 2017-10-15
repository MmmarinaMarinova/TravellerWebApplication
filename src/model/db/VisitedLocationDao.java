package model.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import model.VisitedLocation;
import model.exceptions.VisitedLocationException;

public final class VisitedLocationDao { //used to operate with table 'visited_locations' from db
	private static VisitedLocationDao instance;

	private VisitedLocationDao() {
	}

	public static synchronized VisitedLocationDao getInstance() {
		if (instance == null) {
			instance = new VisitedLocationDao();
		}
		return instance;
	}
	
	public void insertVisitedLocation(VisitedLocation vl) throws SQLException {
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"insert into visited_locations (user_id, location_id, date_time) values (?, ?, ?);",
				Statement.RETURN_GENERATED_KEYS);
		ps.setLong(1, vl.getUserId());
		ps.setLong(2, vl.getLocationId());
		ps.setTimestamp(3, vl.getDatetime());
		ps.executeUpdate();
		ResultSet rs = ps.getGeneratedKeys();
		rs.next();
		vl.setId(rs.getLong(1));
	}
	
	public HashSet<VisitedLocation> getVisitedLocationsForUser(long user_id) throws SQLException, VisitedLocationException {
		HashSet<VisitedLocation> visitedLocations = new HashSet<VisitedLocation>();
		Connection con = DBManager.getInstance().getConnection();
		PreparedStatement ps = con.prepareStatement(
				"select id, location_id, date_time from visited_locations where user_id = ?;");
		ps.setLong(1, user_id);
		ResultSet rs = ps.executeQuery();
		while(rs.next()) {
			visitedLocations.add(new VisitedLocation(rs.getLong("id"), user_id, rs.getLong("location_id"), rs.getTimestamp("date_time")));
		}
		return visitedLocations;
	}
	
}