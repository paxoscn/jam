package cn.paxos.jam.preset.http.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

public class ChunkTest
{

  @Test
  public void testChunked() throws IOException
  {
    File f = new File("H:/_ws_java/classpath/ref/bing-cn-chunks.txt");
    byte[] bs = new byte[(int) f.length()];
    FileInputStream fs = new FileInputStream(f);
    fs.read(bs);
    fs.close();
    String[] parts = new String(bs, "UTF-8").split("\r\nXXXXXXXXXXXXXXXXXXXXX\r\n");
    List<Trigger> triggers = new LinkedList<Trigger>();
    triggers.add(new ResponseHandler());
    StateContext stateContext = new StateContext();
    stateContext.setTriggers(triggers);
    stateContext.start();
    stateContext.addState(new BytesState());
    stateContext.addState(new ProtocolState());
    for (String part : parts)
    {
//      System.out.println("<<<"+part+">>>");
      stateContext.publish(new BytesEvent(part.getBytes("UTF-8")));
    }
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
