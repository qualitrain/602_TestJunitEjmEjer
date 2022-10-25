package qtx.org.negocio;

import qtx.org.entidades.Producto;

public class CalculadoraComisiones {
	private double porcentajeComision;	

	public CalculadoraComisiones(double porcentajeComision) {
		super();
		this.porcentajeComision = porcentajeComision;
	}

	public double getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(double porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

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
		double comision = utilidad * this.porcentajeComision;
		return comision;
	}
}
