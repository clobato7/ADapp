/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Caio Lobato
 */
public class Principal {
        
    //Principal app = new Principal();
    


    
    
    /**
     * @param args the command line arguments
     */
    
        private Connection connect(String db) {
        // SQLite connection string
        String url = "jdbc:sqlite:" + db;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    
    public void insertFuncionario(String nome, int id_cargo) {
        String sql = "INSERT INTO funcionario(nome,id_cargo) VALUES(?,?)";
 
        try (Connection conn = this.connect("funcionario.db");
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setInt(2, id_cargo);        
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    boolean simInputData(boolean conex,int t) throws IOException { // conex verifica a conexao | t numero de registros a simular o input
        int i = 1; // contar os loops
        Random gerador = new Random(); // para colocar id_cargo aleatorios no insert
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                "temp.txt")); // setando o writer pro arquivo temp.txt
            
        //if (conex = true) {
            while (conex == true) {

                insertFuncionario("Funcionario " + i, gerador.nextInt(5) + 1); // nome , id_cargo

                if (i == t) {
                    conex = false;
                    return conex;
                } // simulando desconexão

                i++;

            }
        //} else if (conex = false) {
            while (conex == false) {

                writer.write("FuncionarioTex " + i + "\t" + (gerador.nextInt(5) + 1));
                writer.newLine();

                if (i == t) {
                    conex = true; // simulando reconexão
                }
                i++;

            }
            writer.close();// isso aqui encerra o input no txt

        return conex;
    
    }
    
    
    
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        
        boolean conexao = true; // situação da "conexao"
//        int i=1; // contar os loops e parar no 10 (desconexão)
        Principal app = new Principal(); // para colocar id_cargo aleatorios no insert
    
        
        conexao = app.simInputData(conexao, 5); // primeira leva de dados = 5 registros
        
        // desconexão aqui
                
        conexao = app.simInputData(conexao, 7); // segunda leva de dados = 7 registros
        
        // reconexão aqui

        
//        while (conexao == true) {
//            
//            app.insertFuncionario("Funcionario " + i, gerador.nextInt(5) + 1); // nome , id_cargo
//            
//            if (i == 10) {
//                conexao = false;
//                //break;
//                  } // simulando desconexão
//           
//            i++;
//            
//        }
//        
//        // salvando no temp.txt
//        
//        i=1;
//        
//        while (conexao == false) {
//            
//            writer.write("FuncionarioTex " + i +"\t" + (gerador.nextInt(5) + 1));
//            writer.newLine();
//                       
//            if (i == 5) conexao = true; // simulando reconexão
//            
//            i++;
//            
//        }
//        writer.close();// isso aqui encerra o input no txt

        
//        BufferedWriter writer = new BufferedWriter(new FileWriter(
//                "C:\\sqliteDB\\temp.txt"));
//        // Write these lines to the file.
//        // ... We call newLine to insert a newline character.
//        writer.write("CAT");
//        writer.newLine();
//        writer.write("DOG");
//        writer.newLine();
//        writer.write("BIRD");
//        writer.close();
//        
        
        
    }
    

}
