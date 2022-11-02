package qtx.org.entidades;

public enum Temporalidad {
	ANUAL(360),
	SEMESTRAL(180),
	TRIMESTRAL(90),
	BIMESTRAL(60),
	MENSUAL(30),
	QUINCENAL(15);

	private int ndias;

	Temporalidad(int i) {
		this.ndias = i;
	}

	public int getNdias() {
		return ndias;
	}
	
}
