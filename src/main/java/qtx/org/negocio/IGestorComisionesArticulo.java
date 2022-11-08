package qtx.org.negocio;

public interface IGestorComisionesArticulo {
	double getComisionItem(String id);
	String getConceptoComisionItem(String id);
	void agregarPorcComision(String id, String concepto, double porcentaje);
	void eliminarComision(String id);
}
