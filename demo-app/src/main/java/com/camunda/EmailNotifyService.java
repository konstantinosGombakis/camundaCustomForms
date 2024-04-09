package com.camunda;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.impl.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskAssignmentListener implements TaskListener {
    protected static final String HOST = "mail-hog";
    protected static final int PORT = 1025;

    protected final static Logger LOGGER = LoggerFactory.getLogger(TaskAssignmentListener.class);

    public void notify(DelegateTask delegateTask) {

        String assignee = delegateTask.getAssignee();
        String taskId = delegateTask.getId();
        LOGGER.info("assignee '" + assignee);
        LOGGER.info("taskId '" + taskId);

        if (assignee != null) {

            IdentityService identityService = Context.getProcessEngineConfiguration().getIdentityService();
            User user = identityService.createUserQuery().userId(assignee).singleResult();

            if (user != null) {

                // Get Email Address from User Profile
                String recipient = user.getEmail();

                if (recipient != null && !recipient.isEmpty()) {

                    Email email = new SimpleEmail();
                    email.setCharset("utf-8");
                    email.setHostName(HOST);
                    email.setSmtpPort(PORT);

                    try {
                        email.setFrom("noreply@camunda.org");
                        email.setSubject("Task assigned: " + delegateTask.getName());
                        email.setMsg("Please set temperature value in task : http://localhost:8080/camunda/app/tasklist/default/#/task=" + taskId);

                        email.addTo(recipient);

                        LOGGER.info(
                                "Email-------------------------------------- '" + email);
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
