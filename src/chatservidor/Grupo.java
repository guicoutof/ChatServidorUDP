/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatservidor;

import java.util.ArrayList;

/**
 *
 * @author AlphaLegends
 */
public class Grupo {
    private String name;
    ArrayList<Usuario> members;
    

    public Grupo() {
    }

    public Grupo(String name) {
        this.members = new ArrayList();
        this.name = name;
    }

    public ArrayList<Usuario> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }

    public void setMembers(ArrayList<Usuario> group) {
        this.members = group;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Usuario buscarMembro(String nick){
        for(int i=0;i<members.size();i++){
        if(members.get(i).getNick().equals(nick))return members.get(i);
        }
        return null;
    }
    
    public boolean adicionar(Usuario usuario){
       return members.add(usuario);
    }
    
    public boolean remover(Usuario usuario){
        return members.remove(usuario);
    }
    
    
}
