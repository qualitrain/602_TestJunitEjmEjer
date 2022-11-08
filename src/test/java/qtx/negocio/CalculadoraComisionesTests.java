package qtx.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Stream;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import qtx.org.entidades.Producto;
import qtx.org.entidades.Servicio;
import qtx.org.entidades.Temporalidad;
import qtx.org.negocio.CalculadoraComisiones;
import qtx.org.negocio.IMovtoComisionable;
import qtx.org.negocio.IdInvalidoException;
import qtx.org.negocio.NegocioException;

public class CalculadoraComisionesTests {
	
	@Nested
	@Tag("Depreciadas")
	@DisplayName("Tests que van a ser remplazados en la siguiente versión")
//	@Disabled("Para ver que pasa")
	class ProductoTestsPorDepreciar {
		
	@Test
	@DisplayName("Calcular comision s/Producto válido, con Utilidad y cant=1")
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
	
	@ParameterizedTest
	@DisplayName("Usando @MethodSource: Calcular comision s/Producto válido, con Utilidad y precio variable")
	@MethodSource("qtx.negocio.CalculadoraComisionesTests#getPreciosProducto")
	public void testCalcularComisionVariandoPrecioOK(double precio) {
		
		//Dados
		double costo = 355;
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		double porcComision = 0.10;
		int cantidad = 1;
		double comisionEsperada = (precio - costo) * cantidad * porcComision;
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision(producto, cantidad);
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}
	
	@ParameterizedTest(name = "Iteracion: {index} con porc de comision = {0}")
	@DisplayName("** Calcular comision s/Producto válido, con Utilidad y comision variable **")
	@ValueSource(doubles = {0.05,0.1,0.2,0.6,0.5})
	public void testCalcularComisionOK(double porcComision) {
		
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
	@DisplayName("Calcular comision s/Producto válido, sin Utilidad y cant=1")
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
	@DisplayName("Calcular comision s/Producto nulo")
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
	@DisplayName("Calcular comision s/Producto válido con cantidad <= 0")
	public void testCalcularComision_cantidadEquivocada() {
		//Dados
		double precio = 600;
		double costo = 355;
		Producto producto = new Producto("X-1", "Camisa", precio, costo); 
		int cantidad = -1;
		
		//Cuando		
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.10);
		assertThrows(NegocioException.class, () -> {		
			calculadora.calcularComision(producto, cantidad);
			});			
		}
	}
	@Test
	@DisplayName("Comision sobre servicio anual y cantidad = 10")
	@Tag("Humo")
	public void testCalcularComision_servicioAnual_cant10() {
		//Dados: Valores usados en la prueba
		double precioDiario = 10.0;
		Temporalidad duracionServicio = Temporalidad.ANUAL;
		double porcComisionAnual = 0.20;
		int cantServicios = 10;
		Servicio servicio = new Servicio("Str01","Streaming HBO", precioDiario);
		double comisionEsperada = precioDiario * duracionServicio.getNdias() 
				                               * porcComisionAnual * cantServicios;	
		
		//Cuando: Código a evaluar
		IMovtoComisionable posibleVenta = servicio.getIMovtoComisionable(duracionServicio, cantServicios);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		calculadora.agregarPorcComisionXtemporalidad(duracionServicio, porcComisionAnual);
		double comisionCalculada = calculadora.calcularComision(posibleVenta);
		
		//Entonces: Validación de resultados esperados vc obtenidos
		assertEquals(comisionEsperada, comisionCalculada);
	}
	
	@ParameterizedTest
	@DisplayName("Comision sobre servicio periodico y cantidad = 10")
	@ValueSource(ints = {0,2,3})
	public void testCalcularComision_servicioPeriodico_cant10(int iPeriodicidad) {
		Temporalidad[] temporalidades = {
				Temporalidad.MENSUAL,
				Temporalidad.BIMESTRAL,
				Temporalidad.SEMESTRAL,
				Temporalidad.ANUAL};
		
		//Dados: Valores usados en la prueba
		double precioDiario = 10.0;
		Temporalidad duracionServicio = temporalidades[iPeriodicidad];
		double porcComisionAnual = 0.20;
		int cantServicios = 10;
		Servicio servicio = new Servicio("Str01","Streaming HBO", precioDiario);
		double comisionEsperada = precioDiario * duracionServicio.getNdias() 
				                               * porcComisionAnual * cantServicios;	
		
		//Cuando: Código a evaluar
		IMovtoComisionable posibleVenta = servicio.getIMovtoComisionable(duracionServicio, cantServicios);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		calculadora.agregarPorcComisionXtemporalidad(duracionServicio, porcComisionAnual);
		double comisionCalculada = calculadora.calcularComision(posibleVenta);
		
		//Entonces: Validación de resultados esperados vc obtenidos
		assertEquals(comisionEsperada, comisionCalculada);
	}
	
	@ParameterizedTest
	@DisplayName("Comision sobre servicio periodico y cantidad = 10 usando @EnumSource")
	@EnumSource(value = Temporalidad.class, names = { "ANUAL", "MENSUAL" }, mode = Mode.EXCLUDE)
	public void testCalcularComision_servicioPeriodico_cant10_B(Temporalidad temporalidadI) {
		
		//Dados: Valores usados en la prueba
		double precioDiario = 10.0;
		Temporalidad duracionServicio = temporalidadI;
		double porcComisionAnual = 0.20;
		int cantServicios = 10;
		Servicio servicio = new Servicio("Str01","Streaming HBO", precioDiario);
		double comisionEsperada = precioDiario * duracionServicio.getNdias() 
				                               * porcComisionAnual * cantServicios;	
		
		//Cuando: Código a evaluar
		IMovtoComisionable posibleVenta = servicio.getIMovtoComisionable(duracionServicio, cantServicios);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		calculadora.agregarPorcComisionXtemporalidad(duracionServicio, porcComisionAnual);
		double comisionCalculada = calculadora.calcularComision(posibleVenta);
		
		//Entonces: Validación de resultados esperados vc obtenidos
		assertEquals(comisionEsperada, comisionCalculada);
	}

	@ParameterizedTest
	@DisplayName("Comision sobre servicio periodico y cantidad = 10 usando strings")
	@ValueSource(strings = {"ANUAL","MENSUAL"})
	public void testCalcularComision_servicioPeriodico_cant10_A(String valTemporalidadI) {
		
		//Dados: Valores usados en la prueba
		double precioDiario = 10.0;
		Temporalidad duracionServicio = Temporalidad.valueOf(valTemporalidadI);
		double porcComisionAnual = 0.20;
		int cantServicios = 10;
		Servicio servicio = new Servicio("Str01","Streaming HBO", precioDiario);
		double comisionEsperada = precioDiario * duracionServicio.getNdias() 
				                               * porcComisionAnual * cantServicios;	
		
		//Cuando: Código a evaluar
		IMovtoComisionable posibleVenta = servicio.getIMovtoComisionable(duracionServicio, cantServicios);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		calculadora.agregarPorcComisionXtemporalidad(duracionServicio, porcComisionAnual);
		double comisionCalculada = calculadora.calcularComision(posibleVenta);
		
		//Entonces: Validación de resultados esperados vc obtenidos
		assertEquals(comisionEsperada, comisionCalculada);
	}
	
	@DisplayName("Calcular comision s/Producto válido, con Utilidad y cant=1 usando IMovtoComisionable")
	@RepeatedTest(4)
	@Tag("Humo")
	public void testCalcularComisionProductoViaIMovtoComisionableOK(RepetitionInfo info) {
		//Dados: Valores usados en la prueba
		double precio=500;
		double costo=100 + (100 * info.getCurrentRepetition());
		int cantidad=1;
		double porComision=0.10;
		double comisionEsperada = (precio - costo) * porComision * cantidad;
		
		Producto prod = new Producto("Z23","Sudadera",precio,costo);
		
		System.out.println("Costo utilizado:" + costo);
		//Cuando: Código a evaluar
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porComision);
		double comisionCalculada = calculadora.calcularComision( prod.getIMovtoComisionable(cantidad) );
		//Entonces: Validación de resultados esperados vc obtenidos
		assertEquals(comisionEsperada, comisionCalculada);
	}
	
	@Test
	@DisplayName("Calcular comision s/Producto válido, sin Utilidad y cant=1 usando IMovtoComisionable")
	public void testCalcularComisionProductoViaIMovtoComisionable_utilidadNegativa() {
		//Dados: Valores usados en la prueba
		double precio=500;
		double costo=600;
		int cantidad=1;
		double porComision=0.10;
		double comisionEsperada = 0;
		Producto prod = new Producto("Z23","Sudadera",precio,costo);
		
		//Cuando: Código a evaluar
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porComision);
		double comisionCalculada = calculadora.calcularComision( prod.getIMovtoComisionable(cantidad) );
		//Entonces: Validación de resultados esperados vc obtenidos
		assertEquals(comisionEsperada, comisionCalculada);
		
	}
	@Test
	@DisplayName("Comision sobre servicio anual, sin tener configurado porc de comision anual")
	public void testCalcularComision_servicioAnual_cant10_sinPorcComsion() {
		//Dados: Valores usados en la prueba
		double precioDiario = 10.0;
		Temporalidad duracionServicio = Temporalidad.ANUAL;
		int cantServicios = 10;
		Servicio servicio = new Servicio("Str01","Streaming HBO", precioDiario);
		
		//Cuando: Código a evaluar
		IMovtoComisionable posibleVenta = servicio.getIMovtoComisionable(duracionServicio, cantServicios);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		Executable funcion = () -> calculadora.calcularComision(posibleVenta);
		
		//Entonces: Validación de resultados esperados vc obtenidos
		assertThrows(NegocioException.class, funcion ,"No lanza la excepcion esperada");
	}
	
	@ParameterizedTest(name="iteracion {index}, valor probado: [{0}]")
	@DisplayName("Comision sobre servicio con id inválido")
	@NullAndEmptySource
	@ValueSource(strings = {"   ", "\t", "\t "})
	@Disabled
	public void testCalcularComision_servicioAnual_idInvalido(String id) {
		//Dados: Valores usados en la prueba
		Servicio servicio = new Servicio(id,"Streaming HBO", 50);
		
		//Cuando: Código a evaluar
		IMovtoComisionable posibleVenta = servicio.getIMovtoComisionable(Temporalidad.BIMESTRAL, 1);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		Executable funcion = () -> calculadora.calcularComision(posibleVenta);
		
		//Entonces: Validación de resultados esperados vc obtenidos
		assertThrows(IdInvalidoException.class, funcion ,"No lanza la excepcion esperada");
	}
	public static Stream<Double> getPreciosProducto(){
		return Stream.of(
				(Double)600.0,
				(Double)600.80, 
				(Double)750.0, 
				(Double)1500.0, 
				(Double)1150.0);
	}
	
}
