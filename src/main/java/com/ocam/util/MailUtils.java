package com.ocam.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.env.Environment;

import com.ocam.model.exception.BusinessException;

public class MailUtils {

	public static void sendEmail(String toEmail, String title, String content)
			throws BusinessException {
		try {
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");

			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						@Override
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(
									"ocamuniovi@gmail.com", "ocamuniovi123");
						}
					});

			MimeMessage msg = new MimeMessage(session);
			msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
			msg.addHeader("format", "flowed");
			msg.addHeader("Content-Transfer-Encoding", "8bit");

			msg.setFrom(
					new InternetAddress("no_reply@ocam.com", "NoReply OCAM"));

			msg.setReplyTo(InternetAddress.parse("no_reply@ocam.com", false));

			msg.setSubject(title, "UTF-8");
			msg.setText(content, "UTF-8");
			msg.setSentDate(new Date());

			msg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toEmail, false));
			Transport.send(msg);

		} catch (Exception e) {
			throw new BusinessException("Error enviando mail de confirmación");
		}
	}

	/**
	 * Genera una cadena única y aleatoria por usuario
	 * 
	 * @param name
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String generateKey(String name)
			throws NoSuchAlgorithmException {
		SecureRandom random = new SecureRandom();
		String salt = new BigInteger(130, random).toString(32);
		return MD5Util.MD5(name) + salt;
	}

	/**
	 * Obtiene el perfil de la aplicación
	 * 
	 * @return
	 */
	public static boolean assertPuedeEnviarEmails(Environment environment) {

		String[] prof = environment.getActiveProfiles();
		for (String s : prof) {
			if (s.equals("test") || s.equals("prodPostgre")) {
				return Boolean.FALSE;
			}
		}
		return Boolean.TRUE;
	}
}
