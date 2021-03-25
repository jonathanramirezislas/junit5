package com.junit5.junit.models;

import com.junit5.junit.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void TestNombreCuenta() {
        Cuenta cuenta = new Cuenta("Jonathan", new BigDecimal("1000.500"));
        String real = cuenta.getPersona();
        String esperado="Jonathan";
        assertEquals(esperado, real);
    }

    @Test
    void TestSaldoCuenta(){
        Cuenta cuenta = new Cuenta("Jonathan", new BigDecimal("1000.512"));
        assertEquals(1000.512, cuenta.getSaldo().doubleValue());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);

    }

    @Test
    void testDebitoCuenta() {
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));
        cuenta.debito(new BigDecimal(100)); //restar 100
        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }


    @Test
    void testCreditoCuenta() {
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));
        cuenta.credito(new BigDecimal(100)); //+100
        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }


    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("John Doe", new BigDecimal("1000.12345"));

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
















