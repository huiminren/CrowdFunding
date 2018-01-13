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
 * ��Ŀ��չ
 * @author RHM
 *
 */
public class Progress 
{
	private Connection conn = null;//��������
	private PreparedStatement sql = null;//����Statement����

	public Progress() throws SQLException
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
		Progress pg = new Progress();
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
			lineLink[i] = lineLink[i].replaceAll("show", "march");
			//			System.out.println(i+"_"+lineLink[i]);
		}
		return lineLink;
	}

	public void spider() throws SQLException, IOException
	{
		Progress pg = new Progress();
		String url[] = pg.webpage();

		for(int u = 0; u < url.length; u++)
		{
			try 
			{
				String ID = url[u].substring(url[u].indexOf("id-")+3,url[u].length());
				System.out.println(u+"_"+ID);
				Document doc = Jsoup.connect(url[u]).get();//����ץ����ַ
				String str_time = doc.getElementsByAttributeValue("class", "time-zhou").toString();//������ҳץ��λ�� 
				String times = Jsoup.parse(str_time).text();
				//				System.out.println(times);
				String time [] = times.split(" +");

				Elements econtent = doc.getElementsByAttributeValue("class", "p-content");//������ҳץ��λ��
				String string_content = econtent.toString();//�õ�ץ������
				String reg = "</p>";//һ���и��־
				String[] content = string_content.split(reg);//����־�и��־���飬Ϊ������������ȡ��׼��
				for(int i = 0; i<content.length; i++)
				{
					content[i] = content[i].substring(content[i].indexOf(">")+1,content[i].length());
					//					System.out.println(time[i]);
					//					System.out.println(content[i]);

					String insertCondition="INSERT INTO ItemProgress VALUES(?,?,?)";
					sql = conn.prepareStatement(insertCondition);//����Statement����
					sql.setString(1, ID); 
					sql.setString(2, content[i]);
					sql.setString(3, time[i]);
					sql.executeUpdate();
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
