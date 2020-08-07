/** 
* Permite ejecutar sentencias SQL sobre la base de datos de Liferay 
*/

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.liferay.portal.kernel.dao.jdbc.DataAccess;

Connection con = null;
Statement st = null;
try {
	con = DataAccess.getUpgradeOptimizedConnection();
	st = con.createStatement();
	ResultSet rs = st.executeQuery("SELECT");
	rs.next();
	println(rs.getInt(1));
}
finally {
	DataAccess.cleanUp(con, st);
}
