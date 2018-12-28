public class Weapon extends Item
{
	private int damageValue;
    private double critChance;
	
	public Weapon()
	{
		super("Weapon");
		damageValue = 0;
		critChance = .05;
	}
	
	public Weapon(String s, int damage)
	{
		super(s);
		damageValue = damage;
		critChance = .05;
	}
	
    public Weapon(String s, int damage, double criticalChance)
    {
        super(s);
        damageValue = damage;
        critChance = criticalChance;
    }
	
	public int getDamage()
	{
		return damageValue;
	}
	
	public double getCritChance()
    {
        return critChance;
    }
}