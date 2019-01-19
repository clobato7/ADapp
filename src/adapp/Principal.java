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
    
    public boolean conexao = true; // situação da "conexao"
    
    public boolean getConexao() {
        return this.conexao;
    }

    public void setConexao(boolean c) {
        this.conexao = c;
        //setChanged();
        //notifyObservers(name);
    }
    
    
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

    boolean simInputData(boolean conex, int t, BufferedWriter w) throws IOException { // conex verifica a conexao | t numero de registros a simular o input
        int i = 1; // contar os loops
        Random gerador = new Random(); // para colocar id_cargo aleatorios no insert

        //if (conex = true) {
        while (conex == true) {

            insertFuncionario("Funcionario " + i, gerador.nextInt(5) + 1); // nome , id_cargo

            if (i == t) {
                conex = false;
                return conex;
            } // simulando desconexão

            i++;

        }

        if (conex == false) {
            // cria o temp.txt se conex tiver off 
            

            //} else if (conex = false) {
            while (conex == false) {

                w.write("FuncionarioTex " + i + "\t" + (gerador.nextInt(5) + 1));
                w.newLine();

                if (i == t) {
                    conex = true; // simulando reconexão
                }
                i++;

            }
            //writer.close();// isso aqui encerra o input no txt // isso n deve encerrar aqui
        }
        
        
        
        return conex;

    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        
        Principal app = new Principal(); // para colocar id_cargo aleatorios no insert
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(
                    "temp.txt")); // setando o writer pro arquivo temp.txt

        app.conexao = app.simInputData(app.conexao, 5, writer); // primeira leva de dados = 5 registros
        // desconexão aqui
        
        app.conexao = app.simInputData(app.conexao, 7, writer); // segunda leva de dados = 7 registros
        // reconexão aqui 
        
        app.conexao = app.simInputData(app.conexao, 5, writer); // primeira leva de dados = 5 registros
        // desconexão aqui
        
        app.conexao = app.simInputData(app.conexao, 3, writer); // segunda leva de dados = 7 registros
        // reconexão aqui 
        
        
        
        
        writer.close(); /* encerra o txt e salva nele, isso aqui eu to testando ainda, ele n deve ficar 
                na class input pq se quiser botar mais de 2 inputs offline ele lá vai apagar o 1º*/
        
    }

}
