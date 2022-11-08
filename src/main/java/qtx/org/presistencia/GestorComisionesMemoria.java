package qtx.org.presistencia;

import java.util.HashMap;
import java.util.Map;

import qtx.org.negocio.IGestorComisionesArticulo;

public class GestorComisionesMemoria implements IGestorComisionesArticulo {
	private Map<String,Double> porcComisionXid;
	private Map<String,String> conceptoComisionXid;

	public GestorComisionesMemoria() {
		this.conceptoComisionXid = new HashMap<>();
		this.porcComisionXid = new HashMap<>();
	}

	@Override
	public double getComisionItem(String id) {
		return this.porcComisionXid.getOrDefault(id, (Double)0.0 );
	}

	@Override
	public String getConceptoComisionItem(String id) {
		return this.conceptoComisionXid.getOrDefault(id, "");
	}

	@Override
	public void agregarPorcComision(String id, String concepto, double porcentaje) {
		this.porcComisionXid.put(id, porcentaje);
		this.conceptoComisionXid.put(id, concepto);
	}

	@Override
	public void eliminarComision(String id) {
		this.porcComisionXid.remove(id);
		this.conceptoComisionXid.remove(id);
	}

}
