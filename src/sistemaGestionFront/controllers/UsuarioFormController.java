package sistemaGestionFront.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sistemaGestionFront.models.Usuario;
import sistemaGestionFront.services.UsuarioService;

public class UsuarioFormController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private ComboBox<String> comboRol;

    private final UsuarioService service = new UsuarioService();

    @FXML
    public void initialize(){

        comboRol.getItems().addAll("ADMIN","VENDEDOR");

    }

    @FXML
    private void guardarUsuario(){

        try{

            Usuario u = new Usuario();

            u.setNombre(txtNombre.getText());
            u.setUsuario(txtUsuario.getText());
            u.setPassword(txtPassword.getText());
            u.setRol(comboRol.getValue());

            service.registrar(u);

            // 🔹 RECARGAR TABLA DE USUARIOS
            UsuarioController.recargarTabla();

            Alert a = new Alert(Alert.AlertType.INFORMATION,
                    "Usuario creado correctamente");

            a.showAndWait();

            Stage stage =
                    (Stage) txtNombre.getScene().getWindow();

            stage.close();

        }catch(Exception e){
            e.printStackTrace();
        }

    }

}