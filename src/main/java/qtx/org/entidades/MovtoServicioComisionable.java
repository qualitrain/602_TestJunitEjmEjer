package qtx.org.entidades;

import qtx.org.negocio.IMovtoComisionable;
import qtx.org.negocio.MetodoNoAplicableException;

public class MovtoServicioComisionable implements IMovtoComisionable {
	private Servicio servicio;
	private Temporalidad duracion;
	private int cantidad;

	public MovtoServicioComisionable(Servicio servicio, Temporalidad duracion, int cantidad) {
		super();
		this.servicio = servicio;
		this.duracion = duracion;
		this.cantidad = cantidad;
	}

	@Override
	public double getPrecioAplicablePorUnidad() {
		return this.servicio.getPrecioPorDia() * duracion.getNdias();
	}

	@Override
	public int getCantidadUnidades() { //indica que no maneja unidades
		return this.cantidad;
	}

	@Override
	public String getId() {
		return this.getId();
	}

	@Override
	public String getUnidad() {
		return null;
	}

	@Override
	public Temporalidad getTemporalidad() {
		return this.duracion;
	}

	@Override
	public boolean tieneUtilidad() {
		return true;
	}

	@Override
	public double getUtilidadPorUnidad() {
		throw new MetodoNoAplicableException("El servicio " + this.getId() + " no puede calcular utilidad unitaria");
	}

}
