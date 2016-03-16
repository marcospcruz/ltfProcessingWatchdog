package br.com.marcospcruz.ltfProcessingWatchdog.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import br.com.marcospcruz.ltfProcessingWatchdog.util.MyLoggerSingleton;
import br.com.marcospcruz.ltfProcessingWatchdog.util.OracleDSParameterMappingClass;
import br.com.marcospcruz.ltfProcessingWatchdog.util.PropertiesManager;
import br.com.marcospcruz.ltfProcessingWatchdog.util.XMLUtils;

public abstract class Database {

	protected Properties queriesProperties;

	private final static String DATASOURCE_XML = "/oracle-ds.xml";

	private static final String QUERY_PROPERTIES_FILE = "/queries.properties";

	public Database() {

		// openConnection();

		loadProperties();

	}

	private void loadProperties() {
		// TODO Auto-generated method stub

		try {

			InputStream stream = getClass().getResourceAsStream(
					QUERY_PROPERTIES_FILE);

			queriesProperties = PropertiesManager
					.getPropertiesParameters(stream);

		} catch (NullPointerException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	protected Connection getConnection() throws SQLException {

		XMLUtils xml = null;

		Connection conn = null;

		try {

			xml = new XMLUtils(DATASOURCE_XML);

			Properties connectionProps = new Properties();

			connectionProps.put("user",
					xml.getValue(OracleDSParameterMappingClass.USER_NAME_TAG));

			// teste
			// connectionProps.put("user", "sgmp_lekki");

			connectionProps.put("password",
					xml.getValue(OracleDSParameterMappingClass.PASSWORD_TAG));

			String urlDB = ((String) xml
					.getValue(OracleDSParameterMappingClass.URL_CONNECTION_TAG));

			// teste
			// urlDB = "jdbc:oracle:thin:@10.0.0.47:1521:SGMP";

			log("Opening connection to: " + urlDB);

			conn = DriverManager.getConnection(urlDB, connectionProps);

			log(this, "Connection open");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;

	}

	// /**
	// * xxx
	// */
	// protected void openConnection() {
	// // TODO Auto-generated method stub
	//
	// Connection conn = getConnection();
	//
	// try {
	// if (conn != null && !conn.isClosed())
	// conn.close();
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	protected void log(String string) {
		// TODO Auto-generated method stub

		// log(this, string);

	}

	/**
	 * 
	 * @param object
	 * @param string
	 */
	public void log(Object object, String string) {
		// TODO Auto-generated method stub

		// MyLoggerSingleton.getInstance().writeLog(object, string);

	}

	/**
	 * 
	 * @param connection
	 * @param ps
	 * @param rs
	 * @throws SQLException
	 */
	protected void close(Connection connection, Statement st, ResultSet rs)
			throws SQLException {
		// TODO Auto-generated method stub

		closeResultSet(rs);

		closeStatement(st);

		closeConnection(connection);

		log("Connection Closed.");

	}

	private void closeConnection(Connection connection) throws SQLException {
		// TODO Auto-generated method stub
		if (connection != null && !connection.isClosed()) {
			connection.close();
		}
	}

	private void closeStatement(Statement st) throws SQLException {
		// TODO Auto-generated method stub
		if (st != null && !st.isClosed())
			st.close();

	}

	private void closeResultSet(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub

		if (rs != null && !rs.isClosed())
			rs.close();
	}

	/**
	 * 
	 * @param connection
	 * @param ps
	 * @param rs
	 * @throws SQLException
	 */
	protected void close(Connection connection, PreparedStatement ps,
			ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub

		closeResultSet(rs);

		closeStatement(ps);

		closeConnection(connection);

		log("Connection Closed.");

	}

}
