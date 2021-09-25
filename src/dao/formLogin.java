/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Lucas Hype
 */
public class formLogin extends javax.swing.JFrame{
    
    private int Acesso;
    
    public boolean consultar(String login, String senha) {
        boolean autenticado = false;
        String sql;
        try {
            Connection conn = ConnectionFactory.getConnection();

            sql = "SELECT cd_usuario, nm_usuario, nm_senha, cd_tipo FROM Login WHERE nm_usuario=? and nm_senha=?";
            PreparedStatement ps;
            ps = conn.prepareStatement(sql);
            ps.setString(1, login);
            ps.setString(2, senha);

            ResultSet rs;
            rs = ps.executeQuery();

            if (rs.next()) {                
                setAcesso(rs.getInt("cd_tipo"));
                autenticado = true;
            } else {
                ps.close();
                return autenticado;
            }
            
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, ex);
        }
        
        
        return autenticado;
    }

    public int getAcesso() {
        return Acesso;
    }

    public void setAcesso(int Acesso) {
        this.Acesso = Acesso;
    }

}
