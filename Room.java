public class Room
{
	private String description;
	private boolean[] doors; // true if has a door in direction: N, E, S, W
	private Inventory items;
	private boolean visited;
	
	public Room() // constructs an empty room with no doors
	{
		description = "A room.";
		doors = new boolean[4];
		for(int i = 0; i < doors.length; i++)
			doors[i] = false;
		items = new Inventory();
		visited = false;
	}
	
	public Room(String description)
	{
		this.description = description;
		doors = new boolean[4];
		for(int i = 0; i < doors.length; i++)
			doors[i] = false;
		items = new Inventory();
		visited = false;
	}
	
	public String getDescription()
	{
		return description;
	}
	
	public void setDescription(String desc)
	{
		description = desc;
	}
	
	public void print()
	{
		System.out.println(description);
		System.out.print("Doors: ");
		if(hasDoorN())
		{
			System.out.print("N");
			if(hasDoorE() || hasDoorS() || hasDoorW())
				System.out.print(", ");
		}
		if(hasDoorE())
		{
			System.out.print("E");
			if(hasDoorS() || hasDoorW())
				System.out.print(", ");
		}
		if(hasDoorS())
		{
			System.out.print("S");
			if(hasDoorW())
				System.out.print(", ");
		}
		if(hasDoorW())
			System.out.print("W");
		System.out.println();
		if(!items.isEmpty())
		{
			System.out.println("Items: " + items);
		}
	}
	
	public boolean hasDoorN()
	{
		return doors[0];
	}
	
	public boolean hasDoorE()
	{
		return doors[1];
	}
	
	public boolean hasDoorS()
	{
		return doors[2];
	}
	
	public boolean hasDoorW()
	{
		return doors[3];
	}
	
	/**
	 * Removes the door from the room in direction i (makes doors[i] false)
	 * i should be 0, 1, 2, or 3
	 * @param i the direction of the door (0 north, 1 east, 2 south, 3 west)
	 */
	public void removeDoor(int i)
	{
		if(i >= 0 && i < doors.length)
			doors[i] = false;
	}
	
	/**
	 * Adds door to the room in direction i (makes doors[i] true)
	 * i should be 0, 1, 2, or 3
	 * @param i the direction of the door (0 north, 1 east, 2 south, 3 west)
	 */
	public void addDoor(int i)
	{
		if(i >= 0 && i < doors.length)
			doors[i] = true;
	}
	
	public Inventory getInventory()
	{
		return items;
	}
	
	public boolean isVisited()
	{
		return visited;
	}
	
	public void markVisited()
	{
		visited = true;
	}
	
	public boolean isItemRoom()
	{
		return false;
	}
	
	public boolean isBossRoom()
	{
		return false;
	}
}