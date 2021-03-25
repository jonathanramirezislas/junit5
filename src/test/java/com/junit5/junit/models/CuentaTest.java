package com.junit5.junit.models;

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


}
















