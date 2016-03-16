package br.com.marcospcruz.ltfProcessingWatchdog.util;

public enum ConstantesEnum {

	SgmpLancamentoCCQuery(0),

	DateTimestampPattern(1),

	DateHourWithoutSeparators(2),

	SGMPStartupBatch(3),

	EmailAddressId(4),

	EmailAccountId(5),

	EmailPasswordId(6),

	EmailSmtpAddressId(7),

	EmailSmtpPort(8),

	EmailConfigurationQuery(9),

	LineSeparator(10), JMXURL(11);

	private Object value;

	private Object[] values = { "query.sgmp.entries.last_entry_date",// 0
			"dd/MM/yyyy HH:mm:ss", // 1
			"yyyyMMdd", // 2
			"d:\\x.bat", // 3
			2010, // 4
			2011, // 5
			2012,// 6
			2013, // 7
			25, // 8
			"query.sgmp.mail.configuration",// 9
			System.getProperty("line.separator"),// 10
			"/jndi/rmi://10.1.0.81:55555/jmxrmi"// 11
	};

	ConstantesEnum(int indice) {

		value = values[indice];

	}

	public Object getValue() {
		return value;
	}

}
