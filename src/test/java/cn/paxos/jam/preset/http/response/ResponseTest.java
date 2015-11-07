package cn.paxos.jam.preset.http.response;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.Trigger;
import cn.paxos.jam.event.BytesEvent;
import cn.paxos.jam.preset.http.response.event.ResponseCompletedEvent;
import cn.paxos.jam.preset.http.response.state.ProtocolState;
import cn.paxos.jam.state.BytesState;
import cn.paxos.jam.util.BytesWithOffset;

public class ResponseTest
{

  @Test
  public void testPlain()
  {
    List<Trigger> triggers = new LinkedList<Trigger>();
    triggers.add(new ResponseHandler());
    StateContext stateContext = new StateContext();
    stateContext.setTriggers(triggers);
    stateContext.start();
    stateContext.addState(new BytesState());
    stateContext.addState(new ProtocolState());
    stateContext.publish(new BytesEvent("HTTP/1.1 200 OK\r\nHost: 127.0.0.1\r\nContent-Length: 10\r\nConnection: close\r\n\r\nabcdefghij".getBytes()));
  }

  @Test
  public void testChunked()
  {
    List<Trigger> triggers = new LinkedList<Trigger>();
    triggers.add(new ResponseHandler());
    StateContext stateContext = new StateContext();
    stateContext.setTriggers(triggers);
    stateContext.start();
    stateContext.addState(new BytesState());
    stateContext.addState(new ProtocolState());
    stateContext.publish(new BytesEvent("HTTP/1.1 200 OK\r\nHost: 127.0.0.1\r\nTransfer-Encoding: chunked\r\nConnection: close\r\n\r\n5\r\nabcde\r\n5\r\nfghi".getBytes()));
    stateContext.publish(new BytesEvent("j\r\n5\r\nklmno\r\n5\r\npqrst\r\n0\r\n".getBytes()));
  }
  
  private static class ResponseHandler implements Trigger, State
  {

    @Override
    public State trigger(Event event)
    {
      if (event instanceof ResponseCompletedEvent)
      {
        return this;
      }
      return null;
    }

    @Override
    public State onEvent(Event event, StateContext stateContext)
    {
      if (event instanceof ResponseCompletedEvent)
      {
        Response response = ((ResponseCompletedEvent) event).getResponse();
        System.out.println("Received: " + response.getProtocol() + ", " + response.getStatus());
        System.out.println("Headers:\r\n" + response.getHeaders());
        BytesWithOffset mergedBodyBytes = BytesWithOffset.merge(response.getChunks());
        try
        {
          System.out.println("Body:\r\n" + new String(mergedBodyBytes.getBytes(), mergedBodyBytes.getOffset(), mergedBodyBytes.getLength(), "UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      return null;
    }
    
  }

}
