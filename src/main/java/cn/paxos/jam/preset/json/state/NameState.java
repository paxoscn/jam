package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.json.Container;
import cn.paxos.jam.util.BytesWrapper;

public class NameState implements State
{

  private final Container container;

  public NameState(Container container)
  {
    this.container = container;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == ' ' || b == '\r' || b == '\n' || b == '\t')
      {
        return this;
      } else
      {
        if (b == '"' || b == '\'')
        {
          return new StringState(container, b);
        } else
        {
          return new NameEndState(container, b);
        }
      }
    }
    return this;
  }

}
