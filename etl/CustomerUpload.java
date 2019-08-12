// CustomerUpload.java

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CustomerUpload {
	//would want to input the server URL string below
	private static String JDBC_CONNECTION_URL = "...";
	
	
	private static Connection getConnection(){
		Connection conn = null;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(JDBC_CONNECTION_URL);
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	public static void main(String[] args) {
		try{
			CSVLoader load = new CSVLoader(getConnection());
			//for the loadCSV method, takes 3 args:
			//the file path, the TABLE NAME, and the bool param that decides if the table needs to be trunc'd before inserting new entries
			load.loadCSV("C:\\path\to\file.sql", "customers", true);
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
