
public class Argument
{
	
	private String name, type;
	private boolean isFinal;

	public Argument(String n, String t, boolean f)
	{
		name = n.trim();
		type = t.trim();
		isFinal = f;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public boolean isFinal()
	{
		return isFinal;
	}

	public void setFinal(boolean isFinal)
	{
		this.isFinal = isFinal;
	}
	
	public String getDisplay()
	{
		return name + ": " + type;
	}
	
	public String toString()
	{
		return (isFinal ? "final ": "") + type + " " + name;
	}
}
