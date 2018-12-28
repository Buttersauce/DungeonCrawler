public class Position
{
	private int r;
	private int c;
	
	public Position()
	{
		r = 0;
		c = 0;
	}
	
	public Position(int r, int c)
	{
		this.r = r;
		this.c = c;
	}
	
	public int getR()
	{
		return r;
	}
	
	public int getC()
	{
		return c;
	}
	
	public String toString()
	{
		return "(" + r + ", " + c + ")";
	}
	
	public void moveR()
	{
		r++;
	}
	public void moveR(int num)
	{
		r += num;
	}
	
	public void moveC()
	{
		c++;
	}
	
	public void moveC(int num)
	{
		c += num;
	}
	
	public boolean equals(Position p)
	{
		return r == p.getR() && c == p.getC();
	}
}