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
	private Connection conn = null;//��������
	private PreparedStatement sql = null;//����Statement����
	private ResultSet rs = null;//�����������

	/** 
	 *���幹�췽�������ڼ������ݿ�����
	 * @throws SQLException 
	 */
	public BrowseHref() throws SQLException
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
		BrowseHref bh = new BrowseHref();
		bh.Spider();
	}

	public void Spider()
	{
		String category[] = {"di-p-","ds-p-","rs-p-","rd-p-"};//��Ŀ�������  ,"ds-p-","rs-p-","rd-p-"
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
				System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(10000));// ����λ�����룩  
				System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(10000)); // ����λ�����룩 

				try 
				{
					//ץ������
					Document doc = Jsoup.connect(url).get();//����ץ����ַ
					Elements content = doc.getElementsByAttributeValue("class", "PListTabI_imgA btnLink");//������ҳץ��λ��
					String string_content = content.toString();//�õ�ץ������
//					System.out.println("content_"+string_content);//success!
					String reg = "</a>";//һ���и��־
					ary = string_content.split(reg);//����־�и��־���飬Ϊ������������ȡ��׼��
				}
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
				for (int i = 0; i < ary.length; i++ )
				{
//					System.out.println("ary_"+ary[i]);//success!��ʾץȡ��ȫ����Ϣ
					number = number + 1;//�з���һ����Ϣ
					String div_ary = ary[i]; 
					String regEx_href = "(href=\"(.*?)\")";
					Pattern p_href = Pattern.compile(regEx_href);
					Matcher m_href = p_href.matcher(div_ary);
					while (m_href.find())
					{
						String temp_href = m_href.group();
						href[c][number] ="http://www.zhongchou.com/"+temp_href;
						System.out.println("href_"+c+"_"+number+"_"+href[c][number]);
						//������Ҫ����һ��ѭ�� ץȡ���������
					}
				}
			}
		}
	}
}
