package sistemaGestionFront.models;

public class ItemVenta {

    private Producto producto;
    private int cantidad;
    private double precioUnitario; // ✅ editable

    // ✅ NUEVO CONSTRUCTOR
    public ItemVenta(Producto producto,
                     int cantidad,
                     double precioUnitario) {

        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // =========================
    // GETTERS
    // =========================
    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    // ✅ IMPORTANTE (para descuentos)
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    // =========================
    // TOTAL REAL VENDIDO
    // =========================
    public double getTotal() {
        return cantidad * precioUnitario;
    }
}