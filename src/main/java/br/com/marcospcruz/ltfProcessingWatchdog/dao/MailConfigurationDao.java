package br.com.marcospcruz.ltfProcessingWatchdog.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.com.marcospcruz.ltfProcessingWatchdog.util.ConstantesEnum;

public class MailConfigurationDao extends Database {

	public List readAll() throws SQLException {
		// TODO Auto-generated method stub

		List retorno = null;

		String query = queriesProperties.getProperty(
				ConstantesEnum.EmailConfigurationQuery.getValue().toString())
				.concat(" in (");
		query += ConstantesEnum.EmailSmtpAddressId.getValue().toString() + ",";
		query += ConstantesEnum.EmailAddressId.getValue().toString() + ",";
		query += ConstantesEnum.EmailAccountId.getValue().toString() + ",";
		query += ConstantesEnum.EmailPasswordId.getValue().toString() + ")";

		Connection connection = null;

		Statement stmt = null;

		ResultSet rs = null;

		try {

			connection = getConnection();

			stmt = connection.createStatement();

			rs = stmt.executeQuery(query);

			retorno = new ArrayList();

			while (rs.next()) {

				retorno.add(rs.getString(1));

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			close(connection, stmt, rs);

		}

		return retorno;
	}
}
