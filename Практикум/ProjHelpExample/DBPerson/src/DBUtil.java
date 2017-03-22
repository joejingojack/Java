import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBUtil {
	
	static Connection connected = null;
	
	static Connection getConnected(){
		try {
			Class.forName("org.h2.Driver");
			connected = DriverManager.getConnection("jdbc:h2:C:\\Users\\Krasi\\Desktop\\Praktikum\\ProjHelpExample\\lib", "sa","");
			return connected;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// end try/catch
		return connected;
	}// end getConnected
	
}// end DBUtil
