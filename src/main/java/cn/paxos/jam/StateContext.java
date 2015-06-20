package cn.paxos.jam;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.paxos.jam.event.ContextStartedEvent;
import cn.paxos.jam.util.Ring;

public class StateContext
{

  private final Ring<State> stateRing;
  
  private List<Trigger> triggers;
  private boolean started;
  
  public StateContext()
  {
    this.stateRing = new Ring<State>();
    this.triggers = new LinkedList<Trigger>();
    this.started = false;
  }

  public void start()
  {
    this.started = true;
    this.publish(new ContextStartedEvent());
  }
  
  public void publish(Event event)
  {
    if (!started)
    {
      return;
    }
    for (Trigger trigger : triggers)
    {
      State newState = trigger.trigger(event);
      if (newState != null)
      {
        stateRing.add(newState, null);
      }
    }
    for (Iterator<State> iterator = stateRing.iterator(); iterator.hasNext();)
    {
      State state = iterator.next();
      State nextState = state.onEvent(event, this);
//      System.out.println("returned: " + state);
      if (nextState != null)
      {
        if (nextState != state)
        {
          stateRing.replace(nextState, state);
        }
      } else
      {
        iterator.remove();
      }
    }
  }

  public void addState(State newState)
  {
    stateRing.add(newState, null);
  }

  public void setTriggers(List<Trigger> triggers)
  {
    this.triggers = triggers;
  }
  
}
