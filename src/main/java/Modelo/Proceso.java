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
public interface Proceso {
    public abstract boolean esFinal(String estado); //true si estado es un estado final
    public abstract boolean reconocer(String cadena);
    public abstract String toString(); //muestra las transiciones y estados finales
}
