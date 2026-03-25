package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

import sistemaGestionFront.utils.PdfGenerator;

import java.awt.Desktop;
import java.io.File;
import java.util.List;
import java.util.Map;

public class FacturaController {

    @FXML private Label lblFactura;
    @FXML private Label lblFecha;
    @FXML private Label lblCliente;

    @FXML private Label lblSubtotal;
    @FXML private Label lblDescuento;
    @FXML private Label lblTotal;

    @FXML private TableView<Map<String,Object>> tablaFactura;

    @FXML private TableColumn<Map<String,Object>,String> colProducto;
    @FXML private TableColumn<Map<String,Object>,Integer> colCantidad;
    @FXML private TableColumn<Map<String,Object>,Double> colPrecio;
    @FXML private TableColumn<Map<String,Object>,Double> colSubtotal;

    private Map<String,Object> ventaActual;


    // =====================================
    // CARGAR FACTURA
    // =====================================
    public void cargarFactura(Map<String,Object> venta){

        this.ventaActual = venta;

        lblFactura.setText(
                String.format("%06d",
                ((Number)venta.get("id")).intValue())
        );

        lblFecha.setText(
                venta.get("fecha").toString()
        );

        // ✅ CLIENTE CORREGIDO
        Object cliente = venta.get("cliente");

        if(cliente != null){

            Map clienteMap =
                    (Map) cliente;

            lblCliente.setText(
                    clienteMap.get("nombre")
                    .toString()
            );

        }else{

            lblCliente.setText(
                    "CONSUMIDOR FINAL"
            );

        }


        List<Map<String,Object>> detalles =
                (List<Map<String,Object>>) venta.get("detalles");


        colProducto.setCellValueFactory(c ->

                new SimpleStringProperty(

                        ((Map)c.getValue()
                                .get("producto"))
                                .get("nombre")
                                .toString()
                ));


        colCantidad.setCellValueFactory(c ->

                new SimpleObjectProperty<>(

                        ((Number)c.getValue()
                                .get("cantidad"))
                                .intValue()
                ));


        colPrecio.setCellValueFactory(c ->

                new SimpleObjectProperty<>(

                        ((Number)c.getValue()
                                .get("precioVendido"))
                                .doubleValue()
                ));


        colSubtotal.setCellValueFactory(c ->

                new SimpleObjectProperty<>(

                        ((Number)c.getValue()
                                .get("subtotal"))
                                .doubleValue()
                ));


        tablaFactura.getItems().setAll(detalles);


        double subtotal =
                ((Number)venta.get("totalSinDescuento"))
                        .doubleValue();

        double descuento =
                ((Number)venta.get("descuento"))
                        .doubleValue();

        double total =
                ((Number)venta.get("totalFinal"))
                        .doubleValue();


        lblSubtotal.setText(
                "Subtotal: $" + subtotal
        );

        lblDescuento.setText(
                "Descuento: -$" + descuento
        );

        lblTotal.setText(
                "TOTAL: $" + total
        );

    }


    // =====================================
    // GENERAR PDF REAL
    // =====================================
    @FXML
    private void imprimirFactura(){

        try{

            String ruta =
                    PdfGenerator.generarFactura(
                            ventaActual
                    );

            Desktop.getDesktop().open(
                    new File(ruta)
            );

        }
        catch(Exception e){

            e.printStackTrace();

            new Alert(
                    Alert.AlertType.ERROR,
                    "Error generando PDF"
            ).show();
        }

    }

}