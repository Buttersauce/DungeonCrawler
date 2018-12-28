public class BossRoom extends Room
{
	public BossRoom()
	{
		super();
		setDescription("A dangerous-looking room.");
	}
	
	public boolean isBossRoom()
	{
		return true;
	}
}