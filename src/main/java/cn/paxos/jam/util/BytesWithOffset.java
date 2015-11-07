package cn.paxos.jam.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class BytesWithOffset
{

  private final byte[] bytes;
  private final int offset;
  private final int length;

  public BytesWithOffset(byte[] bytes, int offset, int length)
  {
    this.bytes = bytes;
    this.offset = offset;
    this.length = length;
  }
  
  public static int length(List<BytesWithOffset> arrays)
  {
    if (arrays.size() > 1)
    {
      int length = 0;
      for (BytesWithOffset array : arrays)
      {
        length += array.getLength();
      }
      return length;
    } else
    {
      return arrays.iterator().next().getLength();
    }
  }
  
  public static BytesWithOffset merge(List<BytesWithOffset> arrays)
  {
    if (arrays.size() > 1)
    {
      int length = length(arrays);
      byte[] stringBytes = new byte[length];
      int appended = 0;
      for (BytesWithOffset array : arrays)
      {
        int newAppended = Math.min(array.getLength(), length - appended);
        System.arraycopy(array.getBytes(), array.getOffset(), stringBytes, appended, newAppended);
        appended += newAppended;
      }
      return new BytesWithOffset(stringBytes, 0, length);
    } else
    {
      return arrays.iterator().next();
    }
  }
  
  public static ByteBuffer toBuffer(List<BytesWithOffset> arrays)
  {
    int length = length(arrays);
    ByteBuffer buffer = ByteBuffer.allocate(length);
    if (arrays.size() > 1)
    {
      int appended = 0;
      for (BytesWithOffset array : arrays)
      {
        int newAppended = Math.min(array.getLength(), length - appended);
        buffer.put(array.getBytes(), array.getOffset(), newAppended);
        appended += newAppended;
      }
      return buffer;
    } else
    {
      BytesWithOffset array = arrays.iterator().next();
      buffer.put(array.getBytes(), array.getOffset(), array.getLength());
      return buffer;
    }
  }
  
  public byte[] getBytes()
  {
    return bytes;
  }
  public int getOffset()
  {
    return offset;
  }
  public int getLength()
  {
    return length;
  }
  
  public static void main(String[] args) throws UnsupportedEncodingException
  {
    List<BytesWithOffset> bs = new LinkedList<BytesWithOffset>();
    bs.add(new BytesWithOffset("abcde   fghij".getBytes(), 0, 5));
    bs.add(new BytesWithOffset("abcde   fghij".getBytes(), 8, 5));
    BytesWithOffset mergedBodyBytes = BytesWithOffset.merge(bs);
    System.out.println(new String(mergedBodyBytes.getBytes(), mergedBodyBytes.getOffset(), mergedBodyBytes.getLength(), "UTF-8"));
  }

}
