public class Entity
{
	private Position p;
	private int hp;
	private int maxHp;
	private String name;
	
	public Entity()
	{
		p = new Position(0, 0);
		hp = 100;
		maxHp = 100;
		name = "Entity";
	}
	
	public Entity(String name)
	{
		p = new Position(0, 0);
		hp = 100;
		maxHp = 100;
		this.name = name;
	}
	
	public Entity(Position p)
	{
		this.p = p;
		hp = 100;
		maxHp = 100;
		name = "Entity";
	}
	
	public Entity(Position p, String name)
	{
		this.p = p;
		hp = 100;
		maxHp = 100;
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void addHp()
	{
		if(hp < maxHp)
			hp++;
	}
	
	public void addHp(int num)
	{
		hp += num;
		if(hp > maxHp)
			hp = maxHp;
	}
	
	public void removeHp(int num)
	{
		hp -= num;
	}
	
	public int getHp()
	{
		return hp;
	}
	
	public int getMaxHp()
	{
		return maxHp;
	}
	
	public void setMaxHp(int num)
	{
		maxHp = num;
	}
	
	public void setHp(int num)
	{
		hp = num;
	}
	
	public Position getPosition()
	{
		return p;
	}
	
	public void setPosition(Position p)
	{
		this.p = p;
	}
	
	public void setPosition(int r, int c) 
	{
		p = new Position(r, c);
	}
	
	public void moveN()
	{
		p.moveR(-1);
	}
	
	public void moveE()
	{
		p.moveC(1);
	}
	
	public void moveS()
	{
		p.moveR(1);
	}
	
	public void moveW()
	{
		p.moveC(-1);
	}
	
	public void attack(Entity enemy)
	{
		if(Math.random() < getMissChance())
		{
			System.out.println("You missed!");
		}
		else
		{
			int dmg = 0;
			if(Math.random() < getCritChance())
			{
				System.out.println("You hit a critical!");
				dmg = enemy.takeDamage(getDamageValue() * 2);
			}
			else
				dmg = enemy.takeDamage(getDamageValue());
			System.out.println("You did " + dmg + " damage!");
		}
	}
	
	public int takeDamage(int damageValue)
	{
		int dmg = damageValue - getArmorValue();
		if(dmg < 0)
			dmg = 0;
		removeHp(dmg);
		return dmg;
	}
	
	public int getDamageValue()
	{
		return 0;
	}
	
	public int getArmorValue()
	{
		return 0;
	}
	
	public double getCritChance()
	{
		return .05;
	}
	
	public double getMissChance()
	{
		return .05;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public boolean isDead()
	{
		return getHp() <= 0;
	}
}