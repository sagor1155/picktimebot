import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class SlotFinder {

    public void findAvailableSlots(LocalDate startDate, LocalDate endDate) throws IOException, InterruptedException {
        HashMap<String, String> paramsMap = buildDefaultParams();
        System.out.println("<------------------------------------------------->");

        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            // get epoch time in millisecond
            long timeInMillis = getEpochTimeNow();
            paramsMap.put("_", String.valueOf(timeInMillis));

            // format current date
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYYMMdd");
            paramsMap.put("dateAndTime", formatter.format(date));

            // request
            Response response = sendRequest(buildUrl(paramsMap));

            // print
            System.out.println(response);
            System.out.println("[EPOCH] " + timeInMillis);
            System.out.println("[DATE]  " + date);
            System.out.println("[SCHEDULE AVAILABLE] " + (response.data.length != 0));
            System.out.println("<------------------------------------------------->");

            if (response.data.length !=0 ) {
                System.out.println("FOUND SCHEDULE ON: " + date);
                break;
            }

            // TODO: add delay
        }
    }

    private long getEpochTimeNow() {
        Instant instant = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        return instant.toEpochMilli();
    }

    private HashMap<String, String> buildDefaultParams() {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("_", "1694501674745"); // epoch time now: update
        paramsMap.put("dateAndTime", "202309180000"); // schedule date: update
        paramsMap.put("schedulerId", "a8587abd-6d91-46f1-b8c8-566507679976");
        paramsMap.put("locationId", "50364cd4-4914-456b-93e3-5432ee3ccd33");
        paramsMap.put("duration", "15");
        paramsMap.put("slot", "15");
        paramsMap.put("offBooking", "false");
        paramsMap.put("eventType", "appointment");
        paramsMap.put("serviceClassId", "f396e7c7-be16-4367-bb82-e6c73f6b2bed");
        paramsMap.put("accountId", "79ff6077-3b18-4eb7-b535-37fd22e451f5");
        paramsMap.put("timezone", "America%2FChicago");
        return paramsMap;
    }

    private String buildUrl(HashMap<String, String> paramsMap) {
        StringBuilder url = new StringBuilder("https://www.picktime.com/book/slots?");
        for (String key: paramsMap.keySet()) {
            url.append(key).append("=").append(paramsMap.get(key)).append("&");
        }
        return url.toString();
    }

    private Response sendRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(response.body(), Response.class);
    }
}
