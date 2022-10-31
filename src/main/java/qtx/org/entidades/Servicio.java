package qtx.org.entidades;

import qtx.org.negocio.IMovtoComisionable;
import qtx.org.negocio.MetodoNoAplicableException;

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

	public IMovtoComisionable getIMovtoComisionable_InnerClass(Temporalidad duracion, int cantidad) {
		return new MovtoServicioComisionable_InnClass(this, duracion, cantidad);
	}
	
	public IMovtoComisionable getIMovtoComisionable(Temporalidad duracion, int cantidad) {
		return new MovtoServicioComisionable(this, duracion, cantidad);
	}
	
	//----------------------------------- ANONYMOUS CLASS -----------------------------------//
	
	public IMovtoComisionable getIMovtoComisionable_ClAnon(Temporalidad duracion, int cantidad) {
		return new IMovtoComisionable (){
			@Override
			public double getPrecioAplicablePorUnidad() {
				return getPrecioPorDia() * duracion.getNdias();
			}

			@Override
			public double getUtilidadPorUnidad() {
				throw new MetodoNoAplicableException("El servicio " + this.getId() 
				                                       + " no puede calcular utilidad unitaria");
			}

			@Override
			public int getCantidadUnidades() {
				return cantidad;
			}

			@Override
			public String getId() {
				return id;
			}

			@Override
			public String getUnidad() {
				return null;
			}

			@Override
			public Temporalidad getTemporalidad() {
				return duracion;
			}

			@Override
			public boolean tieneUtilidad() {
				return true;
			}
			
		};
	}
	
	//----------------------------------- INNER CLASS -----------------------------------//
	
	public static class MovtoServicioComisionable_InnClass implements IMovtoComisionable {
		private Servicio servicio;
		private Temporalidad duracion;
		private int cantidad;

		public MovtoServicioComisionable_InnClass(Servicio servicio, Temporalidad duracion, int cantidad) {
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


}
