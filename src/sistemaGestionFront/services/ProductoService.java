package sistemaGestionFront.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import sistemaGestionFront.models.Producto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ProductoService {

    private static final String URL_BASE = "http://localhost:8080/api/productos";
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public List<Producto> listarProductos() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(
                    response.body(),
                    new TypeReference<List<Producto>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException("Error al listar productos", e);
        }
    }

    public Producto guardar(Producto producto) {
        try {
            String json = mapper.writeValueAsString(producto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            return mapper.readValue(response.body(), Producto.class);

        } catch (Exception e) {
            throw new RuntimeException("Error al guardar producto", e);
        }
    }

    public void eliminar(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_BASE + "/" + id))
                    .DELETE()
                    .build();

            client.send(request, HttpResponse.BodyHandlers.discarding());

        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar producto", e);
        }
    }
}