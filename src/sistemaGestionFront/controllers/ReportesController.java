package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sistemaGestionFront.services.ReporteService;

import java.util.Map;

public class ReportesController {

    @FXML private DatePicker fechaInicio;
    @FXML private DatePicker fechaFin;

    @FXML private Label lblVentas;
    @FXML private Label lblCosto;
    @FXML private Label lblGastos;
    @FXML private Label lblBruta;
    @FXML private Label lblNeta;

    private final ReporteService service =
            new ReporteService();

    @FXML
    private void consultarReporte() {

        if(fechaInicio.getValue()==null ||
           fechaFin.getValue()==null){

            new Alert(Alert.AlertType.WARNING,
                    "Seleccione fechas")
                    .show();
            return;
        }

        Map<String,Object> r =
                service.obtenerGanancia(
                        fechaInicio.getValue().toString(),
                        fechaFin.getValue().toString()
                );

        if(r==null) return;

        lblVentas.setText("$"+r.get("totalVentas"));
        lblCosto.setText("$"+r.get("costoProductos"));
        lblGastos.setText("$"+r.get("gastos"));
        lblBruta.setText("$"+r.get("utilidadBruta"));
        lblNeta.setText("$"+r.get("utilidadNeta"));
    }
}