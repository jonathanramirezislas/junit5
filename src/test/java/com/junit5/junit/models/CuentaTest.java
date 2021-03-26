package com.junit5.junit.models;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.junit5.junit.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Jonathan", new BigDecimal("1000.12345"));

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

    @Test
    void TestNombreCuenta() {
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
















