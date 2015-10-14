package cn.paxos.jam.preset.json;

import org.junit.Test;

import cn.paxos.jam.StateContext;
import cn.paxos.jam.event.BytesEvent;
import cn.paxos.jam.preset.json.state.InitState;
import cn.paxos.jam.state.BytesState;

public class JsonTest
{

  @Test
  public void testSimplest()
  {
    StateContext stateContext = new StateContext();
    stateContext.start();
    stateContext.addState(new BytesState());
    InitState initState = new InitState();
    stateContext.addState(initState);
    stateContext.publish(new BytesEvent("{a:1}".getBytes()));
    System.out.println(initState.getContainer());
  }

  @Test
  public void testSimpler()
  {
    StateContext stateContext = new StateContext();
    stateContext.start();
    stateContext.addState(new BytesState());
    InitState initState = new InitState();
    stateContext.addState(initState);
    stateContext.publish(new BytesEvent("{a:1,\"b\":\"2\",'c':'3',de:45,f:null}".getBytes()));
    System.out.println(initState.getContainer());
  }

  @Test
  public void testSimple()
  {
    StateContext stateContext = new StateContext();
    stateContext.start();
    stateContext.addState(new BytesState());
    InitState initState = new InitState();
    stateContext.addState(initState);
    String str = "{a:1,\"bb\":\"2\\r\\n2\", 'cc\\'c':'3',d\\u25B2e:45,f:null, g:[1,'2',\"3\\\"3\"]}";
    System.out.println(str);
    stateContext.publish(new BytesEvent(str.getBytes()));
    System.out.println(initState.getContainer());
  }

  @Test
  public void testPerf()
  {
    StateContext stateContext = new StateContext();
    stateContext.start();
    stateContext.addState(new BytesState());
    InitState initState = new InitState();
    stateContext.addState(initState);
    String str = "[{\"target\":\"chinese\",\"url\":\"http://www.apple.com/cn/\",\"title\":\"<strong>Apple</strong> (\u4e2d\u56fd)\",\"desc\":\"&ensp;&#0183;&ensp;<strong>Apple</strong> \u51ed\u501f iPhone\u3001iPad\u3001<strong>Mac</strong>\u3001<strong>Apple</strong> Watch\u3001iOS\u3001OS X\u3001watchOS \u7b49\u4ea7\u54c1\u5f15\u9886\u4e86\u5168\u7403\u521b\u65b0\u3002\u4f60\u53ef\u4ee5\u8bbf\u95ee\u7f51\u7ad9\uff0c\u4e86\u89e3\u548c\u8d2d\u4e70\u4ea7\u54c1\uff0c\u5e76\u83b7\u5f97\u6280\u672f\u652f\u6301\u3002 ... \u66f4\u591a\u9009\u8d2d\u65b9\u5f0f ...\"},{\"target\":\"chinese\",\"url\":\"https://appleid.apple.com/\",\"title\":\"<strong>Apple</strong> - <strong>Apple</strong> ID\",\"desc\":\"An <strong>Apple</strong> ID is your user name for everything you do with <strong>Apple</strong>: Shop the iTunes Store, enable iCloud on all your devices, buy from the <strong>Apple</strong> Online Store, make a ...\"},{\"target\":\"chinese\",\"url\":\"http://stock.finance.sina.com.cn/usstock/quotes/AAPL.html\",\"title\":\"<strong>\u82f9\u679c\u516c\u53f8</strong>(AAPL)\u80a1\u7968\u80a1\u4ef7,\u5b9e\u65f6\u884c\u60c5,\u65b0\u95fb,\u8d22\u62a5,\u7814\u62a5\u8bc4\u7ea7 ...\",\"desc\":\"\u65b0\u6d6a\u8d22\u7ecf-\u7f8e\u80a1\u9891\u9053\u4e3a\u60a8\u63d0\u4f9b<strong>\u82f9\u679c\u516c\u53f8</strong>(AAPL)\u80a1\u7968\u80a1\u4ef7,\u80a1\u7968\u5b9e\u65f6\u884c\u60c5,\u65b0\u95fb,\u8d22\u62a5,\u7f8e\u80a1\u5b9e\u65f6\u4ea4\u6613\u6570\u636e,\u7814\u7a76\u62a5\u544a,\u8bc4\u7ea7,\u8d22\u52a1\u6307\u6807\u5206\u6790\u7b49\u4e0e<strong>\u82f9\u679c\u516c\u53f8</strong>(AAPL)\u80a1\u7968\u76f8\u5173\u7684\u4fe1\u606f\u4e0e\u670d\u52a1\"},{\"target\":\"chinese\",\"url\":\"http://developer.apple.com/\",\"title\":\"<strong>Apple</strong> Developer\",\"desc\":\"&ensp;&#0183;&ensp;One membership. Unlimited possibilities. The new <strong>Apple</strong> Developer Program combines everything you need to develop, distribute, and manage your apps on all <strong>Apple</strong> ...\"},{\"target\":\"chinese\",\"url\":\"http://investor.apple.com/\",\"title\":\"<strong>Apple</strong> - Investor Relations\",\"desc\":\"The Investor Relations website contains information about <strong>Apple Inc</strong>. business for stockholders, potential investors, and financial analysts.\"},{\"target\":\"chinese\",\"url\":\"http://product.pconline.com.cn/mobile/apple/\",\"title\":\"<strong>\u82f9\u679c</strong>(<strong>Apple</strong>)\u624b\u673a\u5b98\u7f51\u62a5\u4ef7_<strong>\u82f9\u679c</strong>(<strong>Apple</strong>)iphone\u624b\u673a\u62a5\u4ef7\u5927\u5168 ...\",\"desc\":\"&ensp;&#0183;&ensp;\u592a\u5e73\u6d0b\u7535\u8111\u7f51\u63d0\u4f9b<strong>\u82f9\u679c</strong>\u624b\u673a\u5927\u5168\u5168\u9762\u670d\u52a1\u4fe1\u606f\uff0c\u5305\u542b<strong>\u82f9\u679c</strong>\u624b\u673a\u62a5\u4ef7\u3001\u53c2\u6570\u3001\u8bc4\u6d4b\u3001\u6bd4\u8f83\u3001\u70b9\u8bc4\u3001\u8bba\u575b\u7b49\uff0c\u5e2e\u60a8\u5168\u9762\u4e86\u89e3<strong>\u82f9\u679c</strong>\u624b\u673a\u3002 ... <strong>\u82f9\u679c</strong>6s\u97f3\u9891\u5bf9\u6bd4\u4f53\u9a8c:\u6ee1\u8db3\u4e00\u822c\u7528\u6237 ...\"},{\"target\":\"chinese\",\"url\":\"https://discussions.apple.com/welcome\",\"title\":\"Official \u007c <strong>Apple</strong> Support Communities\",\"desc\":\"This site contains user submitted content, comments and opinions and is for informational purposes only. <strong>Apple</strong> may provide or recommend responses as a possible ...\"},{\"target\":\"chinese\",\"url\":\"https://support.apple.com/manuals\",\"title\":\"<strong>Apple</strong> - Support - Manuals\",\"desc\":\"More Resources Need Help with Manuals? Find out how to download and view our manuals.\"},{\"target\":\"chinese\",\"url\":\"http://apple.tmall.com/\",\"title\":\"\u9996\u9875-<strong>Apple</strong> Store \u5b98\u65b9\u65d7\u8230\u5e97-\u5929\u732bTmall.com\",\"desc\":\"&ensp;&#0183;&ensp;\u6dd8\u5b9d, \u5e97\u94fa, \u65fa\u94fa, <strong>Apple</strong> Store \u5b98\u65b9\u65d7\u8230\u5e97 ... \u6240\u6709\u5728 <strong>Apple</strong> Store \u5b98\u65b9\u65d7\u8230\u5e97\u7684\u8ba2\u5355\u7684\u53d1\u8d27\u65f6\u95f4\u3001\u4ed8\u6b3e\u671f\u9650\u53ca\u8d2d\u4e70\u4e0a\u9650\u5747\u5df2\u5546\u54c1\u9875\u9762\u89c4\u5b9a\u4e3a\u51c6\u3002\"},{\"target\":\"chinese\",\"url\":\"http://digi.tech.qq.com/d/mbrand/1/38/\",\"title\":\"\u3010<strong>Apple</strong>\uff08<strong>\u82f9\u679c</strong>\uff09\u3011\u673a\u578b_\u62a5\u4ef7_\u56fe\u7247_\u70b9\u8bc4_\u817e\u8baf\u6570\u7801\",\"desc\":\"\u817e\u8baf\u6570\u7801\u63d0\u4f9b<strong>Apple</strong>\uff08<strong>\u82f9\u679c</strong>\uff09\u5168\u90e8\u673a\u578b\uff0c\u6700\u65b0\u62a5\u4ef7\uff0c\u540c\u65f6\u5305\u62ec\u673a\u578b\u56fe\u7247\u3001\u8be6\u7ec6\u53c2\u6570\u3001\u8bc4\u6d4b\u884c\u60c5\u3001\u8bba\u575b\u3001\u70b9\u8bc4\u548c\u7ecf\u9500\u5546\u4ef7\u683c\u7b49\u4fe1\u606f\uff0c\u4e3a\u60a8\u8d2d\u4e70<strong>Apple</strong>\uff08<strong>\u82f9\u679c</strong>\uff09\u624b\u673a\u63d0\u4f9b\u6700 ...\"},{\"target\":\"chinese\",\"url\":\"http://trailers.apple.com/trailers/\",\"title\":\"<strong>Apple</strong> Footer - iTunes Movie Trailers\",\"desc\":\"View the latest movie trailers for many current and upcoming releases. Many trailers are available in high-quality HD, iPod, and iPhone versions.\"},{\"target\":\"chinese\",\"url\":\"http://www.cnet.com/apple/\",\"title\":\"<strong>Apple</strong> - The latest on <strong>Apple</strong> - CNET\",\"desc\":\"&ensp;&#0183;&ensp;Check out the latest <strong>Apple</strong> news on CNET, featuring developments on the iPhone, iPad, Macbooks, OS X and much more.\"},{\"target\":\"chinese\",\"url\":\"http://www.jd.com/pinpai/655-14026.html\",\"title\":\"<strong>Apple</strong>\u624b\u673a - \u4eac\u4e1c\u5546\u57ce\",\"desc\":\"\u4eac\u4e1cJD.COM\u662f\u56fd\u5185\u6700\u4e13\u4e1a\u7684<strong>Apple</strong> \u624b\u673a\u7f51\u4e0a\u8d2d\u7269\u5546\u57ce\uff0c\u63d0\u4f9b<strong>Apple</strong>\u4ea7\u54c1\u7684\u6700\u65b0\u62a5\u4ef7\u3001<strong>Apple</strong> \u624b\u673a\u8bc4\u8bba\u3001<strong>Apple</strong>\u5bfc\u8d2d\u3001<strong>Apple</strong> \u624b\u673a\u56fe\u7247\u7b49\u76f8\u5173\u4fe1\u606f\"},{\"target\":\"chinese\",\"url\":\"http://www.engadget.com/topics/apple/\",\"title\":\"Topic: <strong>apple</strong> articles on Engadget\",\"desc\":\"&ensp;&#0183;&ensp;<strong>Apple</strong> has removed several ad-blocking apps from its Store that created a risk of &quot;man-in-the-middle&quot; security breaches. While <strong>Apple</strong> now permits ad-blockers for Safari ...\"},{\"target\":\"chinese\",\"url\":\"http://topics.nytimes.com/top/news/business/companies/apple_computer_inc/index.html\",\"title\":\"<strong>Apple</strong> Incorporated - Times Topics\",\"desc\":\"<strong>Apple</strong> Incorporated financial and business news, updates, and information from The New York Times and other leading providers.\"},{\"target\":\"chinese\",\"url\":\"http://www.forbes.com/companies/apple/\",\"title\":\"<strong>Apple</strong> on the Forbes World&#039;s Most Valuable Brands List\",\"desc\":\"&ensp;&#0183;&ensp;<strong>Apple, Inc</strong>. designs, manufactures, and markets mobile communication and media devices, personal computers, portable digital music players, and sells a variety of ...\"},{\"target\":\"chinese\",\"url\":\"http://developer.apple.com/swift/\",\"title\":\"Swift - Overview - <strong>Apple</strong> Developer\",\"desc\":\"&ensp;&#0183;&ensp;Swift is an innovative new programming language for iOS and OS X with concise yet expressive syntax that produces lightning-fast apps. It makes writing code ...\"},{\"target\":\"chinese\",\"url\":\"https://support.apple.com/downloads/\",\"title\":\"<strong>Apple</strong> - Support - Downloads\",\"desc\":\"<strong>Apple</strong>; Shopping Bag; <strong>Apple</strong>; <strong>Mac</strong>; iPad; iPhone; Watch; TV; Music; Support; Search <strong>apple</strong>.com; Shopping Bag; Downloads. Downloads in other languages. Browse Downloads by ...\"},{\"target\":\"chinese\",\"url\":\"http://events.apple.com.edgesuite.net/10oiuhfvojb23/event/index.html\",\"title\":\"<strong>Apple</strong> - <strong>Apple</strong> Events - Celebrating Steve\",\"desc\":\"Watch the Presentation in HD; Watch the special event, filmed live at the <strong>Apple</strong> campus in Cupertino, California. Streaming video requires Safari 4 or 5 on <strong>Mac</strong> OS X ...\"}]";
    System.out.println(str);
    long s = System.currentTimeMillis();
    System.out.println("s " + s);
    stateContext.publish(new BytesEvent(str.getBytes()));
    System.out.println("e " + (System.currentTimeMillis() - s));
    System.out.println(initState.getContainer());
  }
  
  public static void main(String[] args)
  {
    StringBuilder sb = new StringBuilder();
    sb.appendCodePoint(Integer.parseInt("25B2", 16));
    System.out.println(sb);
  }

}
