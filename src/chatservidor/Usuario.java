/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservidor;

import java.net.InetAddress;

/**
 *
 * @author AlphaLegends
 */
public class Usuario {
    private String nick;
    private InetAddress ip;
    private int port;

    public Usuario() {
    }

    public Usuario(String nick, InetAddress ip,int port) {
        this.nick = nick;
        this.ip = ip;
        this.port = port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }
    
    

    public InetAddress getIp() {
        return ip;
    }

    public String getNick() {
        return nick;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String exibir(){
        String exibir = "Nick: "+nick+ " IP: "+ip;
        return exibir;
    }
    
}
