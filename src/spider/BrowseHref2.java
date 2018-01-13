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
	private Connection conn = null;//��������
	private PreparedStatement sql = null;//����Statement����

	/** 
	 *���幹�췽�������ڼ������ݿ�����
	 * @throws SQLException 
	 */
	public BrowseHref2() throws SQLException
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

	public static void main(String args[]) throws SQLException
	{
		BrowseHref2 bh = new BrowseHref2();
		bh.Spider();
	}

	public void Spider() throws SQLException
	{
		String category[] = {"di-p-"};//��Ŀ������� "di-p-","ds-p-","rs-p-","rd-p-"
		String url_1 = "http://www.zhongchou.com/browse/";
		for(int c = 0; c < category.length; c++)
		{
			ArrayList<String> link = new ArrayList<String>();
			int i = 0;
			for(int p =1; p<=2; p++)
			{
				String url = url_1+category[c]+p;
				//				System.out.println("url_"+url);//success!
				//������ʱ
				System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(10000));// ����λ�����룩  
				System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(10000)); // ����λ�����룩 
				
				try 
				{
					//ץ������
					Document doc = Jsoup.connect(url).get();//����ץ����ַ
					Elements content = doc.getElementsByAttributeValue("class", "PListTabI_imgA btnLink");//������ҳץ��λ��
					Document mydoc = Jsoup.parse(content.toString());//תΪ�ַ���
					
					Elements hrefs = mydoc.getElementsByTag("a");//�и���Ҫ������ �е���ByID �е���ByTag��õ����ַ���
					for (Element href : hrefs)
					{
						String mylink =href.attr("href");
//						System.out.println(p+"_"+i+"_"+mylink);
						link.add("http://www.zhongchou.com/"+mylink);
						String insertCondition="INSERT INTO amid VALUES(?)";
						sql = conn.prepareStatement(insertCondition);//����Statement����
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
