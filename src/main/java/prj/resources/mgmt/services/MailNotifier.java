package prj.resources.mgmt.services;


import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MailNotifier {

	private MailSender mailSender;

	public MailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	public void sendEmail(String from, String to, String message, String subject) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(from);
		msg.setTo(to);
		msg.setSubject(subject);
		message = message.concat("\n\n" + "from \n" + from);
		msg.setText(message);
		try {
			mailSender.send(msg);
		}catch(Exception e) {
			System.out.println(e);
		}
	}
}
