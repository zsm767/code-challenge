import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class FileLoader {
	
	private static final String SQL_INSERT = "INSERTO INTO ${tables} (${keys}) VALUES(${values})";
	private static final String TABLE_REGEX = "\\$\\{table\\}";
	private static final String KEY_REGEX = "\\$\\{keys\\}";
	private static final String VALUE_REGEX = "\\$\\{values\\}";
	
	private Connection connection;
	private char delim;
	
	//initializer, setting the given connection to the DB, and  the approvriate delim for the CSV file.
	public FileLoader(Connection connection) {
		this.connection = connection;
		this.delim = ',';
	}
	
	//Chunk of the code involving parsing the CSV file 
	public void loadCSV(String csvFile, String tableName, boolean preloadTruncate) throws Exception {
		CSVReader reader = null;
		if( this.connection = null ){
			throw new Exception("Invalid connection.");
		}
		
		try{
			reader = new CSVReader( new FileReader(fileName), this.separator);
		} catch(Exception e){
			e.printStackTrace();
			throw new Exception("Error occurred while reading the file: " + e.getMessage());
		}
		
		String[] headers = csvReader.readNext();
		
		if(headers == null){
			throw new FileNotFoundException("No columns defined in the CSV file. Check the file format, please.");
		}
		
		String query = SQL_INSERT.replaceFirst(TABLE_REGEX, tableName);
		query = query.replaceFirst(KEY_REGEX, Stringutils.join(headers, ","));
		query = query.replaceFirst(VALUE_REGEX);
		System.out.println("Query: " + query );
		
		String[] nextLine;
		Connection conn = null;
		PreparedStatement ps = null;
		ps = conn.prepareStatement(query);
		
		try{
			conn = this.connection;
			conn.setAutoCommit(false);
			ps = con.prepareStatement(query);
		
			if( preloadTruncate ){
				conn.createStatement().execute("DELETE FROM " + tableName);
			}
			
			final int batchSize = 1000;
			int count = 0;
			Date date = null;
			while((nextLine = reader.readNext()) != null){
				if(nextLine != null){
					int index = 1;
					
					for( String s : nextLine ){
						date = Dateutil.convertToDate(s);
						if( date != null ){
							ps.setDate(index++, new java.sql.Date(date.getTime()));
						} else {
							ps.setString(index++, s);
						}
						ps.addBatch();
					}
				}
				
				if (++count % batchSize == 0) {
						ps.executeBatch();
					}
			}
			
			ps.executeBatch(); // insert remaining records
			con.commit();
		} catch (Exception e) {
			con.rollback();
			e.printStackTrace();
			throw new Exception("Error occured while loading data from file to database: "+ e.getMessage());
		} finally {
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();

			reader.close();
		}
	}
	
	
	public char getDelim() {
		return delim;
	}
	
	public void setDelim(char delim){
		this.delim = delim;
	}
	
}