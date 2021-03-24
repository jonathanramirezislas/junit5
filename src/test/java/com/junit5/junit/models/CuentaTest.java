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

}
















