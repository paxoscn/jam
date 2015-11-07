package cn.paxos.jam.preset.http.request.event;

import cn.paxos.jam.Event;
import cn.paxos.jam.preset.http.request.Request;

public class URIEvent implements Event
{

  private final Request request;

  public URIEvent(Request request)
  {
    this.request = request;
  }

  public Request getRequest()
  {
    return request;
  }

}
