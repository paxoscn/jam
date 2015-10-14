package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.json.Container;
import cn.paxos.jam.util.BytesWrapper;

public class InitState implements State
{
  
  private Container container = null;

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == '[')
      {
        container = new Container(null, true);
        return new ElementState(container);
      } else
      {
        container = new Container(null, false);
        return new NameState(container);
      }
    }
    return this;
  }

  public Container getContainer()
  {
    return container;
  }

}
