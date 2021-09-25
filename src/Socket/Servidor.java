/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Socket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor extends Thread {
    

    // Array list para reter informações sobre os arquivos recebidos
    static ArrayList<MyFile> myFiles = new ArrayList<>();
    private static ArrayList<BufferedWriter>clientes;
    private static ServerSocket server;
    private String nome;
    private Socket con;
    private InputStream in;
    private InputStreamReader inr;
    private BufferedReader bfr;
    

    public static void main(String[] args) throws IOException {

        // Usado para rastrear o arquivo (jpanel que contém o nome do arquivo em uma Label).
        int fileId = 0;

        
        JFrame jFrame = new JFrame("Arquivos");
        jFrame.setSize(400, 400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jlTitle = new JLabel("Arquivos");
        jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20,0,10,0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jlTitle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);

        // Crie um soquete de servidor com o qual o servidor estará escutando.
        ServerSocket serverSocket = new ServerSocket(1234);

        // Este loop while será executado para sempre, então o servidor nunca irá parar a menos que o aplicativo seja fechado. 
        while (true) {

            try {
                // Aguarde até que um cliente se conecte e, quando ele fizer isso, crie um soquete para se comunicar com ele.
                Socket socket = serverSocket.accept();

                // Stream para receber dados do cliente por meio do soquete.
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                // Leia o tamanho do nome do arquivo para saber quando parar de ler.
                int fileNameLength = dataInputStream.readInt();
                // Se o arquivo existir
                if (fileNameLength > 0) {
                    // Matriz de bytes para conter o nome do arquivo.
                    byte[] fileNameBytes = new byte[fileNameLength];
                    // Leia a partir do fluxo de entrada na matriz de bytes.
                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                    // Crie o nome do arquivo a partir da matriz de bytes.
                    String fileName = new String(fileNameBytes);
                    // Leia quantos dados esperar para o conteúdo real do arquivo.
                    int fileContentLength = dataInputStream.readInt();
                    // Se o arquivo existir.
                    if (fileContentLength > 0) {
                        // Matriz para armazenar os dados do arquivo.
                        byte[] fileContentBytes = new byte[fileContentLength];
                        // Leia a partir do fluxo de entrada na matriz fileContentBytes.
                        dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);
                        // Painel para armazenar a imagem e o nome do arquivo.
                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.X_AXIS));
                        //Defina o nome do arquivo.
                        JLabel jlFileName = new JLabel(fileName);
                        jlFileName.setFont(new Font("Arial", Font.BOLD, 20));
                        jlFileName.setBorder(new EmptyBorder(10,0, 10,0));
                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                            // Defina o nome como fileId para que você possa obter o arquivo correto no painel.
                            jpFileRow.setName((String.valueOf(fileId)));
                            jpFileRow.addMouseListener(getMyMouseListener());
                            // Adicione tudo.
                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            jFrame.validate();
                        } else {
                            // Defina o nome como fileId para que você possa obter o arquivo correto no painel.
                            jpFileRow.setName((String.valueOf(fileId)));
                            // Adicione um ouvinte de mouse para que apareça quando ele for clicado.
                            jpFileRow.addMouseListener(getMyMouseListener());
                            // Adicione o nome do arquivo e o tipo de imagem ao painel e, a seguir, adicione o painel ao painel principal.
                            jpFileRow.add(jlFileName);
                            jPanel.add(jpFileRow);
                            // Execute um relayout.
                            jFrame.validate();
                        }

                        // Adicione o novo arquivo à lista de array que contém todos os nossos dados.
                        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                        // Aumente o fileId para o próximo arquivo a ser recebido.
                        fileId++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param fileName
     * @return The extension type of the file.
     */
    public static String getFileExtension(String fileName) {
        // Obtenha o tipo de arquivo usando a última ocorrência de. (por exemplo, meajude.txt retorna txt).
        // Terá problemas com arquivos como myFile.tar.gz.
        int i = fileName.lastIndexOf('.');
        // Se houver uma extensão.
        if (i > 0) {
            // Defina a extensão para a extensão do nome do arquivo.
            return fileName.substring(i + 1);
        } else {
            return "Nenhuma extensão encontrada.";
        }
    }

    /**
     * When the jpanel is clicked a popup shows to say whether the user wants to download
     * the selected document.
     *
     * @return A mouselistener that is used by the jpanel.
     */
    public static MouseListener getMyMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Obtenha a origem do clique, que é o JPanel.
                JPanel jPanel = (JPanel) e.getSource();
                // Obtenha o ID do arquivo.
                int fileId = Integer.parseInt(jPanel.getName());
                // Percorra o armazenamento de arquivos e veja qual arquivo é o selecionado.
                for (MyFile myFile : myFiles) {
                    if (myFile.getId() == fileId) {
                        JFrame jfPreview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
                        jfPreview.setVisible(true);
                    }
                }
            }

            @Override       
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

        public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {

            JFrame jFrame = new JFrame("Download");
            jFrame.setSize(400, 400);
            jFrame.setLocationRelativeTo(null);

            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

            JLabel jlTitle = new JLabel("Download");
            jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            jlTitle.setFont(new Font("Arial", Font.BOLD, 25));
            jlTitle.setBorder(new EmptyBorder(20,0,10,0));

            JLabel jlPrompt = new JLabel("Tem certeza que deseja baixar " + fileName + "?");
            jlPrompt.setFont(new Font("Arial", Font.BOLD, 20));
            jlPrompt.setBorder(new EmptyBorder(20,0,10,0));
            jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton jbYes = new JButton("Sim");
            jbYes.setPreferredSize(new Dimension(150, 75));
            jbYes.setFont(new Font("Arial", Font.BOLD, 20));

            JButton jbNo = new JButton("Nao");
            jbNo.setPreferredSize(new Dimension(150, 75));
            jbNo.setFont(new Font("Arial", Font.BOLD, 20));

            JLabel jlFileContent = new JLabel();
            jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel jpButtons = new JPanel();
            jpButtons.setBorder(new EmptyBorder(20, 0, 10, 0));
            jpButtons.add(jbYes);
            jpButtons.add(jbNo);

            // Se o arquivo for um arquivo de texto, exiba o texto.
            if (fileExtension.equalsIgnoreCase("txt")) {
                // Envolva-o com <html> para que novas linhas sejam feitas
                jlFileContent.setText("<html>" + new String(fileData) + "</html>");
                // Se o arquivo não for um arquivo de texto, transforme-o em uma imagem.
            } else {
                jlFileContent.setIcon(new ImageIcon(fileData));
            }

            // Sim, baixe o arquivo.
            jbYes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Crie o arquivo com seu nome.
                    File fileToDownload = new File(fileName);
                    try {
                        //Crie um fluxo para gravar dados no arquivo.
                        FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                        // Grave os dados reais do arquivo no arquivo.
                        fileOutputStream.write(fileData);
                        // Feche a conexao.
                        fileOutputStream.close();
                        // Apaga do jFrame. depois que o usuário clicou em sim.
                        jFrame.dispose();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }



                }
            });

            jbNo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // O usuário clicou em não, então não baixe o arquivo, mas feche o jframe.
                    jFrame.dispose();
                }
            });

            jPanel.add(jlTitle);
            jPanel.add(jlPrompt);
            jPanel.add(jlFileContent);
            jPanel.add(jpButtons);

            jFrame.add(jPanel);

            return jFrame;

        }


}



