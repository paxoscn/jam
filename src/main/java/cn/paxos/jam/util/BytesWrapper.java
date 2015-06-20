package cn.paxos.jam.util;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

public class BytesWrapper
{
  
  private final int length;
  private final List<BytesWithOffset> arrays;

  private int appended;
  
  public BytesWrapper(int length)
  {
    this.length = length;
    this.arrays = new LinkedList<BytesWithOffset>();
    this.appended = 0;
  }

  public int append(byte[] bytes, int offset)
  {
    if (this.isDone())
    {
      return bytes.length;
    }
    int newAppended = Math.min(bytes.length - offset, length - appended);
    arrays.add(new BytesWithOffset(bytes, offset, newAppended));
    appended += newAppended;
    return bytes.length - newAppended - offset;
  }

  public boolean isDone()
  {
    return appended == length;
  }

  public String buildString(String charsetName) throws UnsupportedEncodingException
  {
    if (!this.isDone())
    {
      throw new IllegalStateException();
    }
    BytesWithOffset merged = BytesWithOffset.merge(arrays);
    return new String(merged.getBytes(), merged.getOffset(), merged.getLength(), charsetName);
  }

  public byte get(int index)
  {
    int count = 0;
    for (BytesWithOffset array : arrays)
    {
      if (index < count + array.getBytes().length - array.getOffset())
      {
        return array.getBytes()[index - count + array.getOffset()];
      } else
      {
        count += array.getBytes().length - array.getOffset();
      }
    }
    throw new ArrayIndexOutOfBoundsException(index);
  }

  public List<BytesWithOffset> getArrays()
  {
    return arrays;
  }

  public static void main(String[] args) throws UnsupportedEncodingException
  {
    byte[] bb = "12345".getBytes();
    for (int i = 0; i < 5; i++)
    {
      BytesWrapper ib = new BytesWrapper(1);
      ib.append(bb, i);
      byte b = ib.get(0);
      System.out.println("b = " + (char) b);
    }
    bb = "67890".getBytes();
    BytesWrapper ib = new BytesWrapper(5);
    ib.append(bb, 3);
    System.out.println("false = " + ib.isDone());
    bb = "!abcd".getBytes();
    int left = ib.append(bb, 0);
    System.out.println("true = " + ib.isDone());
    System.out.println("90!ab = " + ib.buildString("UTF-8"));
    ib = new BytesWrapper(11);
    ib.append(bb, bb.length - left);
    System.out.println("false = " + ib.isDone());
    bb = "efghi".getBytes();
    ib.append(bb, 0);
    System.out.println("false = " + ib.isDone());
    bb = "jklmn".getBytes();
    ib.append(bb, 0);
    System.out.println("true = " + ib.isDone());
    System.out.println("cdefghijklm = " + ib.buildString("UTF-8"));
    System.out.println("h = " + (char) ib.get(5));
    System.out.println("m = " + (char) ib.get(10));
  }

}
