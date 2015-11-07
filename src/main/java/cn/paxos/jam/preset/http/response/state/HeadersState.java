package cn.paxos.jam.preset.http.response.state;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesWrapperEvent;
import cn.paxos.jam.event.OutputLengthChangedEvent;
import cn.paxos.jam.preset.http.response.Response;
import cn.paxos.jam.preset.http.response.event.ResponseCompletedEvent;
import cn.paxos.jam.preset.http.util.Swifter;
import cn.paxos.jam.util.BytesWrapper;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public class HeadersState implements State
{

  private final Response response;
  private final LightByteArrayOutputStream baos = new LightByteArrayOutputStream();
  private final Swifter neckSwifter = new Swifter(4);

  public HeadersState(Response response)
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
      baos.write(b);
      neckSwifter.append(b);
      if (Arrays.equals(neckSwifter.get(), "\r\n\r\n".getBytes()))
      {
        String headers = new String(baos.toByteArray());
        StringTokenizer st = new StringTokenizer(headers, "\r\n");
        while (st.hasMoreTokens())
        {
          String kv = st.nextToken();
          if (kv.length() < 1)
          {
            continue;
          }
          int indexOfEqual = kv.indexOf(':');
          String k = kv.substring(0, indexOfEqual);
          String v = kv.substring(indexOfEqual + 1).trim();
          List<String> value = response.getHeaderList(k);
          if (value == null)
          {
            value = new LinkedList<String>();
            response.putHeaderList(k, value);
          }
          value.add(v);
        }
        List<String> transferEncoding = response.getHeaderList("Transfer-Encoding");
        if (transferEncoding != null && (!transferEncoding.isEmpty()) && "chunked".equals(transferEncoding.iterator().next()))
        {
          return new ChunkHeaderState(response);
        } else
        {
          int contentLength = Integer.parseInt(response.getHeaderList("Content-Length").iterator().next());
          if (contentLength > 0)
          {
            stateContext.publish(new OutputLengthChangedEvent(contentLength));
            return new PlainBodyState(response);
          } else
          {
            stateContext.publish(new ResponseCompletedEvent(response));
            // TODO
            return null;
          }
        }
      }
    }
    return this;
  }

}
