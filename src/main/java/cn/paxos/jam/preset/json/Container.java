package cn.paxos.jam.preset.json;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        String s = escape((String) e, escapingUnicode);
        sb.append(s);
        sb.append('"');
      } else if (e instanceof Container)
      {
        sb.append(((Container) e).toString(escapingUnicode));
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
  
  public Object toCollection(boolean escapingUnicode)
  {
    Object collection = this.isArray() ? new LinkedList<Object>() : new HashMap<String, Object>();
    String key = null;
    for (Object e : list)
    {
      if (e instanceof String)
      {
        String s = escape((String) e, escapingUnicode);
        key = add(collection, key, s);
      } else if (e instanceof Container)
      {
        key = add(collection, key, ((Container) e).toCollection(escapingUnicode));
      } else
      {
        if (e == null)
        {
          key = add(collection, key, null);
        } else
        {
          key = add(collection, key, Integer.parseInt(e.toString()));
        }
      }
    }
    return collection;
  }

  @SuppressWarnings("unchecked")
  private String add(Object collection, String key, Object s)
  {
    if (this.isArray())
    {
      ((List<Object>) collection).add(s);
      return null;
    } else
    {
      if (key == null)
      {
        return (String) s;
      } else
      {
        ((Map<String, Object>) collection).put(key, s);
        return null;
      }
    }
  }

  private String escape(String str, boolean escapingUnicode)
  {
    StringBuilder sb = new StringBuilder();
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
      } else if (c == '\t')
      {
        escaped = "\\t";
      } else if (c == '\\')
      {
        escaped = "\\\\";
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
    return sb.toString();
  }

}
