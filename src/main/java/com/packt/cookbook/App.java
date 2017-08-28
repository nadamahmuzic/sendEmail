package com.packt.cookbook;


import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

/**
 *
 *
 */
public class App {


    public static void main(String[] args) {

        // serversetup
        ServerSetup setup = new ServerSetup(3025, "localhost", "smtp");
        setup.setServerStartupTimeout(20000);

        // greenmailsetup
        GreenMail greenMail = new GreenMail(setup);
        greenMail.setUser("from@user.com", "nadja", "ajdan");
        greenMail.start();

        // set properties with localhost, port and protocol
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "localhost");
        properties.put("mail.smtp.port", "3025");
        properties.put("mail.transport.protocol", "smtp");

        // set timeout
        properties.put("mail.smtp.connectiontimeout", "2000");
        properties.put("mail.smtp.timeout", "2000");

        // array of emails
        String[] mailAddressTo = new String[2];
        mailAddressTo[0] = "to0@user.com";
        mailAddressTo[1] = "to1user@com";

        // declaring session, message and registration array of emailaddresses
        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        InternetAddress[] mailInternetAddressTo = new InternetAddress[mailAddressTo.length];

        // declaration of emailaddresses
        for (int i = 0; i < mailAddressTo.length; i++) {

            try {
                mailInternetAddressTo[i] = new InternetAddress(mailAddressTo[i]);
            } catch (AddressException e) {
                e.printStackTrace();
            }
        }

        // decleration of subject and text and sending it to the emailaddresses
        try {
            message.setSubject("subject");
            message.setText("text");
            message.setFrom(new InternetAddress("from@user.com"));
            // message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("to@user.com"));
            message.addRecipients(Message.RecipientType.TO, mailInternetAddressTo);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

            Transport transport = null;
        try {
            transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        // setup.getConnectionTimeout();

        // receiving messages
        greenMail.waitForIncomingEmail(1);
        Message[] messages = greenMail.getReceivedMessages();

        // number of messages
        System.out.println("Messages: " + messages.length);


        // output of messages
        for (int i = 0; i < messages.length; i++) {
            try {
                System.out.println("Subject: " + messages[i].getSubject());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            try {
                System.out.println("Content: " + messages[i].getContent());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            System.out.println("Done");
            greenMail.stop();

        }
    }
}