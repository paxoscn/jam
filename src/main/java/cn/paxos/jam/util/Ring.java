package cn.paxos.jam.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Ring<E> implements Iterable<E>
{
  
  private final Node<E> head;

  public Ring()
  {
    head = new Node<E>();
    head.prev = head;
    head.next = head;
  }
  
  public void add(E e, E prev)
  {
    if (prev == null)
    {
      add(e, head);
      return;
    }
    Node<E> current = head.next;
    while (current != head)
    {
      if (current.value == prev)
      {
        add(e, current);
        return;
      }
      current = current.next;
    }
  }

  public void replace(E newE, E e)
  {
    Node<E> current = head.next;
    while (current != head)
    {
      if (current.value == e)
      {
        current.value = newE;
        return;
      }
      current = current.next;
    }
  }
  
  @Override
  public Iterator<E> iterator()
  {
    return new Iterator<E>()
    {

      private Node<E> next;
      private List<Node<E>> iterated;
      
      {
        next = null;
        iterated = new LinkedList<Node<E>>();
//        System.out.println("-- " + Ring.this);
//        System.out.println("-- " + (pointer == head));
      }
      
      @Override
      public boolean hasNext()
      {
        Node<E> current = head.next;
        while (current != head)
        {
          if (!iterated.contains(current))
          {
            next = current;
            return true;
          }
          current = current.next;
        }
        return false;
      }

      @Override
      public E next()
      {
        E returned = next.value;
        iterated.add(next);
        return returned;
      }

      @Override
      public void remove()
      {
        if (next == null)
        {
          throw new NoSuchElementException();
        }
        delete(next);
      }
      
    };
  }
  
  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[");
    sb.append(head);
    Node<E> current = head.next;
    while (current != head)
    {
      sb.append(", ");
      sb.append(current);
      current = current.next;
    }
    sb.append("]");
    return sb.toString();
  }

  private Node<E> add(E e, Node<E> prev)
  {
    Node<E> node = new Node<E>();
    node.value = e;
    return add(node, prev);
  }
  
  private Node<E> add(Node<E> node, Node<E> prev)
  {
    node.prev = prev;
    node.next = prev.next;
    prev.next.prev = node;
    prev.next = node;
    return node;
  }
  
  private void delete(Node<E> node)
  {
//    System.out.println("< " + this);
    node.next.prev = node.prev;
    node.prev.next = node.next;
    node.prev = null;
    node.next = null;
    node.value = null;
//    System.out.println("> " + this);
  }
  
  public static void main(String[] args)
  {
    Ring<Integer> ring = new Ring<Integer>();
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    ring.add(3, (Integer) null);
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    ring.add(5, (Integer) null);
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    ring.add(7, 3);
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    ring.replace(9, 3);
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      if (i == 9)
      {
        iterator.remove();
      }
    }
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      iterator.next();
      iterator.remove();
    }
    for (Iterator<Integer> iterator = ring.iterator(); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
  }
  
  private static class Node<E>
  {

    private E value = null;
    private Node<E> prev = null;
    private Node<E> next = null;
    private String id = super.toString().substring(super.toString().indexOf('@') + 1);
    
    @Override
    public String toString()
    {
      return id + "[" + prev.id + ", " + value + ", " + next.id + "]";
    }
    
  }
  
}
