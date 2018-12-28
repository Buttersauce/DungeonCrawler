public class Armor extends Item
{
	private int armorValue;
	
	public Armor()
	{
		super("Armor");
		armorValue = 0;
	}
	
	public Armor(String s, int armor)
	{
		super(s);
		armorValue = armor;
	}
	
	public int getArmor()
	{
		return armorValue;
	}
}