package springbook.user.service;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class DummyMailSender implements MailSender {

	@Override
	public void send(SimpleMailMessage arg0) throws MailException {
		// TODO Auto-generated method stub
		System.out.println(arg0.getText());
	}

	@Override
	public void send(SimpleMailMessage[] arg0) throws MailException {
		// TODO Auto-generated method stub

	}

}
