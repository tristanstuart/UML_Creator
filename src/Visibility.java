
public enum Visibility
{
	PUBLIC("public"), PRIVATE("private"), PROTECTED("protected");
	
	private final String word;
	
	Visibility(String s)
	{
		word = s;
	}
	
	public String getWord()
	{
		return word;
	}
}
