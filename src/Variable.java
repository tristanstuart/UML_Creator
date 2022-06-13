
public class Variable
{
	
	private String name, type;
	private boolean isFinal, isStatic;
	private Visibility visibility;

	public Variable(String n, String t, Visibility v)
	{
		this(n, t, v, false, false);
	}
	
	public Variable(String n, String t, Visibility v, boolean f, boolean s)
	{
		isStatic = s;
		isFinal = f;
		name = n.trim();
		type = t.trim();
		visibility = v;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name.trim();
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type.trim();
	}

	public boolean isFinal()
	{
		return isFinal;
	}

	public void setFinal(boolean isFinal)
	{
		this.isFinal = isFinal;
	}

	public boolean isStatic()
	{
		return isStatic;
	}

	public void setStatic(boolean isStatic)
	{
		this.isStatic = isStatic;
	}

	public Visibility getVisibility()
	{
		return visibility;
	}

	public void setVisibility(Visibility visibility)
	{
		this.visibility = visibility;
	}
	
	public String getDisplay()
	{
		return (visibility == Visibility.PUBLIC ? "+" : visibility == Visibility.PROTECTED ? "#" : visibility == Visibility.PRIVATE ? "-" : "~") +  name + ": " + type + (isFinal ? " {readOnly}" : "");
	}
	
	public String toString()
	{
		return visibility.getWord() + " " + (isStatic ? "static " : "") + (isFinal ? "final " : "") + type + " " + name + ";";
	}
}
