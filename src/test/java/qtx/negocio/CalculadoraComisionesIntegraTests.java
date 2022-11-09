package qtx.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import qtx.org.entidades.Producto;
import qtx.org.negocio.CalculadoraComisiones;
import qtx.org.negocio.IGestorComisionesArticulo;
import qtx.org.persistencia.GestorComisionesMemoria;

public class CalculadoraComisionesIntegraTests {

	public CalculadoraComisionesIntegraTests() {
	}
	
	@Test
	public void testCalcularComisionProducto() {
		//Dados
		double precio = 700;
		double costo = 250;
		double porcComisionGral = 0.05;
		double porcComisionArt = 0.10;
		int cantidad = 1;
		String idArt = "C-01";
		Producto art = new Producto(idArt,"Camisa",precio,costo);
		
		double baseComision = (precio - costo) * cantidad;
		double comisionEsperada = baseComision * porcComisionGral;
		comisionEsperada += (baseComision - comisionEsperada) * porcComisionArt;
		
		//Cuando
		IGestorComisionesArticulo provComisionesArt = new GestorComisionesMemoria();
		provComisionesArt.agregarPorcComision(idArt, "estimulo Vta Temporada", porcComisionArt);
		
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComisionGral);
		calculadora.setProvComisiones(provComisionesArt);

		double comision = calculadora.calcularComision(art.getIMovtoComisionable(cantidad));
		//Entonces
		assertEquals(comisionEsperada, comision);
		
	}

}
