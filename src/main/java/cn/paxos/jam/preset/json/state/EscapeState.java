package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.event.OutputLengthChangedEvent;
import cn.paxos.jam.util.BytesWrapper;

public class EscapeState implements State
{

  private final AppendableState appendableState;

  public EscapeState(AppendableState appendableState)
  {
    this.appendableState = appendableState;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == 'u')
      {
        stateContext.publish(new OutputLengthChangedEvent(4));
        return new UnicodeState(appendableState);
      } else
      {
        if (b == 'r')
        {
          appendableState.append((byte) '\r');
        } else if (b == 'n')
        {
          appendableState.append((byte) '\n');
        } else if (b == 't')
        {
          appendableState.append((byte) '\t');
        } else
        {
          appendableState.append(b);
        }
        return appendableState;
      }
    }
    return this;
  }

}
