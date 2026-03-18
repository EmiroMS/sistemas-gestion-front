package sistemaGestionFront.services;

import sistemaGestionFront.models.Usuario;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    // ================================
    // LISTAR USUARIOS
    // ================================
    public List<Usuario> listarUsuarios() {

        List<Usuario> lista = new ArrayList<>();

        try {

            URL url =
                    new URL("http://localhost:8080/api/usuarios");

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            BufferedReader br =
                    new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));

            StringBuilder response =
                    new StringBuilder();

            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            JSONArray json =
                    new JSONArray(response.toString());

            for (int i = 0; i < json.length(); i++) {

                JSONObject obj =
                        json.getJSONObject(i);

                Usuario u = new Usuario();

                u.setId(obj.getLong("id"));
                u.setNombre(obj.getString("nombre"));
                u.setUsuario(obj.getString("usuario"));
                u.setRol(obj.getString("rol"));
                u.setActivo(obj.getBoolean("activo"));

                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // ================================
    // REGISTRAR USUARIO
    // ================================
    public void registrar(Usuario u) {

        try {

            URL url =
                    new URL("http://localhost:8080/api/usuarios/registro");

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
                    {
                      "nombre": "%s",
                      "usuario": "%s",
                      "password": "%s",
                      "rol": "%s"
                    }
                    """.formatted(
                    u.getNombre(),
                    u.getUsuario(),
                    u.getPassword(),
                    u.getRol()
            );

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
            }

            conn.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================================
    // ELIMINAR USUARIO
    // ================================
    public void eliminar(Long id) {

        try {

            URL url =
                    new URL("http://localhost:8080/api/usuarios/" + id);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("DELETE");

            conn.getResponseCode();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}