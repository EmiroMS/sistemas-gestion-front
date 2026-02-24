package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sistemaGestionFront.utils.PdfGenerator;

import java.util.List;
import java.util.Map;

public class FacturaController {

    @FXML private Label lblFecha;
    @FXML private Label lblFactura;

    @FXML private VBox boxItems;

    @FXML private Label lblSubtotal;
    @FXML private Label lblDescuento;
    @FXML private Label lblTotal;

    // ✅ guardamos venta actual para PDF
    private Map<String,Object> ventaActual;

    // =====================================
    // CARGAR FACTURA
    // =====================================
    public void cargarFactura(Map<String,Object> venta) {

        this.ventaActual = venta;

        lblFactura.setText(
                String.format("Factura #: %06d",
                        ((Number)venta.get("id")).intValue())
        );

        lblFecha.setText(
                "Fecha: " + venta.get("fecha")
        );

        List<Map<String,Object>> detalles =
                (List<Map<String,Object>>) venta.get("detalles");

        boxItems.getChildren().clear();

        for (Map<String,Object> d : detalles) {

            Map producto =
                    (Map)d.get("producto");

            String nombre =
                    producto.get("nombre").toString();

            int cantidad =
                    ((Number)d.get("cantidad")).intValue();

            double precio =
                    ((Number)d.get("precioVendido"))
                            .doubleValue();

            double subtotal =
                    ((Number)d.get("subtotal"))
                            .doubleValue();

            Label linea1 =
                    new Label(nombre);

            linea1.setStyle("-fx-font-weight:bold;");

            Label linea2 =
                    new Label(
                            cantidad +
                            " x $" + precio +
                            "      $" + subtotal
                    );

            boxItems.getChildren()
                    .addAll(linea1, linea2);
        }

        double subtotal =
                ((Number)venta.get("totalSinDescuento"))
                        .doubleValue();

        double descuento =
                ((Number)venta.get("descuento"))
                        .doubleValue();

        double total =
                ((Number)venta.get("totalFinal"))
                        .doubleValue();

        lblSubtotal.setText("Subtotal: $" + subtotal);
        lblDescuento.setText("Descuento: -$" + descuento);
        lblTotal.setText("TOTAL: $" + total);
    }

    // =====================================
    // IMPRIMIR + GENERAR PDF ✅
    // =====================================
    @FXML
    private void imprimirFactura() {

        try {

            // ✅ Generar PDF primero
            String rutaPDF =
                    PdfGenerator.generarFactura(ventaActual);

            // ✅ Abrir PDF automáticamente
            java.awt.Desktop.getDesktop()
                    .open(new java.io.File(rutaPDF));

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "Factura lista para imprimir"
            ).showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}