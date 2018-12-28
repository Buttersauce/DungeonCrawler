public class Enemy extends Entity
{
	private int level;
	private int damageValue;
	private int armorValue;
	private boolean hostile;
	private int turnsSinceAttacked;
	private int xpValue;
	private int id;
	private double critChance;
	private double missChance;
	
	public Enemy(String name, int lvl, int damage, int armor, int hp, int xp, double crit, double miss)
	{
		super(name);
		level = lvl;
		damageValue = damage;
		armorValue = armor;
		hostile = false;
		super.setMaxHp(hp);
		super.setHp(hp);
		turnsSinceAttacked = 0;
		xpValue = xp;
		critChance = crit;
		missChance = miss;
	}
	
	public Enemy(String name, int lvl, int damage, int armor, int hp, int xp, double crit)
	{
		super(name);
		level = lvl;
		damageValue = damage;
		armorValue = armor;
		hostile = false;
		super.setMaxHp(hp);
		super.setHp(hp);
		turnsSinceAttacked = 0;
		xpValue = xp;
		critChance = crit;
		missChance = .05;
	}
	
	public Enemy(String name, int lvl, int damage, int armor, int hp, int xp)
	{
		super(name);
		level = lvl;
		damageValue = damage;
		armorValue = armor;
		hostile = false;
		super.setMaxHp(hp);
		super.setHp(hp);
		turnsSinceAttacked = 0;
		xpValue = xp;
		critChance = .05;
		missChance = .05;
	}
	
	public Enemy(String name, int lvl, int damage, int armor, int hp, int r, int c, int xp)
	{
		super(name);
		level = lvl;
		damageValue = damage;
		armorValue = armor;
		hostile = false;
		super.setMaxHp(hp);
		super.setHp(hp);
		turnsSinceAttacked = 0;
		super.setPosition(new Position(r, c));
		xpValue = xp;
		critChance = .05;
		missChance = .05;
	}
	
	public Enemy(String name, int lvl, int damage, int armor, int xp)
	{
		super(name);
		level = lvl;
		damageValue = damage;
		armorValue = armor;
		hostile = false;
		turnsSinceAttacked = 0;
		xpValue = xp;
		critChance = .05;
		missChance = .05;
	}
	
	public Enemy()
	{
		super("Goblin");
		level = 1;
		damageValue = 1;
		armorValue = 0;
		hostile = false;
		turnsSinceAttacked = 0;
		xpValue = 0;
		critChance = .05;
		missChance = .05;
	}
	
	public void attack(Entity enemy)
	{
		if(Math.random() < getMissChance())
		{
			System.out.println(super.getName() + " missed!");
		}
		else
		{
			int dmg = 0;
			if(Math.random() < getCritChance())
			{
				System.out.println(super.getName() + " hit a critical!");
				
				dmg = enemy.takeDamage(getDamageValue() * 2);
			}
			else
				dmg = enemy.takeDamage(getDamageValue());
			System.out.println(super.getName() + " hit you for " + dmg + " damage!");
		}
	}
	
	
	public void makeHostile()
	{
		hostile = true;
	}
	
	public void removeHostile()
	{
		hostile = false;
	}
	
	public boolean isHostile()
	{
		return hostile;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getDamageValue()
	{
		return damageValue;
	}
	
	public void setDamageValue(int dmg)
	{
		damageValue = dmg;
	}
	
	public int getArmorValue()
	{
		return armorValue;
	}
	
	public void setArmorValue(int amr)
	{
		armorValue = amr;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}
	
	public void increaseTurns()
	{
		if(isHostile())
		{
			turnsSinceAttacked++;
			if(turnsSinceAttacked > 5)
			{
				removeHostile();
			}
		}
	}
	
	public int takeDamage(int damageValue)
	{
		turnsSinceAttacked = 0;
		if(!isHostile())
		{
			makeHostile();
		}
		return super.takeDamage(damageValue);
	}
	
	public int getXp()
	{
		return xpValue;
	}
	
	public void setXp(int xp)
	{
		xpValue = xp;
	}
	
	public double getCritChance()
	{
		return critChance;
	}
	
	public double getMissChance()
	{
		return missChance;
	}
	
	public void print()
	{
		System.out.println("Level " + level + " " + super.getName());
		System.out.println("HP: " + super.getHp() + "/" + super.getMaxHp());
	}
	
	public String toString()
	{
		String ans = "";
		if(isHostile())
			ans += "(!)";
		ans += "Level " + level + " " + super.getName() + " (HP: " + super.getHp() + "/" + super.getMaxHp() + ")";
		return ans;
	}
	
	public boolean equals(Enemy e)
	{
		return this.id == e.getId();
	}

}
