package qtx.org.negocio;

import qtx.org.entidades.Temporalidad;

public interface IMovtoComisionable {
	double getPrecioAplicablePorUnidad();
	double getUtilidadPorUnidad();
	int getCantidadUnidades();
	String getId();
	String getUnidad();
	Temporalidad getTemporalidad();
	boolean tieneUtilidad();
}
