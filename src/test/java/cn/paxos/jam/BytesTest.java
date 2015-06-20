package cn.paxos.jam;

import org.junit.Test;

import cn.paxos.jam.event.BytesEvent;
import cn.paxos.jam.state.BytesState;
import cn.paxos.jam.state.RepeatedByteFinderState;

public class BytesTest
{

  @Test
  public void testBytes()
  {
    StateContext stateContext = new StateContext();
    stateContext.start();
    stateContext.addState(new BytesState());
    stateContext.addState(new RepeatedByteFinderState());
    stateContext.publish(new BytesEvent("Hello".getBytes()));
    stateContext.publish(new BytesEvent("World".getBytes()));
  }

}
