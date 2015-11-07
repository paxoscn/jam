package cn.paxos.jam;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class StateContextTest
{
  
  private int i = 1;

  @Test
  public void testRing()
  {
    List<Trigger> triggers = new LinkedList<Trigger>();
    triggers.add(new Trigger() {
      @Override
      public State trigger(Event event)
      {
        if (i < 5)
        {
          return new DummyState(i++);
        } else
        {
          return null;
        }
      }
    });
    StateContext stateContext = new StateContext();
    stateContext.setTriggers(triggers);
    stateContext.start();
    stateContext.publish(new IntEvent(1));
  }
  
  private static class IntEvent implements Event
  {
    
    private final Integer i;

    public IntEvent(Integer i)
    {
      this.i = i;
    }

    @Override
    public String toString()
    {
      return "<" + i + ">";
    }
    
  }
  
  private static class DummyState implements State
  {
    
    private static int t = 0;
    
    private final Integer i;

    public DummyState(Integer i)
    {
      this.i = i;
    }

    @Override
    public State onEvent(Event event, StateContext stateContext)
    {
      if (!(event instanceof IntEvent))
      {
        return this;
      }
      int i = ((IntEvent) event).i;
      System.out.println(this.i + " = " + i);
      if (i == 5)
      {
        return null;
      } else
      {
        if (t++ < 5)
        {
          stateContext.publish(new IntEvent(i + 1));
        }
        return new DummyState(this.i + 1);
      }
    }

    @Override
    public String toString()
    {
      return "[i=" + i + "]";
    }
    
  }

}
