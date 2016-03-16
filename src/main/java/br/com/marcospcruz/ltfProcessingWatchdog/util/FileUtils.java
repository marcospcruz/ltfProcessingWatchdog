package br.com.marcospcruz.ltfProcessingWatchdog.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Properties;

public class FileUtils {

	private File file;

	public FileUtils(File file) {
		// TODO Auto-generated constructor stub

		this.file = file;
	}

	private InputStream getInputStream(String fileName) {
		InputStream object = getClass().getResourceAsStream(fileName);
		// TODO Auto-generated method stub
		return object;
	}

	public Properties loadPropertiesFile(String fileName) throws IOException {
		// TODO Auto-generated method stub

		Properties properties = null;

		InputStreamReader input = new InputStreamReader(
				getInputStream(fileName));

		try {
			properties = new Properties();

			properties.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			if (input != null)
				input.close();

		}

		return properties;
	}

	public void writeFile(String content) throws IOException {
		// TODO Auto-generated method stub

		OutputStream fos = null;

		try {

			fos = new FileOutputStream(file, true);

			fos.write(content.getBytes());

			fos.flush();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {

			fos.close();

		}

	}
}
