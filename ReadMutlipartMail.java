import com.sun.mail.pop3.*;
import java.util.*;
import javax.activation.DataHandler;
import javax.mail.*;
import javax.mail.internet.*;
 
public class ReadMultipartMail {
 
  public static void main(String args[]) throws Exception {
 
  String host = "pop3.live.com";
  String username = "meuemail";
  String passwoed = "senha";
 
  Properties props = System.getProperties();
   
  String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
 
  Properties pop3Props = new Properties();
 
  pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
  pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
  pop3Props.setProperty("mail.pop3.port",  "995");
  pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
 
  URLName url = new URLName("pop3", host, 995, "",username, passwoed);
   
  Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                             protected PasswordAuthentication getPasswordAuthentication() 
                             {
                                   return new PasswordAuthentication(usename, passwoed);
                             }
                        });
 
  Store store = new POP3SSLStore(session, url);
  store.connect();
             
  Folder folder = store.getFolder("inbox");
 
  if (!folder.exists()) {
  System.out.println("No INBOX...");
  System.exit(0);
  }
  folder.open(Folder.READ_WRITE);
  Message[] msg = folder.getMessages();
 
  for (int i = 0; i < msg.length; i++) {
  System.out.println("------------ Message " + (i + 1) + " ------------");
  String from = InternetAddress.toString(msg[i].getFrom());
  if (from != null) {
  System.out.println("From: " + from);
  }
 
  String replyTo = InternetAddress.toString(
  msg[i].getReplyTo());
  if (replyTo != null) {
  System.out.println("Reply-to: " + replyTo);
  }
  String to = InternetAddress.toString(
  msg[i].getRecipients(Message.RecipientType.TO));
  if (to != null) {
  System.out.println("To: " + to);
  }
 
  String subject = msg[i].getSubject();
  if (subject != null) {
  System.out.println("Subject: " + subject);
  }
  Date sent = msg[i].getSentDate();
  if (sent != null) {
  System.out.println("Sent: " + sent);
  }
 
  System.out.println();
  System.out.println("Message : ");
 
  Multipart multipart = (Multipart) msg[i].getContent();
   
  for (int x = 0; x < multipart.getCount(); x++) {
  BodyPart bodyPart = multipart.getBodyPart(x);
 
  String disposition = bodyPart.getDisposition();
 
  if (disposition != null && (disposition.equals(BodyPart.ATTACHMENT))) {
  System.out.println("Mail have some attachment : ");
 
  DataHandler handler = bodyPart.getDataHandler();
  System.out.println("file name : " + handler.getName());
  } else {
  System.out.println(bodyPart.getContent());
  }
  }
  System.out.println();
  }
  folder.close(true);
  store.close();
  }