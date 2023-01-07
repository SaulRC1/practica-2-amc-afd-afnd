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
public class TransicionLambda {
    private String estadoOrigen;
    private ArrayList<String> estadosFinales;

    /**
     * Constructor de la clase TransicionLambda
     * @param estadoOrigen Estado de Origen de la Transicion Lambda
     * @param estadosFinales Estados Finales de la Transicion Lambda
     */
    public TransicionLambda(String estadoOrigen, ArrayList<String> estadosFinales) {
        this.estadoOrigen = estadoOrigen;
        this.estadosFinales = estadosFinales;
    }

    /**
     * Devuelve el Estado de Origen de la transicion Lambda
     * @return El Estado de Origen
     */
    public String getEstadoOrigen() {
        return estadoOrigen;
    }

    /**
     * Devuelve los Estados Finales de la Transicion Lambda
     * @return Los Estados Finales
     */
    public ArrayList<String> getEstadosFinales() {
        return estadosFinales;
    }
    
    /**
     * Convierte en un String la transicion Lambda
     * @return String representativo de la transicion Lambda
     */
    @Override
    public String toString(){
        return estadoOrigen + " '" + 'L' + "' " + estadosFinales;
    }
    
    /**
     * Clona la transicion Lambda
     * @return Una copia de la transicion Lambda
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Object obj = null;
        obj = super.clone();
        return obj;
    }
}
