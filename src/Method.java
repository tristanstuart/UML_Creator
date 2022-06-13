import java.util.ArrayList;

public class Method
{
	private String name, returnType;
	private ArrayList<Argument> args;
	private boolean isAbstract, isFinal, isStatic;
	private Visibility visibility;

	public Method()
	{
		this("");
	}

	public Method(String n)
	{
		this(n, "");
	}

	public Method(String n, String t)
	{
		this(n, t, Visibility.PUBLIC);
	}

	public Method(String n, String t, Visibility v)
	{
		args = new ArrayList<Argument>();
		name = n.trim();
		returnType = t.trim();
		visibility = v;
	}

	public void addArg(Argument b)
	{
		args.add(b);
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
		return returnType;
	}

	public void setType(String type)
	{
		this.returnType = type.trim();
	}

	public boolean isAbstract()
	{
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract)
	{
		this.isAbstract = isAbstract;
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
		String arguments = "";
		if (args.size() > 1)
		{
			for (Argument a : args)
				arguments += a.getDisplay() + ", ";
			arguments = arguments.substring(0, arguments.length() - 2);
		}
		else if(args.size() == 1)
			arguments = args.get(0).getDisplay() + "";
		return (visibility == Visibility.PUBLIC ? "+" : visibility == Visibility.PROTECTED ? "#" : visibility == Visibility.PRIVATE ? "-" : "~") +  name + "(" + arguments + ")" + (!returnType.equals("") ? ": " + returnType: "");
		}

	public String toString()
	{
		String arguments = "";
		if (args.size() > 1)
		{
			for (Argument a : args)
				arguments += a + ", ";
			arguments = arguments.substring(0, arguments.length() - 2);
		}
		else if(args.size() == 1)
			arguments = args.get(0) + "";
		return visibility.getWord() + " " + (isAbstract ? "abstract " : "") + (isStatic ? "static " : "") + (isFinal ? "final " : "") + returnType + (returnType.equals("") ? "" : " ") + name + "(" + arguments + ")";
	}
}
