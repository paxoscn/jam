package cn.paxos.jam.preset.http.request.state;

import java.io.ByteArrayOutputStream;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.http.request.Request;
import cn.paxos.jam.util.BytesWrapper;

public class MethodState implements State
{

  private final Request request = new Request();
  private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == ' ')
      {
        String method = new String(baos.toByteArray());
        request.setMethod(method);
        return new URIState(request);
      } else
      {
        baos.write(b);
      }
    }
    return this;
  }

}
