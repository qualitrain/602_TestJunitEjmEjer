package qtx.org.entidades;

public class Producto {
	private String id;
	private String nombre;
	private double precio;
	private double costo;
		
	
	public Producto(String id, String nombre, double precio, double costo) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.costo = costo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public double getPrecio() {
		return precio;
	}
	public void setPrecio(double precio) {
		this.precio = precio;
	}
	public double getCosto() {
		return costo;
	}
	public void setCosto(double costo) {
		this.costo = costo;
	}

	public double getUtilidad() {
		return this.precio - this.costo;
	}
	@Override
	public String toString() {
		return "Producto [id=" + id + ", nombre=" + nombre + ", precio=" + precio + ", costo=" + costo + "]";
	}
	public double getPorcentajeUtilidad() {
		return (this.getUtilidad()/this.costo);
	}
	
}
