package cn.paxos.jam.event;

import cn.paxos.jam.Event;
import cn.paxos.jam.util.BytesWrapper;

public class BytesWrapperEvent implements Event
{
  
  private final BytesWrapper bytesWrapper;

  public BytesWrapperEvent(BytesWrapper bytesWrapper)
  {
    this.bytesWrapper = bytesWrapper;
  }

  public BytesWrapper getBytesWrapper()
  {
    return bytesWrapper;
  }

}
