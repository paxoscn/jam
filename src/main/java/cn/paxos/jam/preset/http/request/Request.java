package cn.paxos.jam.preset.http.request;

import java.util.Arrays;
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

  public String getParam(String key)
  {
    List<String> list = this.getParams().get(key);
    if (list == null || list.isEmpty())
    {
      return null;
    }
    return list.iterator().next();
  }
  
  public String getCookie(String key)
  {
    List<String> cookieLines = headers.get("Cookie");
    if (cookieLines == null)
    {
      return null;
    }
    for (String cookieLine : cookieLines)
    {
      Map<String, String> cookies = new HashMap<String, String>();
      int indexOfSeparator = -1;
      int start = 0;
      do
      {
        int indexOfEqual = cookieLine.indexOf('=', start);
        String cookieKey = cookieLine.substring(start, indexOfEqual);
        indexOfSeparator = cookieLine.indexOf(';', indexOfEqual);
        String cookieValue;
        if (indexOfSeparator < 0)
        {
          cookieValue = cookieLine.substring(indexOfEqual + 1);
        } else
        {
          cookieValue = cookieLine.substring(indexOfEqual + 1, indexOfSeparator);
          start = indexOfSeparator + 2;
        }
        cookies.put(cookieKey, cookieValue);
      } while (indexOfSeparator > -1);
      String found = cookies.get(key);
      if (found != null)
      {
        return found;
      }
    }
    return null;
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
  
  public static void main(String[] args)
  {
    Request r = new Request();
    r.getHeaders().put("Cookie", Arrays.asList(new String[] { "SRCHUID=V=2&GUID=DDC9B05CB28B410596E3753808DDE03E; SRCHUSR=AUTOREDIR=0&GEOVAR=&DOB=20141123; _EDGE_V=1; MUIDB=109577C2CDB26D0321BA7139CC136CD2; _FP=hta=on; _RwBf=s=70&o=16; DUP=Q=BKhmZ__g_TD9cvTOrab4&T=218786194&A=4&IG=2cd30f5709bf4d2ab7b2cfd89b7e1f6b&V=1; FBS=WTS=1418648507373&CR=-1; SRCHD=D=3661295&AF=NOFORM; RMS=F=OCBAIgAwAAR&A=gQCAEECAAAAQ; _SSI=CW=1350&CH=623; MUID=109577C2CDB26D0321BA7139CC136CD2; _EDGE_S=mkt=zh-cn&F=1&SID=153B9562FA41663B10799399FBE06735; _FS=mkt=zh-CN&intlF=&NU=1&ui=en-us; SCRHDN=ASD=0&DURL=#; WLS=TS=63571790021; _HOP=; _SS=SID=D1D83CFB7BEB4FEF89B509411E17E6F0&bIm=16591:&h5comp=0&R=6&nhIm=91-&HVB=1436193229820; SRCHHPGUSR=CW=1350&CH=382&DPR=1" }));
    System.out.println(r.getCookie("SRCHUID"));
    System.out.println(r.getCookie("SRCHUSR"));
    System.out.println(r.getCookie("_FP"));
    System.out.println(r.getCookie("SRCHHPGUSR"));
  }

}
