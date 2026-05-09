package util;
import java.time.LocalDate;

import dao.DatabaseConnection;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SendEmailSSL {

    public Map<String,Boolean> taxPaid = new HashMap<>();

    public static void main(String[] args) {

        DatabaseConnection.loadDriver();
        DatabaseConnection.getConnection();
        DatabaseConnection.getConnection();
        String host = "bronto.ewi.utwente.nl";
        String dbName = "dab_di23242b_133";
        String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
        String username = "dab_di23242b_133";
        String password = "snCN+GM/3fkdyUkv";
        Map<String,Boolean> emailAddresses = getEmailAddressesFromDatabase(url, dbName, password);
        System.out.println(emailAddresses);
        // Fetch email addresses from the database

        // Schedule email sending
       // scheduleEmailSending(emailAddresses);
    }


    private static Map<String,Boolean> getEmailAddressesFromDatabase(String jdbcUrl, String dbUsername, String dbPassword) {
        Map<String,Boolean> emailAddresses = new HashMap<>();

        try (Connection connection = DriverManager.getConnection(jdbcUrl, dbUsername, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT email, ispaid FROM tax ")) {

            while (resultSet.next()) {
                emailAddresses.put(resultSet.getString("email"),resultSet.getBoolean("ispaid"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailAddresses;
    }

    private static String getStudentNameFromDatabase(String email) {
        String studentName = ""; // Initialize with an empty string

        // Database connection and query to fetch student's name based on email
        String host = "bronto.ewi.utwente.nl";
        String dbName = "dab_di23242b_133";
        String url = "jdbc:postgresql://" + host + ":5432/" + dbName;
        String dbUsername = "dab_di23242b_133";
        String dbPassword = "snCN+GM/3fkdyUkv";

        try (Connection connection = DriverManager.getConnection(url, dbUsername, dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT name FROM students WHERE email='" + email + "'")) {

            if (resultSet.next()) {
                studentName = resultSet.getString("name");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return studentName;
    }

//    private static void scheduleEmailSending(List<String> emailAddresses) {
//        // Calculate the delay until one week before March 1st
//        long delay = 1;
//
//
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.schedule(() -> sendEmails(emailAddresses), delay, TimeUnit.MILLISECONDS);
//    }

//
private static long calculateDelayUntil(LocalDate targetDate) {
    long now = System.currentTimeMillis();
    long targetMillis = targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    return targetMillis - now;
}

    private static void scheduleEmailSending(Map<String, Boolean> emailAddresses) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        LocalDate now = LocalDate.now();
        LocalDate nextEventDate = now.plusMonths(3);

        // Schedule for one month before
        long delayOneMonthBefore = calculateDelayUntil(nextEventDate.minusMonths(1));
        scheduler.schedule(() -> sendEmails(emailAddresses), delayOneMonthBefore, TimeUnit.MILLISECONDS);

        // Schedule for two weeks before
        long delayTwoWeeksBefore = calculateDelayUntil(nextEventDate.minusWeeks(2));
        scheduler.schedule(() -> sendEmails(emailAddresses), delayTwoWeeksBefore, TimeUnit.MILLISECONDS);

        // Schedule for one week before
        long delayOneWeekBefore = calculateDelayUntil(nextEventDate.minusWeeks(1));
        scheduler.schedule(() -> sendEmails(emailAddresses), delayOneWeekBefore, TimeUnit.MILLISECONDS);

        // Schedule for three days before
        long delayThreeDaysBefore = calculateDelayUntil(nextEventDate.minusDays(3));
        scheduler.schedule(() -> sendEmails(emailAddresses), delayThreeDaysBefore, TimeUnit.MILLISECONDS);

        // Schedule for one day before
        long delayOneDayBefore = calculateDelayUntil(nextEventDate.minusDays(1));
        scheduler.schedule(() -> sendEmails(emailAddresses), delayOneDayBefore, TimeUnit.MILLISECONDS);

        // Schedule the next set of reminders in three months
        long delayUntilNextEvent = calculateDelayUntil(nextEventDate);
        scheduler.schedule(() -> scheduleEmailSending(emailAddresses), delayUntilNextEvent, TimeUnit.MILLISECONDS);
    }


    private static void sendEmails(Map <String,Boolean> emailAddresses) {
        final String username = "earnitemployee@gmail.com";
        final String password = "upsl ghxx kezf mnsv";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                                              new jakarta.mail.Authenticator() {
                                                  protected PasswordAuthentication getPasswordAuthentication() {
                                                      return new PasswordAuthentication(username, password);
                                                  }
                                              });

        try {

            for(Map.Entry<String, Boolean> entry: emailAddresses.entrySet()){
                if (!entry.getValue()){//if it is not paid
                    String studentName = getStudentNameFromDatabase(entry.getKey());
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress("earnitemployee@gmail.com"));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(entry.getKey()));
                    message.setSubject("Tax submitted");
                    message.setText(
                            "Dear, " + studentName + "!" + "\n\n This is a reminder to file your taxes.");

                    Transport.send(message);
                    System.out.println("email sent");
                }
            }

//                for (Boolean ispaid : emailAddresses.values()) {
//                if(ispaid){
//                for(String email: emailAddresses.keySet()) {
//                    String studentName = getStudentNameFromDatabase(email);
//                    Message message = new MimeMessage(session);
//                    message.setFrom(new InternetAddress("earnitemployee@gmail.com"));
//                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//                    message.setSubject("Tax submitted");
//                    message.setText(
//                            "Dear, " + studentName + "!" + "\n\n This is a reminder to file your taxes.");
//
//                    Transport.send(message);
//                    System.out.println("email sent");
               // }}

          //  }

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public static void sendWelcomeEmail(String recipientEmail, String studentName) {
        final String username = "earnitemployee@gmail.com";
        final String password = "upsl ghxx kezf mnsv";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");



        Session session = Session.getInstance(prop,
                                              new Authenticator() {
                                                  protected PasswordAuthentication getPasswordAuthentication() {
                                                      return new PasswordAuthentication(username, password);
                                                  }
                                              });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("Welcome to EarnIt!");
            message.setText("Dear " + studentName + ", "
                                    + "\n\nWelcome to our application! Thank you for signing up.");

            Transport.send(message);
            System.out.println("Welcome email sent to: " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendApplicationEmail( String studentName) {
        final String username = "earnitemployee@gmail.com";
        final String password = "upsl ghxx kezf mnsv";
        String recipientEmail = "earnitemployee@gmail.com";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "465");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");



        Session session = Session.getInstance(prop,
                                              new Authenticator() {
                                                  protected PasswordAuthentication getPasswordAuthentication() {
                                                      return new PasswordAuthentication(username, password);
                                                  }
                                              });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );
            message.setSubject("Welcome to EarnIt!");
            message.setText("Dear Employee"
                                    + "\n\nAn application has been submitted by "+ studentName + ". You can view this in your dashboard.");

            Transport.send(message);
            System.out.println("Welcome email sent to: " + recipientEmail);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}