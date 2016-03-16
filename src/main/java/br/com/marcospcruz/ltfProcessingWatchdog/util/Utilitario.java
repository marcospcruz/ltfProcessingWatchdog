package br.com.marcospcruz.ltfProcessingWatchdog.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Utilitario {

	private static final int DAY_IN_MILLIS = 86400000;

	private static SimpleDateFormat sdf;

	private Utilitario() {
	}

	public static Date stringToDate(String substring, String datePattern) {
		// TODO Auto-generated method stub

		sdf = new SimpleDateFormat(datePattern);

		try {
			return sdf.parse(substring);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		// TODO Auto-generated method stub

		sdf = new SimpleDateFormat(
				(String) ConstantesEnum.DateTimestampPattern.getValue());

		return sdf.format(date);
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String formatDateWithoutSeparator(Date date) {
		// TODO Auto-generated method stub

		sdf = new SimpleDateFormat(
				(String) ConstantesEnum.DateHourWithoutSeparators.getValue());

		return sdf.format(date);

	}

	/**
	 * 
	 * @param daysDifference
	 * @return
	 */
	public static Calendar getCal(int daysDifference) {

		Calendar cal = Calendar.getInstance();

		cal.setTime(new Date());

		cal.add(Calendar.DAY_OF_MONTH, -daysDifference);

		return cal;

	}

	public static int calculaDiferencaHoras(Date dataAnterior, Date dataAtual) {
		// TODO Auto-generated method stub

		System.out.println(dataAnterior + " " + dataAtual);

		Calendar calDataAnterior = new GregorianCalendar();

		calDataAnterior.setTime(dataAnterior);

		Calendar calDataAtual = new GregorianCalendar();

		calDataAtual.setTime(dataAtual);

		int diff = calDataAtual.get(Calendar.HOUR_OF_DAY)
				- calDataAnterior.get(Calendar.HOUR_OF_DAY);

		return diff;
	}

	/**
	 * 
	 * @param anteriorCal
	 * @param atualCal
	 * @param field
	 * @return
	 */
	public static int validaDiferencaData(Calendar anteriorCal,
			Calendar atualCal, int field) {
		// TODO Auto-generated method stub

		int retorno = atualCal.get(field) - anteriorCal.get(field);

		return retorno;
	}

	public static Date setTime(Date value, String time) {
		// TODO Auto-generated method stub

		Calendar calendar = new GregorianCalendar();

		calendar.setTime(value);
		String[] splittedTime = time.split(":");

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

		calendar.set(year, month, dayOfMonth, new Integer(splittedTime[0]),
				new Integer(splittedTime[1]), new Integer(splittedTime[2]));

		return calendar.getTime();
	}

	/**
	 * 
	 * @param previousDate
	 * @param date
	 * @param field
	 * @return
	 */
	public static int validaDiferencaData(Date previousDate, Date date,
			int field) {
		// TODO Auto-generated method stub

		int retorno = 0;

		try {

			retorno = (int) (date.getTime() - previousDate.getTime());

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (field == Calendar.MINUTE)

			retorno = (retorno / 1000) / 60;

		return retorno;
	}

	public static String getPreviousDate() {
		// TODO Auto-generated method stub

		Calendar calendar = new GregorianCalendar();

		calendar.add(Calendar.DAY_OF_YEAR, -120);

		String retorno = Integer.toString(calendar.get(Calendar.YEAR));

		String month = Integer.toString(calendar.get(Calendar.MONTH));

		retorno += new Integer(month) < 10 ? "0" + month : month;

		retorno += calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0"
				+ Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))
				: Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));

		return retorno;
		// return retorno;
	}

	public static Address[] separateMailAddresses(String string)
			throws AddressException {
		// TODO Auto-generated method stub

		String[] emails = string.split(";");

		Address[] recipients = new Address[emails.length];

		for (int i = 0; i < emails.length; i++) {

			String email = emails[i];

			recipients[recipients.length] = new InternetAddress(email);
		}

		return recipients;
	}
}
