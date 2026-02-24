package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sistemaGestionFront.services.AuthService;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    @FXML
    public void login() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        if (usuario.isBlank() || password.isBlank()) {
            lblMensaje.setText("Ingrese usuario y contraseña");
            lblMensaje.setStyle("-fx-text-fill: red;");
            return;
        }

        boolean ok = AuthService.login(usuario, password);

        if (ok) {
            abrirPantallaPrincipal();
        } else {
            lblMensaje.setText("Credenciales incorrectas");
            lblMensaje.setStyle("-fx-text-fill: red;");
        }
    }

    private void abrirPantallaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/sistemaGestionFront/views/main.fxml")
            );

            Scene scene = new Scene(loader.load(), 1200, 700);

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("LicoSoft HM - Principal");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
