package cn.paxos.jam.event;

import cn.paxos.jam.Event;
import cn.paxos.jam.util.BytesWithOffset;

public class BytesEvent implements Event
{
  
  private final BytesWithOffset bytes;

  public BytesEvent(byte[] bytes)
  {
    this(bytes, 0, bytes.length);
  }

  public BytesEvent(byte[] bytes, int offset, int length)
  {
    this.bytes = new BytesWithOffset(bytes, offset, length);
  }

  public BytesWithOffset getBytes()
  {
    return bytes;
  }

}
