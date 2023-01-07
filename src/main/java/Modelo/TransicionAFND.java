/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Saul Rodriguez Naranjo
 */
public class TransicionAFND {
    private String estadoOrigen;
    private char simbolo;
    private ArrayList<String> estadosFinales;

    /**
     * Constructor de TransicionAFND, dados un Estado de Origen, un Simbolo y un
     * ArrayList de Estados Finales construye la transicion.
     * @param estadoOrigen Estado de Origen
     * @param simbolo Simbolo para la transicion
     * @param estadosFinales Conjunto de Estados Finales
     */
    public TransicionAFND(String estadoOrigen, char simbolo, ArrayList<String> estadosFinales) {
        this.estadoOrigen = estadoOrigen;
        this.simbolo = simbolo;
        this.estadosFinales = estadosFinales;
    }

    /**
     * Devuelve el Estado de Origen
     * @return El Estado de Origen
     */
    public String getEstadoOrigen() {
        return estadoOrigen;
    }

    /**
     * Devuelve el Simbolo de la transicion
     * @return El Simbolo
     */
    public char getSimbolo() {
        return simbolo;
    }

    /**
     * Devuelve los Estados Finales de la transicion
     * @return Los Estados Finales
     */
    public ArrayList<String> getEstadosFinales() {
        return estadosFinales;
    }
    
    /**
     * Convierte en String la transicion
     * @return String representativo de la transicion
     */
    @Override
    public String toString(){
        return estadoOrigen + " '" + simbolo + "' " + estadosFinales;
    }
    
    /**
     * Clona la transicion AFND
     * @return Una copia de la transicion AFND
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Object obj = null;
        obj = super.clone();
        return obj;
    }
}
