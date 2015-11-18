package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.json.Container;
import cn.paxos.jam.util.BytesWrapper;

public class ElementState implements State
{

  private final Container container;

  public ElementState(Container container)
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
        } else if (b == '{')
        {
          Container child = new Container(container, false);
          container.add(child);
          return new NameState(child);
        } else if (b == '[')
        {
          Container child = new Container(container, true);
          container.add(child);
          return new ElementState(child);
        } else if (b == ']')
        {
          return new CommaState(container.getParent());
        } else
        {
          if (container.isArray())
          {
            return new NumberState(container, b);
          } else
          {
            if (container.size() % 2 == 0)
            {
              return new NameEndState(container, b);
            } else
            {
              return new ValueEndState(container, b);
            }
          }
        }
      }
    }
    return this;
  }

}
