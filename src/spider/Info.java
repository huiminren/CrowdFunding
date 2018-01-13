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
		//Write("��ַID�����⣻�������ݣ������ˣ���Ŀ�ص㣻���˼�飻����ɣ��������������ʽ��\n","FIdataamid.txt");
		//Write("��ַID��״̬��Ҫ\n","UNSUREamid1.txt");
		//Write("��ַID��״̬������Ϣ\n","UNSUREamid2.txt");
		//Write("��ַID�����Ͷ�Ӧ����\n","UNSUREamis1.txt");
		//Write("��ַID���ڳ�����\n","UNSUREamis2.txt");
		//Write("��ַID�����˷ѣ��ر�ʱ�����Ϣ\n","UNSUREamis3.txt");
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            //System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
            while ((tempString = reader.readLine()) != null) {
                // ��ʾ�к�
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
        Document doc = Jsoup.connect(tempString).get();//�������ӣ�ȡ�ò�����HTML�ļ�
        Elements hsb=doc.select(".project-right h2 a");//��һ�д���
        Elements hsb2=doc.select(".p-color");//�ڶ�������
        Elements ormoney=doc.select(".zcz_money span");//���ʽ�������
        Elements content=doc.select(".s_c >p");//��������
        Elements fix=doc.select(".span-image");//���˷ѵ�
        
        
        
        Elements name=doc.select(".name");//������
        Elements place=doc.select(".p-c span");//��Ŀ�ص㣡��������������������������������������������������������������������������������������������������������������������
        Elements intro=doc.getElementsByClass("p-pb");//���˼��
        Elements finished=doc.select(".bfb_ts span");//����ɣ�ʣ��
//        Elements money=doc.select(".czmb");//���ʽ��
        Elements money=doc.select(".fr");//���ʽ�����������������������������������������������������������������������������������������������������������������������������������
        Elements bar=doc.select(".particulars li h4");//���ʽ���
        Elements barinfo=doc.select(".particulars-right-bottom li p");//���ʽ�����Ϣ
        //Elements more=doc.select(".s_c");
        //Elements fix=doc.getElementsByClass("span-image clear-fix");
   
        
        Write(tempString.substring(39),page);
        Write(";",page);
        print("\nhsb: (%d)", hsb.size());
        
        
        for (Element h2 : hsb) {
                //print(" * %s:%s",h2.tagName(),h2.text());
                Write(h2.text(),page);
                Write(";",page);
        }//���к��е�һ��
        
	
		//print("\nhsb2: (%d)", hsb2.size());
	    for (Element hs2 : hsb2) {
	            //print(" * %s:%s",hs2.tagName(),hs2.text());
	            Write(hs2.text(),page);
                Write(";",page);
	    }//���к��еڶ���
	    
	    //print("\norm: (%d)", ormoney.size());
	    for (Element orm : ormoney) {
	            //print(" * %s:%s",orm.tagName(),orm.text());
	            Write(tempString.substring(39),pages1);
	            Write(";",pages1);
	            Write(orm.text(),pages1);
                Write(line,pages1);
	    }//���к��г��ʽ��
	    
	    //print("\ncontent: (%d)", content.size());
	    for (Element con : content) {
	            //print(" * %s:%s",con.tagName(),con.text());
	            Write(tempString.substring(39),pages2);
	            Write(";",pages2);
	            Write(con.text(),pages2);
                Write(line,pages2);
                
	    }//���к��о�����Ϣ
	    
	    //print("\nfix: (%d)", fix.size());
	    for (Element fi : fix) {
	            //print(" * %s:%s",fi.tagName(),fi.text());
	            Write(tempString.substring(39),pages3);
	            Write(";",pages3);
	            Write(fi.text(),pages3);
                Write(line,pages3);
	    }//���к������˷ѵ�
	    
	    
	    
	    
	    
	    //print("\nname: (%d)", name.size());
	    for (Element fqr : name) {
	            //print(" * %s:%s",fqr.tagName(),fqr.text());
	            Write(fqr.text(),page);
                Write(";",page);
	    }//���к��з���������
	    
	    //print("\nplace: (%d)", place.size());
	    for (Element pl : place) {
	            //print(" * %s:%s",pl.tagName(),pl.text());
	            Write(pl.text(),page);
                Write(";",page);
	            break;
	    }//���к�����Ŀ�ص�
	    
	    //print("\nintro: (%d)", intro.size());
	    for (Element in : intro) {
	            //print(" * %s:%s",in.tagName(),in.text());
	            Write(in.text().substring(5),page);
                Write(";",page);
	            
	    }//���к��и��˼��
	    
	    //print("\nfinished: (%d)", finished.size());
	    for (Element fi : finished) {
	            //print(" * %s:%s",fi.tagName(),fi.text());
	            Write(fi.text(),page);
                Write(";",page);
	           
	    }//���к��н���
	    
	    //print("\nmoney: (%d)", money.size());
	    for (Element mo : money) {
	            //print(" * %s:%s",mo.tagName(),mo.text());
	            Write(mo.text(),page);
                Write(";",page);
	           
	    }//���к��г������
	    
	    //print("\nbar: (%d)", bar.size());
	    for (Element ba : bar) {
	            //print(" * %s:%s",ba.tagName(),ba.text());
	            Write(tempString.substring(39),paged1);
	            Write(";",paged1);
	            Write(ba.text(),paged1);
                Write(line,paged1);
	           
	    }//���к��г������
	    
	    //print("\nbarinfo: (%d)", barinfo.size());
	    for (Element bari : barinfo) {
	            //print(" * %s:%s",bari.tagName(),bari.text());
	            Write(tempString.substring(39),paged2);
	            Write(";",paged2);
	            Write(bari.text(),paged2);
                Write(line,paged2);
	           
	    }//���к��г������
	    Write(line,page);
		
	}
	private static String trim(String s, int width) {
		// ��ȡ�ַ���
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
