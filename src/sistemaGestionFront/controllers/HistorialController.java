package sistemaGestionFront.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sistemaGestionFront.services.VentaService;

import java.util.List;
import java.util.Map;

public class HistorialController {

    @FXML private TableView<Map<String,Object>> tablaVentas;

    @FXML private TableColumn<Map<String,Object>, Integer> colId;
    @FXML private TableColumn<Map<String,Object>, String> colFecha;
    @FXML private TableColumn<Map<String,Object>, Double> colTotal;
    @FXML private TableColumn<Map<String,Object>, Void> colAcciones;

    // ✅ NUEVOS DATE PICKERS
    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;

    private final VentaService service = new VentaService();

    // =====================================
    // INIT
    // =====================================
    @FXML
    public void initialize() {

        colId.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        ((Number)c.getValue().get("id")).intValue()
                ));

        colFecha.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().get("fecha").toString()
                ));

        colTotal.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        ((Number)c.getValue()
                                .get("totalFinal"))
                                .doubleValue()
                ));

        agregarAcciones();
        cargarVentas();
    }

    // =====================================
    // CARGAR TODAS
    // =====================================
    @FXML
    public void cargarVentas() {

        List<Map<String,Object>> ventas =
                service.listarVentas();

        tablaVentas.getItems().setAll(ventas);
    }

    // =====================================
    // FILTRAR POR RANGO ✅
    // =====================================
    @FXML
    private void filtrar() {

        if (fechaInicio.getValue() == null ||
            fechaFin.getValue() == null) {

            new Alert(Alert.AlertType.WARNING,
                    "Seleccione ambas fechas")
                    .show();
            return;
        }

        String inicio =
                fechaInicio.getValue().toString();

        String fin =
                fechaFin.getValue().toString();

        List<Map<String,Object>> ventas =
                service.filtrarVentas(inicio, fin);

        tablaVentas.getItems().setAll(ventas);
    }

    // =====================================
    // BOTON VER FACTURA
    // =====================================
    private void agregarAcciones() {

        colAcciones.setCellFactory(param ->
                new TableCell<>() {

            private final Button btnVer =
                    new Button("Ver");

            {
                btnVer.setOnAction(e -> {

                    Map<String,Object> venta =
                            getTableView()
                                    .getItems()
                                    .get(getIndex());

                    abrirFactura(venta);
                });
            }

            @Override
            protected void updateItem(
                    Void item,
                    boolean empty) {

                super.updateItem(item, empty);

                setGraphic(empty
                        ? null
                        : new HBox(btnVer));
            }
        });
    }

    // =====================================
    // ABRIR FACTURA
    // =====================================
    private void abrirFactura(
            Map<String,Object> venta) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/sistemaGestionFront/views/factura.fxml"
                            ));

            Parent root = loader.load();

            FacturaController controller =
                    loader.getController();

            controller.cargarFactura(venta);

            Stage stage = new Stage();
            stage.setTitle("Factura");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}