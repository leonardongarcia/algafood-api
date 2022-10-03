package com.garcia.algafoodapi.infrastructure.service.email;

import com.garcia.algafoodapi.core.email.EmailProperties;
import com.garcia.algafoodapi.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public class SmtpEnvioEmailService implements EnvioEmailService {

  @Autowired private JavaMailSender javaMailSender;

  @Autowired private EmailProperties emailProperties;

  @Autowired private ProcessadorEmailTemplate processadorEmailTemplate;

  @Override
  public void enviar(Mensagem mensagem) {
    try {
      MimeMessage mimeMessage = criarMimeMessage(mensagem);

      javaMailSender.send(mimeMessage);
    } catch (Exception e) {
      throw new EmailException("Não foi possível enviar e-mail", e);
    }
  }

  protected MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
    String corpo = processadorEmailTemplate.processarTemplate(mensagem);

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();

    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
    helper.setFrom(emailProperties.getRemetente());
    helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
    helper.setSubject(mensagem.getAssunto());
    helper.setText(corpo, true);

    return mimeMessage;
  }
}
