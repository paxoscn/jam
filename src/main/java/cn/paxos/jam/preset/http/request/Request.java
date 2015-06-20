package cn.paxos.jam.preset.http.request;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.paxos.jam.util.BytesWithOffset;

public class Request
{

  private final Map<String, List<String>> headers;
  private final Map<String, List<String>> params;
  private final List<BytesWithOffset> chunks;
  
  private String method;
  private String uri;
  private String path;
  private String protocol;

  public Request()
  {
    this.headers = new HashMap<String, List<String>>();
    this.params = new HashMap<String, List<String>>();
    this.chunks = new LinkedList<BytesWithOffset>();
  }
  
  public Map<String, List<String>> getHeaders()
  {
    return headers;
  }
  public Map<String, List<String>> getParams()
  {
    return params;
  }
  public List<BytesWithOffset> getChunks()
  {
    return chunks;
  }
  public String getMethod()
  {
    return method;
  }
  public void setMethod(String method)
  {
    this.method = method;
  }
  public String getUri()
  {
    return uri;
  }
  public void setUri(String uri)
  {
    this.uri = uri;
  }
  public String getPath()
  {
    return path;
  }
  public void setPath(String path)
  {
    this.path = path;
  }
  public String getProtocol()
  {
    return protocol;
  }
  public void setProtocol(String protocol)
  {
    this.protocol = protocol;
  }

}
