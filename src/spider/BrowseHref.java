package spider;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class BrowseHref 
{
	private Connection conn = null;//声明连接
	private PreparedStatement sql = null;//声明Statement对象
	private ResultSet rs = null;//声明结果对象

	/** 
	 *定义构造方法，用于加载数据库驱动
	 * @throws SQLException 
	 */
	public BrowseHref() throws SQLException
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
		BrowseHref bh = new BrowseHref();
		bh.Spider();
	}

	public void Spider()
	{
		String category[] = {"di-p-","ds-p-","rs-p-","rd-p-"};//项目进程类别  ,"ds-p-","rs-p-","rd-p-"
		String ary[] = null;
		String url_1 = "http://www.zhongchou.com/browse/";

		for(int c = 0; c < category.length; c++)
		{
			String href[][] = new String[4][];
			href[c] = new String[2500];
			int number = 0;
			for(int p =1; p<=19; p++)
			{
				String url = url_1+category[c]+p;
//				System.out.println("url_"+url);//success!
				System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(10000));// （单位：毫秒）  
				System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(10000)); // （单位：毫秒） 

				try 
				{
					//抓爬方法
					Document doc = Jsoup.connect(url).get();//进入抓爬地址
					Elements content = doc.getElementsByAttributeValue("class", "PListTabI_imgA btnLink");//声明网页抓爬位置
					String string_content = content.toString();//得到抓爬内容
//					System.out.println("content_"+string_content);//success!
					String reg = "</a>";//一次切割标志
					ary = string_content.split(reg);//按标志切割标志分组，为各属性正则提取做准备
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				for (int i = 0; i < ary.length; i++ )
				{
//					System.out.println("ary_"+ary[i]);//success!显示抓取的全部信息
					number = number + 1;//切分下一组信息
					String div_ary = ary[i]; 
					String regEx_href = "(href=\"(.*?)\")";
					Pattern p_href = Pattern.compile(regEx_href);
					Matcher m_href = p_href.matcher(div_ary);
					while (m_href.find())
					{
						String temp_href = m_href.group();
						href[c][number] ="http://www.zhongchou.com/"+temp_href;
						System.out.println("href_"+c+"_"+number+"_"+href[c][number]);
						//这里需要再套一个循环 抓取里面的内容
					}
				}
			}
		}
	}
}
