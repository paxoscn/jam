package cn.paxos.jam.preset.http.util;

import java.util.Arrays;

public class Swifter
{
  
  private final byte[] bytes;

  private int offset;
  private int set;

  public Swifter(int length)
  {
    bytes = new byte[length];
    offset = set = 0;
  }

  public void append(byte b)
  {
    if (set == bytes.length)
    {
      bytes[offset] = b;
      offset = (offset + 1) % bytes.length;
    } else
    {
      bytes[offset + set] = b;
      set++;
    }
  }
  
  public byte[] get()
  {
    byte[] returned = new byte[set];
    for (int i = 0; i < set; i++)
    {
      returned[i] = bytes[(offset + i) % bytes.length];
    }
    return returned;
  }
  
  public static void main(String[] args)
  {
    Swifter swifter = new Swifter(4);
    for (int i = 0; i < 100; i++)
    {
      swifter.append((byte) i);
      System.out.println(Arrays.toString(swifter.get()));
    }
  }

}
