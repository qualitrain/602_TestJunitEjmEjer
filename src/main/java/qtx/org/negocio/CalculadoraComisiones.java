package qtx.org.negocio;

import java.util.HashMap;
import java.util.Map;

import qtx.org.entidades.Producto;
import qtx.org.entidades.Temporalidad;

public class CalculadoraComisiones {
	
	//Politicas de aplicacion General
	private double porcentajeComision;	
	private Map<Temporalidad,Double> dsctosXTemporalidad;

	public CalculadoraComisiones(double porcentajeComision) {
		super();
		this.porcentajeComision = porcentajeComision;
		this.dsctosXTemporalidad = new HashMap<>();
	}
	
	public void setDscto(Temporalidad temp, double dscto) {
		this.dsctosXTemporalidad.put(temp, dscto);
	}
	public void eliminarDscto(Temporalidad temp) {
		this.dsctosXTemporalidad.remove(temp);
	}

	public Map<Temporalidad, Double> getDsctosXTemporalidad() {
		return dsctosXTemporalidad;
	}
	public void setDsctosXTemporalidad(Map<Temporalidad, Double> dsctosXTemporalidad) {
		this.dsctosXTemporalidad = dsctosXTemporalidad;
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
		if(movto.getTemporalidad() == null) {
			return this.calcularComisionItemAtemporal(movto);
		}
		else {
			return this.calcularComisionItemConTemporalidad(movto.getTemporalidad(),
					                                 movto.getPrecioAplicablePorUnidad(), 
					                                 movto.getCantidadUnidades()   );
		}
	}

	private double calcularComisionItemConTemporalidad(Temporalidad temporalidad, double precioPorUnidad,
			int cantUnidades) {
		double porcenComision = this.dsctosXTemporalidad.getOrDefault(temporalidad, this.porcentajeComision);
		return porcenComision * precioPorUnidad * cantUnidades;
		
	}

	private double calcularComisionItemAtemporal(IMovtoComisionable movto) {
		if(movto.getCantidadUnidades() <= 0) {
			throw new ValorPropiedadInvalidoException("la cantidad de unidades es menor o igual a cero");			
		}
		if(movto.tieneUtilidad() == false) {
			return 0;
		}
		double comision = movto.getUtilidadPorUnidad() * movto.getCantidadUnidades() * this.porcentajeComision;
		return comision;
	}
}
