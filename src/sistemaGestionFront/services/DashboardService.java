package sistemaGestionFront.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class DashboardService {

    private JSONArray obtenerVentas(){

        try{

            URL url =
                    new URL("http://localhost:8080/api/ventas");

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

            while((line=br.readLine())!=null){
                response.append(line);
            }

            return new JSONArray(response.toString());

        }catch(Exception e){
            e.printStackTrace();
        }

        return new JSONArray();
    }

    // =========================
    // VENTAS POR DIA
    // =========================

    public Map<String,Double> obtenerVentasPorDia(){

        Map<String,Double> mapa =
                new HashMap<>();

        JSONArray ventas = obtenerVentas();

        for(int i=0;i<ventas.length();i++){

            JSONObject v = ventas.getJSONObject(i);

            String fecha =
                    v.getString("fecha")
                     .substring(0,10);

            double total =
                    v.getDouble("totalFinal");

            mapa.put(
                    fecha,
                    mapa.getOrDefault(fecha,0.0)+total
            );
        }

        return mapa;
    }

    // =========================
    // PRODUCTOS MAS VENDIDOS
    // =========================

    public Map<String,Integer> obtenerProductosMasVendidos(){

        Map<String,Integer> mapa =
                new HashMap<>();

        JSONArray ventas = obtenerVentas();

        for(int i=0;i<ventas.length();i++){

            JSONObject venta =
                    ventas.getJSONObject(i);

            JSONArray detalles =
                    venta.getJSONArray("detalles");

            for(int j=0;j<detalles.length();j++){

                JSONObject d =
                        detalles.getJSONObject(j);

                String nombre =
                        d.getJSONObject("producto")
                         .getString("nombre");

                int cantidad =
                        d.getInt("cantidad");

                mapa.put(
                        nombre,
                        mapa.getOrDefault(nombre,0)+cantidad
                );
            }
        }

        return mapa;
    }

    // =========================
    // TOTAL VENTAS HOY
    // =========================

    public double obtenerVentasHoy(){

        JSONArray ventas = obtenerVentas();

        double total = 0;

        String hoy =
                LocalDate.now().toString();

        for(int i=0;i<ventas.length();i++){

            JSONObject v =
                    ventas.getJSONObject(i);

            String fecha =
                    v.getString("fecha")
                     .substring(0,10);

            if(fecha.equals(hoy)){

                total += v.getDouble("totalFinal");

            }
        }

        return total;
    }

    // =========================
    // GANANCIA HOY
    // =========================

    public double obtenerGananciaHoy(){

        JSONArray ventas = obtenerVentas();

        double ganancia = 0;

        String hoy =
                LocalDate.now().toString();

        for(int i=0;i<ventas.length();i++){

            JSONObject venta =
                    ventas.getJSONObject(i);

            String fecha =
                    venta.getString("fecha")
                         .substring(0,10);

            if(fecha.equals(hoy)){

                JSONArray detalles =
                        venta.getJSONArray("detalles");

                for(int j=0;j<detalles.length();j++){

                    JSONObject d =
                            detalles.getJSONObject(j);

                    int cantidad =
                            d.getInt("cantidad");

                    double precioVenta =
                            d.getDouble("precioVendido");

                    double precioCosto =
                            d.getJSONObject("producto")
                             .getDouble("precioCosto");

                    ganancia +=
                            (precioVenta - precioCosto) * cantidad;
                }
            }
        }

        return ganancia;
    }
}