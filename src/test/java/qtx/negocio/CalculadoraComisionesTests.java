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
		double costo = 355;
		double precio = 700;
		int cantidad = 5;
		double porcComision = 0.10;
		
		double comisionEsperada = (precio - costo) * cantidad * porcComision;
		
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision(producto, cantidad);
		
		//Entonces
//		assertEquals(comisionEsperada, comision,0.00001,"costo distinto del esperado");
		assertEquals(comisionEsperada, comision);
	}

	@Test
	public void testCalcularComisionOK_UtilidadAlta() {
		
		//Dados
		double costo  = 300;
		double precio = 700;
		int cantidad  = 5;
		double porcComision      = 0.10;
		double porcUtilidadAlta  = 1.0; 
		double porcComisionExtra = 0.10;
		
		double comisionEsperada = (precio - costo) * cantidad * porcComision;
		comisionEsperada += (precio - costo) * porcComisionExtra;
		
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision, porcComisionExtra, porcUtilidadAlta);
		double comision = calculadora.calcularComision(producto, cantidad);
		
		//Entonces
		assertEquals(comisionEsperada, comision);
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
