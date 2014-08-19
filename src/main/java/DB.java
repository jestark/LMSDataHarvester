import java.util.Properties;
import java.sql.*;

public class DB
{
	public static void main (String args[]) throws Throwable
	{
		String dburl = "jdbc:postgresql://***REMOVED***";

		Class.forName ("org.postgresql.Driver");

		Properties props = new Properties ();
		props.setProperty ("user", "***REMOVED***");
		props.setProperty ("password", "***REMOVED***");
		props.setProperty ("ssl", "true");
		props.setProperty ("sslfactory", "org.postgresql.ssl.NonValidatingFactory");

		Connection conn = DriverManager.getConnection(dburl);
	}
}
