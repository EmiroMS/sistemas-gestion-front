package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import sistemaGestionFront.session.Sesion;

public class MainController {

    @FXML
    private StackPane contentArea;

    // BOTON REPORTES
    @FXML
    private Button btnReportes;

    // BOTON USUARIOS
    @FXML
    private Button btnUsuarios;

    // BOTON DASHBOARD
    @FXML
    private Button btnDashboard;

    // ==========================
    // INICIALIZAR
    // ==========================
    @FXML
    public void initialize() {

        // Si el usuario es vendedor ocultamos modulos ADMIN
        if ("VENDEDOR".equals(Sesion.getRol())) {

            // ocultar reportes
            btnReportes.setVisible(false);
            btnReportes.setManaged(false);

            // ocultar usuarios
            btnUsuarios.setVisible(false);
            btnUsuarios.setManaged(false);

            // ocultar dashboard
            btnDashboard.setVisible(false);
            btnDashboard.setManaged(false);
        }
    }

    // ==========================
    // INICIO
    // ==========================
    @FXML
    public void inicio() {
        contentArea.getChildren().clear();
    }

    // ==========================
    // MODULOS
    // ==========================
    @FXML
    public void productos() {
        cargarVista("/sistemaGestionFront/views/productos.fxml");
    }

    @FXML
    public void ventas() {
        cargarVista("/sistemaGestionFront/views/ventas.fxml");
    }

    @FXML
    public void historial() {
        cargarVista("/sistemaGestionFront/views/historialVentas.fxml");
    }

    @FXML
    public void inventario() {
        cargarVista("/sistemaGestionFront/views/inventario.fxml");
    }

    @FXML
    public void reportes() {
        cargarVista("/sistemaGestionFront/views/reportes.fxml");
    }

    @FXML
    public void gastos() {
        cargarVista("/sistemaGestionFront/views/gastos.fxml");
    }

    // MODULO USUARIOS (SOLO ADMIN)
    @FXML
    public void usuarios() {
        cargarVista("/sistemaGestionFront/views/usuarios.fxml");
    }

    // MODULO DASHBOARD (SOLO ADMIN)
    @FXML
    public void dashboard() {
        cargarVista("/sistemaGestionFront/views/dashboard.fxml");
    }
    
    @FXML
    public void entradas() {
        cargarVista("/sistemaGestionFront/views/entradaProductos.fxml");
    }

    // ==========================
    // LOGOUT
    // ==========================
    @FXML
    public void logout() {

        Alert confirmacion = new Alert(
                Alert.AlertType.CONFIRMATION,
                "¿Desea cerrar sesión?"
        );

        confirmacion.setHeaderText("Cerrar sesión");

        confirmacion.showAndWait().ifPresent(respuesta -> {

            if (respuesta == ButtonType.OK) {

                try {

                    Stage stageActual =
                            (Stage) contentArea
                                    .getScene()
                                    .getWindow();

                    FXMLLoader loader =
                            new FXMLLoader(
                                    getClass().getResource(
                                            "/sistemaGestionFront/views/login.fxml"
                                    )
                            );

                    Parent root = loader.load();

                    Stage loginStage = new Stage();

                    loginStage.setTitle("Login - LicoSoft HM");

                    loginStage.setScene(new Scene(root, 900, 600));

                    loginStage.centerOnScreen();

                    loginStage.setResizable(false);

                    loginStage.show();

                    stageActual.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // ==========================
    // CARGAR VISTAS
    // ==========================
    private void cargarVista(String ruta) {

        try {

            Node vista =
                    FXMLLoader.load(
                            getClass().getResource(ruta)
                    );

            contentArea.getChildren().setAll(vista);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}