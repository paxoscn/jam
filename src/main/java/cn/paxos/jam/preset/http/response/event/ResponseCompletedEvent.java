package cn.paxos.jam.preset.http.response.event;

import cn.paxos.jam.Event;
import cn.paxos.jam.preset.http.response.Response;

public class ResponseCompletedEvent implements Event
{
  
  private final Response response;

  public ResponseCompletedEvent(Response response)
  {
    this.response = response;
  }

  public Response getResponse()
  {
    return response;
  }

}
