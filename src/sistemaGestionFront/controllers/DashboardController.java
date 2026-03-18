package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import sistemaGestionFront.services.DashboardService;

import java.util.Map;

public class DashboardController {

    @FXML
    private BarChart<String, Number> chartVentas;

    @FXML
    private PieChart chartProductos;

    @FXML
    private Label lblVentasHoy;

    @FXML
    private Label lblGananciaHoy;

    private final DashboardService service =
            new DashboardService();

    @FXML
    public void initialize(){

        cargarVentasPorDia();
        cargarProductosMasVendidos();
        cargarVentasHoy();
        cargarGananciaHoy();

    }

    // =========================
    // VENTAS POR DIA
    // =========================

    private void cargarVentasPorDia(){

        XYChart.Series<String, Number> serie =
                new XYChart.Series<>();

        Map<String,Double> ventas =
                service.obtenerVentasPorDia();

        ventas.forEach((fecha,total)->{

            serie.getData().add(
                    new XYChart.Data<>(fecha,total));

        });

        chartVentas.getData().add(serie);
    }

    // =========================
    // PRODUCTOS MAS VENDIDOS
    // =========================

    private void cargarProductosMasVendidos(){

        Map<String,Integer> productos =
                service.obtenerProductosMasVendidos();

        productos.forEach((nombre,cantidad)->{

            chartProductos.getData().add(
                    new PieChart.Data(nombre,cantidad));

        });
    }

    // =========================
    // TOTAL VENTAS HOY
    // =========================

    private void cargarVentasHoy(){

        double total =
                service.obtenerVentasHoy();

        lblVentasHoy.setText("$ " + total);

    }

    // =========================
    // GANANCIA HOY
    // =========================

    private void cargarGananciaHoy(){

        double total =
                service.obtenerGananciaHoy();

        lblGananciaHoy.setText("$ " + total);

    }
}