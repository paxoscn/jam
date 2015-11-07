package cn.paxos.jam.preset.http.request.state;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.http.request.Request;
import cn.paxos.jam.preset.http.request.event.RequestCompletedEvent;
import cn.paxos.jam.util.BytesWrapper;

public class BodyState implements State
{

  private final Request request;

  public BodyState(Request request)
  {
    this.request = request;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      request.getChunks().addAll(bytesWrapper.getArrays());
      stateContext.publish(new RequestCompletedEvent(request));
      // TODO
      return null;
    }
    return this;
  }

}
