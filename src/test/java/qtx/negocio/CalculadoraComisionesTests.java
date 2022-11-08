package qtx.negocio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import qtx.org.entidades.Producto;
import qtx.org.entidades.Servicio;
import qtx.org.entidades.Temporalidad;
import qtx.org.negocio.CalculadoraComisiones;
import qtx.org.negocio.IMovtoComisionable;
import qtx.org.negocio.NegocioException;

public class CalculadoraComisionesTests {
	
	@BeforeEach
	@Timeout(value = 500, unit = TimeUnit.MILLISECONDS)
	void preProcesarPrueba() {
		System.out.println("pre-procesamiento");
	}
	@AfterEach
	void postProcesarPrueba() {
		System.out.println("post-procesamiento");
	}
	
	@Nested
	@DisplayName("Pruebas que deben ser remplazadas -deben usar IMovtoConfigurable-")
	class PruebasDepreciadas{
		@Test
		@DisplayName("Calcular comision s/Producto válido, con Utilidad y cant=1")
		@Tag("Humo")
		@Tag("deprecated")
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
		@DisplayName("Calcular comision s/Producto válido, sin Utilidad y cant=1")
		@Tag("deprecated")
		@Disabled
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
		@Tag("deprecated")
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
		@Tag("deprecated")
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
	
	@Tag("Humo")
	@Test
	@DisplayName("Comisión sobre servicio anual con cantidad = 10")
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
	@Test
	@DisplayName("Calcular comision s/Producto válido recibiendo un IMovtoComisionable, con Utilidad y cant=10")
	public void testCalcularComisionOK_ProductoviaIMovtoComisionable() {
		
		//Dados
		double precio = 500;
		double costo = 250;
		int cantidad = 10;
		double porcComision = 0.15;
		double comisionEsperada = (precio - costo) * porcComision * cantidad;
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision( producto.getIMovtoComisionable(cantidad) );
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}
	@Test
	@DisplayName("Servicio semestral sin porcentaje de comisión definido")
	public void testCalcularComision_servicioAnual_sinComisionDefinida() {
		//Dados: Valores usados en la prueba
		double precioDiario = 10.0;
		Temporalidad duracionServicio = Temporalidad.SEMESTRAL;
		int cantServicios = 10;
		Servicio servicio = new Servicio("Str01","Streaming HBO", precioDiario);
		
		//Cuando: Código a evaluar
		IMovtoComisionable posibleVenta = servicio.getIMovtoComisionable(duracionServicio, cantServicios);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		Executable calComision = () -> calculadora.calcularComision(posibleVenta);
		
		//Cuando-Entonces: Validación de resultados esperados vc obtenidos
		assertThrows(NegocioException.class, calComision);
	}
	@RepeatedTest(4)
	@DisplayName("Calcular comision s/Producto válido recibiendo un IMovtoComisionable, con costo iterado")
	public void testCalcularComisionOK_ProductoviaIMovtoComisionable_variandoCosto(RepetitionInfo infoRep) {
		
		//Dados
		double precio = 500;
		double costo = 100 + 100 * infoRep.getCurrentRepetition();
		int cantidad = 10;
		double porcComision = 0.15;
		double comisionEsperada = (precio - costo) * porcComision * cantidad;
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		System.out.println("Repeticion "+infoRep.getCurrentRepetition() + " de " 
		                     + infoRep.getTotalRepetitions() + ". Producto:" + producto);   
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision( producto.getIMovtoComisionable(cantidad) );
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}
	@ParameterizedTest(name = "Test # {index}. Cantidad probada = {0}")
	@ValueSource(ints = {7, 10, 18, 1200, 5000})
	@DisplayName("Calcular comision s/Producto válido recibiendo un IMovtoComisionable, con Utilidad y cantidad parametrizada")
	public void testCalcularComisionOK_ProductoviaIMovtoComisionable(int cantidad) {
		
		//Dados
		double precio = 500;
		double costo = 250;
		double porcComision = 0.15;
		double comisionEsperada = (precio - costo) * porcComision * cantidad;
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision( producto.getIMovtoComisionable(cantidad) );
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}
	@ParameterizedTest(name = "Test # {index}. Prueba con: cantidad = {0}, precio = {1}, costo = {2} ")
	@DisplayName("Calcular comision s/Producto válido recibiendo PARAMETROS MÚLTIPLES")
	@MethodSource("getParametrosCalcularComisionOK")
	public void testCalcularComisionOK_ProductoviaIMovtoComisionable(int cantidad,double precio, double costo) {
		
		//Dados
		double porcComision = 0.15;
		double comisionEsperada = (precio - costo) * porcComision * cantidad;
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision( producto.getIMovtoComisionable(cantidad) );
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}
	@ParameterizedTest(name = "Test # {index}. Temporalidad duraciones[{0}]")
	@ValueSource(ints = {0, 2, 3, 4})
	@DisplayName("Comisión sobre servicio. Temporalidad parametrizada, cantidad = 10")
	public void testCalcularComision_servicioParametrizado_cant10(int iTemporalidad) {
		//Dados: Valores usados en la prueba
		Temporalidad[] duraciones = {Temporalidad.ANUAL, 
				                     Temporalidad.BIMESTRAL, 
				                     Temporalidad.MENSUAL, 
				                     Temporalidad.SEMESTRAL, 
				                     Temporalidad.QUINCENAL}; 
		double precioDiario = 10.0;
		Temporalidad duracionServicio = duraciones[iTemporalidad];
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
	@ParameterizedTest(name = "Test # {index}. Temporalidad duraciones[{0}]")
	@MethodSource("getTemporalidades")
	@DisplayName("Comisión sobre servicio. Temporalidad parametrizada, cantidad = 10")
	public void testCalcularComision_servicioParametrizado_cant10_2(Temporalidad tempI) {
		//Dados: Valores usados en la prueba
		double precioDiario = 10.0;
		Temporalidad duracionServicio = tempI;
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

	@ParameterizedTest(name = "Test # {index}. Cantidad probada = {0}")
	@MethodSource("getCantidades")
	@DisplayName("Calcular comision s/Producto válido recibiendo un IMovtoComisionable, con Utilidad y cantidad parametrizada (2)")
	public void testCalcularComisionOK_ProductoviaIMovtoComisionable02(int cantidad) {
		
		//Dados
		double precio = 500;
		double costo = 250;
		double porcComision = 0.15;
		double comisionEsperada = (precio - costo) * porcComision * cantidad;
		Producto producto = new Producto("X-1", "Camisa", precio, costo);
		
		//Cuando
		CalculadoraComisiones calculadora = new CalculadoraComisiones(porcComision);
		double comision = calculadora.calcularComision( producto.getIMovtoComisionable(cantidad) );
		
		//Entonces
		assertEquals(comisionEsperada, comision);
	}
	@ParameterizedTest(name="{index}, valor probado=[{0}]")
	@NullAndEmptySource
	@ValueSource(strings = {"   ","\t"})
	@DisplayName("Servicio con id inválido ")
	public void testIdServicioInvalido(String id){
		//Dados
		Servicio servicio = new Servicio(id,"renta Bodega",50);
		CalculadoraComisiones calculadora = new CalculadoraComisiones(0.05);
		calculadora.agregarPorcComisionXtemporalidad(Temporalidad.MENSUAL, 0.1);
		//Cuando y entonces
		assertThrows(NegocioException.class, 
					()->calculadora.calcularComision(servicio.getIMovtoComisionable(Temporalidad.MENSUAL, 1))
		);
	}
	public static Stream<Integer> getCantidades(){
		return Stream.of(12,34,55,67,99,106);
	}
	public static Stream<Temporalidad> getTemporalidades(){
		return Stream.of(Temporalidad.BIMESTRAL, Temporalidad.QUINCENAL, Temporalidad.MENSUAL);
	}
    public static Stream<Arguments> getParametrosCalcularComisionOK(){
    	return Stream.of(
    			Arguments.arguments(4,300,200),
    			Arguments.arguments(6,300,250),
    			Arguments.arguments(8,300,120)    			
    			);
    }
}
