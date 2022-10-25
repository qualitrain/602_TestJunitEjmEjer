package qtx.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import qtx.org.entidades.Producto;
import qtx.org.negocio.CalculadoraComisiones;
import qtx.org.negocio.NegocioException;

public class CalculadoraComisionesTests {
	
	@Test
	public void testCalcularComisionOK() {
		
		//Dados
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
//		Producto producto=null;
		Producto producto = new Producto("X-1", "Camisa", 855, 355);
		
		//Cuando
		double comision = calculadora.calcularComision(producto, 1);
		
		//Entonces
//		assertEquals(50,comision,0.00001,"costo distinto del esperado");
		assertEquals(50, comision);
	}

	@Test
	public void testCalcularComision_utilidadNegativa() {
		//Dados
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		Producto producto = new Producto("X-1", "Camisa", 255, 355); //Tiene utilidad negativa
		//Cuando
		double comision = calculadora.calcularComision(producto, 1);
		//Entonces
		assertEquals(0, comision);
	}
	
	@Test
	public void testCalcularComision_productoNulo() {
		//Dados
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		Producto producto = null;
		//Cuando
		
		assertThrows(NegocioException.class, () -> {		
			calculadora.calcularComision(producto, 1);
		});
			
	}
	@Test
	public void testCalcularComision_cantidadEquivocada() {
		//Dados
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		Producto producto = new Producto("X-1", "Camisa", 255, 355); //Tiene utilidad negativa
		//Cuando
		
		assertThrows(NegocioException.class, () -> {		
			calculadora.calcularComision(producto, -1);
		});
			
	}
	
}
