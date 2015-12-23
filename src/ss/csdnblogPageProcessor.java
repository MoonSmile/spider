package ss;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

public class csdnblogPageProcessor implements PageProcessor{
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    
	public void process(Page page) {
		
		String articlePattern="http://blog.csdn.net/\\w+/article/details/\\d+";
		String expertPattern="\\bhttp://blog.csdn.net/\\w+(?!/)\\b";
		Selectable url = page.getUrl();	
		if (url.regex("http://blog.csdn.net/index.html").match()) //首页
		{
			//article
			addRequests(page, articlePattern);
			//expert		
			addRequests(page, expertPattern);
//			//category
			List<String> category_urls = page.getHtml().css("div.side_nav").links().all();
			page.addTargetRequests(category_urls);
//			//next page
			List<String> page_urls = page.getHtml().css("div.page_nav").links().all();
			page.addTargetRequests(page_urls);
//			String lastpage_url=page_urls.get(page_urls.size()-1);
			// "/?&page=17"
//			String[] analysis=lastpage_url.split("=");
//			Integer page_count=Integer.parseInt(analysis[analysis.length-1]);
//			addRequests(page, nextPagePattern);

		}
		//article
		if (url.regex(articlePattern).match())
		{
//			page.putField("author", url.toString());
		}
//		if url.rege()
		//age.getHtml().xpath("");
		
		
	}

	public void addRequests(Page page,String urlPattern) {
		String formatPattern="(" + urlPattern.replace(".", "\\.").replace("*", "[^\"'#]*") + ")";
		List<String> requests = page.getHtml().links().regex(formatPattern).all();	
		page.addTargetRequests(requests);
	}
	@Override
	public Site getSite() {
		// TODO Auto-generated method stub
		return site;
	}

	public void testurl(Selectable url) {
		Selectable current=url.regex("index.html");
		String bString=current.toString();
		System.out.println(current.match());
	}
}
