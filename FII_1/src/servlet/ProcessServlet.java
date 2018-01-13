package servlet;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import bean.DataBean;

/**
 * Servlet implementation class ProcessServlet
 */
@WebServlet(asyncSupported = true, description = "Process the data from the front", urlPatterns = { "/ProcessServlet" })
public class ProcessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProcessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String retUrl = request.getHeader("Referer");
		retUrl = retUrl.substring(retUrl.lastIndexOf("/")+1);
		DataBean dbean = new DataBean();
		request.getSession(true).setAttribute("dbean", dbean);
		
		String[] myurl = new String[21];
		
		int amount = Integer.parseInt(request.getParameter("choice_amount").trim());
		dbean.setAmount(amount);
		for(int i=1;i<=amount;i++)
		{
			myurl[i] = request.getParameter("uName"+i).trim();
		}
		
		this.getScore(amount, myurl,dbean);
		
		if(retUrl.equals("FII.jsp"))
			request.getRequestDispatcher("FIIResult2.jsp").forward(request, response);
		else
			request.getRequestDispatcher("FIIAPPR.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	public ArrayList getUrl(int amount,String[] myurl)//接收前台传来的URL，里面具体的接收方法需要改一下
	{
		ArrayList url = new ArrayList();
		for(int i=1;i<=amount;i++)
			url.add(myurl[i]);
		return url;
	}
	
	public HashMap getAttribute(int amount,String[] myurl) throws IOException
	{
		ArrayList url = getUrl(amount,myurl);
		ArrayList list_mymoney = new ArrayList();
		ArrayList list_myplace = new ArrayList();
		ArrayList list_mycontent = new ArrayList();
		ArrayList list_mycategory = new ArrayList();
		ArrayList list_mylabel = new ArrayList();
		
		
		System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(10000));// （单位：毫秒）  
		System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(10000)); // （单位：毫秒） 

		for(int i=0;i<url.size();i++)
		{
			Document doc = Jsoup.connect((String) url.get(i)).get();//进入抓爬地址
			Elements moneys = doc.select(".fr");//目标金额
			Elements places = doc.select(".p-c span");//项目地点
			Elements contents = doc.select(".content");//主页内容 
			
			for(Element money:moneys)
			{
				list_mymoney.add(money.text());
			}
			for(int x = 1; x < places.size(); x = 2*x)//place里面包含两个，取第一个
			{
				list_myplace.add(places.get(x-1).text());
			}
			for(Element content:contents)
			{
				list_mycontent.add(content.text());
			}
			
			//分类、标签
			String str_label = doc.getElementsByAttributeValue("class", "z-corner").toString();
			String temp_label = Jsoup.parse(str_label).text();
			int cut = temp_label.indexOf("标签");
			list_mycategory.add(temp_label.substring(3,cut));//分类
			
			if(temp_label.length()>9)
			{
				list_mylabel.add(temp_label.substring(cut+4,temp_label.length()));//标签
			}
		}
		
		String[] mycontent = new String[list_mycontent.size()];
		String[] myplace = new String[list_mycontent.size()];
		String[] mymoney = new String[list_mycontent.size()];
		String[] mycategory = new String[list_mycontent.size()];
		String[] mylabel = new String[list_mycontent.size()];
		
		
		for(int j = 0; j<list_mycontent.size(); j++)
		{
			mycontent[j] = (String)list_mycontent.get(j);
			myplace[j] = (String)list_myplace.get(j);
			mymoney[j] = (String)list_mymoney.get(j);
			mycategory[j] = (String)list_mycategory.get(j);
			mylabel[j] = (String)list_mylabel.get(j);
		}
		
		HashMap map = new HashMap();
		map.put("money", mymoney);
		map.put("content", mycontent);
		map.put("place", myplace);
		map.put("category", mycategory);
		map.put("label", mylabel);
		return map;
	}
	
	public HashMap getScore(int amount,String[] myurl, DataBean dbean) throws IOException
	{
		HashMap map = new HashMap();
		HashMap mapa = new HashMap();
		mapa = getAttribute(amount,myurl);
	    String[] content = (String[])mapa.get("content");
	    String[] money = (String[])mapa.get("money");
	    String[] place = (String[])mapa.get("place");
		String[] category = (String[])mapa.get("category");
		String[] label = (String[])mapa.get("label");
	    
	    
		Integer [] score = new Integer[content.length];
		Integer[] rank = new Integer[content.length];
		
		for(int i = 0; i < content.length; i++)
		{
			//主页内容字数评分
			int scontent = 0;
			if(content[i].length()>=1350)
			{
				scontent=89;
			}
			if(content[i].length()<1350)
			{
				scontent=41;
			}
//			System.out.println(i+"scon_"+scontent+"con_"+content[i].length());
			
			//项目地点评分
			int splace = 0;
			if(place[i].substring(0,2).equals("天津")||place[i].substring(0,2).equals("宁夏")
					||place[i].substring(0,2).equals("黑龙")||place[i].substring(0,2).equals("山西")
					||place[i].substring(0,2).equals("内蒙")||place[i].substring(0,2).equals("西藏")
					||place[i].substring(0,2).equals("香港")||place[i].substring(0,2).equals("台湾"))
			{
				splace = 28;
			}
			if(place[i].substring(0,2).equals("浙江")||place[i].substring(0,2).equals("北京")
					||place[i].substring(0,2).equals("广西")||place[i].substring(0,2).equals("重庆")
					||place[i].substring(0,2).equals("上海")||place[i].substring(0,2).equals("广东"))
			{
				splace = 87;
			}
			if(place[i].substring(0,2).equals("河北")||place[i].substring(0,2).equals("四川")
					||place[i].substring(0,2).equals("山东")||place[i].substring(0,2).equals("江西")
					||place[i].substring(0,2).equals("甘肃")||place[i].substring(0,2).equals("辽宁")
					||place[i].substring(0,2).equals("湖南")||place[i].substring(0,2).equals("安徽"))
			{
				splace = 77;
			}
			if(place[i].substring(0,2).equals("新疆")||place[i].substring(0,2).equals("陕西")
					||place[i].substring(0,2).equals("福建"))
			{
				splace=56;
			}
			if(place[i].substring(0,2).equals("云南")||place[i].substring(0,2).equals("吉林")
					||place[i].substring(0,2).equals("贵州")||place[i].substring(0,2).equals("青海")
					||place[i].substring(0,2).equals("海南"))
			{
				splace=37;
			}
//			System.out.println(i+"spl_"+splace+"pl_"+place[i].substring(0,2));
			
			//目标金额评分
			int smoney = 0;
			double temp_money = 0.0;
			Pattern p = Pattern.compile("[^0-9]");
			Matcher m = p.matcher(money[i]);
			while(m.find())
			{
				temp_money = Double.parseDouble(m.replaceAll("").trim());  
			}
			
			if(temp_money<1038)
			{
				smoney = 175;
			}
			if(temp_money>=1038 & temp_money<10000)
			{
				smoney=84;
			}
			if(temp_money>=10000)
			{
				smoney=48;
			}
//			System.out.println(i+"smo_"+smoney+"mo_"+money[i]+"temo_"+temp_money);
			
			//分类评分
			int scategory = 0;
			if(category[i].trim().equals("出版"))
			{
				scategory = 110;
			}
			if(category[i].trim().equals("公益"))
			{
				scategory = 148;
			}
			if(category[i].trim().equals("科技"))
			{
				scategory = 165;
			}
			if(category[i].trim().equals("农业"))
			{
				scategory = 68;
			}
			if(category[i].trim().equals("其他"))
			{
				scategory = 132;
			}
			if(category[i].trim().equals("商铺"))
			{
				scategory = 14;
			}
			if(category[i].trim().equals("艺术"))
			{
				scategory = 113;
			}
			if(category[i].trim().equals("娱乐"))
			{
				scategory = 175;
			}
//			System.out.println(i+"scat_"+scategory+"cat_"+category[i].trim());
			
			//标签个数评分
			int slabel = 0;
			int labelno = label[i].length()-label[i].replaceAll(" ","").length()+1;
			if(labelno>=1 & labelno<3)
			{
				slabel=87;
			}
			if(labelno>=3 & labelno<5)
			{
				slabel=83;
			}
			if(labelno>=5 & labelno<6)
			{
				slabel=128;
			}
			if(labelno>=6)
			{
				slabel=42;
			}
			
//			System.out.println(i+"sla_"+slabel+"lab_"+label[i]);
			
			score[i] = scontent+splace+smoney+scategory+slabel;
//			System.out.println(i+"score_"+score[i]);
			
			if(score[i]<200)
			{
				rank[i] = 1;
			}
			if(score[i]>=200 & score[i]<300)
			{
				rank[i] = 2;
			}
			if(score[i]>=300 & score[i]<400)
			{
				rank[i] = 3;
			}
			if(score[i]>=400 & score[i]<500)
			{
				rank[i] = 4;
			}
			if(score[i]>=500)
			{
				rank[i] = 5;
			}
//			System.out.println("rank_"+rank[i]);
		}
		
		//倒序输出排序
		Arrays.sort(rank,Collections.reverseOrder());
		Arrays.sort(score,Collections.reverseOrder());
		
		for(int i=0; i<rank.length; i++)
		{
			dbean.setScore(i+1, score[i]);
			dbean.setUrl(i+1, myurl[i+1]);
			dbean.setRank(i+1, rank[i]);
		}
		map.put("score", score);
		map.put("rank", rank);
		return map;
	}
}
