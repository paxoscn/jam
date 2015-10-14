package cn.paxos.jam.preset.json;

import java.util.LinkedList;
import java.util.List;

public class Container
{
  
  private final Container parent;
  private final List<Object> list = new LinkedList<Object>();
  private final boolean array;

  public Container(Container parent, boolean array)
  {
    this.parent = parent;
    this.array = array;
  }

  public Container getParent()
  {
    return parent;
  }

  public boolean isArray()
  {
    return array;
  }
  
  public int size()
  {
    return list.size();
  }
  
  public void add(Object e)
  {
    list.add(e);
  }

  public List<Object> getList()
  {
    return list;
  }

  @Override
  public String toString()
  {
    return toString(true);
  }

  public String toString(boolean escapingUnicode)
  {
    StringBuilder sb = new StringBuilder();
    if (this.isArray())
    {
      sb.append('[');
    } else
    {
      sb.append('{');
    }
    int i = 0;
    for (Object e : list)
    {
      if (i > 0)
      {
        if (this.isArray())
        {
          sb.append(',');
        } else
        {
          if (i % 2 == 0)
          {
            sb.append(',');
          } else
          {
            sb.append(':');
          }
        }
      }
      if (e instanceof String)
      {
        sb.append('"');
        String str = (String) e;
        for (int j = 0; j < str.length(); j++)
        {
          int c = str.codePointAt(j);
          String escaped = null;
          if (c == '\r')
          {
            escaped = "\\r";
          } else if (c == '\n')
          {
            escaped = "\\n";
          } else if (c == '"')
          {
            escaped = "\\\"";
          } else if (c > 'z' && escapingUnicode)
          {
            escaped = "\\u" + Integer.toHexString(0x10000 + c).substring(1);
          }
          if (escaped == null)
          {
            sb.append((char) c);
          } else
          {
            sb.append(escaped);
          }
        }
        sb.append('"');
      } else
      {
        if (e == null)
        {
          sb.append("null");
        } else
        {
          sb.append(e.toString());
        }
      }
      i++;
    }
    if (this.isArray())
    {
      sb.append(']');
    } else
    {
      sb.append('}');
    }
    return sb.toString();
  }

}
