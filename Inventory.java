import java.util.ArrayList;
import java.util.List;

public class Inventory
{
	private List<Item> itemList;
	
	public Inventory()
	{
		itemList = new ArrayList<Item>();
	}
	
	public Inventory(List<Item> list)
	{
		itemList = list;
	}
	
	public void addItem(Item e)
	{
		itemList.add(e);
	}
	
	public Item retrieveItem(Item e)
	{
		for(Item i : itemList)
			if(i.equals(e))
				return i;
		return null;
	}
	
	public Item retrieveItem(String s)
	{
		for(Item i : itemList)
			if(i.getName().equalsIgnoreCase(s))
				return i;
		return null;
	}
	
	public Item retrieveItem()
	{
		if(isEmpty())
			return null;
		else
			return itemList.get(0);
	}
	
	public Item removeItem(Item e)
	{
		for(int i = 0; i < itemList.size(); i++)
			if(itemList.get(i).equals(e))
				return itemList.remove(i);
		return null;
	}
	
	public Item removeItem(String s)
	{
		for(int i = 0; i < itemList.size(); i++)
			if(itemList.get(i).getName().equalsIgnoreCase(s))
				return itemList.remove(i);
		return null;
	}
	
	public Item removeItem()
	{
		if(isEmpty())
			return null;
		else
			return itemList.remove(0);
	}
	
	public void reset()
	{
		for(int i = 0; i < itemList.size(); i++)
			itemList.remove(i);
	}
	
	public String toString()
	{
		return itemList.toString();
	}
	
	public boolean isEmpty()
	{
		return itemList.size() == 0;
	}
}