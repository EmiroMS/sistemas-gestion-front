package sistemaGestionFront.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.json.JSONArray;
import org.json.JSONObject;
import sistemaGestionFront.models.Producto;
import sistemaGestionFront.services.EntradaService;
import sistemaGestionFront.services.ProductoService;

import java.util.List;

public class EntradaController {

    @FXML
    private ComboBox<Producto> comboProducto;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TableView<JSONObject> tablaEntradas;

    @FXML
    private TableColumn<JSONObject,String> colFecha;

    @FXML
    private TableColumn<JSONObject,String> colProducto;

    @FXML
    private TableColumn<JSONObject,String> colCantidad;

    @FXML
    private TableColumn<JSONObject,String> colPrecio;

    private final EntradaService service = new EntradaService();
    private final ProductoService productoService = new ProductoService();

    private List<Producto> productos;

    @FXML
    public void initialize(){

        cargarProductos();
        configurarTabla();
        cargarHistorial();

    }

    // =========================
    // CARGAR PRODUCTOS
    // =========================

    private void cargarProductos(){

        productos = productoService.listarProductos();

        comboProducto.setItems(
                FXCollections.observableArrayList(productos)
        );
    }

    // =========================
    // REGISTRAR ENTRADA
    // =========================

    @FXML
    private void registrarEntrada(){

        Producto producto =
                comboProducto.getSelectionModel().getSelectedItem();

        if(producto == null){
            new Alert(Alert.AlertType.WARNING,
                    "Seleccione un producto").show();
            return;
        }

        if(txtCantidad.getText().isEmpty()){
            new Alert(Alert.AlertType.WARNING,
                    "Ingrese la cantidad").show();
            return;
        }

        int cantidad;

        try{
            cantidad = Integer.parseInt(txtCantidad.getText());
        }catch(Exception e){
            new Alert(Alert.AlertType.WARNING,
                    "Cantidad inválida").show();
            return;
        }

        // SIN PRECIO NI MOTIVO
        service.registrar(
                producto.getId(),
                cantidad,
                0,
                ""
        );

        limpiar();
        cargarHistorial();

        new Alert(Alert.AlertType.INFORMATION,
                "Entrada registrada correctamente").show();
    }

    // =========================
    // HISTORIAL
    // =========================

    private void cargarHistorial(){

        JSONArray data = service.listar();

        tablaEntradas.setItems(
                FXCollections.observableArrayList()
        );

        for(int i=0;i<data.length();i++){

            tablaEntradas.getItems()
                    .add(data.getJSONObject(i));
        }
    }

    // =========================
    // CONFIGURAR TABLA
    // =========================

    private void configurarTabla(){

        colFecha.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue()
                         .getString("fecha")
                         .substring(0,16)));

        colProducto.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue()
                         .getJSONObject("producto")
                         .getString("nombre")));

        colCantidad.setCellValueFactory(
                c -> new SimpleStringProperty(
                        String.valueOf(
                                c.getValue()
                                 .getInt("cantidad"))));

        colPrecio.setCellValueFactory(
                c -> new SimpleStringProperty(
                        String.valueOf(
                                c.getValue()
                                 .getDouble("precioCompra"))));
    }

    private void limpiar(){

        txtCantidad.clear();
        comboProducto.getSelectionModel().clearSelection();
    }
}