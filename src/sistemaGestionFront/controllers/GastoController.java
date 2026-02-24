package sistemaGestionFront.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sistemaGestionFront.services.GastoService;

import java.util.List;
import java.util.Map;

public class GastoController {

    @FXML private TextField txtDescripcion;
    @FXML private TextField txtMonto;
    @FXML private ComboBox<String> comboCategoria;

    @FXML private TableView<Map<String,Object>> tablaGastos;

    @FXML private TableColumn<Map<String,Object>,Integer> colId;
    @FXML private TableColumn<Map<String,Object>,String> colDescripcion;
    @FXML private TableColumn<Map<String,Object>,Double> colMonto;
    @FXML private TableColumn<Map<String,Object>,String> colFecha;

    private final GastoService service =
            new GastoService();

    @FXML
    public void initialize(){

        comboCategoria.setItems(
                FXCollections.observableArrayList(
                        "NOMINA",
                        "SERVICIOS",
                        "ARRIENDO",
                        "TRANSPORTE",
                        "OTROS"
                )
        );

        colId.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        ((Number)c.getValue()
                                .get("id")).intValue()));

        colDescripcion.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue()
                                .get("descripcion")
                                .toString()));

        colMonto.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        ((Number)c.getValue()
                                .get("monto"))
                                .doubleValue()));

        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue()
                                .get("fecha")
                                .toString()));

        cargarGastos();
    }

    // ==========================
    // GUARDAR
    // ==========================
    @FXML
    private void guardarGasto(){

        if(txtDescripcion.getText().isEmpty()
           || txtMonto.getText().isEmpty()
           || comboCategoria.getValue()==null){

            new Alert(Alert.AlertType.WARNING,
                    "Complete los campos")
                    .show();
            return;
        }

        service.guardar(
                txtDescripcion.getText(),
                Double.parseDouble(
                        txtMonto.getText()),
                comboCategoria.getValue()
        );

        txtDescripcion.clear();
        txtMonto.clear();

        cargarGastos();
    }

    // ==========================
    // RECARGAR TABLA
    // ==========================
    private void cargarGastos(){

        List<Map<String,Object>> gastos =
                service.listar();

        tablaGastos.setItems(
                FXCollections.observableArrayList(
                        gastos));
    }
}