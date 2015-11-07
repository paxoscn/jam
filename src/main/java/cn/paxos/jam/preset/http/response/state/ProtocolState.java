package cn.paxos.jam.preset.http.response.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.http.response.Response;
import cn.paxos.jam.util.BytesWrapper;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public class ProtocolState implements State
{

  private final Response response = new Response();
  private final LightByteArrayOutputStream baos = new LightByteArrayOutputStream();

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == ' ')
      {
        String protocol = new String(baos.toByteArray());
        response.setProtocol(protocol);
        return new StatusState(response);
      } else
      {
        baos.write(b);
      }
    }
    return this;
  }

}
