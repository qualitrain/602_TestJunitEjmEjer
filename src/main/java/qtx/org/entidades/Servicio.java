package qtx.org.entidades;

import qtx.org.negocio.IMovtoComisionable;
import qtx.org.negocio.Temporalidad;

public class Servicio {
	private String id;
	private String nombre;
	private double precioPorDia;	
	
	public Servicio() {
	}
	public Servicio(String id, String nombre, double precioPorDia) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.precioPorDia = precioPorDia;
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

	public double getPrecioPorDia() {
		return precioPorDia;
	}

	public void setPrecioPorDia(double precioPorDia) {
		this.precioPorDia = precioPorDia;
	}

	public IMovtoComisionable getIMovtoComisionable(Temporalidad duracion, int cantidad) {
		return new MovtoServicioComisionable(this, duracion, cantidad);
	}
}
