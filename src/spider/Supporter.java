package spider;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * ��Ŀ֧����
 * @author RHM
 *
 */
public class Supporter 
{
	private Connection conn = null;//��������
	private PreparedStatement sql = null;//����Statement����

	public Supporter() throws SQLException
	{
		try  
		{ 
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");//�������ݿ�����
		}
		catch(ClassNotFoundException e) 
		{ 
			e.printStackTrace();
		} 

		String surl = "jdbc:sqlserver://localhost:1433;DatabaseName=CrowdFunding";//���ݿ�iSwufe��URL
		String username = "sa";//���ݿ���û���
		String password = "123456";//���ݿ�����
		conn = DriverManager.getConnection(surl,username,password);//��������
	}

	public static void main(String args[]) throws SQLException, IOException
	{
		Supporter pg = new  Supporter();
		pg.spider();
	}

	public String[] webpage() throws IOException
	{
		String lineLink[]= null;
		ArrayList<String> ary_line = new ArrayList<String>();

		Scanner in = new Scanner(new File("WebPage//done.txt"));//amid,done
		while(in.hasNextLine())
		{
			ary_line.add(in.nextLine());
		}

		lineLink = new String[ary_line.size()];
		for(int i = 0; i < ary_line.size(); i++)
		{
			lineLink[i] = (String) ary_line.get(i);
			lineLink[i] = lineLink[i].replaceAll("show", "dealitem");
			//			System.out.println(i+"_"+lineLink[i]);
		}

		//				String lineLink[] = {"http://www.zhongchou.com/deal-show/id-4231"};
		//				lineLink[0] = lineLink[0].replaceAll("show", "dealitem");

		return lineLink;
	}

	public void spider() throws SQLException, IOException
	{
		Supporter sp = new Supporter();
		String url[] = sp.webpage();

		for(int u = 0; u < url.length; u++)
		{
			try 
			{
				String ID = url[u].substring(url[u].indexOf("id-")+3,url[u].length());
				System.out.println(u+"_"+ID);

				Document doc2 = Jsoup.connect(url[u]).get();//����ץ����ַ
				Elements econtent2 = doc2.getElementsByAttributeValue("class", "common-sprite");//������ҳץ��λ��
				Document mydoc2 = Jsoup.parse(econtent2.toString());//תΪ�ַ���
				Elements hrefs = mydoc2.getElementsByTag("a");//�и���Ҫ������ �е���ByID �е���ByTag��õ����ַ���

				//ץȡβҳҳ�룬ѭ��ץȡ
				String mylink = null;
				for (Element href : hrefs)
				{
					mylink =href.attr("href");
				}
				//				System.out.println(mylink);
				int page = 1;
				if(mylink != null)
				{
					page = Integer.parseInt(mylink.substring(mylink.indexOf("p-")+2));
				}
				//				System.out.println(page);

				for(int p = 1; p<=page; p++)
				{
					url[u] = url[u]+"-p-"+p;
					Document doc = Jsoup.connect(url[u]).get();//����ץ����ַ
					Elements econtent = doc.getElementsByAttributeValue("class", "support-table");//������ҳץ��λ��
					Document mydoc = Jsoup.parse(econtent.toString());//תΪ�ַ���
					Elements trs = mydoc.select("table").select("tr");
					for(int i = 0;i<trs.size();i++)
					{
						Elements tds = trs.get(i).select("td");
						for(int j = 0;j<tds.size();j++)
						{
							String name = tds.get(0).text();
							String returnmethod = tds.get(1).text();
							String money = tds.get(2).text().substring(1);
							if(money.contains(","))
							{
								money = money.replaceAll(",", "");
							}
							String time = tds.get(3).text();

							String insertCondition="INSERT INTO ItemSupporter VALUES(?,?,?,?,?)";
							sql = conn.prepareStatement(insertCondition);//����Statement����
							sql.setString(1, ID); 
							sql.setString(2, name);
							sql.setString(3, returnmethod);
							sql.setString(4, money);
							sql.setString(5, time);
							sql.executeUpdate();

//							System.out.println(u+"_"+p+"_"+i+"_"+name);
//							System.out.println(u+"_"+p+"_"+i+"_"+returnmethod);
//							System.out.println(u+"_"+p+"_"+i+"_"+money);
//							System.out.println(u+"_"+p+"_"+i+"_"+time);
						}
					}
				}
			}
			catch 
			(Exception e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("finish");
	}
}
