package cn.paxos.jam.preset.json.state;

import cn.paxos.jam.State;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public abstract class AppendableState implements State
{
  
  protected final LightByteArrayOutputStream baos = new LightByteArrayOutputStream();

  final void append(byte b)
  {
    baos.write(b);
  }

  final void append(byte[] b)
  {
    baos.write(b);
  }

}
