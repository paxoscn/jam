package cn.paxos.jam.preset.http.client;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.LinkedList;
import java.util.List;

import cn.paxos.jam.Event;
import cn.paxos.jam.State;
import cn.paxos.jam.StateContext;
import cn.paxos.jam.Trigger;
import cn.paxos.jam.event.BytesEvent;
import cn.paxos.jam.preset.http.response.Response;
import cn.paxos.jam.preset.http.response.event.ResponseCompletedEvent;
import cn.paxos.jam.preset.http.response.state.ProtocolState;
import cn.paxos.jam.preset.http.util.GZIP;
import cn.paxos.jam.state.BytesState;
import cn.paxos.jam.util.BytesWithOffset;

public class HTTPClient
{

  public static void visit(int bufferSize, AsynchronousChannelGroup asynchronousChannelGroup, String ip, int port, String reqStr,
      CompletionHandler<BytesWithOffset, Void> completionHandler)
  {
    if (asynchronousChannelGroup != null)
    {
      visitByAIO(bufferSize, asynchronousChannelGroup, ip, port, reqStr, completionHandler);
    } else
    {
      visitByBIO(bufferSize, ip, port, reqStr, completionHandler);
    }
  }

  private static void visitByAIO(int bufferSize, AsynchronousChannelGroup asynchronousChannelGroup, String ip, int port, String reqStr,
      CompletionHandler<BytesWithOffset, Void> completionHandler)
  {
    AsynchronousSocketChannel socketChannel = null;
    try
    {
      socketChannel = AsynchronousSocketChannel.open(asynchronousChannelGroup);
      socketChannel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
      socketChannel.setOption(StandardSocketOptions.SO_RCVBUF, bufferSize);
      socketChannel.connect(new InetSocketAddress(ip, port), null, new VisitCompletionHandler(bufferSize, reqStr, socketChannel, completionHandler));
    } catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
      if (socketChannel != null)
      {
        try
        {
          socketChannel.close();
        } catch (IOException e1)
        {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
      }
    }
  }
  
  private static class VisitCompletionHandler implements CompletionHandler<Void, Object>
  {

    private final int bufferSize;
    private final String reqStr;
    private final AsynchronousSocketChannel out;
    private final CompletionHandler<BytesWithOffset, Void> completionHandler;

    public VisitCompletionHandler(int bufferSize, String reqStr, AsynchronousSocketChannel out, CompletionHandler<BytesWithOffset, Void> completionHandler)
    {
      this.bufferSize = bufferSize;
      this.reqStr = reqStr;
      this.out = out;
      this.completionHandler = completionHandler;
    }

    @Override
    public void completed(Void v, Object attachment)
    {
      byte[] req;
      try
      {
        req = reqStr.getBytes("UTF-8");
      } catch (UnsupportedEncodingException e)
      {
        // TODO Auto-generated catch block
        throw new RuntimeException(e);
      }
      ByteBuffer buffer = ByteBuffer.allocate(req.length);
      buffer.put(req);
      buffer.flip();
      out.write(buffer, null, new WriteCompletionHandler(bufferSize, req.length, out, completionHandler));
    }

    @Override
    public void failed(Throwable exc, Object attachment)
    {
      // TODO Auto-generated method stub
      exc.printStackTrace();
      try
      {
        completionHandler.failed(exc, null);
        out.close();
      } catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
  }

  private static void visitByBIO(int bufferSize, String ip, int port, String reqStr,
      CompletionHandler<BytesWithOffset, Void> completionHandler)
  {
    Socket s = null;
    OutputStream os = null;
    try
    {
      s = new Socket(ip, port);
      os = s.getOutputStream();
    } catch (IOException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
      if (s != null)
      {
        try
        {
          s.close();
        } catch (IOException e)
        {
        }
      }
      completionHandler.failed(e1, null);
      return;
    }
    byte[] req = null;
    try
    {
      req = reqStr.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e0)
    {
      if (os != null)
      {
        try
        {
          os.close();
        } catch (IOException e)
        {
        }
      }
      if (s != null)
      {
        try
        {
          s.close();
        } catch (IOException e)
        {
        }
      }
      completionHandler.failed(e0, null);
      return;
    }
    InputStream is = null;
    try
    {
      os.write(req);
      is = s.getInputStream();
    } catch (IOException e0)
    {
      // TODO Auto-generated catch block
      e0.printStackTrace();
      if (os != null)
      {
        try
        {
          os.close();
        } catch (IOException e)
        {
        }
      }
      if (s != null)
      {
        try
        {
          s.close();
        } catch (IOException e)
        {
        }
      }
      completionHandler.failed(e0, null);
      return;
    }
    StateContext stateContext = new StateContext();
    List<Trigger> triggers = new LinkedList<Trigger>();
    FinalHandler finalHandler = new FinalHandler(s, completionHandler);
    triggers.add(finalHandler);
    stateContext.setTriggers(triggers);
    stateContext.addState(new BytesState());
    stateContext.addState(new ProtocolState());
    DoneState doneState = new DoneState();
    stateContext.addState(doneState);
    stateContext.start();
    byte[] b = new byte[bufferSize];
    try
    {
      for (int read = 0; (!finalHandler.isDone()) && (read = is.read(b)) > 0; )
      {
//        System.out.println("==========" + new String(b, 0, read));
        stateContext.publish(new BytesEvent(b, 0, read));
      }
    } catch (IOException e0)
    {
      // TODO Auto-generated catch block
      e0.printStackTrace();
      if (is != null)
      {
        try
        {
          is.close();
        } catch (IOException e)
        {
        }
      }
      if (os != null)
      {
        try
        {
          os.close();
        } catch (IOException e)
        {
        }
      }
      if (s != null)
      {
        try
        {
          s.close();
        } catch (IOException e)
        {
        }
      }
      completionHandler.failed(e0, null);
    }
  }
  
  private static class WriteCompletionHandler implements CompletionHandler<Integer, Object>
  {
    private final int bufferSize;
    private final int reqLength;
    private final AsynchronousSocketChannel out;
    private final CompletionHandler<BytesWithOffset, Void> completionHandler;

    private int totalWrote = 0;

    public WriteCompletionHandler(int bufferSize, int reqLength,
        AsynchronousSocketChannel out, CompletionHandler<BytesWithOffset, Void> completionHandler)
    {
      this.bufferSize = bufferSize;
      this.reqLength = reqLength;
      this.out = out;
      this.completionHandler = completionHandler;
    }

    @Override
    public void completed(Integer wrote, Object attachment)
    {
      if (wrote < 0)
      {
        // TODO
        try
        {
          completionHandler.failed(new Exception("Wrote < 0"), null);
          out.close();
        } catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        return;
      }
      if (wrote > 0)
      {
        totalWrote += wrote;
        if (totalWrote == reqLength)
        {
          StateContext stateContext = new StateContext();
          List<Trigger> triggers = new LinkedList<Trigger>();
          triggers.add(new FinalHandler(out, completionHandler));
          stateContext.setTriggers(triggers);
          stateContext.addState(new BytesState());
          stateContext.addState(new ProtocolState());
          DoneState doneState = new DoneState();
          stateContext.addState(doneState);
          stateContext.start();

          ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
          out.read(readBuffer, stateContext, new OutCompletionHandler(bufferSize, doneState, out, readBuffer, completionHandler));
        }
      }
    }

    @Override
    public void failed(Throwable exc, Object attachment)
    {
      // TODO Auto-generated method stub
      exc.printStackTrace();
      try
      {
        completionHandler.failed(exc, null);
        out.close();
      } catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
  }
  
  private static class OutCompletionHandler implements CompletionHandler<Integer, StateContext>
  {

    private final int bufferSize;
    private final DoneState doneState;
    private final AsynchronousSocketChannel out;
    private final ByteBuffer readBuffer;
    private final CompletionHandler<BytesWithOffset, Void> completionHandler;

    public OutCompletionHandler(int bufferSize, DoneState doneState,
        AsynchronousSocketChannel out, ByteBuffer readBuffer,
        CompletionHandler<BytesWithOffset, Void> completionHandler)
    {
      this.bufferSize = bufferSize;
      this.doneState = doneState;
      this.out = out;
      this.readBuffer = readBuffer;
      this.completionHandler = completionHandler;
    }

    @Override
    public void completed(Integer read, StateContext stateContext)
    {
      if (read < 0)
      {
        // TODO
        return;
      }
      if (read > 0)
      {
        readBuffer.flip();
        byte[] b = new byte[readBuffer.remaining()];
        readBuffer.get(b);
        stateContext.publish(new BytesEvent(b));
      }
      if (out.isOpen() && !doneState.done)
      {
        ByteBuffer readBuffer = ByteBuffer.allocate(bufferSize);
        out.read(readBuffer, stateContext, new OutCompletionHandler(bufferSize, doneState, out, readBuffer, completionHandler));
      }
    }

    @Override
    public void failed(Throwable exc, StateContext stateContext)
    {
      // TODO Auto-generated method stub
      exc.printStackTrace();
      try
      {
        completionHandler.failed(exc, null);
        out.close();
      } catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    
  }
  
  private static class DoneState implements State
  {

    public boolean done = false;
    
    @Override
    public State onEvent(Event event, StateContext stateContext)
    {
      if (event instanceof ResponseCompletedEvent)
      {
        done = true;
        return null;
      }
      return this;
    }
    
  }
  
  private static class FinalHandler implements Trigger, State
  {

    private final Closeable closeable;
    private final CompletionHandler<BytesWithOffset, Void> completionHandler;
    
    private boolean done;

    public FinalHandler(Closeable closeable, CompletionHandler<BytesWithOffset, Void> completionHandler)
    {
      this.closeable = closeable;
      this.completionHandler = completionHandler;
      this.done = false;
    }

    @Override
    public State trigger(Event event)
    {
      if (event instanceof ResponseCompletedEvent)
      {
        return this;
      }
      return null;
    }

    @Override
    public State onEvent(Event event, StateContext stateContext)
    {
      if (event instanceof ResponseCompletedEvent)
      {
        done = true;
        try
        {
          closeable.close();
        } catch (IOException e1)
        {
          // TODO Auto-generated catch block
          e1.printStackTrace();
        }
        Response response = ((ResponseCompletedEvent) event).getResponse();
        BytesWithOffset body;
        if (response.getChunks().isEmpty())
        {
          body = new BytesWithOffset(new byte[0], 0, 0);
        } else
        {
          BytesWithOffset bodyBytes = BytesWithOffset.merge(response.getChunks());
          List<String> encodingList = response.getHeaderList("Content-Encoding");
          if (encodingList != null && !encodingList.isEmpty())
          {
            String encoding = encodingList.iterator().next();
            if (encoding.indexOf("gzip") > -1)
            {
              try
              {
                bodyBytes = GZIP.unzip(bodyBytes);
              } catch (IOException e)
              {
                // TODO Auto-generated catch block
                throw new RuntimeException(e);
              }
            }
          }
          body = bodyBytes;
        }
//        System.out.println(body);
        completionHandler.completed(body, null);
        return null;
      }
      return this;
    }

    public boolean isDone()
    {
      return done;
    }
    
  }

}
