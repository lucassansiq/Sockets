/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import dao.UsuarioDAO;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Lucas Hype
 */
public class Usuario {
    
    private int codigo,tipo = 0;
    private String nome, senha = null;
    

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Usuario(int codigo, String nome, String senha, int tipo) {
        setCodigo(codigo);
        setNome(nome);
        setSenha(senha);
        setTipo(tipo);
    }
    
    public Usuario(String nome, String senha, int tipo) {
        setNome(nome);
        setSenha(senha);
        setTipo(tipo);
        gravar();
    }
    
    public Usuario(){}

    @Override
    public String toString() {
        return (
                "Codigo......." + getCodigo() + "\n" + 
                "Usuario......" + getNome() + "\n" + 
                "Senha........" + getSenha() + "\n" + 
                "Tipo........." + getTipo());
    }
    
     private void gravar(){
        UsuarioDAO dao = new UsuarioDAO();
        int codigo = dao.create(this);
    }
    

     public static DefaultTableModel getTableModel(){
        List<Usuario> lista = UsuarioDAO.getInstance().read();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Codigo");
        modelo.addColumn("Nome");
        modelo.addColumn("Senha");
        modelo.addColumn("Tipo");
        for (Usuario c: lista){
            String nome = String.valueOf(c.getCodigo());
            int numero = c.getTipo();
            String tipo = "";
            if(numero == 0)
                tipo = "Usuario";
            else
                tipo = "Administrador"; 
            String[] reg = {nome, c.getNome(), c.getSenha(), tipo};
            modelo.addRow(reg);
        }
        
        return modelo;

    }
    
    
    

}
  
     
   
 