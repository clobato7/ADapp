/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapp;

import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Caio Lobato
 */
public class Monitoramento implements Observer {

    Principal app;

    public Monitoramento(Principal p) {
        this.app = p;
        //m_conexao = null;
        System.out.println("Monitoramento de Conexão criado");
    }

    @Override
    public void update(Observable obs, Object arg) {
        
        if(app.getConexao()==true) {
            System.out.println("Conexão = ON");
            //s            
            String tempQuery =""; 
            try {
                tempQuery = app.readFile("temp.txt"); // salva o conteudo do txt numa string
            } catch (IOException ex) {
                Logger.getLogger(Monitoramento.class.getName()).log(Level.SEVERE, null, ex);
            }
                        
            app.insertFuncionarioFromTxt(tempQuery); // inserte da string (txt)
        }
        else if (app.getConexao()==false) System.out.println("Conexão = OFF");;               
                                
    }

}
