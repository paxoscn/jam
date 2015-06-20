package cn.paxos.jam.event;

import cn.paxos.jam.Event;

public class ByteEvent implements Event
{
  
  private final byte b;

  public ByteEvent(byte b)
  {
    this.b = b;
  }

  public byte getByte()
  {
    return b;
  }

}
