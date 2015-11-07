package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.json.Container;
import cn.paxos.jam.util.BytesWrapper;

public class StringState extends AppendableState
{

  private final Container container;
  private final byte quoter;
  
  public StringState(Container container, byte quoter)
  {
    this.container = container;
    this.quoter = quoter;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == '\\')
      {
        return new EscapeState(this);
      } else
      {
        if (b == quoter)
        {
          String str = new String(baos.toByteArray());
          container.add(str);
          if (container.isArray())
          {
            return new CommaState(container);
          } else
          {
            if (container.size() % 2 == 0)
            {
              return new CommaState(container);
            } else
            {
              return new ColonState(container);
            }
          }
        } else
        {
          append(b);
          return this;
        }
      }
    }
    return this;
  }

}
