package br.com.marcospcruz.ltfProcessingWatchdog.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class XMLUtils {

	private static final String[] IGNORE = { "<!--", "<?", "-->" };
	// private static final char[] IGNORE_CHARS = { '<', '>' };

	private HashMap tags;

	private StringBuffer xml;

	public XMLUtils(String xmlFile) throws IOException {
		// TODO Auto-generated constructor stub

		InputStream stream = getClass().getResourceAsStream(xmlFile);

		// File arquivoXml = new File(xmlFile);

		open(stream);

	}

	private void open(InputStream stream) throws IOException {
		// TODO Auto-generated method stub
		tags = new HashMap();

		BufferedReader reader = null;

		InputStreamReader in = null;

		try {

			in = new InputStreamReader(stream);

			reader = new BufferedReader(in);
			xml = new StringBuffer();
			while (reader.ready()) {

				String row = reader.readLine();

				if (!disregardRow(row))
					xml.append(row);

			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} finally {

			reader.close();

			in.close();

			stream.close();

		}

	}

	/**
	 * 
	 * @param row
	 * @return
	 */
	private String getTag(String row) {
		// TODO Auto-generated method stub
		return row.substring(1, row.length() - 1);
	}

	private Boolean disregardRow(String row) {
		// TODO Auto-generated method stub

		for (String tag : IGNORE) {

			if (row.contains(tag) || row.length() == 0)
				return true;

		}

		return false;
	}

	// private Boolean disregardChar(char c) {
	// // TODO Auto-generated method stub
	//
	// for (char cc : IGNORE_CHARS) {
	//
	// if (cc == c)
	// return true;
	//
	// }
	//
	// return false;
	// }

	public Object getValue(String tagName) {
		// TODO Auto-generated method stub
		String tag = "", valor = "";
		String xmlTmp = xml.toString();
		Boolean isTag = null;
		for (int i = 0; i < xmlTmp.length(); i++) {

			char x = xmlTmp.charAt(i);

			if (x == '>') {

				// if (!tag.equals(tagName)) {

				tag = tag.trim();

				// }

				isTag = false;

			} else if (x == '<') {

				if (tag.equals(tagName)) {

					return valor;

				}

				// inicia nova tag
				isTag = true;
				tag = "";
				valor = "";
			} else if (x != '<' && isTag) {

				tag += x;

			} else if (!isTag) {

				valor += x;
				valor = valor.trim();

			}

		}

		return null;

	}
}
