public class HealthItem extends Item
{
	private int healthValue;
	
	public HealthItem()
	{
		super();
		healthValue = 0;
	}
	
	public HealthItem(String name, int health)
	{
		super(name);
		healthValue = health;
	}
	
	public int getHealthValue()
	{
		return healthValue;
	}
}