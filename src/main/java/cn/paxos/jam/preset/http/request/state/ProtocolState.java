package cn.paxos.jam.preset.http.request.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.http.request.Request;
import cn.paxos.jam.util.BytesWrapper;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public class ProtocolState implements State
{

  private final Request request;
  private final LightByteArrayOutputStream baos = new LightByteArrayOutputStream();

  public ProtocolState(Request request)
  {
    this.request = request;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == '\n')
      {
        String protocol = new String(baos.toByteArray()).trim();
        request.setProtocol(protocol);
        return new HeadersState(request);
      } else
      {
        baos.write(b);
      }
    }
    return this;
  }

}
