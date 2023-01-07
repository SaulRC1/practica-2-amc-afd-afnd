/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.AFD;
import Modelo.AFND;
import Vista.PanelAFD;
import Vista.PanelAFND;
import Vista.VistaPrincipal;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author Saul Rodriguez Naranjo
 */
public class ControladorVistaPrincipal {
    
    private VistaPrincipal vPrincipal;
    private PanelAFD vAFD;
    private PanelAFND vAFND;
    private JPanel vVacio;
    private AFD automataAFD;
    private AFND automataAFND;
    private int lugarCadena = 0; //lugar de la cadena actual en el paso a paso del
                                 //Automata AFD
    private String estadoActual = null; //Estado actual en el que se encuentra el
                                        //Automata AFD
    private String pasoAnterior = ""; //String que representa el Estado Anterior del
                                      //Automata AFD
    
    private ArrayList<String> estadoActualAFND = null;
    private int lugarCadenaAFND = 0;
    private String pasoAnteriorAFND = "";

    /**
     * Constructor del controlador, dentro se definiran las vistas y se gestionara
     * la funcionalidad
     */
    public ControladorVistaPrincipal() {
        vPrincipal = new VistaPrincipal();
        vPrincipal.setLocationRelativeTo(null);
        vAFD = new PanelAFD();
        vAFND = new PanelAFND();
        vVacio = new JPanel();
        //vVacio.setSize(600, 400);
        
        
        
        vPrincipal.mi_AFD.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ev){
                mostrarVentanaAFD(ev);
            }
        });
        
        vPrincipal.mi_AFND.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarVentanaAFND(e);
            }
        });
        
        vAFD.btnCargar.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent ev){
                cargarFichero(ev);
            }
        });
        vAFD.btnUnaVez.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                hacerDeUnaVez(evt);
            }
        });
        
        vAFD.btnPaso.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                simularPasoAPaso(evt);
            }
        });
        
        vAFD.btnLimpiarPaso.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                limpiarPasoAPaso(evt);
            }
        });
        
        vAFND.btnCargarAFND.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                cargarAutomataAFND(evt);
            }
        });
        
        vAFND.btnUnaVez.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent evt){
                hacerAFNDDeUnaVez(evt);
            }
        });
        
        vAFND.btnPaso.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt){
                simularAFNDPasoAPaso(evt);
            }
        });
        
        vAFND.btnLimpiarPasoAPaso.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt){
                limpiarAFNDPasoAPaso(evt);
            }
        });
        
        vPrincipal.getContentPane().setLayout(new CardLayout());
        vPrincipal.add(vAFD);
        vPrincipal.add(vVacio);
        vPrincipal.add(vAFND);
        vVacio.setVisible(true);
        vAFD.setVisible(false);
        vAFND.setVisible(false);
        vPrincipal.setVisible(true);
        vPrincipal.setTitle("Practica 2 - AMC Saul Rodriguez Naranjo");
    }
    
    /**
     * Muestra la ventana para representar el Automata AFND
     * @param ev 
     */
    private void mostrarVentanaAFND(ActionEvent ev){
        vVacio.setVisible(false);
        vAFD.setVisible(false);
        vAFND.setVisible(true);
        
    }
    
    /**
     * Muestra la ventana para representar el Automata AFD
     * @param ev ActionEvent
     */
    private void mostrarVentanaAFD(ActionEvent ev){
        vVacio.setVisible(false);
        vAFND.setVisible(false);
        vAFD.setVisible(true);
    }
    
    /**
     * Muestra una ventana para cargar el fichero que define un Automata AFD
     * @param ev MouseEvent
     */
    private void cargarFichero(MouseEvent ev){
        automataAFD = new AFD();
        JFileChooser fc = new JFileChooser();
        int seleccion = fc.showOpenDialog(null);
        
        if(seleccion == JFileChooser.APPROVE_OPTION){
            File ficheroSeleccionado = fc.getSelectedFile();
            AFD.leeFichero(ficheroSeleccionado.getAbsolutePath(), automataAFD);
            this.leeFicheroAFD(ficheroSeleccionado.getAbsolutePath(), vAFD.txaAutomataCargado);
            System.out.println(automataAFD);
            this.estadoActual = automataAFD.getEstadoInicial();
            vAFD.txaPaso.setText(this.estadoActual);
        }
        
        
    }
    
    /**
     * Realiza de una sola vez el Automata AFD
     * @param evt 
     */
    private void hacerDeUnaVez(MouseEvent evt){
        String cadena = vAFD.txtEstados.getText();
        boolean cadenareconocida;
        if(automataAFD != null){
            cadenareconocida = automataAFD.reconocer(cadena);
            
            if(cadenareconocida){
                System.out.println("SE RECONOCE LA CADENA");
                vAFD.lblValido.setForeground(Color.GREEN);
                vAFD.lblValido.setText("CADENA VALIDA");
                
            } else {
                System.out.println("NO SE RECONOCE");
                vAFD.lblValido.setForeground(Color.RED);
                vAFD.lblValido.setText("CADENA NO VALIDA");
            }
        }
    }
    
    /**
     * Realiza paso a paso el Automata AFD
     * @param evt 
     */
    private void simularPasoAPaso(MouseEvent evt){
        String cadena = vAFD.txtEstados.getText();
        String[] split = cadena.split("");
        
        this.pasoAnterior = vAFD.txaPaso.getText();
        if(lugarCadena != split.length){
            estadoActual = this.automataAFD.transicion(this.estadoActual, split[this.lugarCadena].toCharArray()[0]);
            vAFD.txaPaso.setText(this.pasoAnterior + "\n" + this.estadoActual);
            this.lugarCadena++;
        } else {
            this.hacerDeUnaVez(evt);
        }
        
        
    }
    
    /**
     * Realiza paso a paso el Automata AFND
     * @param evt 
     */
    private void simularAFNDPasoAPaso(MouseEvent evt){
        String cadena = vAFND.txtSimbolos.getText();
        String[] split = cadena.split("");
        
        this.pasoAnterior = vAFND.txtSimulacion.getText();
        if(lugarCadenaAFND != split.length){
            estadoActualAFND = this.automataAFND.transicion(this.estadoActualAFND, split[this.lugarCadena].toCharArray()[0]);
            vAFND.txtSimulacion.setText(this.pasoAnterior + "\n" + this.estadoActualAFND);
            this.lugarCadenaAFND++;
        } else {
            this.hacerAFNDDeUnaVez(evt);
        }
    }
    
    
    /**
     * Limpia el JTextArea del paso a paso del Automata AFD y devuelve los parametros
     * necesarios a su estado inicial
     * @param evt 
     */
    private void limpiarPasoAPaso(MouseEvent evt){
        this.estadoActual = this.automataAFD.getEstadoInicial();
        vAFD.txaPaso.setText(estadoActual);
        this.lugarCadena = 0;
    }
    
    /**
     * Limpia el JTextArea del paso a paso del Automata AFND y devuelve los parametros
     * necesarios a su estado inicial
     * @param evt 
     */
    private void limpiarAFNDPasoAPaso(MouseEvent evt){
        this.estadoActualAFND = this.automataAFND.getEstadoInicial();
        vAFND.txtSimulacion.setText(this.estadoActualAFND.toString());
        this.lugarCadenaAFND = 0;
    }
    
    /**
     * Representa en el JTextArea el Automata AFD cargado
     * @param ruta Ruta del fichero que contiene al automata
     * @param txArea JTextArea donde se representa
     */
    private void leeFicheroAFD(String ruta, JTextArea txArea){
        FileReader entrada = null;
        try {
            File archivo = new File(ruta);
            entrada = new FileReader(archivo);

            BufferedReader buffer = new BufferedReader(entrada);
            String linea = "";
            String mostrarAutomata = "";

            //Comenzamos a leer las transiciones e insertarlas en el automata
            while (!linea.equals("FIN")) {
                linea = buffer.readLine();
                if (!linea.equals("FIN")) {
                    mostrarAutomata += "\n" + linea;
                }

            }

            entrada.close();
            txArea.setText(mostrarAutomata);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ControladorVistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ControladorVistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                entrada.close();
            } catch (IOException ex) {
                Logger.getLogger(ControladorVistaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Muestra una ventana para cargar el fichero que define un Automata AFND
     * @param evt 
     */
    private void cargarAutomataAFND(MouseEvent evt){
        automataAFND = new AFND();
        JFileChooser fc = new JFileChooser();
        int seleccion = fc.showOpenDialog(null);
        
        if(seleccion == JFileChooser.APPROVE_OPTION){
            File ficheroSeleccionado = fc.getSelectedFile();
            AFND.leeFichero(ficheroSeleccionado.getAbsolutePath(), automataAFND);
            //this.leeFicheroAFD(ficheroSeleccionado.getAbsolutePath(), vAFND.txtAutomataCargado);
            vAFND.txtAutomataCargado.setText(this.automataAFND.toString());
            System.out.println(automataAFND);
            this.estadoActualAFND = automataAFND.getEstadoInicial();
            vAFND.txtSimulacion.setText(this.estadoActualAFND.toString());
        }
    }
    
    /**
     * Realiza de una sola vez el Automata AFND
     * @param evt 
     */
    private void hacerAFNDDeUnaVez(MouseEvent evt){
        String cadena = vAFND.txtSimbolos.getText();
        boolean cadenareconocida;
        if(automataAFND != null){
            cadenareconocida = automataAFND.reconocer(cadena);
            
            if(cadenareconocida){
                System.out.println("SE RECONOCE LA CADENA");
                vAFND.lblValida.setForeground(Color.GREEN);
                vAFND.lblValida.setText("CADENA VALIDA");
                
            } else {
                System.out.println("NO SE RECONOCE");
                vAFND.lblValida.setForeground(Color.RED);
                vAFND.lblValida.setText("CADENA NO VALIDA");
            }
        }
    }
    
}
