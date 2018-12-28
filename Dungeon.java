import java.util.ArrayList;
import java.util.List;

public class Dungeon
{
	private Room[][] rooms;
	
	public Dungeon(int rows, int columns)
	{
		rooms = new Room[rows][columns];
		for(int r = 0; r < rooms.length; r++)
		{
			for(int c = 0; c < rooms[r].length; c++)
			{
				rooms[r][c] = new Room();
				if(r == 0)
					rooms[r][c].removeDoor(0);
				if(c == 0)
					rooms[r][c].removeDoor(3);
				if(r == rooms.length - 1)
					rooms[r][c].removeDoor(2);
				if(c == rooms[r].length - 1)
					rooms[r][c].removeDoor(1);
			}
		}
	}
	
	/**
	 * sets all rooms to null
	*/
	public void resetDungeon()
	{
		for(int r = 0; r < rooms.length; r++)
		{
			for(int c = 0; c < rooms[r].length; c++)
			{
				rooms[r][c] = null;
			}
		}
	}
	
	public Room getRoom(int r, int c)
	{
		if(r >= 0 && r < getRows() && c >= 0 && c < getCols())
			return rooms[r][c];
		else
			return null;
	}
	
	public Room getRoom(Position p)
	{
		return rooms[p.getR()][p.getC()];
	}
	
	public void setRoom(Room room, int r, int c)
	{
		rooms[r][c] = room;
	}
	
	private void setRoom(Room room, Position p) 
	{
		setRoom(room, p.getR(), p.getC());
	}
	
	public int getRows()
	{
		return rooms.length;
	}
	
	public int getCols()
	{
		return rooms[0].length;
	}
	
	public List<Position> roomPositions()
	{
		List<Position> ans = new ArrayList<Position>();
		for(int r = 0; r < rooms.length; r++)
		{
			for(int c = 0; c < rooms[r].length; c++)
			{
				if(rooms[r][c] != null)
				{
					ans.add(new Position(r, c));
				}
			}
		}
		return ans;
	}
	
	public List<Position> roomPositionsNotSpecial()
	{
		List<Position> ans = new ArrayList<Position>();
		for(int r = 0; r < rooms.length; r++)
		{
			for(int c = 0; c < rooms[r].length; c++)
			{
				if(rooms[r][c] != null && !rooms[r][c].isItemRoom() && !rooms[r][c].isBossRoom())
				{
					ans.add(new Position(r, c));
				}
			}
		}
		return ans;
	}
	
	public int numOfAdjacentRooms(int r, int c)
	{
		Room rm = getRoom(r, c);
		int count = 0;
		if(rm != null)
		{
			int sR = r + 1;
			int nR = r - 1;
			int eC = c + 1;
			int wC = c - 1;
			if(sR >= 0 && sR < getRows() && getRoom(sR, c) != null)
			{
				count++;
			}
			if(nR >= 0 && nR < getRows() && getRoom(nR, c) != null)
			{
				count++;
			}
			if(eC >= 0 && eC < getRows() && getRoom(r, eC) != null)
			{
				count++;
			}
			if(wC >= 0 && wC < getRows() && getRoom(r, wC) != null)
			{
				count++;
			}
		}
		return count;
	}
	
	public int numOfAdjacentRooms(Position p)
	{
		return numOfAdjacentRooms(p.getR(), p.getC());
	}
	
	public void printMap(Position playerPosition)
	{
		char[][] map = new char[getRows()][getCols()];
		for(int r = 0; r < map.length; r++)
			for(int c = 0; c < map[0].length; c++)
			{
				Room rm = getRoom(r, c);
				if(rm == null)
				{
					map[r][c] = '-';
				}
				else if(new Position(r, c).equals(playerPosition))
				{
					map[r][c] = '*';
				}
				else if(rm.isVisited())
				{
					if(rm.isItemRoom())
						map[r][c] = '#';
					else if(rm.isBossRoom())
						map[r][c] = '!';
					else
						map[r][c] = '.';
				}
				else
				{
					int sR = r + 1;
					int nR = r - 1;
					int eC = c + 1;
					int wC = c - 1;
					Room southRoom = getRoom(sR, c);
					Room northRoom = getRoom(nR, c);
					Room eastRoom = getRoom(r, eC);
					Room westRoom = getRoom(r, wC);
					if((southRoom != null && southRoom.isVisited()) 
						|| (northRoom != null && northRoom.isVisited())
						|| (eastRoom != null && eastRoom.isVisited()
						|| (westRoom != null && westRoom.isVisited())))
					{
						map[r][c] = '?';
					}
					else
					{
						map[r][c] = '-';
					}
				}
			}
		for(int r = 0; r < map.length; r++)
		{
			for(int c = 0; c < map[0].length; c++)
				System.out.print(map[r][c]);
			System.out.println();
		}
	}
	
	public void revealAll()
	{
		for(int r = 0; r < rooms.length; r++)
		{
			for(int c = 0; c < rooms[r].length; c++)
			{
				if(rooms[r][c] != null)
				{
					rooms[r][c].markVisited();
				}
			}
		}
	}
	
	public boolean addItemRoom()
	{
		List<Position> roomList = roomPositions();
		boolean placed = false;
		for(int i = 0; i < roomList.size(); i++) // shuffle positions
		{
			Position temp = roomList.get(i);
			int rand = (int)(Math.random() * roomList.size());
			roomList.set(i, roomList.get(rand));
			roomList.set(rand, temp);
		}
		for(int i = 0; i < roomList.size(); i++)
		{
			if(!placed && numOfAdjacentRooms(roomList.get(i)) == 1 && !getRoom(roomList.get(i)).isBossRoom())
			{
				setRoom(new ItemRoom(), roomList.get(i));
				placed = true;
			}
		}
		if(!placed)
		{
			for(int i = 0; i < roomList.size(); i++)
			{
				if(!placed && numOfAdjacentRooms(roomList.get(i)) == 2 && !getRoom(roomList.get(i)).isBossRoom())
				{
					setRoom(new ItemRoom(), roomList.get(i));
					placed = true;
				}
			}
			if(!placed)
			{
				for(int i = 0; i < roomList.size(); i++)
				{
					if(!placed && numOfAdjacentRooms(roomList.get(i)) == 3 && !getRoom(roomList.get(i)).isBossRoom())
					{
						setRoom(new ItemRoom(), roomList.get(i));
						placed = true;
					}
				}
			}
		}
		return placed;
	}

	public boolean addBossRoom()
	{
		List<Position> roomList = roomPositions();
		boolean placed = false;
		for(int i = 0; i < roomList.size(); i++) // shuffle positions
		{
			Position temp = roomList.get(i);
			int rand = (int)(Math.random() * roomList.size());
			roomList.set(i, roomList.get(rand));
			roomList.set(rand, temp);
		}
		for(int i = 0; i < roomList.size(); i++)
		{
			if(!placed && numOfAdjacentRooms(roomList.get(i)) == 1 && !getRoom(roomList.get(i)).isItemRoom())
			{
				setRoom(new BossRoom(), roomList.get(i));
				placed = true;
			}
		}
		if(!placed)
		{
			for(int i = 0; i < roomList.size(); i++)
			{
				if(!placed && numOfAdjacentRooms(roomList.get(i)) == 2 && !getRoom(roomList.get(i)).isItemRoom())
				{
					setRoom(new BossRoom(), roomList.get(i));
					placed = true;
				}
			}
			if(!placed)
			{
				for(int i = 0; i < roomList.size(); i++)
				{
					if(!placed && numOfAdjacentRooms(roomList.get(i)) == 3 && !getRoom(roomList.get(i)).isItemRoom())
					{
						setRoom(new BossRoom(), roomList.get(i));
						placed = true;
					}
				}
			}
		}
		return placed;
	}
	
	public Position getBossRoomPosition() // returns position of boss room. There should be only one, or none if not generated
	{
		List<Position> rooms = roomPositions();
		for(Position p : rooms)
		{
			if(getRoom(p).isBossRoom())
			{
				return p;
			}
		}
		return null;
	}
}