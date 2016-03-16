package br.com.marcospcruz.ltfProcessingWatchdog.watchdog;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import br.com.marcospcruz.ltfProcessingWatchdog.communicator.MailSender;
import br.com.marcospcruz.ltfProcessingWatchdog.dao.EntryDateDao;
import br.com.marcospcruz.ltfProcessingWatchdog.util.ConstantesEnum;
import br.com.marcospcruz.ltfProcessingWatchdog.util.FileUtils;
import br.com.marcospcruz.ltfProcessingWatchdog.util.FullThreadDump;
import br.com.marcospcruz.ltfProcessingWatchdog.util.MyLoggerSingleton;
import br.com.marcospcruz.ltfProcessingWatchdog.util.Utilitario;

public class WatchDog {

	private static final long INTERVALO_TEMPO_DUMPS = 1000 * 3;
	private static final int QT_THREAD_DUMPS = 3;
	private static WatchDog instance = new WatchDog();
	private String[] emailsRecipients;
	private boolean alertAll;

	private WatchDog() {
	}

	public static WatchDog getInstance() {
		return instance;
	}

	public void watch(Integer idleMinutes) {
		// TODO Auto-generated method stub
		EntryDateDao dao = new EntryDateDao();

		try {

			Date lastEntryDate = dao.read();

			Date currentDate = new Timestamp(new Date().getTime());
			int dateDifference = Utilitario.validaDiferencaData(lastEntryDate,
					currentDate, Calendar.MINUTE);

			if (dateDifference > idleMinutes) {

				StringBuffer alert = new StringBuffer(
						"The processing is on hold for " + dateDifference
								+ " minutes. From " + lastEntryDate + " to "
								+ currentDate + ".");

				log(alert.toString());
				// restarting the SGMP

				generateThreadDumpAndSendByEmail(alert);

				restartSGMP();

			} else {

				String alert = "The SGMP is processing the LTF files. Last entry date: "
						+ lastEntryDate;

				log(alert);

				if (alertAll) {

					sendAlertByEmail(new StringBuffer(alert));

				}

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// generateThreadDumpAndSendByEmail(new
		// StringBuffer("Marcos Pereira da Cruz"));

	}

	/**
	 * 
	 * @param alert
	 */
	private void generateThreadDumpAndSendByEmail(StringBuffer alert) {
		// TODO Auto-generated method stub
		StringBuffer threadDump = generateThreadDump();

		sendAlertByEmail(alert, threadDump);

	}

	/**
	 * 
	 * @return
	 */
	private StringBuffer generateThreadDump() {
		// TODO Auto-generated method stub

		StringBuffer threadDump = new StringBuffer();

		log("Generating " + QT_THREAD_DUMPS + " Thread Dumps");

		for (int i = 0; i < QT_THREAD_DUMPS; i++) {

			threadDump
					.append("================================= Begin of ThreadDump "
							+ (i + 1)
							+ "================================="
							+ ConstantesEnum.LineSeparator.getValue()
									.toString());

			threadDump.append(generateSGMPThreadDump());

			threadDump
					.append("================================= End of ThreadDump "
							+ (i + 1)
							+ "================================="
							+ ConstantesEnum.LineSeparator.getValue()
									.toString());

			try {
				Thread.sleep(INTERVALO_TEMPO_DUMPS);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return threadDump;

	}

	private void sendAlertByEmail(StringBuffer stringBuffer) {
		// TODO Auto-generated method stub
		sendAlertByEmail(stringBuffer, null);
	}

	/**
	 * xxx
	 * 
	 * @return
	 */
	private StringBuffer generateSGMPThreadDump() {
		// TODO Auto-generated method stub

		FullThreadDump fullThreadDump = new FullThreadDump(
				ConstantesEnum.JMXURL.getValue().toString());

		return fullThreadDump.dump();

	}

	/**
	 * @param alert
	 * @param threadDump
	 * 
	 */
	private void sendAlertByEmail(StringBuffer alert, StringBuffer threadDump) {
		// TODO Auto-generated method stub

		File anexo = null;

		MailSender agente = new MailSender();

		if (threadDump != null) {

			anexo = new File("threadDump.log");

			FileUtils fu = new FileUtils(anexo);

			try {

				fu.writeFile(threadDump.toString());

				agente.attachFile(anexo);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		agente.setEmailRecipients(emailsRecipients);

		alert.append(ConstantesEnum.LineSeparator.getValue().toString());

		agente.attachFile(anexo);

		agente.sendEmailAlert(alert);

	}

	/**
	 * x
	 */
	private void restartSGMP() {
		// TODO Auto-generated method stub
		log("Restarting the SGMP...");

		try {
			// Runtime.getRuntime().exec("d:\\x.bat");
			Runtime.getRuntime().exec(
					ConstantesEnum.SGMPStartupBatch.getValue().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param string
	 */
	private void log(Object log) {
		// TODO Auto-generated method stub

		String string = log.toString();

		MyLoggerSingleton.getInstance().writeLog(this, string);
	}

	/**
	 * 
	 * @param emailsRecipients
	 */
	public void setEmailsRecipents(String[] emailsRecipients) {
		// TODO Auto-generated method stub

		this.emailsRecipients = emailsRecipients;

	}

	public void alertAll(boolean alertAll) {
		// TODO Auto-generated method stub

		this.alertAll = alertAll;

	}

	public void justSendThreadDump() {
		// TODO Auto-generated method stub

		StringBuffer threadDump = generateThreadDump();

		sendAlertByEmail(new StringBuffer("Thread Dump Request."), threadDump);
		;

	}

}
