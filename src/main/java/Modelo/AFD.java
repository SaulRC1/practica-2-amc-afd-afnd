/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Saul Rodriguez Naranjo
 */
public class AFD implements Cloneable, Proceso {

    private String[] estadosFinales;
    private String estadoInicial;
    private ArrayList<TransicionAFD> transiciones;

    /**
     * Constructor de un Automata AFD
     */
    public AFD() {
        transiciones = new ArrayList<>();
    }

    /**
     * Agrega una transicion AFD al automata
     *
     * @param e1 Estado de Origen
     * @param simbolo Simbolo para transitar
     * @param e2 Estado de Destino
     */
    public void agregarTransicion(String e1, char simbolo, String e2) {
        this.transiciones.add(new TransicionAFD(e1, simbolo, e2));
    }

    /**
     * Transita dado un Estado de Origen a un Estado de Destino
     *
     * @param estado Estado de Origen
     * @param simbolo Simbolo para transitar
     * @return Estado de Destino de la transicion
     */
    public String transicion(String estado, char simbolo) {
        boolean encontrado = false;
        int i = 0;
        while (i < this.transiciones.size() && !encontrado) {
            if (this.transiciones.get(i).getEstadoOrigen().equals(estado)
                    && this.transiciones.get(i).getSimbolo() == simbolo) {
                encontrado = true;
            } else {
                i++;
            }
        }
        if (encontrado) {
            return this.transiciones.get(i).getEstadoFinal();
        } else {
            return "M";
        }

    }

    /**
     * Analiza si el Estado insertado es Final o no
     *
     * @param estado Estado para analizar
     * @return Si el Estado es Final o no
     */
    @Override
    public boolean esFinal(String estado) {
        int i = 0;
        boolean encontrado = false;

        while (i < this.estadosFinales.length && !encontrado) {
            if (this.estadosFinales[i].equals(estado)) {
                encontrado = true;
            } else {
                i++;
            }
        }

        return encontrado;
    }

    /**
     * Dada una cadena de simbolos, indica si es reconocida o no por el automata
     * AFD
     *
     * @param cadena Cadena de Simbolos
     * @return Si la cadena es valida
     */
    @Override
    public boolean reconocer(String cadena) {
        char[] simbolo = cadena.toCharArray();
        String estado = this.estadoInicial;
        for (int i = 0; i < simbolo.length; i++) {
            estado = transicion(estado, simbolo[i]);
            System.out.println("Estado: " + estado);
        }
        return esFinal(estado);
    }

    /**
     * Convierte a String el Automata AFD
     *
     * @return El automata como String
     */
    @Override
    public String toString() {
        String devolver = "";

        devolver += "Estado Inicial: " + this.estadoInicial + "\n";

        devolver += "Estados Finales: ";

        for (int i = 0; i < this.estadosFinales.length; i++) {
            devolver += this.estadosFinales[i] + " ";
        }

        devolver += "\nTRANSICIONES:\n";

        for (int i = 0; i < this.transiciones.size(); i++) {
            devolver += this.transiciones.get(i).toString() + "\n";
        }

        return devolver;
    }

    /**
     * Clona el Automata AFD
     *
     * @return Clon del Automata AFD
     * @throws CloneNotSupportedException
     * @see Object
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        AFD automataCopia = new AFD();
        automataCopia.setEstadoInicial(this.estadoInicial);
        String[] estadosFinalesCopia = new String[this.estadosFinales.length - 1];
        for (int i = 0; i < this.estadosFinales.length; i++) {
            estadosFinalesCopia[i] = this.estadosFinales[i];
        }
        automataCopia.setEstadosFinales(estadosFinalesCopia);

        for (int i = 0; i < this.transiciones.size(); i++) {
            TransicionAFD transicionCopia = (TransicionAFD) this.transiciones.get(i).clone();
            automataCopia.agregarTransicion(transicionCopia.getEstadoOrigen(),
                    transicionCopia.getSimbolo(), transicionCopia.getEstadoFinal());
        }

        return (Object) automataCopia;
    }

    /**
     * Define los Estados Finales del automata
     *
     * @param estados Array de Estados Finales
     */
    public void setEstadosFinales(String[] estados) {
        this.estadosFinales = estados;
    }

    /**
     * Define el Estado Inicial del automata
     *
     * @param estadoInicial Estado Inicial
     */
    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    /**
     * Devuelve cual es el Estado Inicial del Automata
     *
     * @return El Estado Inicial
     */
    public String getEstadoInicial() {
        return this.estadoInicial;
    }

    /**
     * Dada una ruta a un fichero con un Automata AFD definido, se construira el
     * mismo dentro del programa.
     *
     * @param ruta Ruta del fichero seleccionado
     * @param automata Automata AFD donde se insertara el fichero
     */
    public static void leeFichero(String ruta, AFD automata) {
        FileReader entrada = null;
        try {
            File archivo = new File(ruta);
            entrada = new FileReader(archivo);

            BufferedReader buffer = new BufferedReader(entrada);
            String linea = "";
            String split_linea[] = null;

            //Leemos las dos primeras lineas, "ESTADOS:" e "INICIAL:" 
            //que no nos interesan
            for (int i = 0; i < 2; i++) {
                linea = buffer.readLine();
                if (i == 1) {
                    String[] split = linea.split(" ");
                    automata.setEstadoInicial(split[1]);
                }
            }

            //Leemos los estados finales y los añadimos a nuestro automata
            linea = buffer.readLine();
            split_linea = linea.split(" ");
            String[] estadosFinales = new String[split_linea.length - 1];
            for (int i = 0; i < estadosFinales.length; i++) {
                estadosFinales[i] = split_linea[i + 1];
            }
            automata.setEstadosFinales(estadosFinales);
            //SE LEERA LA CADENA "TRANSICIONES:" y se desecha
            linea = buffer.readLine();

            //Comenzamos a leer las transiciones e insertarlas en el automata
            while (!linea.equals("FIN")) {
                linea = buffer.readLine();
                if (!linea.equals("FIN")) {
                    split_linea = linea.split(" ");

                    String simbolo_array[] = split_linea[2].split("'");
                    char simbolo = simbolo_array[1].toCharArray()[0];

                    automata.agregarTransicion(split_linea[1], simbolo, split_linea[3]);
                }

            }

            entrada.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(AFD.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AFD.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                entrada.close();
            } catch (IOException ex) {
                Logger.getLogger(AFD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
