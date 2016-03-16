package br.com.marcospcruz;

import br.com.marcospcruz.ltfProcessingWatchdog.watchdog.WatchDog;

/**
 * Hello world!
 *
 */
public class WatchdogMainClass {
	public static void main(String[] args) {
		try {

			Integer timeInMinutes = 0;

			try {
				
				timeInMinutes = new Integer(args[0]);
				
			} catch (NumberFormatException e) {

				System.err.println("First argument is non-numeric.");

			}

			// new MailConfigurationDao().readAll();

			String[] recipients = args[1].split(";");

			WatchDog.getInstance().setEmailsRecipents(recipients);

			if (args.length == 2) {

				boolean sendDump = args[0].equals("dump");
				// only thread dump;
				if (sendDump) {

					WatchDog.getInstance().justSendThreadDump();
					return;
				}

			} else if (args.length == 3) {
				String alertAll = args[2];

				WatchDog.getInstance().alertAll(alertAll.equals("ALL"));
			}

			WatchDog.getInstance().watch(timeInMinutes);

		} catch (ArrayIndexOutOfBoundsException e) {

			System.err
					.println("ERROR! The syntax is: \"java -jar ltfProcessingWatchdog.jar <period_in_minutes>\" \"<e-mail1;e-mail2>\"");

		}

	}
}
