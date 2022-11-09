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

@ExtendWith(MockitoExtension.class)
public class CalculadoraComisionesMocksTests {
	
	@Mock
	private IGestorComisionesArticulo provComisionesArt;
	
	@InjectMocks
	private CalculadoraComisiones calculadora;

	public CalculadoraComisionesMocksTests() {
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
		when(provComisionesArt.getComisionItem(idArt))
		      .thenReturn(porcComisionArt);
		calculadora.setPorcentajeComision(porcComisionGral);

		double comision = calculadora.calcularComision(art.getIMovtoComisionable(cantidad));
		//Entonces
		assertEquals(comisionEsperada, comision);
		verify(provComisionesArt).getComisionItem(idArt);
		
	}

}
