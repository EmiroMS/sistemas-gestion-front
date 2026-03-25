package sistemaGestionFront.controllers;

import javafx.application.Platform;
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

import sistemaGestionFront.models.Cliente;
import sistemaGestionFront.models.ItemVenta;
import sistemaGestionFront.models.Producto;

import sistemaGestionFront.services.ClienteService;
import sistemaGestionFront.services.ProductoService;
import sistemaGestionFront.services.VentaService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VentaController {

    @FXML private TextField txtCliente;
    @FXML private ListView<Cliente> listaClientes;

    @FXML private ComboBox<Producto> comboProductos;
    @FXML private TextField txtCantidad;

    @FXML private TableView<ItemVenta> tablaVenta;

    @FXML private TableColumn<ItemVenta,String> colProducto;
    @FXML private TableColumn<ItemVenta,Integer> colCantidad;
    @FXML private TableColumn<ItemVenta,Double> colPrecio;
    @FXML private TableColumn<ItemVenta,Double> colTotal;
    @FXML private TableColumn<ItemVenta,Void> colAcciones;

    @FXML private Label lblTotal;

    private ProductoService productoService =
            new ProductoService();

    private VentaService ventaService =
            new VentaService();

    private ClienteService clienteService =
            new ClienteService();

    private final List<ItemVenta> carrito =
            new ArrayList<>();



    @FXML
    public void initialize(){

        tablaVenta.setEditable(true);

        tablaVenta.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS
        );

        cargarClientes();
        cargarProductos();

        configurarColumnas();
        configurarAcciones();

    }



    // =====================================
    // CLIENTES AUTOCOMPLETE ESTABLE
    // =====================================
    private void cargarClientes(){

        listaClientes.setVisible(false);
        listaClientes.setManaged(false);
        listaClientes.setPrefHeight(0);

        listaClientes.setCellFactory(lv ->
                new ListCell<>(){

                    @Override
                    protected void updateItem(Cliente c, boolean empty){

                        super.updateItem(c, empty);

                        setText(
                                empty ? null :
                                c.getNombre()
                        );

                    }

                });


        txtCliente.textProperty()
        .addListener((obs,oldVal,newVal)->{

            if(newVal==null){
                return;
            }

            if(newVal.trim().length()<2){

                listaClientes.setVisible(false);
                listaClientes.setManaged(false);
                listaClientes.setPrefHeight(0);

                return;
            }

            new Thread(()->{

                List<Cliente> encontrados =
                        clienteService.buscar(newVal.trim());

                Platform.runLater(()->{

                    if(encontrados.isEmpty()){

                        listaClientes.setVisible(false);
                        listaClientes.setManaged(false);
                        listaClientes.setPrefHeight(0);

                        return;
                    }

                    listaClientes.getItems().setAll(encontrados);

                    listaClientes.setManaged(true);
                    listaClientes.setVisible(true);
                    listaClientes.setPrefHeight(120);

                });

            }).start();

        });


        listaClientes.setOnMouseClicked(e->{

            Cliente cliente =
                    listaClientes.getSelectionModel()
                    .getSelectedItem();

            if(cliente!=null){

                txtCliente.setText(
                        cliente.getNombre()
                );

                listaClientes.setVisible(false);
                listaClientes.setManaged(false);
                listaClientes.setPrefHeight(0);

            }

        });

    }



    // =====================================
    // PRODUCTOS
    // =====================================
    private void cargarProductos(){

        comboProductos.setItems(
                FXCollections.observableArrayList(
                        productoService.listarProductos()
                )
        );

        comboProductos.setCellFactory(lv ->
                new ListCell<>(){

                    @Override
                    protected void updateItem(
                            Producto item,
                            boolean empty){

                        super.updateItem(item, empty);

                        setText(
                                empty ? null :
                                item.getNombre()
                        );
                    }
                });

        comboProductos.setButtonCell(
                new ListCell<>(){

                    @Override
                    protected void updateItem(
                            Producto item,
                            boolean empty){

                        super.updateItem(item, empty);

                        setText(
                                empty ? null :
                                item.getNombre()
                        );
                    }
                });

    }



    // =====================================
    // COLUMNAS
    // =====================================
    private void configurarColumnas(){

        colProducto.setCellValueFactory(c ->
                new SimpleStringProperty(
                        c.getValue()
                        .getProducto()
                        .getNombre()
                ));

        colCantidad.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getCantidad()
                ));

        colPrecio.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue()
                        .getPrecioUnitario()
                ));

        colPrecio.setCellFactory(
                TextFieldTableCell.forTableColumn(
                        new DoubleStringConverter()
                )
        );

        colPrecio.setOnEditCommit(event -> {

            ItemVenta item =
                    event.getRowValue();

            Double nuevoPrecio =
                    event.getNewValue();

            if(nuevoPrecio==null ||
               nuevoPrecio<=0){

                mostrarMensaje(
                        "Precio inválido"
                );

                tablaVenta.refresh();

                return;
            }

            item.setPrecioUnitario(
                    nuevoPrecio
            );

            calcularTotal();

            tablaVenta.refresh();

        });

        colTotal.setCellValueFactory(c ->
                new SimpleObjectProperty<>(
                        c.getValue().getTotal()
                ));

    }



    // =====================================
    // BOTON QUITAR
    // =====================================
    private void configurarAcciones(){

        colAcciones.setCellFactory(param ->
                new TableCell<>(){

                    private final Button btnQuitar =
                            new Button("❌");

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
                    protected void updateItem(
                            Void item,
                            boolean empty){

                        super.updateItem(item, empty);

                        setGraphic(
                                empty ? null :
                                btnQuitar
                        );

                    }
                });

    }



    // =====================================
    // AGREGAR PRODUCTO
    // =====================================
    @FXML
    private void agregarProducto(){

        Producto producto =
                comboProductos.getValue();

        if(producto==null ||
           txtCantidad.getText().isEmpty()){

            mostrarMensaje(
                    "Seleccione producto y cantidad"
            );

            return;
        }

        int cantidad =
                Integer.parseInt(
                        txtCantidad.getText()
                );

        if(cantidad >
           producto.getStock()){

            mostrarMensaje(
                    "Stock insuficiente"
            );

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



    private void actualizarTabla(){

        tablaVenta.setItems(
                FXCollections.observableArrayList(
                        carrito
                )
        );

        calcularTotal();

    }



    private void calcularTotal(){

        double total =
                carrito.stream()
                        .mapToDouble(
                                ItemVenta::getTotal
                        )
                        .sum();

        lblTotal.setText(
                String.format("%.2f",total)
        );

    }



    // =====================================
    // CONFIRMAR VENTA
    // =====================================
    @FXML
    private void confirmarVenta(){

        if(carrito.isEmpty()){

            mostrarMensaje(
                    "No hay productos"
            );

            return;
        }

        String nombreCliente =
                txtCliente.getText();

        if(nombreCliente.isBlank()){

            nombreCliente =
                    "CONSUMIDOR FINAL";

        }

        Map<String,Object> venta =
                ventaService.enviarVenta(
                        carrito,
                        nombreCliente
                );

        if(venta!=null){

            mostrarFactura(venta);

            carrito.clear();

            actualizarTabla();

            txtCliente.clear();

            listaClientes.setVisible(false);
            listaClientes.setManaged(false);
            listaClientes.setPrefHeight(0);

            lblTotal.setText("0.0");

        }
        else{

            mostrarMensaje(
                    "Error registrando venta"
            );

        }

    }



    private void mostrarFactura(
            Map<String,Object> venta){

        try{

            FXMLLoader loader =
                    new FXMLLoader(
                    getClass().getResource(
                    "/sistemaGestionFront/views/factura.fxml"
                    ));

            Parent root =
                    loader.load();

            FacturaController controller =
                    loader.getController();

            controller.cargarFactura(venta);

            Stage stage =
                    new Stage();

            stage.setTitle("Factura");

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        }
        catch(Exception e){

            e.printStackTrace();

        }

    }



    private void mostrarMensaje(String mensaje){

        new Alert(
                Alert.AlertType.INFORMATION,
                mensaje
        ).showAndWait();

    }

}