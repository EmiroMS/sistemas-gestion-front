package sistemaGestionFront.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class GastoService {

    private final ObjectMapper mapper =
            new ObjectMapper();

    private final String URL_API =
            "http://localhost:8080/api/gastos";

    // ===============================
    // GUARDAR
    // ===============================
    public void guardar(
            String descripcion,
            double monto,
            String categoria) {

        try {

            URL url = new URL(URL_API);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty(
                    "Content-Type",
                    "application/json");

            conn.setDoOutput(true);

            Map<String,Object> body =
                    new HashMap<>();

            body.put("descripcion", descripcion);
            body.put("monto", monto);
            body.put("categoria", categoria);

            String json =
                    mapper.writeValueAsString(body);

            OutputStream os =
                    conn.getOutputStream();

            os.write(json.getBytes());
            os.close();

            conn.getInputStream();

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // ===============================
    // LISTAR
    // ===============================
    public List<Map<String,Object>> listar(){

        try{

            URL url = new URL(URL_API);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            InputStream is =
                    conn.getInputStream();

            return mapper.readValue(is,List.class);

        }catch(Exception e){
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}