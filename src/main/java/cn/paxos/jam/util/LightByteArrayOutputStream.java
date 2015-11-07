package cn.paxos.jam.util;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LightByteArrayOutputStream extends OutputStream
{
  
  private final List<Object> list = new LinkedList<Object>();
  
  private int length = 0;

  @Override
  public void write(int b)
  {
    list.add((byte) b);
    length += 1;
  }

  @Override
  public void write(byte[] b)
  {
    list.add(b);
    length += b.length;
  }

  @Override
  public void write(byte[] b, int off, int len)
  {
    byte[] bb = new byte[len];
    System.arraycopy(b, off, bb, 0, len);
    write(bb);
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
