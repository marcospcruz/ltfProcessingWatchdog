package br.com.marcospcruz.ltfProcessingWatchdog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import br.com.marcospcruz.ltfProcessingWatchdog.util.ConstantesEnum;
import br.com.marcospcruz.ltfProcessingWatchdog.util.Utilitario;

public class EntryDateDao extends Database {

	public EntryDateDao() {
		super();
	}

	public List readAll(long timeInMillis1, long timeInMillis2)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Timestamp read() throws SQLException {
		// TODO Auto-generated method stub

		Timestamp date = null;

		Connection connection = null;

		PreparedStatement ps = null;

		// Statement st = null;

		ResultSet rs = null;

		String query = queriesProperties
				.getProperty(ConstantesEnum.SgmpLancamentoCCQuery.getValue()
						.toString());

		try {

			connection = getConnection();

			// st = connection.createStatement();
			ps = connection.prepareStatement(query);

			String truncdate = Utilitario.getPreviousDate();

			ps.setString(1, truncdate);

			rs = ps.executeQuery();

			if (rs.next())

				date = rs.getTimestamp(1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log(this, e.getMessage());

		} finally {

			close(connection, ps, rs);

		}

		return date;
	}

}
