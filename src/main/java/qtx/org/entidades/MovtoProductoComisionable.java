package qtx.org.entidades;

import qtx.org.negocio.IMovtoComisionable;

public class MovtoProductoComisionable implements IMovtoComisionable {
	private Producto producto;
	private int cantidad;

	public MovtoProductoComisionable() {
	}

	public MovtoProductoComisionable(Producto producto, int cantidad) {
		super();
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	@Override
	public double getPrecioAplicablePorUnidad() {
		return this.producto.getPrecio();
	}

	@Override
	public double getUtilidadPorUnidad() {
		return this.producto.getUtilidad();
	}

	@Override
	public int getCantidadUnidades() {
		return this.cantidad;
	}

	@Override
	public String getId() {
		return this.producto.getId();
	}

	@Override
	public String getUnidad() {
		return null;
	}

	@Override
	public Temporalidad getTemporalidad() {
		return null;
	}

	@Override
	public boolean tieneUtilidad() {
		return (this.producto.getPrecio() > this.producto.getCosto());
	}

}
