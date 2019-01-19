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
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;

/**
 *
 * @author Caio Lobato
 */
public class Principal extends Observable {

    /**
     * @param args the command line arguments
     */
    public boolean conexao = true; // situação da "conexao"
    BufferedWriter writer;
            
    public Principal() {

    }

    public boolean getConexao() {
        return this.conexao;
    }

    public void setConexao(boolean c) {
        this.conexao = c;

        String status = "";
        if (c == true) {
            status = "ON";
        } else if (c == false) {
            status = "OFF";
        }
        setChanged();
        notifyObservers(c);
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

    String readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
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

    public void insertFuncionarioFromTxt(String sql) {
   
        String sqlArray[] = sql.split("\\r?\\n"); //separa os comandos em uma array pq se n ele executa só o primeiro 


        try (Connection conn = this.connect("funcionario.db");) {
            for (int i = 0; i < sqlArray.length; i++) {
                PreparedStatement pstmt = conn.prepareStatement(sqlArray[i]);
                pstmt.executeUpdate();
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        System.out.println("Inseriu no banco o(s) registro(s) do temp.txt.");

    }

    public void simInputData(Principal a, int t) throws IOException { // conex verifica a conexao | t numero de registros a simular o input
        int i = 1; // contar os loops
        Random gerador = new Random(); // para colocar id_cargo aleatorios no insert
        
        if(a.getConexao() == false){
            a.writer = new BufferedWriter(new FileWriter(
                "temp.txt")); // setando o writer pro arquivo temp.txt só se tiver offline
        }
        
        

        while (a.getConexao() == true) {

            insertFuncionario("Funcionario " + i, gerador.nextInt(5) + 1); // nome , id_cargo

            if (i == t) {
                //conex = false;
                //return conex;
                System.out.println("Inseriu no banco " + i + " registro(s).");
                //a.setConexao(false);
                return;
            } // simulando desconexão

            i++;

        }

        while (a.getConexao() == false) {
            
            
            
            a.writer.write("INSERT INTO funcionario(nome,id_cargo) VALUES('FuncionarioText " + i + "'," + (gerador.nextInt(5) + 1) + ");");
            //w.write("FuncionarioTex " + i + "\t" + (gerador.nextInt(5) + 1));
            a.writer.newLine();

            if (i == t) {
                //conex = true; // simulando reconexão
                a.writer.close(); 
                /* encerra o txt e salva nele, isso aqui eu to testando ainda, ele n deve ficar 
         na class input pq se quiser botar mais de 2 inputs offline ele lá vai apagar o 1º*/
                
                System.out.println("Inseriu no temp.txt " + i + " registro(s).");
                //setConexao(true);
                return;
            }
            i++;

        }


    }

    public static void main(String[] args) throws IOException {
        // TODO code application logic here

        Principal app = new Principal(); // para colocar id_cargo aleatorios no insert

        app.writer = new BufferedWriter(new FileWriter(
                "temp.txt")); // setando o writer pro arquivo temp.txt

        Monitoramento monit = new Monitoramento(app);

        app.addObserver(monit);

        app.simInputData(app, 5); // primeira leva de dados = 5 registros
        
        // desconexão aqui
        app.setConexao(false);

        app.simInputData(app, 7); // segunda leva de dados = 7 registros
                
        // reconexão aqui 
        app.setConexao(true);
            

    }

}
