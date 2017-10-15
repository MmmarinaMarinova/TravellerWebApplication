package Model.DBManagement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBManager {

	private static DBManager instance;
	private Connection con;
	
	private DBManager(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			//TODO
			//System.out.println("Driver not found or failed to load. Check your libraries");
		}
		
		//to be initialized with proper data:
		final String DB_IP = "";
		final String DB_PORT = "3306";
		final String DB_DBNAME = "";
		final String DB_USER = "";
		final String DB_PASS = "";
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + DB_IP + ":" + DB_PORT + "/" + DB_DBNAME, DB_USER, DB_PASS);
		} catch (SQLException e) {
			//TODO
		}
		
	}
	
	public static synchronized DBManager getInstance(){
		if(instance == null){
			instance = new DBManager();
		}
		return instance;
	}
	
	public Connection getConnection(){
		return this.con;
	}
	
	public void closeConnection(){
		if(this.con != null){
			try {
				this.con.close();
			} catch (SQLException e) {
				//TODO
			}
		}
	}
	
}