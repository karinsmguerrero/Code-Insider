/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebita.logic;

public class SimpleList {
    private Node head;
    private Node last;
    protected int size = 0;
    
    /**
     * Constructor de la clase SimpleList
     */
    public SimpleList(){
        this.head = null;
        this.last = null;
        this.size = 0;
    }
    
    public Node getHead(){
        return this.head;
    }
    
    /**
     * Metodo privado encargado de agregar un nodo al inicio de la lista.
     * @param nodo Nodo a agregar en la lista
     */
    private void add_first(Node nodo){
        nodo.setNext(this.head);
        this.head = nodo;
        this.size++;
    }
    
    /**
     * Metodo privado para insertar al final de la lista
     * @param nodo Nodo a agregar en la lista
     */
    private void add_last(Node nodo){
        this.last.setNext(nodo);
        this.last = nodo;
        this.last.setNext(null);
        this.size++;
    }
    
    /**
     * Metodo auxiliar que llamara al metodo de insertar al inicio o insertar al final dependiendo del caso que se necesite
     * @param nodo Elemento a agregar en la lista.
     */
    public void add_element(Node nodo){
        if (this.head == null){
            this.add_first(nodo);
            this.last = nodo;
        }
        else{
            this.add_last(nodo);
        }
    }
    /**
     * Metodo para buscar un nodo dentro de una lista. Funcion especifica para valores String
     * @param element Valor del elemento que desea buscar.
     * @return Retorna el nodo que contiene el elemento buscado.
     */
    public Node search_element(String element){
        Node aux = this.head;
        for (int i = 0; i < this.size; i++){
            if(aux.getDato().equals(element)){
                return aux;
            }else{
                aux = aux.getNext();
            }
        }
        return null;
    }
    
    
    /**
     * Metodo para editar el valor de un elemento dentro de la lista.
     * @param old_data objeto que desea editar
     * @param new_data valor nuevo para el elemento que desea editar.
     * @return retorna el elemento editado de la lista.
     */
    public Node editNode(Object old_data, Object new_data){
        Node aux = this.search_element(old_data.toString());
        aux.setData(new_data);
        return aux;
    }
    
    /**
     * Metodo engargado de eliminar un nodo de la lista.
     * @param delete elemento a elminar de la lista.
     * @return  retorna el elemento eliminado o null si el elemento no se encuentra en la list
     */
    public Node deleteNode(Object delete){
        Node aux1 = this.head;
        Node aux2 = this.head;
        for (int i = 0; i < this.size; i++){
            if (aux1.getDato().equals(delete)){
                aux2.setNext(aux1.getNext());
                aux1.setNext(null);
                this.size--;
                return aux1;
            }
            else{
                aux2 = aux1;
                aux1 = aux1.getNext();
            }
        }
        return null;
    }
    
    public boolean listContains(String contain) {
    	Node aux = this.head;
    	for (int i = 0; i < this.size; i++) {
    		if (aux.getDato().toString().contains(contain)) {
    			return true;
    		}else {
    			aux = aux.getNext();
    		}
    	}
    	return false;
    }
    
    public int getSize() {
    	return this.size;
    }
    
    public boolean isEmpty(){
        return this.head == null && this.last == null;
    }
}
