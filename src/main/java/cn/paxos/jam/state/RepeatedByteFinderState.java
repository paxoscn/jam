package cn.paxos.jam.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.ByteEvent;

public class RepeatedByteFinderState implements State
{
  
  private byte last;

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof ByteEvent)
    {
      byte b = ((ByteEvent) event).getByte();
      System.out.println(this.getClass().getSimpleName() + " : " + b);
      if (b == last)
      {
        System.out.println(b + " repeated");
      }
      last = b;
    }
    return this;
  }

}
