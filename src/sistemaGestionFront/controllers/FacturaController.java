package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sistemaGestionFront.session.Sesion;
import sistemaGestionFront.utils.PdfGenerator;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FacturaController {

    @FXML private Label lblEmpresa;
    @FXML private Label lblNit;
    @FXML private Label lblFecha;
    @FXML private Label lblFactura;
    @FXML private Label lblVendedor;

    @FXML private VBox boxItems;

    @FXML private Label lblSubtotal;
    @FXML private Label lblDescuento;
    @FXML private Label lblTotal;

    private Map<String,Object> ventaActual;

    // formato moneda colombiana
    private final NumberFormat moneda =
            NumberFormat.getCurrencyInstance(new Locale("es","CO"));

    // formato fecha
    private final DateTimeFormatter formatoFecha =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // =====================================
    // CARGAR FACTURA
    // =====================================
    public void cargarFactura(Map<String,Object> venta) {

        this.ventaActual = venta;

        // DATOS EMPRESA
        lblEmpresa.setText("LICOSOFT HM");
        lblNit.setText("NIT: 900123456-7");

        // VENDEDOR
        lblVendedor.setText("Vendedor: " + Sesion.getNombre());

        int idVenta =
                ((Number)venta.get("id")).intValue();

        // NUMERO CONSECUTIVO
        String numeroFactura =
                String.format("%08d", idVenta);

        lblFactura.setText("FACTURA POS #" + numeroFactura);

        // FECHA
        LocalDateTime fecha =
                LocalDateTime.parse(
                        venta.get("fecha").toString()
                );

        lblFecha.setText(
                "Fecha: " + fecha.format(formatoFecha)
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
                            " x " +
                            moneda.format(precio) +
                            "        " +
                            moneda.format(subtotal)
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

        lblSubtotal.setText(
                "SUBTOTAL: " +
                moneda.format(subtotal)
        );

        lblDescuento.setText(
                "DESCUENTO: -" +
                moneda.format(descuento)
        );

        lblTotal.setText(
                "TOTAL: " +
                moneda.format(total)
        );
    }

    // =====================================
    // IMPRIMIR + PDF
    // =====================================
    @FXML
    private void imprimirFactura() {

        try {

            String rutaPDF =
                    PdfGenerator.generarFactura(ventaActual);

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