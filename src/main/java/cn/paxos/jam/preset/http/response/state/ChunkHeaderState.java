package cn.paxos.jam.preset.http.response.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.event.OutputLengthChangedEvent;
import cn.paxos.jam.preset.http.response.Response;
import cn.paxos.jam.preset.http.response.event.ResponseCompletedEvent;
import cn.paxos.jam.util.BytesWrapper;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public class ChunkHeaderState implements State
{

  private final Response response;
  private final LightByteArrayOutputStream baos = new LightByteArrayOutputStream();

  public ChunkHeaderState(Response response)
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
        String chunkLengthAsString = new String(baos.toByteArray()).trim();
        if (chunkLengthAsString.length() < 1)
        {
          baos.write(b);
        } else
        {
          int chunkLength = Integer.parseInt(chunkLengthAsString, 16);
          if (chunkLength > 0)
          {
            stateContext.publish(new OutputLengthChangedEvent(chunkLength));
            return new ChunkBodyState(response);
          } else
          {
            stateContext.publish(new ResponseCompletedEvent(response));
            // TODO
            return null;
          }
        }
      } else
      {
        baos.write(b);
      }
    }
    return this;
  }

}
