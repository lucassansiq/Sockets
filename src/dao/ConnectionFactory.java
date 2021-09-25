/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Lucas Hype
 */
public class ConnectionFactory {
    
    private static final String URL = "jdbc:sqlite:C:\\Users\\Lucas\\Documents\\Estudos\\APS\\APS - Sockets\\Sockets\\db\\LoginDB.db";

    
    public static Connection getConnection(){
      try{
          return DriverManager.getConnection(URL);
      }catch (SQLException e){
          throw new RuntimeException("Erro de conexão");
      }
      
    }
    
    public static void closeConnection(Connection con){
        try{
            if(con != null) con.close();
        }catch (SQLException e){
            throw new RuntimeException("Erro de fechamento de conexao");
        }
    }
    
    public static void closeConnection(Connection con,PreparedStatement pst){
        try{
            if(pst != null) pst.close();
            closeConnection(con);
        }catch (SQLException e){
            throw new RuntimeException("Erro de fechamento de conexao");
        }
    }
    
    public static void closeConnection(Connection con,PreparedStatement pst,ResultSet rs){
        try{
            if(rs != null) rs.close();
            closeConnection(con,pst);
        }catch (SQLException e){
            throw new RuntimeException("Erro de fechamento de conexao");
        }
    }

}
