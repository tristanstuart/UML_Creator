import java.util.ArrayList;

public class Class
{

	private boolean isAbstract;
	
	private Visibility visibility;
	
	private String name;
	private ArrayList<Method> methods;
	private ArrayList<Variable> variables;
	
	public Class()
	{
		this("");
	}
	
	public Class(String name)
	{
		this(name, false, Visibility.PUBLIC);
	}
	
	public Class(String name, boolean ab, Visibility v)
	{
		visibility = v;
		isAbstract = ab;
		methods = new ArrayList<Method>();
		variables = new ArrayList<Variable>();
	}

	public void addMethod(Method m)
	{
		methods.add(m);
	}
	
	public void addvariable(Variable m)
	{
		variables.add(m);
	}
	
	public Visibility getVisibility()
	{
		return visibility;
	}

	public void setVisibility(Visibility visibility)
	{
		this.visibility = visibility;
	}

	public boolean isAbstract()
	{
		return isAbstract;
	}

	public void setAbstract(boolean isAbstract)
	{
		this.isAbstract = isAbstract;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public ArrayList<Method> getMethods()
	{
		return methods;
	}
	
	public ArrayList<Variable> getVariables()
	{
		return variables;
	}
	
	public String getDisplay()
	{
		String vars = "";
		for(Variable v : variables)
			vars += "\n\t" + v.getDisplay();
		
		String meths = "";
		for(Method m : methods)
			meths += "\n\t" + m.getDisplay();
		return visibility.getWord() + " class " + name + vars + "\n" + meths;
	}
	
	public String toString()
	{
		String vars = "";
		for(Variable v : variables)
			vars += "\n\t" + v;
		
		String meths = "";
		for(Method m : methods)
			meths += "\n\t" + m;
		return visibility.getWord() + " class " + name + vars + "\n" + meths;
	}
}
