package sistemaGestionFront.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class ReporteService {

    private final ObjectMapper mapper =
            new ObjectMapper();

    public Map<String,Object> obtenerGanancia(
            String inicio,
            String fin) {

        try {

            URL url = new URL(
              "http://localhost:8080/api/reportes/ganancia"
              + "?inicio=" + inicio
              + "&fin=" + fin
            );

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            InputStream is =
                    conn.getInputStream();

            return mapper.readValue(is, Map.class);

        } catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}