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
import java.util.ArrayList;
import java.util.List;
import model.Usuario;

/**
 *
 * @author Lucas Hype
 */
public class UsuarioDAO implements dao.Persistencia<Usuario>{
    
    private static UsuarioDAO dao = null ;
    
    public UsuarioDAO(){
        
    }
    
    public static UsuarioDAO getInstance(){
        if(dao == null) dao = new UsuarioDAO();
        
        return dao;
    }
    
    
    
    @Override
    public int create(Usuario c) {
        int id = 0;
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "INSERT INTO Login(nm_usuario, nm_senha, cd_tipo) VALUES"
                + "(?,?,?)";
        try{
            pst = con.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, c.getNome());
            pst.setString(2, c.getSenha());
            pst.setInt(3, c.getTipo());
            pst.execute();
            rs = pst.getGeneratedKeys();
            if (rs.next()){
                id = rs.getInt(1);
            }
        }catch(SQLException ex){
            id = 0;
        }finally{
            ConnectionFactory.closeConnection(con,pst,rs);
        }
        
        return id;
    }

    @Override
    public Usuario findByCodigo(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Usuario c = null;
        String sql = "SELECT * FROM Login where cd_usuario = ?";
        try{
            pst = con.prepareStatement(sql);
            pst.setInt(1,id);
            rs = pst.executeQuery();
            while (rs.next()){
                int codigo = rs.getInt("cd_usuario");
                String nome = rs.getString("nm_usuario");
                String Senha = rs.getString("nm_senha");
                int tipo = rs.getInt("cd_tipo");
                c = new Usuario(codigo,nome,Senha,tipo);
            }
            
        }catch(SQLException ex){
            throw new RuntimeException("Erro no Select");
        }finally{
            ConnectionFactory.closeConnection(con, pst, rs);
        }
        return c;
    }

    @Override
    public void delete(int id) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "DELETE from Login where cd_usuario = ?";
        try{
            pst = con.prepareStatement(sql);
            pst.setInt(1,id);
            pst.execute();
        }catch(SQLException ex){
            throw new RuntimeException("Erro no Delete");
        }finally{
            ConnectionFactory.closeConnection(con, pst, rs);
        }
    }

    @Override
    public void update(Usuario c) {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        String sql = "UPDATE Login set nm_usuario=?,nm_senha=?,cd_tipo=? where cd_usuario=?";
        try{
            pst = con.prepareStatement(sql);
            pst.setString(1, c.getNome());
            pst.setString(2, c.getSenha());
            pst.setInt(3, c.getTipo());
            pst.setInt(4,c.getCodigo());
            pst.execute();
        }catch(SQLException ex){
            throw new RuntimeException("Erro no update");
        }finally{
            ConnectionFactory.closeConnection(con, pst, rs);
        }
    } 

    @Override
    public List<Usuario> read() {
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement pst = null;
        ResultSet rs = null;
        Usuario c = null;
        String sql = "SELECT * FROM Login ORDER BY cd_usuario";
        List<Usuario> lista = new ArrayList<Usuario>();
        try{
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()){
                int codigo = rs.getInt("cd_usuario");
                String nome = rs.getString("nm_usuario");
                String senha = rs.getString("nm_senha");
                int tipo = rs.getInt("cd_tipo");
                lista.add(new Usuario(codigo,nome,senha,tipo));
            }
            
        }catch(SQLException ex){
            throw new RuntimeException("Erro no Select");
        }finally{
            ConnectionFactory.closeConnection(con, pst, rs);
        }
        return lista;
    }
        
    }
