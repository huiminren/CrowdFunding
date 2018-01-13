package spider;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author ZYC
 *
 */
public class Info
{

	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		readFileByLines("/Users/joy/Downloads/done.txt");
	}
	public static void readFileByLines(String fileName)
	{
		//Write("网址ID；标题；标题内容；发起人；项目地点；个人简介；已完成；筹资天数；筹资金额\n","FIdataamid.txt");
		//Write("网址ID；状态概要\n","UNSUREamid1.txt");
		//Write("网址ID；状态具体信息\n","UNSUREamid2.txt");
		//Write("网址ID；金额和对应的人\n","UNSUREamis1.txt");
		//Write("网址ID；众筹内容\n","UNSUREamis2.txt");
		//Write("网址ID；免运费，回报时间等信息\n","UNSUREamis3.txt");
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                //System.out.println("line " + line + ": " + tempString);
                //System.out.println(tempString.substring(39));
                Analyse(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }
	
	private static void Analyse(String tempString) throws IOException {
		// TODO Auto-generated method stub
		String page="FIdataamid.txt";
		String pages1="UNSUREamis1.txt";
		String pages2="UNSUREamis2.txt";
		String pages3="UNSUREamis3.txt";
		String paged1="UNSUREamid1.txt";
		String paged2="UNSUREamid2.txt";
		
		String line ="\n";
		
	
        //File input = new File("/Users/joy/Documents/workspace/Fetch/zizhucan.html");
		//Document doc = Jsoup.parse(input, "UTF-8");
        Document doc = Jsoup.connect(tempString).get();//创建连接，取得并解析HTML文件
        Elements hsb=doc.select(".project-right h2 a");//第一行粗体
        Elements hsb2=doc.select(".p-color");//第二行字体
        Elements ormoney=doc.select(".zcz_money span");//筹资金额和人数
        Elements content=doc.select(".s_c >p");//具体内容
        Elements fix=doc.select(".span-image");//免运费等
        
        
        
        Elements name=doc.select(".name");//发起人
        Elements place=doc.select(".p-c span");//项目地点！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        Elements intro=doc.getElementsByClass("p-pb");//个人简介
        Elements finished=doc.select(".bfb_ts span");//已完成，剩余
//        Elements money=doc.select(".czmb");//筹资金额
        Elements money=doc.select(".fr");//筹资金额！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        Elements bar=doc.select(".particulars li h4");//筹资进度
        Elements barinfo=doc.select(".particulars-right-bottom li p");//筹资进度信息
        //Elements more=doc.select(".s_c");
        //Elements fix=doc.getElementsByClass("span-image clear-fix");
   
        
        Write(tempString.substring(39),page);
        Write(";",page);
        print("\nhsb: (%d)", hsb.size());
        
        
        for (Element h2 : hsb) {
                //print(" * %s:%s",h2.tagName(),h2.text());
                Write(h2.text(),page);
                Write(";",page);
        }//其中含有第一行
        
	
		//print("\nhsb2: (%d)", hsb2.size());
	    for (Element hs2 : hsb2) {
	            //print(" * %s:%s",hs2.tagName(),hs2.text());
	            Write(hs2.text(),page);
                Write(";",page);
	    }//其中含有第二行
	    
	    //print("\norm: (%d)", ormoney.size());
	    for (Element orm : ormoney) {
	            //print(" * %s:%s",orm.tagName(),orm.text());
	            Write(tempString.substring(39),pages1);
	            Write(";",pages1);
	            Write(orm.text(),pages1);
                Write(line,pages1);
	    }//其中含有筹资金额
	    
	    //print("\ncontent: (%d)", content.size());
	    for (Element con : content) {
	            //print(" * %s:%s",con.tagName(),con.text());
	            Write(tempString.substring(39),pages2);
	            Write(";",pages2);
	            Write(con.text(),pages2);
                Write(line,pages2);
                
	    }//其中含有具体信息
	    
	    //print("\nfix: (%d)", fix.size());
	    for (Element fi : fix) {
	            //print(" * %s:%s",fi.tagName(),fi.text());
	            Write(tempString.substring(39),pages3);
	            Write(";",pages3);
	            Write(fi.text(),pages3);
                Write(line,pages3);
	    }//其中含有免运费等
	    
	    
	    
	    
	    
	    //print("\nname: (%d)", name.size());
	    for (Element fqr : name) {
	            //print(" * %s:%s",fqr.tagName(),fqr.text());
	            Write(fqr.text(),page);
                Write(";",page);
	    }//其中含有发起人姓名
	    
	    //print("\nplace: (%d)", place.size());
	    for (Element pl : place) {
	            //print(" * %s:%s",pl.tagName(),pl.text());
	            Write(pl.text(),page);
                Write(";",page);
	            break;
	    }//其中含有项目地点
	    
	    //print("\nintro: (%d)", intro.size());
	    for (Element in : intro) {
	            //print(" * %s:%s",in.tagName(),in.text());
	            Write(in.text().substring(5),page);
                Write(";",page);
	            
	    }//其中含有个人简介
	    
	    //print("\nfinished: (%d)", finished.size());
	    for (Element fi : finished) {
	            //print(" * %s:%s",fi.tagName(),fi.text());
	            Write(fi.text(),page);
                Write(";",page);
	           
	    }//其中含有进度
	    
	    //print("\nmoney: (%d)", money.size());
	    for (Element mo : money) {
	            //print(" * %s:%s",mo.tagName(),mo.text());
	            Write(mo.text(),page);
                Write(";",page);
	           
	    }//其中含有筹资情况
	    
	    //print("\nbar: (%d)", bar.size());
	    for (Element ba : bar) {
	            //print(" * %s:%s",ba.tagName(),ba.text());
	            Write(tempString.substring(39),paged1);
	            Write(";",paged1);
	            Write(ba.text(),paged1);
                Write(line,paged1);
	           
	    }//其中含有筹资情况
	    
	    //print("\nbarinfo: (%d)", barinfo.size());
	    for (Element bari : barinfo) {
	            //print(" * %s:%s",bari.tagName(),bari.text());
	            Write(tempString.substring(39),paged2);
	            Write(";",paged2);
	            Write(bari.text(),paged2);
                Write(line,paged2);
	           
	    }//其中含有筹资情况
	    Write(line,page);
		
	}
	private static String trim(String s, int width) {
		// 截取字符串
		 if (s.length() > width)
	            return s.substring(0, width-1) + ".";
	        else
	            return s;
	}

	private static void print(String msg, Object... args) {
		// TODO Auto-generated method stub
		System.out.println(String.format(msg, args));
	}
	
	private static void Write(String a,String b) {
		// TODO Auto-generated method stub

		 try{
		      File file =new File(b);

		      //if file doesnt exists, then create it
		      if(!file.exists()){
		       file.createNewFile();
		      }

		      //true = append file
		      FileWriter fileWritter = new FileWriter(file.getName(),true);
		             BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
		             bufferWritter.write(a);
		             bufferWritter.close();

		         System.out.println("Done");

		     }catch(IOException e){
		      e.printStackTrace();
		     }
		
	}


}
