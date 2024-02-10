package com.example.ninemensmorrisapp;

/**
 * This class creates a Doubly Circular Linked List, with special methods to view all elements
 * in the list, and to get the current element in the doubly linked list.
 *
 * @param <E> generic class
 */
public class DCircularLinkedList<E> {

  private Node<E> head;
  private int size;

  /**
   * Builds the DCircularLinkedList, setting the node of the head to null, indicating that the
   * Doubly Circular Linked List is empty.
   */
  public DCircularLinkedList() {
    head = null;
    size = 0;
  } // DCircularLinkedList

  public E getCurrentElement() {
    return head.getElement();
  } // getCurrentElement

  /**
   * Adds a node with an element to the front of the list.
   *
   * @param element the new information to be stored in the new node
   */
  public void addFirst(E element) {
    if (size == 0) {
      head = new Node<E>(element);
      head.setPrevious(head);
      head.setNext(head);
    } else {
      Node<E> newNode = new Node<E>(element);
      newNode.setPrevious(head.getPrevious());
      newNode.setNext(head);
      head.getPrevious().setNext(newNode);
      head.setPrevious(newNode);
      head = newNode;
    }
    size++;
  } // addFirst

  /**
   * Adds a node with an element to the end of the list.
   *
   * @param element the new information to be stored in the new node
   */
  public void addLast(E element) {
    addFirst(element);
    head = head.getNext();
  } // addLast

  /**
   * Makes the head point to next element in the list.
   */
  public void toNextNodeInList() {
    head = head.getNext();
  } // toNextNodeInList

  /**
   * Makes the head point to the previous element in the list.
   */
  public void toPreviousNodeInList() {
    head = head.getPrevious();
  } // toPreviousNodeInList

} // class
