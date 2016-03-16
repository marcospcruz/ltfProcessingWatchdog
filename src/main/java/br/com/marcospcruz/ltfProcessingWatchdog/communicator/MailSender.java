package br.com.marcospcruz.ltfProcessingWatchdog.communicator;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import br.com.marcospcruz.ltfProcessingWatchdog.dao.MailConfigurationDao;
import br.com.marcospcruz.ltfProcessingWatchdog.util.ConstantesEnum;

public class MailSender {

	private MailConfigurationDao dao;
	private String[] emailRecipients;
	private File anexo;

	public void sendEmailAlert(StringBuffer textMessage) {
		// TODO Auto-generated method stub

		dao = new MailConfigurationDao();

		try {

			final List configuracoes = dao.readAll();

			Properties props = new Properties();

			props.put("mail.smtp.host", configuracoes.get(3));

			props.put("mail.debug", true);

			props.put("mail.transport.protocol", "smtp");

			props.put("mail.smtp.port", ConstantesEnum.EmailSmtpPort.getValue()
					.toString());

			props.put("mail.smtp.auth", "true");

			Authenticator auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(configuracoes.get(1)
							.toString(), configuracoes.get(2).toString());
				}
			};
			Session session = Session.getDefaultInstance(props, auth);

			MimeMessage message = new MimeMessage(session);

			Address from = new InternetAddress(configuracoes.get(0).toString());

			message.setFrom(from);

			message.addRecipients(RecipientType.BCC, getAddressess());

			message.setSentDate(new Date());

			message.setSubject("SGMP LTF Processing Alert");

			// message.setText(textMessage.toString());

			message.setContent(createMultiPart(textMessage.toString()));

			Transport.send(message);

			anexo.delete();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param textMessage
	 * @return
	 * @throws MessagingException
	 */
	private Multipart createMultiPart(String textMessage)
			throws MessagingException {
		// TODO Auto-generated method stub

		Multipart multiPart = new MimeMultipart();

		MimeBodyPart messageBodyPart = new MimeBodyPart();

		messageBodyPart.setText(textMessage);

		if (anexo != null) {

			// messageBodyPart = new MimeBodyPart();

			DataSource source = new FileDataSource(anexo);

			messageBodyPart.setDataHandler(new DataHandler(source));

			messageBodyPart.setFileName(anexo.getName());

		}

		multiPart.addBodyPart(messageBodyPart);

		return multiPart;
	}

	/**
	 * 
	 * @return
	 * @throws AddressException
	 */
	private Address[] getAddressess() throws AddressException {
		// TODO Auto-generated method stub

		Address[] addresses = new Address[emailRecipients.length];

		for (int i = 0; i < emailRecipients.length; i++)

			addresses[i] = new InternetAddress(emailRecipients[i]);

		return addresses;
	}

	/**
	 * 
	 * @param emailsRecipients
	 */
	public void setEmailRecipients(String[] emailsRecipients) {
		// TODO Auto-generated method stub

		this.emailRecipients = emailsRecipients;

	}

	public void attachFile(File anexo) {
		// TODO Auto-generated method stub

		this.anexo = anexo;

	}

}
