/**
 * 
 */
package bean;

/**
 * @author Jona
 *
 */
public class DataBean {
	int amount=0;
	String[] url = new String[21];
	int[] score = new int[21];
	int[] rank = new int[21];
	
	
	public void setAmount(int i)
	{
		this.amount = i;
	}
	public void setUrl(int i,String u)
	{
		this.url[i] = u.trim();
	}
	public void setScore(int i,int s)
	{
		this.score[i] = s;
	}
	public void setRank(int i,int r)
	{
		this.rank[i] = r;
	}
	
	public int getAmount()
	{
		return this.amount;
	}
	public String getUrl(int i)
	{
		return this.url[i];
	}
	public int getScore(int i)
	{
		return this.score[i];
	}
	public int getRank(int i)
	{
		return this.rank[i];
	}

}
