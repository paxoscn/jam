package cn.paxos.jam.preset.http.response;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.paxos.jam.util.BytesWithOffset;

public class Response
{

  private final Map<String, List<String>> headers;

  private String protocol;
  private String status;
  private final List<BytesWithOffset> chunks;

  public Response()
  {
    this.headers = new HashMap<String, List<String>>();
    this.chunks = new LinkedList<BytesWithOffset>();
  }
  
  public Map<String, List<String>> getHeaders()
  {
    return headers;
  }
  public String getProtocol()
  {
    return protocol;
  }
  public void setProtocol(String protocol)
  {
    this.protocol = protocol;
  }
  public String getStatus()
  {
    return status;
  }
  public void setStatus(String status)
  {
    this.status = status;
  }
  public List<BytesWithOffset> getChunks()
  {
    return chunks;
  }

}
