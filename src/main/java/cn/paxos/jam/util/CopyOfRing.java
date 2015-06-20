package cn.paxos.jam.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class CopyOfRing<E>
{
  
  private final List<E> list;

  public CopyOfRing()
  {
    this.list = new LinkedList<E>();
  }
  
  public void add(E toAdd, E prev)
  {
    int i = 0;
    for (Iterator<E> listIterator = list.iterator(); listIterator.hasNext();)
    {
      E currentInList = listIterator.next();
      if (currentInList == prev)
      {
        if (listIterator.hasNext())
        {
          list.add(i + 1, toAdd);
        } else
        {
          list.add(toAdd);
        }
        System.out.println(list);
        return;
      }
      i++;
    }
    list.add(0, toAdd);
    System.out.println(list);
  }

  public void remove(E e)
  {
    list.remove(e);
    System.out.println(list);
  }

//  public void removeAll(Collection<E> toRemove)
//  {
//    list.removeAll(toRemove);
//    System.out.println(list);
//  }
  
  public Iterator<E> iteratorExclude(final E start)
  {
    return new Iterator<E>()
    {
      
      private E current;
      
      {
        current = start;
      }
      
      @Override
      public boolean hasNext()
      {
        E firstInList = null;
        for (Iterator<E> listIterator = list.iterator(); listIterator.hasNext();)
        {
          E currentInList = listIterator.next();
          if (firstInList == null)
          {
            firstInList = currentInList;
          }
          if (currentInList == current)
          {
            if (listIterator.hasNext())
            {
              E nextInList = listIterator.next();
              return nextInList != start;
            } else
            {
              return firstInList != start;
            }
          }
        }
        return false;
      }

      @Override
      public E next()
      {
        E firstInList = null;
        for (Iterator<E> listIterator = list.iterator(); listIterator.hasNext();)
        {
          E currentInList = listIterator.next();
          if (firstInList == null)
          {
            firstInList = currentInList;
          }
          if (currentInList == current)
          {
            if (listIterator.hasNext())
            {
              E nextInList = listIterator.next();
              if (nextInList != start)
              {
                current = nextInList;
                return current;
              } else
              {
                throw new NoSuchElementException();
              }
            } else
            {
              if (firstInList != start)
              {
                current = firstInList;
                return current;
              } else
              {
                throw new NoSuchElementException();
              }
            }
          }
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove()
      {
        throw new UnsupportedOperationException();
//        list.remove(current);
//        System.out.println(list);
      }
      
    };
  }
  
  public static void main(String[] args)
  {
    CopyOfRing<Integer> ring = new CopyOfRing<Integer>();
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(1); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    ring.add(3, null);
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(1); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(3); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    ring.add(5, null);
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(3); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    ring.add(7, 3);
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(3); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(3); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(5); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
    System.out.println("===");
    for (Iterator<Integer> iterator = ring.iteratorExclude(7); iterator.hasNext();)
    {
      Integer i = iterator.next();
      System.out.println(i);
    }
  }
  
}
