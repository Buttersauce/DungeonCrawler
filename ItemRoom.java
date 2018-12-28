public class ItemRoom extends Room
{
	public ItemRoom()
	{
		super();
		setDescription("A shiny room.");
	}
	public boolean isItemRoom()
	{
		return true;
	}
}