package cn.paxos.jam.event;

import cn.paxos.jam.Event;

public class BytesEvent implements Event
{
  
  private final byte[] bytes;

  public BytesEvent(byte[] bytes)
  {
    this.bytes = bytes;
  }

  public byte[] getBytes()
  {
    return bytes;
  }

}
