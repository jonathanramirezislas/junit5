package com.junit5.junit.models;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.junit5.junit.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;


class CuentaTest {

    Cuenta cuenta;

    private TestInfo testInfo; ///Nos da infromacion del tgest asi como reflection
    private TestReporter testReporter;//Nos ayuda a tener una biblioteca de la fecha y hora

    @BeforeEach
    void initMetodoTest(TestInfo testInfo, TestReporter testReporter) {
        this.cuenta = new Cuenta("Jonathan", new BigDecimal("1000.12345"));
        this.testInfo = testInfo;
        this.testReporter = testReporter;
        System.out.println("iniciando el metodo.");
        testReporter.publishEntry(" ejecutando: " + testInfo.getDisplayName() + " " + testInfo.getTestMethod().orElse(null).getName()
                + " con las etiquetas " + testInfo.getTags());
    }

    @AfterEach
    void tearDown() {
        //System.out.println("finalizando el metodo de prueba.");
    }

    @BeforeAll
    static void beforeAll() {
        //System.out.println("inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        //System.out.println("finalizando el test");
    }
    @Tag("cuenta")
    @Nested
    @DisplayName("probando atributos de la cuenta")
    class CuentaTestNombreSaldo{

        @Test
        void TestNombreCuenta() {
            testReporter.publishEntry(testInfo.getTags().toString());
            if (testInfo.getTags().contains("cuenta")) {
                testReporter.publishEntry("hacer algo con la etiqueta cuenta");
            }
            String real = cuenta.getPersona();
            String esperado="Jonathan";
            assertEquals(esperado, real, () -> "el nombre de la cuenta no es el que se esperaba: se esperaba " + esperado
                    + " sin embargo fue " + real);
        }

        @Test
        @DisplayName("el saldo, que no sea null, mayor que cero, valor esperado.")
        void TestSaldoCuenta(){
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        }

        @Test
        @DisplayName("testeando referencias que sean iguales con el método equals.")
        void testReferenciaCuenta() {
            cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

    //        assertNotEquals(cuenta2, cuenta);
            assertEquals(cuenta2, cuenta);

        }
    }

    @Tag("cuenta")
    @Nested
    @DisplayName("probando operaciones cuenta")
    class CuentaOperacionesTest{

        @Test
        void testDebitoCuenta() {
            cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));
            cuenta.debito(new BigDecimal(100)); //restar 100
            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.12345", cuenta.getSaldo().toPlainString());
        }


        @Test
        void testCreditoCuenta() {
            cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));
            cuenta.credito(new BigDecimal(100)); //+100
            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.12345", cuenta.getSaldo().toPlainString());

        }

        @Test
        void testDineroInsuficienteExceptionCuenta() {
            Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
                cuenta.debito(new BigDecimal(1500));
            });
            String actual = exception.getMessage();
            String esperado = "Dinero Insuficiente";
            assertEquals(esperado, actual);
        }

        @Test
        void testTransferirDineroCuentas() {
            Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

            Banco banco = new Banco();
            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertEquals("1000.8989", cuenta2.getSaldo().toPlainString());
            assertEquals("3000", cuenta1.getSaldo().toPlainString());
        }
        @Test
        @DisplayName("probando relaciones entre las cuentas y el banco con assertAll.")
        void testRelacionBancoCuentas() {
            //fail();
            Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
            Cuenta cuenta2 = new Cuenta("Andres", new BigDecimal("1500.8989"));

            Banco banco = new Banco();
            banco.addCuenta(cuenta1);
            banco.addCuenta(cuenta2);

            banco.setNombre("Banco del Estado");
            banco.transferir(cuenta2, cuenta1, new BigDecimal(500));
            assertAll(
                    () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                        () -> "el valor del saldo de la cuenta2 no es el esperado."),

                    () -> assertEquals("3000", cuenta1.getSaldo().toPlainString(),
                            () -> "el valor del saldo de la cuenta1 no es el esperado."),

                    () -> assertEquals(2, banco.getCuentas().size(), () -> "el banco no tienes las cuentas esperadas"),

                    () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre()),

                    () -> assertEquals("Andres", banco.getCuentas().stream()
                            .filter(c -> c.getPersona().equals("Andres"))
                            .findFirst()
                            .get().getPersona()),

                    () -> assertTrue(banco.getCuentas().stream()
                            .anyMatch(c -> c.getPersona().equals("Jhon Doe")))

            );
        }
    }
    @Nested
    @DisplayName("Systema operativo")
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {
        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {
        }
    }

    @Nested
    @DisplayName("Version JAVA")
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_15)
        void soloJDK15() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_15)
        void testNoJDK15() {
        }
    }

    @Nested
    @DisplayName("Sistema operativo propiedades")
    class SistemPropertiesTest{

        //Ver propiedades del sistema y version java etc
        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((k, v)-> System.out.println(k + ":" + v));

        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = ".*11.*")
        void testJavaVersion() {
        }

        //ejecutamos si solo si estamos en sistema operativo diferente a 32
        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64() {
        }
        //ejeutar solo si es 32
        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNO64() {
        }

        //habilitar si el usuario del sistema operrastivo es
        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "jonathan")
        void testUsername() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }

    @Nested
    @DisplayName("Variables de ambiente")
    class VariableAmbienteTest{
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> getenv = System.getenv();
            getenv.forEach((k, v)-> System.out.println(k + " = " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-11.0.9.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcesadores() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
        }
    }

    @Tag("cuenta")
    @Test
    @DisplayName("test Saldo Cuenta Dev")
    void testSaldoCuentaDev() {
        boolean esDev = "env".equals(System.getProperty("ENV"));
        assumeTrue(esDev); //si es true ejecutara los siguientes assert
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Tag("cuenta")
    @Tag("banco")
    @Test
    @DisplayName("test Saldo Cuenta Dev 2")
    void testSaldoCuentaDev2() {
        boolean esDev = "env".equals(System.getProperty("ENV"));
        assumingThat(esDev, () -> { //si es true ejecutara los asserts 1 y 2
            assertNotNull(cuenta.getSaldo());//1
            assertEquals(1000.12345, cuenta.getSaldo().doubleValue());//2
        });
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    //repetir un test un numero determinado de veces  y displayname nos ayuda a nombre del test
    @DisplayName("Probando Debito Cuenta Repetir!")
    @RepeatedTest(value=5, name = "{displayName} - Repetición numero {currentRepetition} de {totalRepetitions}")
    void testDebitoCuentaRepetir(RepetitionInfo info) {
        if(info.getCurrentRepetition() == 3){
            System.out.println("estamos en la repeticion " + info.getCurrentRepetition());
        }
        cuenta.debito(new BigDecimal(100));
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Tag("param")
    @Nested
    @DisplayName("Pruebas parametrizadas")
    class PruebasParametrizadasTest{

        //test con parametros el cual se repite con cada uno d elos parametros 100,200 ...
        //si restas igual o mas de 1000.12345 lanzara un error ya que es el saldo que que se cuenta
        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @ValueSource(strings = {"100", "200", "300", "500", "700"}) //ints, doubles , Strings etc
        void testDebitoCuentaValueSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700"})//indice y valor
        void testDebitoCuentaCsvSource(String index, String monto) {
            System.out.println(index + " -> " + monto);
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }



        @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFileSource(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertNotNull(cuenta.getSaldo());
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        }

    }

    @ParameterizedTest(name = "numero {index} ejecutando con valor {0} - {argumentsWithNames}")
    @MethodSource("montoList")//usando  un metodo para usar parametros
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700");
    }



    @Nested
    @Tag("timeout")
    class EjemploTimeoutTest{

        //si la rpueba dura mas de 5 segundos falla el tes
        @Test
        @Timeout(5) // segundos
        void pruebaTimeout() throws InterruptedException {
            TimeUnit.SECONDS.sleep(2);//arriba de 5 segundos fallara 6 ,7 ,8 etc
        }

        @Test
        @Timeout(value = 9000, unit = TimeUnit.MILLISECONDS)//otra forma de establecer el tiempo de espera
        void pruebaTimeout2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(100);
        }

        @Test
        void testTimeoutAssertions() {
            //dentro de una parte del test y no todo el test
            assertTimeout(Duration.ofSeconds(5), ()->{
                TimeUnit.MILLISECONDS.sleep(4000);
            });
        }
    }

}
















