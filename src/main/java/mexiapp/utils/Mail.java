package mexiapp.utils;

import mexiapp.data.Account;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class Mail {

    private static final String FROM = "citas_sre@sre.gob.mx";

    public static final String SUBJECT_1 = "Notificación Citas SRE: Código Seguridad y Token";
    public static final String SUBJECT_2 = "Notificación Citas SRE: Confirmación de cita";

    private String username;
    private String password;

    private static Mail instance = null;

    private Mail() {
        Account account = H2.getInstance().getAccount();
        username = account.getUsername();
        password = account.getPassword();
    }

    public static Mail getInstance() {
        if (instance == null) {
            instance = new Mail();
        }

        return instance;
    }

    public String readNext() {
        String subject = null;

        try {
            // Connect store
            Store store = connect(username, password);

            // Open the Folder
            Folder folder = store.getFolder("INBOX");

            // try to open read/write and if that fails try read-only
            try {
                folder.open(Folder.READ_WRITE);
            } catch (MessagingException ex) {
                folder.open(Folder.READ_ONLY);
            }

            // Attributes & Flags for all messages ..
            for (Message m : folder.getMessages()) {
                // FROM
                InternetAddress[] addresses = (InternetAddress[]) m.getFrom();
                if (addresses != null) {
                    for (InternetAddress address : addresses)
                        if (address.getAddress().equals(FROM)) {
                            switch (m.getSubject()) {
                                case SUBJECT_1:
                                    saveFile(m);
                                    subject = SUBJECT_1;
                                    break;
                                case SUBJECT_2:
                                    saveFile(m);
                                    subject = SUBJECT_2;
                                    break;
                            }
                            break;
                        }
                }

//                m.setFlag(Flags.Flag.DELETED, true);
            }

            folder.close();
            store.close();
        } catch (MessagingException e) {
            Log.getInstance().exception(e);
        }

        return subject;
    }

    public static Store connect(String user, String password) {
        // Get a Properties object
        Properties props = new Properties();
        props.setProperty("mail.pop3.ssl.enable", "true");
//      props.setProperty("mail.imap.ssl.enable", "true");

        // Get a Session object
        Session session = Session.getInstance(props);

        Store store = null;
        try {
            // Get a Store object
            store = session.getStore("pop3");
            // Connect
            store.connect("pop.gmail.com", user, password);
        } catch (MessagingException e) {
            Log.getInstance().exception(e);
        }
        return store;
    }

    private static void saveFile(Part p) {
        try {
            /*
             * Using isMimeType to determine the content type avoids
             * fetching the actual content data until we need it.
             */
            if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();

                String filename;
                int count = mp.getCount();
                for (int i = 0; i < count; i++) {
                    p = mp.getBodyPart(i);
                    filename = p.getFileName();
                    boolean isPdf = p.isMimeType("application/pdf");

                    if (isPdf) {
                        /*
                         * If we're saving attachments, write out anything that
                         * looks like an attachment into an appropriately named
                         * file.  Don't overwrite existing files to prevent
                         * mistakes.
                         */
                        if (p instanceof MimeBodyPart && !p.isMimeType("multipart/*")) {
                            String disp = p.getDisposition();
                            // many mailers don't include a Content-Disposition
                            if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                                if (filename == null)
                                    filename = "Attachment";
                                else
                                    filename = "attachments/" + filename;
                                try {
                                    //                                File f = new File(filename);
                                    ((MimeBodyPart) p).saveFile(filename);
                                } catch (IOException ex) {
                                    // TODO: Write a file to save error
                                }
                            }
                        }
                    }
                }
            }
        } catch (MessagingException | IOException e) {
            Log.getInstance().exception(e);
        }
    }

    public static boolean checkAccount(String user, String password) {
        Store store = connect(user, password);
        boolean flag = store != null && store.isConnected();
        if (flag) {
            try {
                store.close();
            } catch (MessagingException e) {
                Log.getInstance().exception(e);
            }
        }
        return flag;
    }

    public void send(String subject) {
        try {
            String encode = Base64.getEncoder().encodeToString((username + " " + password).getBytes());

            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.port","465");
            props.setProperty("mail.smtp.ssl.enable", "true");

            Session session = Session.getInstance(props);
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("yordan.mexitel@gmail.com", false));
            msg.setSubject(subject);
            msg.setText(encode);

            // set the message content here
            Transport.send(msg, username, password);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

