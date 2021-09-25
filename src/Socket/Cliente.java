/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Socket;

import static Socket.Servidor.myFiles;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import javax.swing.*;
import static javax.swing.GroupLayout.Alignment.CENTER;
import javax.swing.border.EmptyBorder;
/**
 *
 * @author Lucas Hype
 */
public class Cliente extends JFrame implements ActionListener, KeyListener {
    
    
    private static final long serialVersionUID = 1L;
    private JTextArea texto;
    private JTextField txtMsg;
    private JButton btnSend;
    private JButton btnSair;
    private JLabel lblHistorico;
    private JLabel lblMsg;
    private JPanel pnlContent;
    private Socket socket;
    private OutputStream ou ;
    private Writer ouw;
    private BufferedWriter bfw;
    private JTextField txtIP;
    private JTextField txtPorta;
    private JTextField txtNome;
    private JButton jbChooseFile;
    private JLabel jlFileName;

    
    
    
    // Defina o tamanho que deve ser o tamanho preferido para dentro de um contêiner.
    // Acessado de dentro da classe interna precisa ser final ou efetivamente final.
    final File[] fileToSend = new File[1];
    
    
            
    public Cliente() throws IOException{
        
        JLabel lblMessage = new JLabel("Verificar!");
        txtIP = new JTextField("127.0.0.1");
        txtPorta = new JTextField("12345");
        txtNome = new JTextField("Cliente");
        Object[] texts = {lblMessage, txtIP, txtPorta, txtNome };
        JOptionPane.showMessageDialog(null, texts);
         

         
        
       
         pnlContent = new JPanel();
         texto = new JTextArea(15,25);
         texto.setEditable(false);
         texto.setBackground(new Color(240,240,240));
         txtMsg = new JTextField(20);
         lblHistorico = new JLabel("Histórico");
         lblMsg = new JLabel("Mensagem");
         btnSend = new JButton("Enviar");
         btnSend.setToolTipText("Enviar Mensagem");
         btnSair = new JButton("Sair");
         btnSair.setToolTipText("Sair do Chat");
         btnSend.addActionListener(this);
         btnSair.addActionListener(this);
         btnSend.addKeyListener(this);
         txtMsg.addKeyListener(this);
         JScrollPane scroll = new JScrollPane(texto);
         texto.setLineWrap(true);
         pnlContent.add(lblHistorico);
         pnlContent.add(scroll);
         pnlContent.add(lblMsg);
         pnlContent.add(txtMsg);
         pnlContent.add(btnSair);
         pnlContent.add(btnSend);
         pnlContent.setBackground(Color.LIGHT_GRAY);
         texto.setBorder(BorderFactory.createEtchedBorder(Color.BLUE,Color.BLUE));
         txtMsg.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLUE));
         setTitle(txtNome.getText());
         setContentPane(pnlContent);
         setLocationRelativeTo(null);
         setResizable(false);
         setSize(290,450);
         setVisible(true);
         setDefaultCloseOperation(EXIT_ON_CLOSE);
         
         jlFileName = new JLabel("Escolha um arquivo para enviar.");
         
         jbChooseFile = new JButton("Escolher arquivo");
         
         pnlContent.add(jlFileName);
         pnlContent.add(jbChooseFile);

         
         // Ação do botão para escolher o arquivo.
        // Esta é uma classe interna, portanto, precisamos que fileToSend seja final.
        jbChooseFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                jFileChooser.setDialogTitle("Escolha um arquivo para enviar.");
                if (jFileChooser.showOpenDialog(null)  == JFileChooser.APPROVE_OPTION) {
                    // Obtenha o arquivo selecionado.
                    fileToSend[0] = jFileChooser.getSelectedFile();
                    // Altere o texto do rótulo java swing para ter o nome do arquivo.
                    jlFileName.setText("Arquivo: " + fileToSend[0].getName());
                }
            }
        });    
    }
    
    
    
    
    /***
  * Método usado para conectar no server socket, retorna IO Exception caso dê algum erro.
  * @throws IOException
  */
    public void conectar() throws IOException{

      socket = new Socket(txtIP.getText(),Integer.parseInt(txtPorta.getText()));
      ou = socket.getOutputStream();
      ouw = new OutputStreamWriter(ou);
      bfw = new BufferedWriter(ouw);
      bfw.write(txtNome.getText()+"\r\n");
      bfw.flush();
    }

    /***
      * Método usado para enviar mensagem para o server socket
      * @param msg do tipo String
      * @throws IOException retorna IO Exception caso dê algum erro.
      */
      public void enviarMensagem(String msg) throws IOException{

        if(msg.equals("Sair")){
          bfw.write("Desconectado \r\n");
          texto.append("Desconectado \r\n");
        }else{
          bfw.write(msg+"\r\n");
          texto.append( txtNome.getText() + " diz -> " +         txtMsg.getText()+"\r\n");
        }
         bfw.flush();
         txtMsg.setText("");
    }

      /**
     * Método usado para receber mensagem do servidor
     * @throws IOException retorna IO Exception caso dê algum erro.
     */
    public void escutar() throws IOException{

       InputStream in = socket.getInputStream();
       InputStreamReader inr = new InputStreamReader(in);
       BufferedReader bfr = new BufferedReader(inr);
       String msg = "";

        while(!"Sair".equalsIgnoreCase(msg))

           if(bfr.ready()){
             msg = bfr.readLine();
           if(msg.equals("Sair"))
             texto.append("Servidor caiu! \r\n");
            else
             texto.append(msg+"\r\n");
            }
    }

        /***
      * Método usado quando o usuário clica em sair
      * @throws IOException retorna IO Exception caso dê algum erro.
      */
      public void sair() throws IOException{

       enviarMensagem("Sair");
       bfw.close();
       ouw.close();
       ou.close();
       socket.close();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

      try {
         if(jlFileName.getText() != "Escolha um arquivo para enviar."){
             enviarArquivo();
         }
         else if(e.getActionCommand().equals(btnSend.getActionCommand()))
            enviarMensagem(txtMsg.getText());
         else
            if(e.getActionCommand().equals(btnSair.getActionCommand()))
            sair();
         } catch (IOException e1) {
              // TODO Auto-generated catch block
              e1.printStackTrace();
         }
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
           try {
              enviarMensagem(txtMsg.getText());
           } catch (IOException e1) {
               // TODO Auto-generated catch block
               e1.printStackTrace();
           }
       }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
      // TODO Auto-generated method stub
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
      // TODO Auto-generated method stub
    }
    
    //Envia o arquivo para o servidor de arquivos e um aviso para o chat
    //Cria um socket na porta para envio de arquivos
    public void enviarArquivo() throws IOException{ 
        
        // Se um arquivo ainda não foi selecionado, exiba esta mensagem.
        if (fileToSend[0] == null) {
            jlFileName.setText("Escolha um arquivo para enviar primeiro!");
            // Se um arquivo foi selecionado, faça o seguinte.
        } else {
            try {
                // Crie um fluxo de entrada no arquivo que deseja enviar.
                FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                // Crie uma conexão de soquete para se conectar ao servidor.
                Socket socket = new Socket("127.0.0.1", 1234);
                // Crie um fluxo de saída para gravar e gravar no servidor por meio da conexão de soquete.
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                // Obtenha o nome do arquivo que deseja enviar e armazene-o em nome do arquivo.
                String fileName = fileToSend[0].getName();
                // Converta o nome do arquivo em uma matriz de bytes a ser enviada ao servidor.
                byte[] fileNameBytes = fileName.getBytes();
                // Crie uma matriz de bytes do tamanho do arquivo para não enviar poucos ou muitos dados ao servidor.
                byte[] fileBytes = new byte[(int)fileToSend[0].length()];
                // Coloque o conteúdo do arquivo na matriz de bytes a ser enviada para que esses bytes possam ser enviados ao servidor.
                fileInputStream.read(fileBytes);
                // Envie o comprimento do nome do arquivo para que o servidor saiba quando parar de ler.
                dataOutputStream.writeInt(fileNameBytes.length);
                // Envie o nome do arquivo.
                dataOutputStream.write(fileNameBytes);
                // Envie o comprimento da matriz de bytes para que o servidor saiba quando parar de ler.
                dataOutputStream.writeInt(fileBytes.length);
                // Envie o arquivo real.
                dataOutputStream.write(fileBytes);      
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        String msg = fileToSend[0].getName();
        if(msg.equals("Sair")){
          bfw.write("Desconectado \r\n");
          texto.append("Desconectado \r\n");
        }else{
          bfw.write(msg+"\r\n");
          texto.append(txtNome.getText() + " Enviou o arquivo -> " + msg + "\r\n");
          jlFileName.setText("Escolha um arquivo para enviar.");
        }
        bfw.flush();
        txtMsg.setText("");
        
    }
       
    public static void main(String[] args) throws IOException{
        Cliente app = new Cliente();
        app.conectar();
        app.escutar();
    }
    
    public static void executar() throws IOException{
        Cliente app = new Cliente();
        app.conectar();
        app.escutar();
    }
    
    
    
   


}
