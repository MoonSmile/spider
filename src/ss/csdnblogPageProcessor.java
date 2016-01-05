package ss;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.UrlUtils;

public class csdnblogPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(2000);
    
	public void process(Page page) {
		
		try {
   
		String articlePattern="http://blog.csdn.net/\\w+/article/details/\\d+";
		String expertPattern="\\bhttp://blog.csdn.net/\\w+(?!/)\\b";
		String exn="(" + expertPattern.replace(".", "\\.").replace("*", "[^\"'#]*") + ")";
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
		else if (url.regex(articlePattern).match())
		{
//			System.out.println("this is article");
			//save article
			String cur_title=page.getHtml().xpath("//h1/span/a/text()").toString();
			String cur_user=page.getHtml().xpath("//div[@id='blog_userface']/span/a[@class='user_name']/text()").toString();
			Article newarticle=new Article(cur_user, cur_title);
			SQLiteHelper.mySqLiteHelper.addArticle(newarticle);
			
//			page.putField("author", url.toString());
			//add user
			String url_str=url.toString();
			Pattern p= Pattern.compile("http://blog.csdn.net/\\w+"); 
			Matcher m = p.matcher(url_str);
			if(m.find())
			{
				String user_url=m.group();
//				System.out.println("==========");
//				System.out.println(user_url);
//				System.out.println("==========");
				page.addTargetRequest(user_url);
			}
			
		}
		
		//user
		else if (url.regex(exn).match())
		{
			System.out.println("this is expert");
			List<String> page_urls = page.getHtml().css("span.link_view").links().all();
			page.addTargetRequests(page_urls);
		}
		else {
			
		}
//		if url.rege()
		//age.getHtml().xpath("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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
