package spider;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DownloadMessages {

	/**
	 * 获取参数指定的网页代码，将其返回给调用者，由调用者对其解析 返回String
	 */
	public static String DivContent(String url) {
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Elements contents = doc.getElementsByAttributeValue("class", "col-xs-12 col-sm-9");
		return contents.toString();
	}

	/**
	 * 返回数据
	 * @param url
	 * @param WhichPart   1==>Title, 2==>Link, 3==>time
	 * @return
	 */
	public static ArrayList<String> getMessage (String url, int WhichPart){
		ArrayList<String> link = new ArrayList<String>();
		ArrayList<String> title = new ArrayList<String>();
		ArrayList<String> time  = new ArrayList<String>();
		int i = 0;

		String divcontent = DivContent(url);
		Document mydoc = Jsoup.parse(divcontent,url);
		Elements hrefs = null;
		Elements times = null;
		if (WhichPart == 1 || WhichPart == 2) 
		{
			hrefs = mydoc.getElementsByTag("a");
		}
		else if(WhichPart == 3)
		{
			times = mydoc.getElementsByAttributeValue("class", "jumbotron");
		}

		if (WhichPart == 1)
		{
			title.clear();
			for (Element href : hrefs)
			{
				String mytitle = href.getElementsByTag("a").attr("title");
				title.add(mytitle);
//				System.out.println(mytitle);
			}
			return title;
		} 
		else if (WhichPart == 2)
		{
			link.clear();
			i = 0;
			for (Element href : hrefs)
			{
				String mylink =href.getElementsByTag("a").attr("href");
				link.add(mylink);
				if (i == 14) 
				{
					break;
				}
//				System.out.println(mylink);
				i++;
			}
			return link;
		}else {
			time.clear();
			for (Element time1 : times) {
				String mytime = time1.text().substring(0, time1.text().indexOf(" "));
				time.add(mytime);
//				System.out.println(mytime);
			}
			return time;
		}
	}

	/**
	 * 
	 * @param url
	 * @return  下一页的网址，如果返回“ ”则表示已到尾页
	 */
	public static String NextPageUrl(String url){
		ArrayList<String> link = new ArrayList<String>();
		int i = 0;

		String divcontent = DivContent(url);
		Document mydoc = Jsoup.parse(divcontent, url);
		Elements hrefs = mydoc.getElementsByTag("a");
		link.clear();
		i = 0; 
		for (Element href : hrefs){
			link.add(href.getElementsByTag("a").attr("href"));
			i++;
		}
		String t = mydoc.text();
		if(t.substring(t.length()-7, t.length()-3).equals("【末页】")){
			return " ";
		}else {
			return link.get(i-1);
		}
	}
	
	public static String getOneMessage (String url, int WhichPart){
		String link = "";
		String title = "";
		String time  = "";
		int i = 0;

		String divcontent = DivContent(url);
		Document mydoc = Jsoup.parse(divcontent, url);
		Elements hrefs = null;
		Elements times = null;
		if (WhichPart == 1 || WhichPart == 2) {
			hrefs = mydoc.getElementsByTag("a");
		}else if(WhichPart == 3){
			times = mydoc.getElementsByAttributeValue("class", "jumbotron");
		}

		if (WhichPart == 1) {
			title = hrefs.get(0).getElementsByTag("a").attr("title");
			return title;
		} else if (WhichPart == 2) {
			hrefs.get(0).getElementsByTag("a").attr("href");
			return link;
		}else {
			time = times.get(0).text().substring(0, times.get(0).text().indexOf(" "));
			return time;
		}
	}
}
