package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.json.Container;
import cn.paxos.jam.util.BytesWrapper;

public class NameEndState extends AppendableState
{

  private final Container container;
  
  public NameEndState(Container container, byte first)
  {
    this.container = container;
    append(first);
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
        String str = new String(baos.toByteArray());
        container.add(str);
        return new ColonState(container);
      } else
      {
        if (b == '\\')
        {
          return new EscapeState(this);
        } else if (b == ':')
        {
          String str = new String(baos.toByteArray());
          container.add(str);
          return new ElementState(container);
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
