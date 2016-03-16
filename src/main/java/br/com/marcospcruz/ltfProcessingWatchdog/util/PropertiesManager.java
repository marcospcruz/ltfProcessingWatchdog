package br.com.marcospcruz.ltfProcessingWatchdog.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

	private PropertiesManager() {
	}

	public static Properties getPropertiesParameters(String fileName)
			throws IOException, NullPointerException {
		// TODO Auto-generated method stub

		Properties properties = new Properties();

		FileInputStream fis = null;

		try {

			fis = new FileInputStream(fileName);

			properties.load(fis);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			fis.close();

		}

		return properties;
	}

	/**
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static Properties getPropertiesParameters(InputStream is)
			throws IOException {
		// TODO Auto-generated method stub

		Properties properties = new Properties();

		try {

			properties.load(is);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			is.close();

		}

		return properties;
	}
}
