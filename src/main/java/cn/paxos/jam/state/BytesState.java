package cn.paxos.jam.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesEvent;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.event.OutputLengthChangedEvent;
import cn.paxos.jam.util.BytesWrapper;

public class BytesState implements State
{

  private int outputLength = 1;
  private BytesWrapper bytesWrapper = null;

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesEvent)
    {
      byte[] bytes = ((BytesEvent) event).getBytes();
//      System.out.println(this.getClass().getSimpleName() + " : " + Arrays.toString(bytes));
      if (bytesWrapper == null)
      {
        bytesWrapper = new BytesWrapper(outputLength);
      }
      int used = 0;
      while (used < bytes.length)
      {
        int left = bytesWrapper.append(bytes, used);
        if (bytesWrapper.isDone())
        {
          stateContext.publish(new BytesWrapperEvent(bytesWrapper));
          bytesWrapper = new BytesWrapper(outputLength);
        }
        used = bytes.length - left;
      }
    } else if (event instanceof OutputLengthChangedEvent)
    {
      outputLength = ((OutputLengthChangedEvent) event).getOutputLength();
    }
    return this;
  }

}
