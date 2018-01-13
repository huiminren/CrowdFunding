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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * ��Ŀ��ҳ����
 * @author RHM
 *
 */
public class Content 
{
	private Connection conn = null;//��������
	private PreparedStatement sql = null;//����Statement����

	public Content() throws SQLException
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

	public static void main(String args[]) throws SQLException, IOException, InterruptedException
	{
		Content ct = new Content();
		ct.spider();
		//		ct.webpage();
	}

	public String[] webpage() throws IOException
	{
		String lineLink[]= null;
		ArrayList<String> ary_line = new ArrayList<String>();

		Scanner in = new Scanner(new File("WebPage//done.txt"));
		while(in.hasNextLine())
		{
			ary_line.add(in.nextLine());
		}

		lineLink = new String[ary_line.size()];
		for(int i = 0; i < ary_line.size(); i++)
		{
			lineLink[i] = (String) ary_line.get(i);
			//			System.out.println(i+"_"+lineLink[i]);
		}
		
//		String lineLink[] = {"http://www.zhongchou.com/deal-show/id-128887"};
		return lineLink;
	}

	public void spider() throws SQLException, IOException, InterruptedException
	{
		Content ct = new Content();
		String url[] = ct.webpage();//��ȡ��ҳ����

		System.out.println(url.length);
		for(int u = 0; u < url.length; u++)
		{
			//���˯��
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
				//				System.out.println(u+"_"+url[u]);
				System.out.println(u+"_"+ID);

				String prono = null, comno = null, supno = null;
				Pattern p = Pattern.compile("[0-9\\.]+");
				Matcher m;
				//ץ������
				Document doc = Jsoup.connect(url[u]).get();//����ץ����ַ
				Elements econtent = doc.getElementsByAttributeValue("class", "m-matter white");//������ҳץ��λ��
				Document mydoc = Jsoup.parse(econtent.toString());//תΪ�ַ���
				//				System.out.println(mydoc);

				System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(10000));// ����λ�����룩  
				System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(10000)); // ����λ�����룩 

				//��Ŀ��չ����
				String str_progress = mydoc.getElementById("deal_detail_progress").toString();
				String progress = Jsoup.parse(str_progress).text();
				//				System.out.println(progress);

				m = p.matcher(progress);
				while(m.find())
				{
					prono = m.group();	
//										System.out.println(prono);  
				}

				//������
				String str_comment = mydoc.getElementById("deal_detail_comment").toString();
				String comment = Jsoup.parse(str_comment).text();
				//				System.out.println(comment);

				m = p.matcher(comment);
				while(m.find())
				{
					comno = m.group();	
//										System.out.println(comno);  
				}

				//֧������
				String str_supporter = mydoc.getElementById("deal_detail_supporter").toString();
				String supporter = Jsoup.parse(str_supporter).text();
				//				System.out.println(supporter);

				m = p.matcher(supporter);
				while(m.find())
				{
					supno = m.group();	
//										System.out.println(supno);  
				}

				//�����ı�
				String str_content = mydoc.getElementsByAttributeValue("class", "Content").toString();
				String content = Jsoup.parse(str_content).text();//ֻҪ����
//								System.out.println(content);

				//�Ƿ������Ƶ
				Elements hrefs = mydoc.getElementsByTag("a");
				String videohref = null;
				for (Element href : hrefs)
				{
					String mylink =href.attr("href");
					if(mylink.contains("player"))
						videohref=mylink;
				}
//								System.out.println(videohref);

				//�ı���ͼƬ����
				Elements pictures = mydoc.getElementsByTag("img");
				int picno = 0;
				for (Element picture : pictures)
				{
					String mypicture =picture.attr("wx-lz");
					picno++;
				}
//								System.out.println(picno);

				//���ࡢ��ǩ
				String str_label = mydoc.getElementsByAttributeValue("class", "z-corner").toString();
				String temp_label = Jsoup.parse(str_label).text();
				int cut = temp_label.indexOf("��ǩ");
				String category = temp_label.substring(3,cut);
				String label = null;
				if(temp_label.length()>9)
				{
					label = temp_label.substring(cut+4,temp_label.length());
				}
//								System.out.println(category);
//								System.out.println(label);
				
				String insertCondition="INSERT INTO ItemHome VALUES(?,?,?,?,?,?,?,?,?,?)";
				sql = conn.prepareStatement(insertCondition);//����Statement����
				sql.setString(1, ID); 
				sql.setString(2, content); 
				sql.setString(3, videohref); 
				sql.setInt(4, picno); 
				sql.setString(5, category); 
				sql.setString(6, label); 
				sql.setString(7, prono); 
				sql.setString(8, comno); 
				sql.setString(9, supno);
				sql.setInt(10, 2);//1--ing; 2--success; 
				sql.executeUpdate();
			}
			catch (Exception e) 
			{
								e.printStackTrace();
			}
		}
		System.out.println("finish");
	}
}
