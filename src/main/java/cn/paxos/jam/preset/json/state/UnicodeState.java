package cn.paxos.jam.preset.json.state;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.event.OutputLengthChangedEvent;
import cn.paxos.jam.util.BytesWrapper;

public class UnicodeState implements State
{

  private final AppendableState appendableState;

  public UnicodeState(AppendableState appendableState)
  {
    this.appendableState = appendableState;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      try
      {
        StringBuilder sb = new StringBuilder();
        sb.appendCodePoint(Integer.parseInt(bytesWrapper.buildString("UTF-8"), 16));
        appendableState.append(sb.toString().getBytes(Charset.forName("UTF-8")));
        stateContext.publish(new OutputLengthChangedEvent(1));
        return appendableState;
      } catch (UnsupportedEncodingException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
    return this;
  }

}
