package sistemaGestionFront.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import sistemaGestionFront.models.Producto;
import sistemaGestionFront.services.ProductoService;

public class ProductoController {

    @FXML private TableView<Producto> tablaProductos;
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, Double> colPrecioVenta;
    @FXML private TableColumn<Producto, Double> colPrecioCosto;
    @FXML private TableColumn<Producto, Integer> colStock;
    @FXML private TableColumn<Producto, String> colEstado;

    private final ProductoService service = new ProductoService();

    @FXML
    public void initialize() {

        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colPrecioVenta.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrecioVenta()));
        colPrecioCosto.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPrecioCosto()));
        colStock.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getStock()));
        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getActivo() ? "Activo" : "Inactivo")
        );

        agregarAcciones();
        cargarProductos();
    }

    private void cargarProductos() {
        tablaProductos.setItems(
                FXCollections.observableArrayList(service.listarProductos())
        );
    }

    private void agregarAcciones() {

        TableColumn<Producto, Void> colAcciones = new TableColumn<>("Acciones");

        colAcciones.setCellFactory(param -> new TableCell<>() {

            private final Button btnEditar = new Button("✏");
            private final Button btnEliminar = new Button("🗑");

            {
                btnEditar.setStyle("-fx-background-color:#3498db; -fx-text-fill:white;");
                btnEliminar.setStyle("-fx-background-color:#e74c3c; -fx-text-fill:white;");

                btnEditar.setOnAction(e -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    editarProducto(p);
                });

                btnEliminar.setOnAction(e -> {
                    Producto p = getTableView().getItems().get(getIndex());
                    service.eliminar(p.getId());
                    cargarProductos();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, btnEditar, btnEliminar);
                    setGraphic(box);
                }
            }
        });

        tablaProductos.getColumns().add(colAcciones);
    }

    private void editarProducto(Producto producto) {

        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Editar Producto");

        TextField nombre = new TextField(producto.getNombre());
        TextField precioVenta = new TextField(producto.getPrecioVenta().toString());
        TextField precioCosto = new TextField(producto.getPrecioCosto().toString());
        TextField stock = new TextField(producto.getStock().toString());

        VBox contenido = new VBox(10,
                new Label("Nombre"), nombre,
                new Label("Precio venta"), precioVenta,
                new Label("Precio costo"), precioCosto,
                new Label("Stock"), stock
        );

        dialog.getDialogPane().setContent(contenido);

        ButtonType guardar = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardar, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == guardar) {
                producto.setNombre(nombre.getText());
                producto.setPrecioVenta(Double.parseDouble(precioVenta.getText()));
                producto.setPrecioCosto(Double.parseDouble(precioCosto.getText()));
                producto.setStock(Integer.parseInt(stock.getText()));
                return producto;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(p -> {
            service.guardar(p);
            cargarProductos();
        });
    }

    @FXML
    private void nuevoProducto() {

        Dialog<Producto> dialog = new Dialog<>();
        dialog.setTitle("Nuevo Producto");

        TextField nombre = new TextField();
        TextField precioVenta = new TextField();
        TextField precioCosto = new TextField();
        TextField stock = new TextField();

        VBox contenido = new VBox(10,
                new Label("Nombre"), nombre,
                new Label("Precio venta"), precioVenta,
                new Label("Precio costo"), precioCosto,
                new Label("Stock"), stock
        );

        dialog.getDialogPane().setContent(contenido);

        ButtonType guardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(guardar, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == guardar) {
                Producto p = new Producto();
                p.setNombre(nombre.getText());
                p.setPrecioVenta(Double.parseDouble(precioVenta.getText()));
                p.setPrecioCosto(Double.parseDouble(precioCosto.getText()));
                p.setStock(Integer.parseInt(stock.getText()));
                return p;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(p -> {
            service.guardar(p);
            cargarProductos();
        });
    }
}