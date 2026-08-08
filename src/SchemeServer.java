import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;

public class SchemeServer {
    public static void main(String[] args) throws Exception {
        // 1. Start a simple server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        SchemeDAO dao = new SchemeDAO();

        // 2. Create an endpoint that the frontend can call
        server.createContext("/api/recommend", exchange -> {
            // Allow the frontend HTML file to call this API without CORS errors
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            
            try {
                // Read the form data sent by the frontend
                String body = new String(exchange.getRequestBody().readAllBytes());
                
                // Very basic manual parsing (e.g. age=22&income=50000...)
                String[] parts = body.split("&");
                int age = 0; double income = 0; 
                String gender = "", category = "", occupation = "", state = "";
                
                for (String part : parts) {
                    String[] kv = part.split("=");
                    if (kv.length < 2) continue;
                    String key = kv[0];
                    String val = kv[1]; // Replace '+' with space for basic URL decoding
                    
                    if (key.equals("age")) age = Integer.parseInt(val);
                    if (key.equals("income")) income = Double.parseDouble(val);
                    if (key.equals("gender")) gender = val;
                    if (key.equals("category")) category = val;
                    if (key.equals("occupation")) occupation = val;
                    if (key.equals("state")) state = val.replace("+", " ");
                }

                // Call our database layer
                ArrayList<Scheme> schemes = dao.getEligibleSchemes(age, income, gender, category, occupation, state);
                
                // Build a simple JSON string to send back to the frontend
                String json = "[";
                for (int i = 0; i < schemes.size(); i++) {
                    Scheme s = schemes.get(i);
                    json += "{\"name\": \"" + s.getSchemeName() + "\", \"description\": \"" + s.getDescription() + "\"}";
                    if (i < schemes.size() - 1) json += ",";
                }
                json += "]";

                // Send the JSON response
                byte[] responseBytes = json.getBytes();
                exchange.sendResponseHeaders(200, responseBytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(responseBytes);
                os.close();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Start listening for requests
        server.start();
        System.out.println("Basic API Server is running on port 8080...");
        System.out.println("You can now double-click index.html to use the app.");
    }
}
