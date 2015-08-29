package cn.paxos.jam.preset.http.response.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.event.OutputLengthChangedEvent;
import cn.paxos.jam.preset.http.response.Response;
import cn.paxos.jam.util.BytesWrapper;

public class ChunkBodyState implements State
{

  private final Response response;

  public ChunkBodyState(Response response)
  {
    this.response = response;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      response.getChunks().addAll(bytesWrapper.getArrays());
      stateContext.publish(new OutputLengthChangedEvent(1));
      return new ChunkHeaderState(response);
    }
    return this;
  }

}
