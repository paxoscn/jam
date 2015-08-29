package cn.paxos.jam.preset.http.request.event;

import cn.paxos.jam.Event;
import cn.paxos.jam.preset.http.request.Request;

public class RequestCompletedEvent implements Event
{
  
  private final Request request;

  public RequestCompletedEvent(Request request)
  {
    this.request = request;
  }

  public Request getRequest()
  {
    return request;
  }

}
