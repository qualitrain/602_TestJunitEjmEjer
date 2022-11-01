package qtx.org.negocio;

import qtx.org.entidades.Producto;

public class CalculadoraComisiones {
	private double porcentajeComision;
	private double porcComisionXutilidadAlta;
	private double porcRef_UtilidadAlta;

	public CalculadoraComisiones(double porcentajeComision) {
		super();
		this.porcentajeComision = porcentajeComision;
		this.porcRef_UtilidadAlta = 1.0;
		this.porcComisionXutilidadAlta = 0.05;
	}

	
	public CalculadoraComisiones(double porcentajeComision, double porcComisionXutilidadAlta,
			double porcRef_UtilidadAlta) {
		super();
		this.porcentajeComision = porcentajeComision;
		this.porcComisionXutilidadAlta = porcComisionXutilidadAlta;
		this.porcRef_UtilidadAlta = porcRef_UtilidadAlta;
	}


	public double getPorcentajeComision() {
		return porcentajeComision;
	}

	public void setPorcentajeComision(double porcentajeComision) {
		this.porcentajeComision = porcentajeComision;
	}

	public double getPorcComisionXutilidadAlta() {
		return porcComisionXutilidadAlta;
	}

	public void setPorcComisionXutilidadAlta(double porcComisionXutilidadAlta) {
		this.porcComisionXutilidadAlta = porcComisionXutilidadAlta;
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
		double comision = utilidad * this.porcentajeComision * cantVendida;
		if(prod.getPorcentajeUtilidad() > this.porcRef_UtilidadAlta) {
			comision += utilidad * this.porcComisionXutilidadAlta;
		}
		return comision;
	}
}
