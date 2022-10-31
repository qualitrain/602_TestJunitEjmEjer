package qtx.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import qtx.org.entidades.Producto;
import qtx.org.entidades.Servicio;
import qtx.org.negocio.CalculadoraComisiones;
import qtx.org.negocio.IMovtoComisionable;
import qtx.org.negocio.NegocioException;
import qtx.org.negocio.Temporalidad;

public class CalculadoraComisionesTests {
	
	@Test
	@DisplayName("Calcular comision s/Producto válido, con Utilidad, cant=1")
	public void testCalcularComisionOK() {
		
		//Dados
		Producto producto = new Producto("X-1", "Camisa", 855, 355);
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		double comision = calculadora.calcularComision(producto, 1);
		
		//Entonces
//		assertEquals(50,comision,0.00001,"costo distinto del esperado");
		assertEquals(50, comision);
	}

	@Test
	public void testCalcularComision_utilidadNegativa() {
		//Dados
		Producto producto = new Producto("X-1", "Camisa", 255, 355); //Tiene utilidad negativa
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		double comision = calculadora.calcularComision(producto, 1);
		//Entonces
		assertEquals(0, comision);
	}
	
	@Test
	public void testCalcularComision_productoNulo() {
		//Dados
		Producto producto = null;
		
		//Cuando		
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		assertThrows(NegocioException.class, () -> {		
			calculadora.calcularComision(producto, 1);
		});
			
	}
	@Test
	public void testCalcularComision_cantidadEquivocada() {
		//Dados
		Producto producto = new Producto("X-1", "Camisa", 255, 355); //Tiene utilidad negativa
		
		//Cuando		
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		assertThrows(NegocioException.class, () -> {		
			calculadora.calcularComision(producto, -1);
		});
			
	}

	@Test
	public void testCalcularComisionServicioOK() {
		
		//Dados
		double porcComision = 0.10;
		double precioDia = 5.00;
		double comisionEsperada = precioDia * 30 * porcComision;
		Servicio servicio = new Servicio("MC01","membresia Club",precioDia);
		
		//Cuando
		IMovtoComisionable movto = servicio.getIMovtoComisionable(Temporalidad.MENSUAL, 1);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision(movto);
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}
	@Test
	public void testCalcularComisionProductoOK() {
		
		//Dados
		double porcComision = 0.10;
		double precio = 500.00;
		double costo = 200.00;
		double comisionEsperada = (precio - costo) * porcComision;
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
				
		//Cuando
		IMovtoComisionable movto = producto.getIMovtoComisionable(1);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision(movto);
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}

}
