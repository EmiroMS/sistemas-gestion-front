package sistemaGestionFront.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class EntradaService {

	// =========================
	// REGISTRAR ENTRADA
	// =========================

	public void registrar(Long productoId, int cantidad, double precio, String motivo) {

		try {

			URL url = new URL("http://localhost:8080/api/entradas");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");

			conn.setRequestProperty("Content-Type", "application/json");

			conn.setDoOutput(true);

			JSONObject json = new JSONObject();

			JSONObject producto = new JSONObject();
			producto.put("id", productoId);

			json.put("producto", producto);
			json.put("cantidad", cantidad);
			json.put("precioCompra", precio);
			json.put("motivo", motivo);

			try (OutputStream os = conn.getOutputStream()) {
				os.write(json.toString().getBytes());
			}

			int responseCode = conn.getResponseCode();

			System.out.println("Respuesta API: " + responseCode);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// =========================
	// HISTORIAL
	// =========================

	public JSONArray listar() {

		try {

			URL url = new URL("http://localhost:8080/api/entradas");

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("GET");

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			StringBuilder response = new StringBuilder();

			String line;

			while ((line = br.readLine()) != null) {
				response.append(line);
			}

			return new JSONArray(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JSONArray();
	}
}