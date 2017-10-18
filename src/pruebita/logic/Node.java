/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebita.logic;

public class Node {
    private Node next;
    private Object dato;
    
    public Node(){
        this.next = null;
        this.dato = null;
    }
    
    public Node(String data) {
    	this.dato = data;
    }
    
    public Node(Object data) {
    	this.dato = data;
    }

    public Object getDato(){
        return this.dato;
    }
     
    public Node getNext(){
        return this.next;
    }
    
    public void setNext(Node nodo){
        this.next = nodo;
    }
    
    public void setData(Object new_data){
        this.dato = new_data;
    }
}
