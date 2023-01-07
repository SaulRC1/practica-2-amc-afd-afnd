/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *
 * @author Saul Rodriguez Naranjo
 */
public class TransicionAFD {
    
    private String estadoOrigen;
    private char simbolo;
    private String estadoFinal;

    /**
     * Constructor de TransicionAFD, dados un Estado de Origen, un Simbolo y un
     * Estado Final construye la transicion.
     * @param estadoOrigen Estado de Origen
     * @param simbolo Simbolo para la transicion
     * @param estadoFinal Estado de Destino
     */
    public TransicionAFD(String estadoOrigen, char simbolo, String estadoFinal) {
        this.estadoOrigen = estadoOrigen;
        this.simbolo = simbolo;
        this.estadoFinal = estadoFinal;
    }

    /**
     * Devuelve el Estado de Origen de la transicion
     * @return El Estado de Origen
     */
    public String getEstadoOrigen() {
        return estadoOrigen;
    }

    /**
     * Devuelve el Simbolo de la transicion
     * @return El Simbolo de la transicion
     */
    public char getSimbolo() {
        return simbolo;
    }

    /**
     * Devuelve el Estado Final de la transicion
     * @return El Estado Final
     */
    public String getEstadoFinal() {
        return estadoFinal;
    }
    
    /**
     * Convierte la transicion a tipo String
     * @return String representativo de la transicion
     */
    @Override
    public String toString(){
        return estadoOrigen + " '" + simbolo + "' " + estadoFinal;
    }
    
    /**
     * Clona la transicion AFD
     * @return Una copia de la transicion AFD
     * @throws CloneNotSupportedException 
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        Object obj = null;
        obj = super.clone();
        return obj;
    }
}
