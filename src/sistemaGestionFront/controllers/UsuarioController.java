package sistemaGestionFront.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import sistemaGestionFront.models.Usuario;
import sistemaGestionFront.services.UsuarioService;

public class UsuarioController {

	// INSTANCIA PARA RECARGAR TABLA
	public static UsuarioController instancia;

	@FXML
	private TableView<Usuario> tablaUsuarios;

	@FXML
	private TableColumn<Usuario, String> colNombre;
	@FXML
	private TableColumn<Usuario, String> colUsuario;
	@FXML
	private TableColumn<Usuario, String> colRol;
	@FXML
	private TableColumn<Usuario, String> colEstado;

	private final UsuarioService service = new UsuarioService();

	@FXML
	public void initialize() {

		// GUARDAMOS INSTANCIA
		instancia = this;

		colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));

		colUsuario.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getUsuario()));

		colRol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRol()));

		colEstado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getActivo() ? "Activo" : "Inactivo"));

		agregarAcciones();

		cargarUsuarios();
	}

	// METODO PARA RECARGAR TABLA
	public static void recargarTabla() {

		if (instancia != null) {
			instancia.cargarUsuarios();
		}

	}

	private void cargarUsuarios() {

		tablaUsuarios.setItems(FXCollections.observableArrayList(service.listarUsuarios()));
	}

	private void agregarAcciones() {

		TableColumn<Usuario, Void> colAcciones = new TableColumn<>("Acciones");

		colAcciones.setCellFactory(param -> new TableCell<>() {

			private final Button btnEliminar = new Button("🗑");
			{
			    btnEliminar.setStyle(
			            "-fx-background-color:#e74c3c;" +
			            "-fx-text-fill:white;" +
			            "-fx-font-weight:bold;");
			}

			{
				btnEliminar.setOnAction(e -> {

					Usuario u = getTableView().getItems().get(getIndex());

					Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION,
							"¿Desea eliminar el usuario " + u.getUsuario() + "?");

					confirmacion.setHeaderText("Eliminar Usuario");

					confirmacion.showAndWait().ifPresent(respuesta -> {

						if (respuesta == ButtonType.OK) {

							service.eliminar(u.getId());

							cargarUsuarios();

						}

					});

				});
			}

			@Override
			protected void updateItem(Void item, boolean empty) {

				super.updateItem(item, empty);

				if (empty) {
					setGraphic(null);
				} else {
					setGraphic(new HBox(btnEliminar));
				}
			}
		});

		tablaUsuarios.getColumns().add(colAcciones);
	}

	@FXML
	private void nuevoUsuario() {

		try {

			FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemaGestionFront/views/usuariosForm.fxml"));

			Parent root = loader.load();

			Stage stage = new Stage();

			stage.setTitle("Nuevo Usuario");

			stage.setScene(new Scene(root));

			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}