package com.junit5.junit.models;

import java.math.BigDecimal;
import java.util.List;

public class Banco {


    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {

    }
}
