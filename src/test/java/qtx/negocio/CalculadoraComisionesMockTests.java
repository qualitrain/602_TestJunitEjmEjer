package qtx.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import qtx.org.entidades.Producto;
import qtx.org.negocio.CalculadoraComisiones;
import qtx.org.negocio.IGestorComisionesArticulo;
//import qtx.org.presistencia.GestorComisionesMemoria;

@ExtendWith(MockitoExtension.class)
public class CalculadoraComisionesMockTests {
	@Mock
	private IGestorComisionesArticulo provComisionesArt;
	
	@InjectMocks
	private CalculadoraComisiones calculadora;

	@Test
	public void testCalcularComision() {
		//Dados
		double precio = 500;
		double costo = 250;
		double porcComisionArt = 0.10;
		double porcComisionGral = 0.15;
		int cantidad = 1;
		String idArt = "1-1";
		
		Producto art = new Producto(idArt,"Sudadera",precio,costo);
		double baseComision = (precio - costo) * cantidad;
		double comisionEsperada = baseComision * porcComisionGral;
		comisionEsperada += (baseComision - comisionEsperada) * porcComisionArt;
		
		//Cuando 
//		IGestorComisionesArticulo provComisionesArt = new GestorComisionesMemoria();
		provComisionesArt.agregarPorcComision(idArt, "estimulo Vta Temprada", porcComisionArt);
		when(provComisionesArt.getComisionItem(idArt)).thenReturn(porcComisionArt);
		
//		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComisionGral);
		calculadora.setPorcentajeComision(porcComisionGral);
//		calculadora.setProvComisiones(provComisionesArt);
		
		double comision = calculadora.calcularComision(art.getIMovtoComisionable(cantidad));
		
		//Entonces
		assertEquals(comisionEsperada, comision);
		verify(provComisionesArt).getComisionItem(idArt);
		
	}

}
