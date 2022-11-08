package qtx.org.negocio;

import java.util.HashMap;
import java.util.Map;

import qtx.org.entidades.Producto;
import qtx.org.entidades.Temporalidad;

public class CalculadoraComisiones {
	//Politicas de aplicación específica
	private IGestorComisionesArticulo provComisiones;
	
	//Politicas de aplicacion General
	private double porcentajeComision;	
	private Map<Temporalidad,Double> porcComisionXTemporalidad;

	public CalculadoraComisiones(double porcentajeComision) {
		super();
		this.porcentajeComision = porcentajeComision;
		this.porcComisionXTemporalidad = new HashMap<>();
	}
	public CalculadoraComisiones() {
		super();
		this.porcentajeComision = 0;
		this.porcComisionXTemporalidad = new HashMap<>();
	}
	
	public IGestorComisionesArticulo getProvComisiones() {
		return provComisiones;
	}

	public void setProvComisiones(IGestorComisionesArticulo provComisiones) {
		this.provComisiones = provComisiones;
	}

	public void agregarPorcComisionXtemporalidad(Temporalidad temp, double dscto) {
		this.porcComisionXTemporalidad.put(temp, dscto);
	}
	public void eliminarPorcComisionXtemporalidad(Temporalidad temp) {
		this.porcComisionXTemporalidad.remove(temp);
	}

	public Map<Temporalidad, Double> getPorcComisionXTemporalidad() {
		return porcComisionXTemporalidad;
	}

	public void setPorcComisionXTemporalidad(Map<Temporalidad, Double> porcComisionXTemporalidad) {
		this.porcComisionXTemporalidad = porcComisionXTemporalidad;
	}

	public double getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(double porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}
	@Deprecated
	public double calcularComision(Producto prod, int cantVendida) {
		if (prod == null) {
			throw new NegocioException("el producto es nulo");
		}
		if(cantVendida <= 0) {
			throw new NegocioException("la cantidad vendida es menor o igual a cero");			
		}
		double utilidad = prod.getUtilidad();
		if(utilidad < 0) {
			return 0;
		}
		double comision = utilidad * cantVendida * this.porcentajeComision;
		return comision;
	}
	
	public double calcularComision(IMovtoComisionable movto) {
		if (movto == null) {
			throw new ArgumentoInvalidoException("el IMovtoComisionable es nulo");
		}
		if (movto.getId() == null || movto.getId().isEmpty() || movto.getId().isBlank()) {
			throw new ArgumentoInvalidoException("el IMovtoComisionable tiene un id inválido");			
		}
		if(movto.getTemporalidad() == null) { //Es un producto
			return this.calcularComisionItemAtemporal(movto);
		}
		else { //Es un servicio
			if(this.porcComisionXTemporalidad.get(movto.getTemporalidad()) == null     ) {
				throw new TemporalidadNoProgramadaException("No se ha configurado un porcentaje "
						+ "de descuento para la temporalidad" + movto.getTemporalidad());
			}
			return this.calcularComisionItemConTemporalidad(movto.getTemporalidad(),
					                                 movto.getPrecioAplicablePorUnidad(), 
					                                 movto.getCantidadUnidades(), movto.getId()   );
		}
	}

	private double calcularComisionItemConTemporalidad(Temporalidad temporalidad, double precioPorUnidad,
			int cantUnidades, String id) {
		double porcenComision = this.porcComisionXTemporalidad.getOrDefault(temporalidad, this.porcentajeComision);
		double baseComision = precioPorUnidad * cantUnidades;
		double comision = porcenComision * baseComision;
		if(this.provComisiones == null)
			return comision;
		comision += (baseComision - comision) * this.provComisiones.getComisionItem(id);
		return comision;
		
	}

	private double calcularComisionItemAtemporal(IMovtoComisionable movto) {
		if(movto.getCantidadUnidades() <= 0) {
			throw new ValorPropiedadInvalidoException("la cantidad de unidades es menor o igual a cero");			
		}
		if(movto.tieneUtilidad() == false) {
			return 0;
		}
		double baseComision = movto.getUtilidadPorUnidad() * movto.getCantidadUnidades();
		double comision = baseComision * this.porcentajeComision;
		if(this.provComisiones == null) {
			return comision;
		}
		double porComisArt = this.provComisiones.getComisionItem(movto.getId());
		if( porComisArt == 0) {
			return  comision;
		}
		comision += ( baseComision - comision) * porComisArt;
		return comision;
	}
}
