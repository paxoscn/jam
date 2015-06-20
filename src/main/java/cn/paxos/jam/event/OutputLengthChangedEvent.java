package cn.paxos.jam.event;

import cn.paxos.jam.Event;

public class OutputLengthChangedEvent implements Event
{
  
  private final int outputLength;

  public OutputLengthChangedEvent(int outputLength)
  {
    this.outputLength = outputLength;
  }

  public int getOutputLength()
  {
    return outputLength;
  }

}
