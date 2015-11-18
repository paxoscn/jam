package cn.paxos.jam.preset.http.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import cn.paxos.jam.util.BytesWithOffset;
import cn.paxos.jam.util.LightByteArrayOutputStream;

public class GZIP
{

  public static BytesWithOffset unzip(BytesWithOffset bytes) throws IOException
  {
    GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(bytes.getBytes(), bytes.getOffset(), bytes.getLength()));
    @SuppressWarnings("resource")
    LightByteArrayOutputStream baos = new LightByteArrayOutputStream();
    byte[] b = new byte[4096];
    for (int read = -1; (read = gzipInputStream.read(b)) > 0;)
    {
      baos.write(b, 0, read);
    }
    gzipInputStream.close();
    byte[] unzipped = baos.toByteArray();
    return new BytesWithOffset(unzipped, 0, unzipped.length);
  }

}
