package qtx.org.negocio;

public interface IMovtoComisionable {
	double getPrecioAplicablePorUnidad();
	double getUtilidadPorUnidad();
	int getCantidadUnidades();
	String getId();
	String getUnidad();
	Temporalidad getTemporalidad();
	boolean tieneUtilidad();
}
