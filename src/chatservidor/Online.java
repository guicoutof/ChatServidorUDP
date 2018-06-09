/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservidor;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 *
 * @author AlphaLegends
 */
public class Online {
     ArrayList<Usuario> listaOnline;
     ArrayList<Grupo> grupos;

    public Online() {
        listaOnline = new ArrayList();
        grupos = new ArrayList();
        
    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(ArrayList<Grupo> grupos) {
        this.grupos = grupos;
    }
    
    public boolean adicionarGrupo(Grupo grupo){
        return grupos.add(grupo);
    }
    
    public ArrayList<Usuario> getListaOnline() {
        return listaOnline;
    }

    public void setListaOnline(ArrayList<Usuario> listaOnline) {
        this.listaOnline = listaOnline;
    }
     
    public Usuario buscarNick(String nick){
        for (int i=1; i < listaOnline.size(); i++){
         if(listaOnline.get(i).getNick().equals(nick))
           return listaOnline.get(i);
       }
        return null;
    }
    
    public Usuario buscarIP(InetAddress ip){
        for (int i=0; i < listaOnline.size(); i++){
         if(listaOnline.get(i).getIp().equals(ip))
           return listaOnline.get(i);
       }
        return null;
    }
    
    public Usuario buscarPorta(int port){
        for (int i=0; i < listaOnline.size(); i++){
         if(listaOnline.get(i).getPort() == port)
           return listaOnline.get(i);
       }
        return null;
    }
    
    
    
    public boolean  logar(String newNick,InetAddress IP,int port){
       for (int i=0; i < listaOnline.size(); i++){
         if(listaOnline.get(i).getNick().equals(newNick))
           return false;
       }
       //adiciona na lista apenas se não existir
       Usuario newUsuario = new Usuario(newNick,IP,port);
       listaOnline.add(newUsuario);
       return true;
    }
    
    public void deslogar(String oldName) {
       for (int i=0; i< listaOnline.size(); i++){
        if(listaOnline.get(i).getNick().equals(oldName)){
            if(!grupos.isEmpty()){ 
                for(int j=0;j<grupos.size();j++){
                    if(grupos.get(j).buscarMembro(oldName)!=null){
                        grupos.get(j).remover(listaOnline.get(i));//remove o membro do grupo
                        if(grupos.get(j).getMembers().isEmpty())grupos.remove(j);//remove grupo se estiver vazio
                    }
                 }
            }
            listaOnline.remove(i);//remove o usuario da lista de online
        }
       }
    }
    
    public String exibirOnlinesNicks(){
        String msg = "";
        for (int i=0; i< listaOnline.size(); i++){
         msg += listaOnline.get(i).getNick()+"\n";
       }
        return msg;
    }
    
}
