package sistemaGestionFront.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import sistemaGestionFront.models.ItemVenta;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class VentaService {

    private static final String BASE_URL =
            "http://localhost:8080/api/ventas";

    private final ObjectMapper mapper = new ObjectMapper();

    // =====================================
    // REGISTRAR VENTA
    // =====================================
    public Map<String, Object> enviarVenta(List<ItemVenta> carrito) {

        try {

            URL url = new URL(BASE_URL);
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            List<Map<String, Object>> items = new ArrayList<>();

            for (ItemVenta item : carrito) {

                Map<String, Object> map = new HashMap<>();
                map.put("productoId",
                        item.getProducto().getId());
                map.put("cantidad",
                        item.getCantidad());
                map.put("totalVendido",
                        item.getTotal());

                items.add(map);
            }

            Map<String, Object> body = new HashMap<>();
            body.put("items", items);

            String json = mapper.writeValueAsString(body);

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            if (conn.getResponseCode() == 200) {

                InputStream is = conn.getInputStream();
                return mapper.readValue(is, Map.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // =====================================
    // LISTAR TODAS LAS VENTAS
    // =====================================
    public List<Map<String, Object>> listarVentas() {

        try {

            URL url = new URL(BASE_URL);
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {

                InputStream is = conn.getInputStream();
                return mapper.readValue(is, List.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    // =====================================
    // FILTRAR POR FECHA (RANGO)
    // =====================================
    public List<Map<String, Object>> filtrarVentas(
            String fechaInicio,
            String fechaFin) {

        try {

            String urlFiltro =
                    BASE_URL +
                    "/filtrar?fechaInicio=" + fechaInicio +
                    "&fechaFin=" + fechaFin;

            URL url = new URL(urlFiltro);

            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == 200) {

                InputStream is = conn.getInputStream();
                return mapper.readValue(is, List.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}