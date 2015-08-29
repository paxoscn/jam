package cn.paxos.jam;

public interface State
{
  
  State onEvent(Event event, StateContext stateContext);

}
