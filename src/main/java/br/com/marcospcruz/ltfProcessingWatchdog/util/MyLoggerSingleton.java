package br.com.marcospcruz.ltfProcessingWatchdog.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class MyLoggerSingleton {

	private static MyLoggerSingleton instance = new MyLoggerSingleton();

	private String lineSeparator = System.getProperty("line.separator");;

	/**
	 * x
	 */
	private MyLoggerSingleton() {
	}

	public static MyLoggerSingleton getInstance() {

		return instance;
	}

	/**
	 * Log information and go to a new row.
	 * 
	 * @param object
	 * @param string
	 */
	public void writeLog(Object object, String string) {
		// TODO Auto-generated method stub

		try {
			log(Utilitario.formatDate(new Date()) + " - ["
					+ object.getClass().getSimpleName() + "] " + string
					+ lineSeparator);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(new Date().toString() + " - ["
		// + object.getClass().getSimpleName() + "] " + string);

	}

	/**
	 * 
	 * @throws IOException
	 */
	private void log(String row) throws IOException {
		// TODO Auto-generated method stub

		String formattedDate = Utilitario
				.formatDateWithoutSeparator(new Date());

		String fileName = "watchdog_" + formattedDate + ".log";

		// fileName = fileName.replace('/', '-');

		File log = new File(fileName);

		FileUtils util = new FileUtils(log);
		
		util.writeFile(row);

	}

	/**
	 * Log information at the same row.
	 * 
	 * @param string
	 */
	public void writeLog(String string) {
		// TODO Auto-generated method stub

		System.out.print(string);

	}
}
