package sistemaGestionFront.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import sistemaGestionFront.models.Cliente;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClienteService {

    private static final String BASE_URL =
            "http://localhost:8080/api/clientes";

    private final ObjectMapper mapper =
            new ObjectMapper();


    // =====================================
    // LISTAR CLIENTES
    // =====================================
    public List<Cliente> listar(){

        try{

            URL url =
            new URL(BASE_URL);

            HttpURLConnection conn =
            (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            if(conn.getResponseCode()==200){

                InputStream is =
                conn.getInputStream();

                return Arrays.asList(
                        mapper.readValue(
                                is,
                                Cliente[].class
                        )
                );

            }

        }
        catch(Exception e){

            e.printStackTrace();

        }

        return new ArrayList<>();

    }



    // =====================================
    // BUSCAR CLIENTES AUTOCOMPLETE
    // =====================================
    public List<Cliente> buscar(String nombre){

        try{

            URL url =
            new URL(
                    BASE_URL +
                    "/buscar?nombre=" +
                    URLEncoder.encode(nombre,"UTF-8")
            );

            HttpURLConnection conn =
            (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");

            if(conn.getResponseCode()==200){

                InputStream is =
                conn.getInputStream();

                return Arrays.asList(

                        mapper.readValue(
                                is,
                                Cliente[].class
                        )

                );

            }

        }
        catch(Exception e){

            e.printStackTrace();

        }

        return new ArrayList<>();

    }



    // =====================================
    // CREAR CLIENTE NUEVO
    // =====================================
    public Cliente crear(String nombre){

        try{

            URL url =
            new URL(BASE_URL);

            HttpURLConnection conn =
            (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty(
                    "Content-Type",
                    "application/json"
            );

            conn.setDoOutput(true);

            Cliente cliente =
                    new Cliente();

            cliente.setNombre(nombre);

            String json =
                    mapper.writeValueAsString(cliente);

            OutputStream os =
                    conn.getOutputStream();

            os.write(json.getBytes());

            os.flush();

            os.close();

            if(conn.getResponseCode()==200 ||
               conn.getResponseCode()==201){

                InputStream is =
                conn.getInputStream();

                return mapper.readValue(
                        is,
                        Cliente.class
                );

            }

        }
        catch(Exception e){

            e.printStackTrace();

        }

        return null;

    }

}