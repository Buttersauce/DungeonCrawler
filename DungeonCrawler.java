import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Updates and todo list:
 * Dungeon generation updated with an algorithm that can look at how many rooms are adjacent. (tweak odds in addRoom)
 * Enemy generation is now handled in helper method, generateEnemies.
 * Enemies drop items randomly. Having this be a variable would be good so we can change the value between floors
 * Map added.
 * There are now 4 levels of floor difficulty. 
 * - To add more, change maxDifficulty and add to arrays in generateEnemy, generateBoss, generateItem, generateConsumable, generateDungeon, generateFloor
 * 
 * Todo: 
 * improved random generation. Currently all enemies/items on the list have equal probabilties. 
 * - A helper method to select from a list with varying probabilties would be good
 * Select from different pools of room descriptions based on difficulty.
 * Money/shop system. Stealing from shops like in Spelunky?
 * We could keep track of enemy and player corpses if you want. That would be fun.
 */

public class DungeonCrawler
{
	private final static boolean I_AM_DEBUGGING = false;
	private static List<Enemy> enemyList;
	private static Player player;
	private static Position startingPosition;
	private static Dungeon d;
	private static int maxDifficulty = 3;
	
	
	private static Enemy generateEnemy(int difficulty)
	{
		// public Enemy(String name, int lvl, int damage, int armor, int hp, int xp)
		final Enemy[][] enemies = {
				{new Enemy("Goblin", 1, 4, 1, 20, 20), new Enemy("Slime", 1, 2, 0, 10, 10), new Enemy("Zombie", 1, 3, 1, 15, 15)},
				{new Enemy("Goblin", 2, 6, 1, 25, 30), new Enemy("Zombie", 2, 4, 1, 20, 20), new Enemy("Spider", 3, 8, 2, 15, 25)},
				{new Enemy("Golem", 4, 10, 4, 30, 40), new Enemy("Skeleton", 5, 15, 2, 20, 35), new Enemy("Zombie", 4, 8, 1, 25, 30)},
				{new Enemy("Cacodemon", 8, 30, 6, 100, 60), new Enemy("Imp", 6, 20, 4, 30, 30), new Enemy("Zombie", 7, 15, 4, 25, 30), new Enemy("Hellknight", 8, 40, 6, 140, 70)}
				};
		Enemy e = enemies[difficulty][(int)(Math.random() * enemies[difficulty].length)];
		if(Math.random() < .25)
			e.makeHostile();
		return e;
	}
	private static void generateEnemies(int difficulty, int numOfEnemies, List<Position> roomPositions) 
	{
		int id = 0;
		for(int i = 0; i < numOfEnemies; i++)
		{
			enemyList.add(0, generateEnemy(difficulty));
			enemyList.get(0).setPosition(roomPositions.get((int)(Math.random() * roomPositions.size())));
			enemyList.get(0).setId(id);
			id++;
		}
	}
	
	private static void removeAllEnemies()
	{
		while(enemyList.size() > 0)
			enemyList.remove(0);
	}
	
	private static void generateBoss(int difficulty, Position bossRoomPosition)
	{
		final Enemy[][] bosses = {
				{new Enemy("King Slime", 2, 5, 2, 50, 100)},
				{new Enemy("Goblin Warchief", 4, 10, 2, 70, 200)},
				{new Enemy("Cyclops Giant", 7, 20, 3, 100, 300)},
				{new Enemy("Cyberdemon", 10, 40, 8, 100, 400)}
		};
		enemyList.add(0, bosses[difficulty][(int)(Math.random() * bosses[difficulty].length)]);
		enemyList.get(0).setPosition(bossRoomPosition);
	}
	
	private static boolean bossRoomIsEmpty()
	{
		boolean ans = true;
		for(Enemy e : enemyList)
		{
			if(e.getPosition().equals(d.getBossRoomPosition()))
			{
				ans = false;
			}
		}
		return ans;
	}
	
	private static Item generateItem(int difficulty)
	{
		final Item[][] items = {
				{new Weapon("wood sword", 2, .03), new Armor("wood armor", 2)},
                {new Weapon("iron sword", 4, .05), new Armor("iron armor", 3)},
                {new Weapon("mithril sword", 8, .07), new Armor("mithril armor", 6)},
                {new Weapon("shotgun", 15, .10), new Armor("power armor", 15)}
				};
		return items[difficulty][(int)(Math.random() * items[difficulty].length)];
	}
	
	private static Item generateConsumable(int difficulty)
	{
		final Item[][] items = {
				{new HealthItem("small health potion", 30)},
				{new HealthItem("health potion", 50)},
				{new HealthItem("healthkit", 100)},
				{new HealthItem("super healthkit", 200)}
				};
		return items[difficulty][(int)(Math.random() * items[difficulty].length)];
	}
	
	// returns a generated dungeon and moves player to starting room
	private static Dungeon generateDungeon(int difficulty)
	{
		final int[][] sizes = {{10, 10}, {15, 15}, {15, 15}, {20, 20}}; // if we use a 2D array we don't need to use if statements for difficulty
		final int[] numIterations = {15, 20, 30, 40};
		Dungeon d = new Dungeon(sizes[difficulty][0], sizes[difficulty][1]);
		d.resetDungeon();
		int startingR = sizes[difficulty][0] / 2;
		int startingC = sizes[difficulty][1] / 2;
		d.setRoom(generateRoom(), startingR, startingC);
		startingPosition = new Position(startingR, startingC);
		player.setPosition(startingPosition);
		for(int i = 0; i < numIterations[difficulty]; i++)
		{
			iterateDungeon(d);
		}
		d.addItemRoom();
		d.addBossRoom();
		fixDoors(d);
		for(int r = 0; r < d.getRows(); r++)
		{
			for(int c = 0; c < d.getCols(); c++)
			{
				if(d.getRoom(r, c) != null)
				{
					if(d.getRoom(r, c).isItemRoom()) // add item to item room
					{
						d.getRoom(r, c).getInventory().addItem(generateItem(difficulty));
					}
				}
			}
		}
		for(int r = 0; r < d.getRows(); r++)
		{
			for(int c = 0; c < d.getCols(); c++)
			{
				if(d.getRoom(r, c) != null)
				{
					if(Math.random() < .15 && !d.getRoom(r, c).isItemRoom() && !d.getRoom(r, c).isBossRoom()) // 15% chance to spawn consumable in non-item/boss room
					{
						d.getRoom(r, c).getInventory().addItem(generateConsumable(difficulty));
					}
				}
			}
		}
		return d;
	}
	
	// check every room that is not null. random chance to generate a room next to it.
	private static void iterateDungeon(Dungeon d)
	{
		for(int r = 1; r < d.getRows() - 1; r++)
		{
			for(int c = 1; c < d.getCols() - 1; c++)
			{
				if(d.getRoom(r, c) != null)
				{
					addRoom(d, r, c);
				}
			}
		}
	}
	
	private static void addRoom(Dungeon d, int r, int c)
	{
		int count = d.numOfAdjacentRooms(r, c);
		if(count == 0) // starting room
		{
			if(Math.random() < .5) // 50% chance for 4 adjacent rooms
			{
				d.setRoom(generateRoom(), r + 1, c);
				d.setRoom(generateRoom(), r - 1, c);
				d.setRoom(generateRoom(), r, c + 1);
				d.setRoom(generateRoom(), r, c - 1);
			}
			else // 50% chance to add one room. room is then treated as 1 adjacent room for subsequent iterations
			{
				if(Math.random() < .5)
				{
					if(Math.random() < .5)
						d.setRoom(generateRoom(), r + 1, c);
					else
						d.setRoom(generateRoom(), r - 1, c);
				}
				else
				{
					if(Math.random() < .5)
						d.setRoom(generateRoom(), r, c + 1);
					else
						d.setRoom(generateRoom(), r, c - 1);
				}
			}
		}
		else if(count == 1) // 1 adjacent room
		{
			if(Math.random() < .80) // 80% chance to add a room
			{
				if(Math.random() < .5) // above or below
				{
					if(Math.random() < .5 && d.getRoom(r + 1, c) == null)
						d.setRoom(generateRoom(), r + 1, c);
					else if(d.getRoom(r - 1, c) == null)
						d.setRoom(generateRoom(), r - 1, c);
				}
				else // left or right
				{
					if(Math.random() < .5 && d.getRoom(r, c + 1) == null)
						d.setRoom(generateRoom(), r, c + 1);
					else if(d.getRoom(r, c - 1) == null)
						d.setRoom(generateRoom(), r, c - 1);
				}
			}
		}
		else if(count == 2) // 2 adjacent rooms
		{
			if(Math.random() < .05) // 5% chance to attempt to add a room
			{
				if(Math.random() < .5) // above or below
				{
					if(Math.random() < .5 && d.getRoom(r + 1, c) == null)
						d.setRoom(generateRoom(), r + 1, c);
					else if(d.getRoom(r - 1, c) == null)
						d.setRoom(generateRoom(), r - 1, c);
				}
				else // left or right
				{
					if(Math.random() < .5 && d.getRoom(r, c + 1) == null)
						d.setRoom(generateRoom(), r, c + 1);
					else if(d.getRoom(r, c - 1) == null)
						d.setRoom(generateRoom(), r, c - 1);
				}
			}
		}
		else if(count == 3) // 3 adjacent rooms
		{
			if(Math.random() < .02) // 2% chance to attempt to add a room
			{
				if(Math.random() < .5) // above or below
				{
					if(Math.random() < .5 && d.getRoom(r + 1, c) == null)
						d.setRoom(generateRoom(), r + 1, c);
					else if(d.getRoom(r - 1, c) == null)
						d.setRoom(generateRoom(), r - 1, c);
				}
				else // left or right
				{
					if(Math.random() < .5 && d.getRoom(r, c + 1) == null)
						d.setRoom(generateRoom(), r, c + 1);
					else if(d.getRoom(r, c - 1) == null)
						d.setRoom(generateRoom(), r, c - 1);
				}
			}
		}
	}
	
	private static Room generateRoom()
	{
		Room rm = new Room();
		return rm;
	}
	
	// adds doors to rooms with rooms next to them
	private static void fixDoors(Dungeon d)
	{
		for(int r = 0; r < d.getRows(); r++)
		{
			for(int c = 0; c < d.getCols(); c++)
			{
				Room rm = d.getRoom(r, c);
				if(rm != null)
				{
					int sR = r + 1;
					int nR = r - 1;
					int eC = c + 1;
					int wC = c - 1;
					if(sR >= 0 && sR < d.getRows() && d.getRoom(sR, c) != null)
					{
						rm.addDoor(2);
					}
					if(nR >= 0 && nR < d.getRows() && d.getRoom(nR, c) != null)
					{
						rm.addDoor(0);
					}
					if(eC >= 0 && eC < d.getRows() && d.getRoom(r, eC) != null)
					{
						rm.addDoor(1);
					}
					if(wC >= 0 && wC < d.getRows() && d.getRoom(r, wC) != null)
					{
						rm.addDoor(3);
					}
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		final String NO_DOOR_MESSAGE = "Can't go that way.";

		Scanner input = new Scanner(System.in);
		
		String q = "";
		player = new Player();
		enemyList = new ArrayList<Enemy>(); // list of all living enemies
		int difficulty = 0;
		generateFloor(difficulty); // generate dungeon. This could be done again in the loop to change floors
		if(I_AM_DEBUGGING)
		{
			d.revealAll();
		}
		boolean firstLoop = true;
		
		while(!q.equalsIgnoreCase("quit")) // game loop
		{
			if(firstLoop)
			{
				firstLoop = false;
				System.out.print("Enter name: ");
				q = input.nextLine();
				player.setName(q);
				if(!q.equalsIgnoreCase("quit"))
					System.out.println("Type \"help\" for a list of commands.");
			}
			else
			{
				Room currentRoom = d.getRoom(player.getPosition());
				currentRoom.markVisited();
				d.printMap(player.getPosition());
				currentRoom.print();
				if(player.getPosition().equals(d.getBossRoomPosition()) && bossRoomIsEmpty())
				{
					if(difficulty == maxDifficulty)
					{
						System.out.println("You win!");
						break;
					}
					System.out.println("You see a trapdoor leading down to the next floor.");
				}
				System.out.println("HP: " + player.getHp() + "/" + player.getMaxHp());
				List<Enemy> enemiesInRoom = new ArrayList<Enemy>();
				for(Enemy e : enemyList)
				{
					if(e.getPosition().equals(player.getPosition()))
						enemiesInRoom.add(e);
				}
				if(enemiesInRoom.size() > 0)
				{
					System.out.println("Enemies in room: " + enemiesInRoom);
				}
				
				if(I_AM_DEBUGGING)
				{
					System.out.println(player.getPosition());
					player.print();
					System.out.println(enemyList);
					for(Enemy e : enemyList)
					{
						System.out.println(e.isHostile());
					}
				}
				q = input.nextLine();
				if(q.equalsIgnoreCase("help"))
				{
					System.out.println("Available commands: move [direction], take [item], drop [item], inventory, attack [number], equip [item], use [item], descend, help, quit");
				}
				else if(q.length() >= 5 && q.substring(0, 5).equalsIgnoreCase("move "))
				{
					q = q.substring(5);
					if(q.equalsIgnoreCase("N") || q.equalsIgnoreCase("north"))
					{
						if(currentRoom.hasDoorN())
						{
							player.moveN();
						}
						else
						{
							System.out.println(NO_DOOR_MESSAGE);
						}
					}
					else if(q.equalsIgnoreCase("E") || q.equalsIgnoreCase("east"))
					{
						if(currentRoom.hasDoorE())
						{
							player.moveE();
						}
						else
						{
							System.out.println(NO_DOOR_MESSAGE);
						}
					}
					else if(q.equalsIgnoreCase("S") || q.equalsIgnoreCase("south"))
					{
						if(currentRoom.hasDoorS())
						{
							player.moveS();
						}
						else
						{
							System.out.println(NO_DOOR_MESSAGE);
						}
					}
					else if(q.equalsIgnoreCase("W") || q.equalsIgnoreCase("west"))
					{
						if(currentRoom.hasDoorW())
						{
							player.moveW();
						}
						else
						{
							System.out.println(NO_DOOR_MESSAGE);
						}
					}
				}
				else if(q.length() >= 5 && q.substring(0, 5).equalsIgnoreCase("take "))
				{
					q = q.substring(5);
					if(currentRoom.getInventory().retrieveItem(q) != null)
					{
						player.getInventory().addItem(currentRoom.getInventory().removeItem(q));
						System.out.println("You pick up the " + q + ".");
					}
					else
					{
						System.out.println("Can't find " + q + ".");
					}
				}
				else if(q.equalsIgnoreCase("take"))
				{
					if(currentRoom.getInventory().retrieveItem() != null)
					{
						Item temp = currentRoom.getInventory().removeItem();
						player.getInventory().addItem(temp);
						System.out.println("You pick up the " + temp + ".");
					}
					else
					{
						System.out.println("There is nothing to take.");
					}
				}
				else if(q.length() >= 5 && q.substring(0, 5).equalsIgnoreCase("drop "))
				{
					q = q.substring(5);
					if(player.getInventory().retrieveItem(q) != null)
					{
						currentRoom.getInventory().addItem(player.getInventory().removeItem(q));
						System.out.println("You drop the " + q + ".");
					}
					else
					{
						System.out.println("You don't have " + q + ".");
					}
				}
				else if(q.equalsIgnoreCase("inventory"))
				{
					player.print();
				}
				else if(q.length() >= 6 && q.substring(0, 6).equalsIgnoreCase("attack"))
				{
					q = q.substring(6);
					if(enemiesInRoom.size() > 0)
					{
						if(enemiesInRoom.size() > 1)
						{
							if(q.length() >= 2)
							{
								try
								{
									int s = Integer.parseInt(q.substring(1));
									if(s >= 0 && s < enemiesInRoom.size())
									{
										player.attack(enemiesInRoom.get(s));
									}
								}	
								catch(NumberFormatException e)
								{
									player.attack(enemiesInRoom.get(0));
								}
							}
							else
							{
								player.attack(enemiesInRoom.get(0));
							}
						}
						else
						{
							player.attack(enemiesInRoom.get(0));
						}
						for(Enemy e : enemiesInRoom)
						{
							e.makeHostile();
						}
					}
				}
				else if(q.length() >= 6 && q.substring(0, 6).equalsIgnoreCase("equip "))
				{
					player.equip(player.getInventory().retrieveItem(q.substring(6)));
				}
				else if(q.length() >= 4 && q.substring(0, 4).equalsIgnoreCase("use "))
				{
					q = q.substring(4);
					player.use(q);
				}
				else if(q.equalsIgnoreCase("descend"))
				{
					if(player.getPosition().equals(d.getBossRoomPosition()) && bossRoomIsEmpty())
					{
						difficulty++;
						if(difficulty > maxDifficulty)
							difficulty = maxDifficulty;
						removeAllEnemies();
						generateFloor(difficulty);
					}
				}
			
				for(int i = 0; i < enemyList.size(); i++)
				{
					if(enemyList.get(i).isDead())
					{
						System.out.println("You killed " + enemyList.get(i).getName() + ".");
						player.addXp(enemyList.get(i).getXp());
						for(int j = 0; j < enemiesInRoom.size(); j++)
						{
							if(enemyList.get(i).getId() == enemiesInRoom.get(j).getId())
								enemiesInRoom.remove(j);
						}
						enemyList.remove(i);
						if(Math.random() < .15)
						{
							if(Math.random() < .15)
								d.getRoom(player.getPosition()).getInventory().addItem(generateItem(difficulty));
							else
								d.getRoom(player.getPosition()).getInventory().addItem(generateConsumable(difficulty));
						}
						i--;
					}
				}
				
				for(Enemy e : enemiesInRoom)
				{
					if(e.isHostile())
					{
						e.attack(player);
					}
					// e.increaseTurns();
				}

				if(player.isDead())
				{
					System.out.println("You are dead!");
					player.reset();
					difficulty = 0;
					removeAllEnemies();
					generateFloor(difficulty);
				}
				if(player.canLevelUp())
				{
					player.levelUp();
					System.out.println("Level up!");
				}
			}
			System.out.println();
			if(I_AM_DEBUGGING)
			{
				System.out.println("End of loop");
			}
		}
		input.close();
		
	}
	
	private static void generateFloor(int difficulty)
	{
		int[] numEnemies = {15, 20, 30, 50};
		d = generateDungeon(difficulty);
		generateEnemies(difficulty, numEnemies[difficulty], d.roomPositionsNotSpecial());
		generateBoss(difficulty, d.getBossRoomPosition());
	}

}