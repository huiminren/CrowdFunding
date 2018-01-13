package spider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * 项目评论
 * @author RHM
 *
 */
public class Comment 
{
	private Connection conn = null;//声明连接
	private PreparedStatement sql = null;//声明Statement对象

	public Comment() throws SQLException
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

	public static void main(String args[]) throws SQLException, IOException, InterruptedException
	{
		Comment pg = new Comment();
		pg.spider();
	}

	public String[] webpage() throws IOException
	{
		String lineLink[]= null;
		ArrayList<String> ary_line = new ArrayList<String>();

		Scanner in = new Scanner(new File("WebPage//amid.txt"));//amid,done
		while(in.hasNextLine())
		{
			ary_line.add(in.nextLine());
		}

		lineLink = new String[ary_line.size()];
		for(int i = 0; i < ary_line.size(); i++)
		{
			lineLink[i] = (String) ary_line.get(i);
			lineLink[i] = lineLink[i].replaceAll("show", "topic");
			//			System.out.println(i+"_"+lineLink[i]);
		}

//				String lineLink[] = {"http://www.zhongchou.com//deal-show/id-74599"};
//				lineLink[0] = lineLink[0].replaceAll("show", "topic");

		return lineLink;
	}

	public void spider() throws SQLException, IOException, InterruptedException
	{
		Comment pg = new Comment();
		String url[] = pg.webpage();

		for(int u = 0; u < url.length; u++)
		{
			//随机睡眠
			int rnumber = new Random().nextInt(20)+1;
			if(rnumber == 3)
			{
				long sleeptime = (long)(Math.random() * 2000);
				Thread.sleep(sleeptime);
				//System.out.println(Thread.currentThread().getName()+page+"_"+sleeptime);
			}
			
			try 
			{
				String ID = url[u].substring(url[u].indexOf("id-")+3,url[u].length());
				System.out.println(u+"_"+ID);
				Document doc = Jsoup.connect(url[u]).get();//进入抓爬地址
				Elements econtent = doc.getElementsByAttributeValue("class", "f-outs-updow clearfix");//声明网页抓爬位置
				String string_content = econtent.toString();//得到抓爬内容
				String reg = "</div>";//一次切割标志
				String[] content = string_content.split(reg);//按标志切割标志分组，为各属性正则提取做准备

				ArrayList<String> name = new ArrayList<String>();
				for(int i = 0; i<content.length; i++)
				{
					if(content[i].contains("&nbsp;"))
					{
						content[i] = content[i].substring(content[i].indexOf(">")+1,content[i].length());
						content[i] = Jsoup.parse(content[i]).text();
						//						System.out.println(content[i]);
						String test [] = content[i].split(" +");
						
						String insertCondition="INSERT INTO ItemComment VALUES(?,?,?,?)";
						sql = conn.prepareStatement(insertCondition);//创建Statement对象
						sql.setString(1, ID); 
						sql.setString(2, test[0]);
						sql.setString(3, test[2]);
						sql.setString(4, test[1].substring(1));
						sql.executeUpdate();
						//						System.out.println(ID);
						//						System.out.println(test[0]);
						//						System.out.println(test[2]);
						//						System.out.println(test[1].substring(1));

					}
				}

			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
		System.out.println("finish");
	}
}
