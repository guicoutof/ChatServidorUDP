/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservidor;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
/**
 *
 * @author AlphaLegends
 */
public class ChatServidor {
    static Online online = new Online();
    static int port = 9999;
    /**  
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    
    
    public static void main(String[] args) throws Exception{
        DatagramSocket serverSocket = new DatagramSocket(port,InetAddress.getByName("localhost")); //numero da porta 9999, poderá ser alterada

        online.logar("ChatGeral",InetAddress.getByName("localhost"),port);
        
        while(true){
            byte[]receiveData = new byte[8300];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            System.out.println("Aguardando pacote...");
            serverSocket.receive(receivePacket);//recebimento de pacotes
            
            tratarMensagem(receivePacket,serverSocket);
        }   
        
    }
    
    public static void tratarMensagem(DatagramPacket receivePacket,DatagramSocket serverSocket) throws IOException{
        String sentence = new String(receivePacket.getData());
        InetAddress IPAddress = receivePacket.getAddress();
        int portReceive = receivePacket.getPort();
        byte[] sendData = new byte[1024];
        
        switch(sentence.charAt(0)){
            case '1':     //login
                if(sentence.charAt(1) == '0'){ //pedido de login
                    String nick[] = sentence.split("\n");
                    if(online.logar(nick[1], IPAddress,portReceive)){
                        //foi logado
                        String newsentence = "11\n";
                        sendData = newsentence.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket (sendData,sendData.length,IPAddress,portReceive);
                        serverSocket.send(sendPacket);
                        
                        newsentence = "12\n";//exibir que foi logado a todos
                        newsentence += nick[1];
                        broadcast(newsentence,serverSocket);
                        
                        newsentence = "13\n";//enviar lista de logados
                        newsentence += online.exibirOnlinesNicks();
                        broadcast(newsentence,serverSocket);
                         
                    }else{
                        String newsentence = "90\n";//erro nick invalido
                        sendData = newsentence.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket (sendData,sendData.length,IPAddress,portReceive);
                        serverSocket.send(sendPacket);
                    }
                    
                }if(sentence.charAt(1)=='4'){
                    String nameAux[] = sentence.split("\n");
                    String name = nameAux[1];
                    String members[] = nameAux[2].split(";");
                    Grupo grupo = new Grupo(name);
                    for(int i=0;i<members.length;i++){
                        Usuario usuario = online.buscarNick(members[i]);
                        if(usuario != null){
                            if(grupo.buscarMembro(usuario.getNick())== null){
                                grupo.adicionar(usuario);
                            }
                        }
                    }
                    if(online.grupos.add(grupo)){
                        sentence = "13\n";//enviar lista de logados
                        sentence += online.exibirOnlinesNicks();
                        sendOnlineGroup(sentence,serverSocket);
                    }
                    
                }
                if(sentence.charAt(1)=='9'){//deslogar
//                    Usuario usuario = online.buscarIP(IPAddress);
                    Usuario usuario = online.buscarPorta(portReceive);
                    online.deslogar(usuario.getNick());
                    
                    sentence = "18\n";//exibir que foi deslogado a todos
                    sentence += usuario.getNick();
                    broadcast(sentence,serverSocket);

                    sentence = "13\n";//enviar lista de logados
                    sentence += online.exibirOnlinesNicks();
                    broadcast(sentence,serverSocket);
                    
                }
                break;
            case '2':     //mensagem
                if(sentence.charAt(1) == '0'){//retransmitir mensagem privada
                    byte[] sendDataMsg = new byte[1024];
                    String message[] = sentence.split("\n");
                    Usuario usuario = online.buscarNick(message[1]);
                    if(usuario!=null){
                        sendDataMsg = sentence.getBytes();
                        IPAddress = usuario.getIp();
                        portReceive = usuario.getPort();
                        DatagramPacket sendPacket = new DatagramPacket (sendDataMsg,sendDataMsg.length,IPAddress,portReceive);
                        serverSocket.send(sendPacket);
                    }else{
                        sendGroup(sentence,serverSocket);
                    }
                    
                }
                if(sentence.charAt(1) == '1'){//retransmitir mensagem members geral
                    broadcast(sentence,serverSocket);
                }
                break;
            case '3':     
                break;
                
            default:
                break;
        }
    }
    
    public static void broadcast(String sentence,DatagramSocket serverSocket) throws IOException{
        byte[] sendData = new byte[1024]; 
        sendData = sentence.getBytes();
        for(int i=1;i<online.listaOnline.size();i++){
            DatagramPacket sendPacket = new DatagramPacket (sendData,sendData.length,online.listaOnline.get(i).getIp(),online.listaOnline.get(i).getPort());
            serverSocket.send(sendPacket);
        }
    }

    private static void sendOnlineGroup(String sentence, DatagramSocket serverSocket) throws IOException {
        InetAddress IPAddress;
        int portReceive;
        byte[] sendData = new byte[1024];
        for(int i=1;i<online.listaOnline.size();i++){
            for(int j=0;j<online.grupos.size();j++){//rever
                String nick = online.listaOnline.get(i).getNick();
                Usuario usuario = online.grupos.get(j).buscarMembro(nick);
                if(usuario!=null){
                    sentence+=online.grupos.get(j).getName()+"\n";
                    sendData = sentence.getBytes();
                    IPAddress = online.listaOnline.get(i).getIp();
                    portReceive = online.listaOnline.get(i).getPort();
                    DatagramPacket sendPacket =new DatagramPacket (sendData,sendData.length,IPAddress,portReceive);
                    serverSocket.send(sendPacket);
                }
            }
        }
    }

    private static void sendGroup(String sentence, DatagramSocket serverSocket) throws IOException {
        InetAddress IPAddress;
        int portReceive;
        byte[] sendData = new byte[1024];
        String message[] = sentence.split("\n");
        String name = message[1];
        for(int i=0;i<online.grupos.size();i++){
            if(online.grupos.get(i).getName().equals(name)){
                ArrayList<Usuario>  members = online.grupos.get(i).getMembers();
                for(int j=0;j<members.size();j++){
                    sendData = sentence.getBytes();
                    IPAddress = members.get(j).getIp();
                    portReceive = members.get(j).getPort();
                    DatagramPacket sendPacket =new DatagramPacket (sendData,sendData.length,IPAddress,portReceive);
                    serverSocket.send(sendPacket);
                }
            }
        }
    }
    
}
