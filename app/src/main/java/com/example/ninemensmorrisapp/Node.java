package com.example.ninemensmorrisapp;

/**
 * This class creates the Node class, with links to the previous and next nodes, along with an
 * element that stores information.
 *
 * @param <E> generic class
 */
public class Node<E> {

  private E element;
  private Node<E> next;
  private Node<E> previous;

  /**
   * Builds a Node.
   *
   * @param anElement the information to be stored in the node
   * @param next      the link to the next node
   * @param previous  the link to the previous node
   */
  public Node(E anElement, Node<E> next, Node<E> previous) {
    element = anElement;
    this.next = next;
    this.previous = previous;
  } // Node

  /**
   * Builds a Node, with the next and previous links set to null.
   *
   * @param anElement the information to be stored in the node
   */
  public Node(E anElement) {
    this(anElement, null, null);
  } // Node

  public E getElement() {
    return element;
  } // getElement

  public Node<E> getNext() {
    return next;
  } // getNext

  public Node<E> getPrevious() {
    return previous;
  } // getPrevious

  public void setElement(E newElement) {
    element = newElement;
  } // setElement

  public void setNext(Node<E> next) {
    this.next = next;
  } // setNext

  public void setPrevious(Node<E> previous) {
    this.previous = previous;
  } // setPrevious

} // class
