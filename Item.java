public class Item
{
	private String itemName;
	
	public Item(String name)
	{
		itemName = name;
	}
	
	public Item()
	{
		itemName = "item";
	}
	
	public String toString()
	{
		return itemName;
	}
	
	public String getName()
	{
		return itemName;
	}
	
	public boolean equals(Item e)
	{
		if(e != null)	
			return itemName.equals(e.getName());
		else
			return false;
	}
	
	public String useString()
	{
		return "You use the " + itemName + ".";
	}
}