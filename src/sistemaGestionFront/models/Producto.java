package sistemaGestionFront.models;


public class Producto {
	   private Long id;
	    private String nombre;
	    private Double precioVenta;
	    private Double precioCosto;
	    private Integer stock;
	    private Boolean activo;
	    
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public Double getPrecioVenta() {
			return precioVenta;
		}
		public void setPrecioVenta(Double precioVenta) {
			this.precioVenta = precioVenta;
		}
		public Double getPrecioCosto() {
			return precioCosto;
		}
		public void setPrecioCosto(Double precioCosto) {
			this.precioCosto = precioCosto;
		}
		public Integer getStock() {
			return stock;
		}
		public void setStock(Integer stock) {
			this.stock = stock;
		}
		public Boolean getActivo() {
			return activo;
		}
		public void setActivo(Boolean activo) {
			this.activo = activo;
		}
}
