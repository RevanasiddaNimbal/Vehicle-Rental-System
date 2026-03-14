package notification.model;

public class EmailMessage {
    private String to;
    private String subject;
    private String body;

    public EmailMessage(String to, String subject, String body) {
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getTo() {
        return to;
    }

    public String getMessageBody() {
        return body;
    }

    public String getSubject() {
        return subject;
    }
}
