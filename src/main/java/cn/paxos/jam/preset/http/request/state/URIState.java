package cn.paxos.jam.preset.http.request.state;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.preset.http.request.Request;
import cn.paxos.jam.preset.http.request.event.URIEvent;
import cn.paxos.jam.util.BytesWrapper;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public class URIState implements State
{

  private final Request request;
  private final LightByteArrayOutputStream baos = new LightByteArrayOutputStream();

  public URIState(Request request)
  {
    this.request = request;
  }

  @Override
  public State onEvent(Event event, StateContext stateContext)
  {
    if (event instanceof BytesWrapperEvent)
    {
      BytesWrapper bytesWrapper = ((BytesWrapperEvent) event).getBytesWrapper();
      byte b = bytesWrapper.get(0);
      if (b == ' ')
      {
        String uri = new String(baos.toByteArray());
        request.setUri(uri);
        setPathAndParams(request, uri);
        stateContext.publish(new URIEvent(request));
        return new ProtocolState(request);
      } else
      {
        baos.write(b);
      }
    }
    return this;
  }

  private void setPathAndParams(Request request, String uri)
  {
    int indexOfQuestion = uri.indexOf('?');
    if (indexOfQuestion < 0)
    {
      request.setPath(uri);
    } else
    {
      request.setPath(uri.substring(0, indexOfQuestion));
      String query = uri.substring(indexOfQuestion + 1);
      StringTokenizer st = new StringTokenizer(query, "&");
      while (st.hasMoreTokens())
      {
        String kv = st.nextToken();
        String k, v;
        int indexOfEqual = kv.indexOf('=');
        if (indexOfEqual < 0)
        {
          k = kv;
          v = "";
        } else
        {
          k = kv.substring(0, indexOfEqual);
          v = kv.substring(indexOfEqual + 1);
        }
        List<String> value = request.getParams().get(k);
        if (value == null)
        {
          value = new LinkedList<String>();
          request.getParams().put(k, value);
        }
        value.add(v);
      }
    }
  }

}
