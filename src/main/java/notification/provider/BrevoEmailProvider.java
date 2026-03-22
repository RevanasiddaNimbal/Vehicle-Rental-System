package notification.provider;

import config.EmailPropertiesConfig;
import exception.DataAccessException;
import notification.model.EmailMessage;
import notification.service.EmailService;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class BrevoEmailProvider implements EmailService {
    private final String apiKey;
    private final String senderEmail;
    private final String senderName;
    private final String apiUrl;

    public BrevoEmailProvider(EmailPropertiesConfig config) {
        this.apiKey = config.getApiKey();
        this.senderEmail = config.getSenderEmail();
        this.senderName = config.getSenderName();
        this.apiUrl = config.getApiUrl();
    }

    @Override
    public boolean sendEmail(EmailMessage message) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("api-key", apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            String jsonBody = String.format(
                    "{\"sender\":{\"name\":\"%s\",\"email\":\"%s\"}," +
                            "\"to\":[{\"email\":\"%s\"}]," +
                            "\"subject\":\"%s\"," +
                            "\"textContent\":\"%s\"}",
                    senderName,
                    senderEmail,
                    message.getTo(),
                    message.getSubject(),
                    message.getMessageBody().replace("\n", "\\n")
            );


            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200 || responseCode == 201) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw new DataAccessException("Failed to send Email.", e);
        }
    }
}
