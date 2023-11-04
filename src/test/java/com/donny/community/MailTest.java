package com.donny.community;

import com.donny.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTestMail() {
        mailClient.sendMail("donnyjin22@gmail.com", "TEST1104","Welcome!");
    }

    @Test
    public void testHTMLMail() {
        Context context = new Context();
        context.setVariable("username", "Donny");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("donnyjin22@gmail.com", "TEST1104",content);
    }
}
