package au.com.covermore;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;

public class LerMeusEmails {

      /* variables */
      
      /**
       * @param args
       * popServer - the url of the POP3 server eg. pop.iprimus.com.au
       * popUser - POP server user name
       * popPassword - password associated with the above username
       */
      public static void main(String[] args) 
      {
            // TODO Auto-generated method stub
            try
          {
                String popServer=args[0];
                String popUser=args[1];
                String popPassword=args[2];
                
                System.out.println("java EmailReader "
                            + popServer + " " + popUser + " " + popPassword);
                
                receive(popServer, popUser, popPassword);
          }
          catch (Exception ex)
          {
                System.out.println("Erro na leitura de email"
                            +" popServer popUser popPassword");
          }

          System.exit(0);

      }
      
      /**
       * this method will retrieve and read the emails from the INBOX
       * @param popServer
       * @param popUser
       * @param popPassword
       */
      public static void receive(String popServer, String popUser, String popPassword)
      {

            Store store=null;
            Folder folder=null;

          try
          {
                // -- Get hold of the default session --
                Properties props = System.getProperties();
                Session session = Session.getDefaultInstance(props, null);
                session.setDebug(true);
      
                // -- Get hold of a POP3 message store, and connect to it --
                //store = session.getStore("pop3");
                store = session.getStore("imap");
                store.connect(popServer, popUser, popPassword);
        
                // -- Try to get hold of the default folder --
                folder = store.getDefaultFolder();
                if (folder == null) throw new Exception("No default folder");
      
                // -- ...and its INBOX --
                folder = folder.getFolder("INBOX");
                if (folder == null) throw new Exception("No POP3 INBOX");
      
                // -- Open the folder for read only --
                folder.open(Folder.READ_ONLY);
      
                // -- Get the message wrappers and process them --
                Message[] msgs = folder.getMessages();
                
                System.out.println("ava au.com.covermore.EmailReader"
                            +" msgs " + msgs.length);
                
                for (int msgNum = 0; msgNum < msgs.length; msgNum++)
                {
                      printMessage(msgs[msgNum]);
                      //processMessage(msgs[msgNum]);
                }
      
          }
          catch (Exception ex)
          {
                ex.printStackTrace();
          }
          finally
          {
                // -- Close down nicely --
                try
                {
                      if (folder!=null) folder.close(false);
                      if (store!=null) store.close();
                }
                catch (Exception ex2) {ex2.printStackTrace();}
          }
      }
      
      /**
       * this method will print the message
       * @param message
       */
      public static void printMessage(Message message)
      {
            try
            {
                  // Get the header information
                  String from=((InternetAddress)message.getFrom()[0]).getPersonal();
                  if (from==null) from=((InternetAddress)message.getFrom()[0]).getAddress();
                  System.out.println("FROM: "+from);
                  
                  String subject=message.getSubject();
                  System.out.println("SUBJECT: "+subject);
                  
                  //String dateTime = message.getSentDate().toString();
                  System.out.println("DATE: "+ message.getSentDate());
                  
                  // -- Get the message part (i.e. the message itself) --
                  Part messagePart=message;
                  Object content=messagePart.getContent();
                  
                  // -- or its first body part if it is a multipart message --
                  if (content instanceof Multipart)
                  {
                        messagePart=((Multipart)content).getBodyPart(0);
                      System.out.println("[ Multipart Message ]");
                  }

                  // -- Get the content type --
                  String contentType=messagePart.getContentType();

                  // -- If the content is plain text, we can print it --
                  System.out.println("CONTENT:"+contentType);

                  if (contentType.startsWith("text/plain") || contentType.startsWith("text/html"))
                  {
                        InputStream is = messagePart.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String thisLine=reader.readLine();
                        
                        while (thisLine!=null)
                        {
                              System.out.println(thisLine);
                              thisLine=reader.readLine();
                        }
                  }

                  System.out.println("-----------------------------");
            }
            catch (Exception ex)
            {
                  ex.printStackTrace();
            }
      }
      
      /**
       * this method will process an email message and write
       * relevant contents into the xml file
       * @param message
       */
      private static void processMessage(Message message)
      {
            try
            {
                  // -- Get the message part (i.e. the message itself) --
                  Part messagePart=message;
                  Object content=messagePart.getContent();
            
                  // -- or its first body part if it is a multipart message --
                  if (content instanceof Multipart)
                  {
                        messagePart=((Multipart)content).getBodyPart(0);
                  }
                  
                  // -- Get the content type --
                  String contentType=messagePart.getContentType();
                  
                  if (contentType.startsWith("text/plain") || contentType.startsWith("text/html"))
                  {
                        InputStream is = messagePart.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                                                
                        
                        // now read the message into a List - each message line is a list item
                        // a List will be easier to manipulate and its indexed
                        // remove blank lines at the same time
                        List msgList = new LinkedList();
                        
                        String thisLine=reader.readLine();
                        while (thisLine!=null)
                        {
                              if(thisLine.trim().length()>0)
                                    msgList.add(thisLine);
                              thisLine=reader.readLine();
                        }
                        
                  }
            }
            catch(Exception x)
            {
                  x.printStackTrace();
            }
            
      }
      
}