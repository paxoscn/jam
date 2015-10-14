package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.json.Container;
import cn.paxos.jam.util.BytesWrapper;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public class NumberState extends AppendableState
{

  private final Container container;
  private final LightByteArrayOutputStream baos = new LightByteArrayOutputStream();
  
  public NumberState(Container container, byte first)
  {
    this.container = container;
    baos.write(first);
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
        add();
        return new CommaState(container);
      } else
      {
        if (b == ',')
        {
          add();
          return new ElementState(container);
        } else if ((b == '}' && !container.isArray())
            || (b == ']' && container.isArray()))
        {
          add();
          return new CommaState(container.getParent());
        } else
        {
          append(b);
          return this;
        }
      }
    }
    return this;
  }

  private void add()
  {
    String str = new String(baos.toByteArray());
    if (str.equals("null"))
    {
      container.add(null);
    } else if (str.equals("true"))
    {
      container.add(true);
    } else if (str.equals("false"))
    {
      container.add(false);
    } else
    {
      if (str.indexOf('.') > -1)
      {
        container.add(Double.parseDouble(str));
      } else
      {
        container.add(Long.parseLong(str));
      }
    }
  }

}
