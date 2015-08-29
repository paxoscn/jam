package cn.paxos.jam.preset.http.request;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.Trigger;
import cn.paxos.jam.event.BytesEvent;
import cn.paxos.jam.preset.http.request.event.RequestCompletedEvent;
import cn.paxos.jam.preset.http.request.state.MethodState;
import cn.paxos.jam.state.BytesState;

public class RequestTest
{

  @Test
  public void testBytes()
  {
    List<Trigger> triggers = new LinkedList<Trigger>();
    triggers.add(new RequestHandler());
    StateContext stateContext = new StateContext();
    stateContext.setTriggers(triggers);
    stateContext.start();
    stateContext.addState(new BytesState());
    stateContext.addState(new MethodState());
    stateContext.publish(new BytesEvent("GET /index.html?a=b&x=y&a=c HTTP/1.1\r\nHost: 127.0.0.1\r\nConnection: close\r\n\r\n".getBytes()));
  }
  
  private static class RequestHandler implements Trigger, State
  {

    @Override
    public State trigger(Event event)
    {
      if (event instanceof RequestCompletedEvent)
      {
        return this;
      }
      return null;
    }

    @Override
    public State onEvent(Event event, StateContext stateContext)
    {
      if (event instanceof RequestCompletedEvent)
      {
        Request request = ((RequestCompletedEvent) event).getRequest();
        System.out.println("Received: " + request.getMethod() + ", " + request.getPath() + ", " + request.getParams());
      }
      return null;
    }
    
  }

}
