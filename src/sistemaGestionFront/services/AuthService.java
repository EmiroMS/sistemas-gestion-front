package sistemaGestionFront.services;

import sistemaGestionFront.session.Sesion;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class AuthService {

    public static boolean login(String usuario, String password) {

        try {

            URL url = new URL("http://localhost:8080/api/usuarios/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                {
                  "usuario": "%s",
                  "password": "%s"
                }
                """.formatted(usuario, password);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            int responseCode = conn.getResponseCode();

            if (responseCode == 200) {

                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonResponse = new JSONObject(response.toString());

                JSONObject data = jsonResponse.getJSONObject("data");

                String rol = data.getString("rol");
                String nombre = data.getString("nombre");

                // GUARDAR DATOS EN SESION
                Sesion.setRol(rol);
                Sesion.setNombre(nombre);

                return true;
            }

            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}