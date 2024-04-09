package com.camunda;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailNotifyService implements JavaDelegate {
    protected static final String HOST = "mail-hog";
    protected static final int PORT = 1025;
    protected final static Logger LOGGER = LoggerFactory.getLogger(EmailNotifyService.class);

    public void execute(DelegateExecution execution){

        String assignee = (String) execution.getVariable("assignee");
        Integer temperature = (Integer) execution.getVariable("temperature");

        if (assignee != null) {

            IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
            User user = identityService.createUserQuery().userId(assignee).singleResult();

            if (user != null) {
                String recipient = user.getEmail();

                if (recipient != null && !recipient.isEmpty()) {

                    Email email = new SimpleEmail();
                    email.setCharset("utf-8");
                    email.setHostName(HOST);
                    email.setSmtpPort(PORT);

                    try {
                        email.setFrom("noreply@camunda.org");
                        email.setSubject("IMPORTANT NOTIFICATION");
                        email.setMsg("Temperature value is " + temperature);
                        email.addTo(recipient);
                        email.send();

                    } catch (Exception e) {
                        LOGGER.info("Could not send email to assignee", e);
                    }

                } else {
                    LOGGER.info("Not sending email to user " + assignee + "', user has no email address.");
                }

            } else {
                LOGGER.info("Not sending email to user " + assignee + "', user is not enrolled with identity service.");
            }

        }

    }

}
