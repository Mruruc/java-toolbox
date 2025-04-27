# 📧 How to Send Email from a Java Application?

Sending emails is a common task in many applications, from sending confirmations and notifications to distributing newsletters. If you're a Java developer, you'll be glad to know that Java provides a standard, powerful way to handle email operations.

This repository will guide you through the process, showing you how to send different types of emails using the standard **Jakarta EE Mail API**.

## ✨ What You Will Learn

By exploring this repository, you will learn how to:

1.  Send simple text-based emails.
2.  Send visually appealing HTML emails (using a templating engine).
3.  Send more complex emails that include both HTML content and file attachments.
4.  Understand the core components needed for email communication in Java.

## 🛠️ Getting Started: The Essential Tools

To send emails from Java, we rely on the **Jakarta Mail API**. Think of this as the official standard library for handling email in Java. It provides everything you need to compose, send, and manage email messages.

You'll typically need two main pieces (dependencies) in your project:

### 1. The Blueprint (`jakarta.mail-api`)

* **What it is:** This dependency provides the core **interfaces** and **classes** that define *how* you interact with email functionality in Java. It's like the blueprint that specifies methods for creating a message, connecting to a mail server, etc.
* **Why you need it:** Your code will be written using these interfaces. This makes your code independent of the specific email library implementation you use.

### 2. The Engine (`jakarta.mail`)

* **What it is:** This dependency provides the **actual working code** that *implements* the blueprint defined by `jakarta.mail-api`. It contains the logic to connect to mail servers using protocols like SMTP (for sending), IMAP, or POP3 (for receiving, though this repository focuses on sending). Currently, the Eclipse Angus project provides a popular implementation.
* **Why you need it:** While the API tells you *what* you can do, the implementation provides the *engine* that performs the actions, like connecting to an SMTP server and sending the email bytes over the network.

Together, the API provides the structure you code against, and the implementation makes it work.

## 📁 Exploring the Code Examples

This repository includes example service classes that demonstrate how to send different email types:

* `SimpleEmailService`: Learn how to construct and send a basic email containing just plain text in the body.
* `HtmlEmailService`: Discover how to create emails with rich HTML content. This example also shows how you can integrate a templating engine like Thymeleaf to build dynamic HTML email bodies.
* `AttachmentEmailService`: See how to send advanced emails that combine different parts, such as an HTML body along with one or more file attachments.

## Dissecting the Core Process

Sending an email programmatically involves several steps, typically orchestrated using the classes provided by the Jakarta Mail API. Here's a look at the common parts you'll interact with:

1.  **Setting up the Mail Session:** You need to configure connection properties for your mail server (like the SMTP host, port, and authentication details). This configuration is managed by a `Session` object.
2.  **Creating the Message:** An email message is represented by a `MimeMessage` object. This object holds all the email's details.
3.  **Adding Recipients:** You specify who the email is for using `InternetAddress` objects and add them to the message using types like `TO`, `CC` (Carbon Copy), and `BCC` (Blind Carbon Copy).
4.  **Setting the Subject:** Provide a clear subject line for the email.
5.  **Setting the Content:** This is where you put the body of the email. It can be plain text, HTML, or a combination of different parts (like text, HTML, and attachments). You specify the content type (e.g., `text/plain`, `text/html`). For emails with multiple parts (like HTML + attachment), you'll use `MimeMultipart`.
6.  **Adding Headers (Optional but Useful):** You can add various headers to an email for more control or information. Some common headers include:
    * `From`: The sender's email address.
    * `SentDate`: The date the email was sent.
    * `Reply-To`: The address replies should be sent to.
    * `Importance`: Indicates the importance level (High, Normal, Low).
    * `List-Unsubscribe`: Provides recipients with an easy way to unsubscribe from mailing lists.
    * `X-Mailer`: Indicates the software used to send the email.
7.  **Sending the Message:** Once the message is fully composed, you use the `Transport` class to connect to the mail server and send the message.

The example code provided in the repository demonstrates how to put these pieces together for different email scenarios.

Feel free to explore the service classes in this repository to see how these steps are implemented for sending simple, HTML, and attachment emails!

-----
### Comprehensive Example Code

Here is a more comprehensive example demonstrating how to set various recipients, subject, content type, and numerous headers for an email message:

```java
   public void sendEmail() {
        try {
            // Assuming 'session' is a pre-configured jakarta.mail.Session object
            MimeMessage message = new MimeMessage(session);

            // 1. From
            message.setFrom(new InternetAddress("sender@example.com"));

            // 2. To
            message.addRecipient(Message.RecipientType.TO, new InternetAddress("recipient@example.com"));

            // 3. CC
            message.addRecipient(Message.RecipientType.CC, new InternetAddress("ccrecipient@example.com"));

            // 4. BCC
            message.addRecipient(Message.RecipientType.BCC, new InternetAddress("bccrecipient@example.com"));

            // 5. Subject
            message.setSubject("Advanced Email with Multiple Headers", "utf-8");

            // 6. Sent Date
            message.setSentDate(new Date());

            // 7. Reply-To
            Address[] replyTo = {new InternetAddress("replyto@example.com")};
            message.setReplyTo(replyTo);

            // 8. List-Unsubscribe
            message.setHeader("List-Unsubscribe", "<mailto:unsubscribe@example.com>, [https://example.com/unsubscribe](https://example.com/unsubscribe)");

            // 9. Importance
            // Assuming Importance is an enum or class providing values like "high"
            // message.setHeader("Importance", Importance.HIGH.getValue());
            // Using a string literal for simplicity here
            message.setHeader("Importance", "high");


            // 10. X-Priority
            message.setHeader("X-Priority", "1"); // Often corresponds to high importance

            // 11. Disposition-Notification-To (Requesting a read receipt)
            message.setHeader("Disposition-Notification-To", "sender@example.com");

            // 12. X-Mailer
            message.setHeader("X-Mailer", "Jakarta Mail API");

            // 13. Content-Type (HTML)
            String htmlContent = "<h1>Welcome!</h1><p>This is an advanced email.</p>";
            message.setContent(htmlContent, "text/html; charset=utf-8");

            // 14. Custom Header
            message.setHeader("X-Custom-Header", "CustomValue");

            // 15. Send the email
            Transport.send(message);

            // LOGGER.info("Advanced Email sent successfully."); // Assuming LOGGER is defined
            System.out.println("Advanced Email sent successfully."); // Using System.out for example

        } catch (MessagingException exception) {
            // exception.printStackTrace(); // Avoid printing stack traces in production logs typically
            // throw new RuntimeException("Failed to send advanced email.", e); // Assuming 'e' was the variable name
            System.err.println("Failed to send advanced email: " + exception.getMessage()); // Using System.err for example
            exception.printStackTrace(); // Still useful for debugging
            throw new RuntimeException("Failed to send advanced email.", exception); // Correct exception variable
        } catch (Exception e) {
             System.err.println("An unexpected error occurred: " + e.getMessage());
             e.printStackTrace();
             throw new RuntimeException("An unexpected error occurred.", e);
        }
    }