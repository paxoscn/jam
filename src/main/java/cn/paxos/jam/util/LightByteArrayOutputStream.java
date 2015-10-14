package cn.paxos.jam.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LightByteArrayOutputStream
{
  
  private final List<Object> list = new LinkedList<Object>();
  
  private int length = 0;
  
  public void write(byte b)
  {
    list.add(b);
    length += 1;
  }

  public void write(byte[] bs)
  {
    list.add(bs);
    length += bs.length;
  }

  public byte[] toByteArray()
  {
    byte[] returned = new byte[length];
    Iterator<Object> iter = list.iterator();
    for (int i = 0; i < length; )
    {
      Object e = iter.next();
      if (e instanceof byte[])
      {
        byte[] bs = (byte[]) e;
        System.arraycopy(bs, 0, returned, i, bs.length);
        i += bs.length;
      } else
      {
        returned[i++] = (byte) e;
      }
    }
    return returned;
  }

}
