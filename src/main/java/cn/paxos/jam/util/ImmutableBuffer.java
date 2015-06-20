package cn.paxos.jam.util;

import java.nio.ByteBuffer;

public class ImmutableBuffer
{
  
  public void flip()
  {
    
  }
  
  public void get(byte[] dst)
  {
    
  }

  private void append(ByteBuffer bb, int i, int j)
  {
    // TODO Auto-generated method stub
    
  }

  private byte get()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  private void fixLastBuffer()
  {
    // TODO Auto-generated method stub
    
  }
  
  public static void main(String[] args)
  {
    ByteBuffer bb = ByteBuffer.allocate(5);
    bb.put("12345".getBytes());
    for (int i = 0; i < 5; i++)
    {
      ImmutableBuffer ib = new ImmutableBuffer();
      ib.append(bb, i, 1);
      byte b = ib.get();
      System.out.println((char) b);
    }
    bb.flip();
    bb.put("67890".getBytes());
    ImmutableBuffer ib = new ImmutableBuffer();
    ib.append(bb, 3, 2);
    ib.fixLastBuffer();
    bb.flip();
    bb.put("abcde".getBytes());
    ib.append(bb, 0, 3);
    byte[] s = new byte[5];
    ib.get(s);
    System.out.println(new String(s));
    ib.fixLastBuffer();
    bb.flip();
    bb.put("fghij".getBytes());
    ib.append(bb, 0, 3);
    s = new byte[5];
    ib.get(s);
    System.out.println(new String(s));
  }

}
