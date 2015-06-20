package cn.paxos.jam.preset.http.response.state;

import java.io.ByteArrayOutputStream;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.http.response.Response;
import cn.paxos.jam.util.BytesWrapper;

public class StatusState implements State
{

  private final Response response;
  private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

  public StatusState(Response response)
  {
    this.response = response;
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
        String status = new String(baos.toByteArray());
        response.setStatus(status.substring(0, status.indexOf(' ')));
        return new HeadersState(response);
      } else
      {
        baos.write(b);
      }
    }
    return this;
  }

}
