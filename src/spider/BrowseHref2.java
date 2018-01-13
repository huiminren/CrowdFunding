package spider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class BrowseHref2 
{
	private Connection conn = null;//声明连接
	private PreparedStatement sql = null;//声明Statement对象

	/** 
	 *定义构造方法，用于加载数据库驱动
	 * @throws SQLException 
	 */
	public BrowseHref2() throws SQLException
	{
		try  
		{ 
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");//加载数据库驱动
		}
		catch(ClassNotFoundException e) 
		{ 
			e.printStackTrace();
		} 

		String surl = "jdbc:sqlserver://localhost:1433;DatabaseName=CrowdFunding";//数据库iSwufe的URL
		String username = "sa";//数据库的用户名
		String password = "123456";//数据库密码
		conn = DriverManager.getConnection(surl,username,password);//建立连接
	} 

	public static void main(String args[]) throws SQLException
	{
		BrowseHref2 bh = new BrowseHref2();
		bh.Spider();
	}

	public void Spider() throws SQLException
	{
		String category[] = {"di-p-"};//项目进程类别 "di-p-","ds-p-","rs-p-","rd-p-"
		String url_1 = "http://www.zhongchou.com/browse/";
		for(int c = 0; c < category.length; c++)
		{
			ArrayList<String> link = new ArrayList<String>();
			int i = 0;
			for(int p =1; p<=2; p++)
			{
				String url = url_1+category[c]+p;
				//				System.out.println("url_"+url);//success!
				//访问延时
				System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(10000));// （单位：毫秒）  
				System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(10000)); // （单位：毫秒） 
				
				try 
				{
					//抓爬方法
					Document doc = Jsoup.connect(url).get();//进入抓爬地址
					Elements content = doc.getElementsByAttributeValue("class", "PListTabI_imgA btnLink");//声明网页抓爬位置
					Document mydoc = Jsoup.parse(content.toString());//转为字符串
					
					Elements hrefs = mydoc.getElementsByTag("a");//切割需要的内容 有的是ByID 有的是ByTag最常用的两种方法
					for (Element href : hrefs)
					{
						String mylink =href.attr("href");
//						System.out.println(p+"_"+i+"_"+mylink);
						link.add("http://www.zhongchou.com/"+mylink);
						String insertCondition="INSERT INTO amid VALUES(?)";
						sql = conn.prepareStatement(insertCondition);//创建Statement对象
						sql.setString(1, link.get(i)); 
						sql.executeUpdate();
//						System.out.println(p+"_"+i+"_"+link.get(i));
						i++;
					}
//					System.out.println(link.size());
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}

			}
			
		}
	}
}
