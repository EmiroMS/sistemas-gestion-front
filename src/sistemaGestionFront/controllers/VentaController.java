package sistemaGestionFront.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import sistemaGestionFront.models.ItemVenta;
import sistemaGestionFront.models.Producto;
import sistemaGestionFront.services.ProductoService;
import sistemaGestionFront.services.VentaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VentaController {

    @FXML private ComboBox<Producto> comboProductos;
    @FXML private TextField txtCantidad;

    @FXML private TableView<ItemVenta> tablaVenta;

    @FXML private TableColumn<ItemVenta,String> colProducto;
    @FXML private TableColumn<ItemVenta,Integer> colCantidad;
    @FXML private TableColumn<ItemVenta,Double> colPrecio;
    @FXML private TableColumn<ItemVenta,Double> colTotal;
    @FXML private TableColumn<ItemVenta,Void> colAcciones;

    @FXML private Label lblTotal;

    private final ProductoService productoService = new ProductoService();
    private final VentaService ventaService = new VentaService();

    private final List<ItemVenta> carrito = new ArrayList<>();


    // ===================================================
    // INIT
    // ===================================================
    @FXML
    public void initialize() {

        tablaVenta.setEditable(true);
        tablaVenta.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        cargarProductos();
        configurarColumnas();
        configurarAcciones();
    }

    private void cargarProductos() {

        comboProductos.setItems(
                FXCollections.observableArrayList(
                        productoService.listarProductos()
                )
        );

        comboProductos.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre());
            }
        });

        comboProductos.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Producto item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item.getNombre());
            }
        });
    }


    // ===================================================
    // COLUMNAS
    // ===================================================
    private void configurarColumnas() {

        colProducto.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue().getProducto().getNombre()
                ));

        colCantidad.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getCantidad()
                ));

        // ✅ PRECIO EDITABLE
        colPrecio.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getPrecioUnitario()
                ));

        colPrecio.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new DoubleStringConverter()
                )
        );

        colPrecio.setOnEditCommit(event -> {

            ItemVenta item = event.getRowValue();
            Double nuevoPrecio = event.getNewValue();

            if (nuevoPrecio == null || nuevoPrecio <= 0) {
                mostrarMensaje("Precio inválido");
                tablaVenta.refresh();
                return;
            }

            item.setPrecioUnitario(nuevoPrecio);

            calcularTotal();
            tablaVenta.refresh();
        });

        colTotal.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getTotal()
                ));
    }


    // ===================================================
    // BOTON QUITAR
    // ===================================================
    private void configurarAcciones() {

        colAcciones.setCellFactory(param ->
                new TableCell<>() {

            private final Button btnQuitar = new Button("❌");

            {
                btnQuitar.setOnAction(e -> {

                    ItemVenta item =
                            getTableView()
                                    .getItems()
                                    .get(getIndex());

                    carrito.remove(item);
                    actualizarTabla();
                });
            }

            @Override
            protected void updateItem(Void item,
                                      boolean empty) {

                super.updateItem(item, empty);
                setGraphic(empty ? null : btnQuitar);
            }
        });
    }


    // ===================================================
    // AGREGAR PRODUCTO
    // ===================================================
    @FXML
    private void agregarProducto() {

        Producto producto = comboProductos.getValue();

        if (producto == null ||
                txtCantidad.getText().isEmpty()) {

            mostrarMensaje("Seleccione producto y cantidad");
            return;
        }

        int cantidad = Integer.parseInt(txtCantidad.getText());

        if (cantidad > producto.getStock()) {
            mostrarMensaje("Stock insuficiente");
            return;
        }

        carrito.add(
                new ItemVenta(
                        producto,
                        cantidad,
                        producto.getPrecioVenta()
                )
        );

        actualizarTabla();
        txtCantidad.clear();
    }


    // ===================================================
    // ACTUALIZAR TABLA
    // ===================================================
    private void actualizarTabla() {

        tablaVenta.setItems(
                FXCollections.observableArrayList(carrito)
        );

        calcularTotal();
    }


    // ===================================================
    // TOTAL GENERAL
    // ===================================================
    private void calcularTotal() {

        double total =
                carrito.stream()
                        .mapToDouble(ItemVenta::getTotal)
                        .sum();

        lblTotal.setText(String.format("%.2f", total));
    }


    // ===================================================
    // CONFIRMAR VENTA
    // ===================================================
    @FXML
    private void confirmarVenta() {

        if (carrito.isEmpty()) {
            mostrarMensaje("No hay productos");
            return;
        }

        Map<String,Object> venta =
                ventaService.enviarVenta(carrito);

        if (venta != null) {

            mostrarFactura(venta);

            carrito.clear();
            actualizarTabla();
            lblTotal.setText("0.0");

        } else {
            mostrarMensaje("Error registrando venta");
        }
    }


    // ===================================================
    // FACTURA
    // ===================================================
    private void mostrarFactura(Map<String,Object> venta) {

        try {

            FXMLLoader loader =
                    new FXMLLoader(
                            getClass().getResource(
                                    "/sistemaGestionFront/views/factura.fxml"
                            ));

            Parent root = loader.load();

            FacturaController controller =
                    loader.getController();

            controller.cargarFactura(venta);

            Stage stage = new Stage();
            stage.setTitle("Factura");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void mostrarMensaje(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje)
                .showAndWait();
    }
}