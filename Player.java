public class Player extends Entity
{
	private Inventory inventory;
	private Item[] equipment;
	private int level;
	private int xp;
	
	public Player()
	{
		super();
		inventory = new Inventory();
		equipment = new Item[2]; // 0 weapon, 1 armor
		level = 1;
		xp = 0;
	}
	
	public Player(String name)
	{
		super(name);
		inventory = new Inventory();
		equipment = new Item[2]; // 0 weapon, 1 armor
		level = 1;
		xp = 0;
	}
	
	public Player(Position p)
	{
		super(p);
		inventory = new Inventory();
		equipment = new Item[2];
		level = 1;
		xp = 0;
	}
	
	public Player(Position p, String name)
	{
		super(p, name);
		inventory = new Inventory();
		equipment = new Item[2];
		level = 1;
		xp = 0;
	}
	
	public Inventory getInventory()
	{
		return inventory;
	}
	
	public void addXp(int num)
	{
		xp += num;
	}
	
	public boolean canLevelUp()
	{
		return xp >= getLevelUpXp(level);
	}
	
	public void levelUp()
	{
			xp -= getLevelUpXp(level);
			level++;
			setMaxHp((int)(getMaxHp() * 1.1));
			setHp(getMaxHp());
	}
	
	private int getLevelUpXp(int level) 
	{
		if(level <= 1)
			return 100;
		else
			return (int)(1.1 * getLevelUpXp(level - 1));
	}
	
	public void equip(String itemName)
	{
		Item temp = inventory.retrieveItem(itemName);
		if(temp != null)
		{
			if(temp.getClass().equals((new Weapon()).getClass()))
			{
				equipment[0] = inventory.removeItem(temp);
			}
			if(temp.getClass().equals(new Armor().getClass()))
			{
				equipment[1] = inventory.removeItem(temp);
			}
		}
	}
	
	public void equip(Item item)
	{
		Item temp = inventory.retrieveItem(item);
		if(temp != null)
		{
			if(temp.getClass().equals((new Weapon()).getClass()))
			{
				if(equipment[0] != null)
					inventory.addItem(equipment[0]);
				equipment[0] = inventory.removeItem(temp);
				System.out.println("Equipped " + equipment[0].getName() + ".");
			}
			if(temp.getClass().equals(new Armor().getClass()))
			{
				if(equipment[1] != null)
					inventory.addItem(equipment[1]);
				equipment[1] = inventory.removeItem(temp);
				System.out.println("Equipped " + equipment[1].getName() + ".");
			}
		}
	}
	
	public int getDamageValue()
	{
		int baseDamage = level + 3;
		if(equipment[0] != null)
		{
			return ((Weapon)equipment[0]).getDamage() + baseDamage;
		}
		else
			return baseDamage;
	}
	
	public int getArmorValue()
	{
		if(equipment[1] != null)
		{
			return ((Armor)equipment[1]).getArmor();
		}
		else
			return 0;
	}
	
	public void use(String itemName)
	{
		Item e = inventory.retrieveItem(itemName);
		if(e != null)
		{
			if(e.getClass().equals((new HealthItem()).getClass()))
			{
				addHp(((HealthItem)e).getHealthValue());
				inventory.removeItem(e);
				System.out.println("Used a " + itemName + ".");
			}
		}
	}
	
	public double getCritChance()
	{
		if(equipment[0] != null)
			return ((Weapon)equipment[0]).getCritChance();
		else
			return 0;
	}
	
	public double getMissChance()
    {
        return .05-.005*(level-1);
    }
	
	public void reset()
	{
		inventory.reset();
		for(int i = 0; i < equipment.length; i++)
			equipment[i] = null;
		level = 1;
		xp = 0;
		setHp(100);
		setMaxHp(100);
	}

	public void print()
	{
		System.out.println("Name: " + super.getName());
		System.out.println("Level: " + level);
		System.out.println("HP: " + getHp() + "/" + getMaxHp());
		System.out.println("XP: " + xp + "/" + getLevelUpXp(level));
		System.out.println("Equipment: " + equipment[0] + ", " + equipment[1]);
		System.out.println("Inventory: " + inventory);
		System.out.println("Damage: " + getDamageValue());
		System.out.println("Armor: " + getArmorValue());
	}
	
}