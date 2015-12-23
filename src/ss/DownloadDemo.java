package ss;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.SimplePageProcessor;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;

public class DownloadDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		testDemo();

		testCustom();

	}
	
	public static void testDemo()
	{
		Spider.create(new SimplePageProcessor("http://blog.csdn.net/index.html",
		"blog.csdn.net/*/article/details/*")).setScheduler(new QueueScheduler()).thread(5).run();

	}
	public static void  testCustom() {
//		String startUrl="http://blog.csdn.net/index.html";
//		String urlPattern="http://blog.csdn.net/\\w+/article/details/\\d+";
		csdnblogPageProcessor ppor=new csdnblogPageProcessor();
		Spider.create(ppor)
		.setScheduler(new QueueScheduler()
		.setDuplicateRemover(new HashSetDuplicateRemover())) 
        //从"h"开始抓
        .addUrl("http://blog.csdn.net/index.html")
        //开启5个线程抓取
        .thread(5)
        //启动爬虫
        .run();
		
	}

}

