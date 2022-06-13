import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class UMLData
{

	private static final boolean DEBUG = false;
	private static String directory;

	public static void main(String[] args) throws IOException
	{
		if (args.length < 1)
		{
			System.out.println("No directory argument provided! Exiting...");
			System.exit(1);
		}
		directory = args[0];
		Class[] classes = getClasses();

		for (int i = 0; i < classes.length; i++)
			drawClass(classes[i]);
	}

	public static void drawClass(Class c)
	{
		BufferedImage img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
		int xOff = 100;
		int yOff = 100;

		Color borderColor = new Color(128, 128, 128);
		Color insideColor = Color.WHITE;

		Graphics2D g = (Graphics2D) img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setFont(new Font("Arial", Font.PLAIN, 13));
		int width = getClassWidth(c, g) + 25;
		int height = (c.getMethods().size() + c.getVariables().size() + 3) * 14 + 5 + 20;

		g.setColor(insideColor);
		g.fillRect(xOff, yOff, width, height);
		g.setColor(borderColor);
		for (int i = 0; i < 2; i++)
			g.drawRect(xOff - i, yOff - i, width + i * 2, height + i * 2);

		g.drawLine(xOff, yOff + 20, xOff + width, yOff + 20);
		g.drawLine(xOff, yOff + 21, xOff + width, yOff + 21);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 13));
		g.drawString(c.getName(), xOff + width / 2 - g.getFontMetrics().stringWidth(c.getName()) / 2, yOff + 15);

		int yMethodOffset = 35;
		g.setFont(new Font("Arial", Font.PLAIN, 13));
		for (int i = 0; i < c.getVariables().size(); i++)
		{
			if (c.getVariables().get(i).isStatic())
			{
				AttributedString as = new AttributedString(c.getVariables().get(i).getDisplay());
				as.addAttribute(TextAttribute.FONT, g.getFont());
				as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, c.getVariables().get(i).getDisplay().length());

				g.drawString(as.getIterator(), xOff + 5, yOff + yMethodOffset);
			} else
				g.drawString(c.getVariables().get(i).getDisplay(), xOff + 5, yOff + yMethodOffset);

			yMethodOffset += 15;
		}

		yMethodOffset -= 10;

		g.setColor(borderColor);
		g.drawLine(xOff, yOff + yMethodOffset, xOff + width, yOff + yMethodOffset);
		g.drawLine(xOff, yOff + yMethodOffset + 1, xOff + width, yOff + yMethodOffset + 1);

		yMethodOffset += 15;

		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.PLAIN, 13));
		for (int i = 0; i < c.getMethods().size(); i++)
		{
			if (c.getMethods().get(i).isStatic())
			{
				AttributedString as = new AttributedString(c.getMethods().get(i).getDisplay());
				as.addAttribute(TextAttribute.FONT, g.getFont());
				as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, c.getMethods().get(i).getDisplay().length());

				g.drawString(as.getIterator(), xOff + 2, yOff + yMethodOffset);
			} else
				g.drawString(c.getMethods().get(i).getDisplay(), xOff + 2, yOff + yMethodOffset);

			yMethodOffset += 15;
		}

		try
		{
			File outputDir = new File(directory, "UML_Outout");
			outputDir.mkdirs();
			File outputFile = new File(outputDir, c.getName() + ".png");
			outputFile.createNewFile();
			ImageIO.write(img, "png", outputFile);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// finds the width of the class box by getting the longest method or variable and measuring the length
	public static int getClassWidth(Class c, Graphics g)
	{
		String longest = "";
		for (int i = 0; i < c.getMethods().size(); i++)
			if (c.getMethods().get(i).getDisplay().length() > longest.length())
				longest = c.getMethods().get(i).getDisplay();

		for (int i = 0; i < c.getVariables().size(); i++)
			if (c.getVariables().get(i).getDisplay().length() > longest.length())
				longest = c.getVariables().get(i).getDisplay();

		return g.getFontMetrics().stringWidth(longest);
	}

	// parses .java files in a given directory into usable object representations
	public static Class[] getClasses() throws IOException
	{
		ArrayList<Class> classes = new ArrayList<Class>();

		File dir = new File(directory);
		File[] files = dir.listFiles();

		// loop all files in the directory
		for (int i = 0; i < files.length; i++)
		{
			// skip files that arent .java files
			if (!files[i].getName().endsWith(".java"))
				continue;
			Scanner sc = new Scanner(files[i]);
			Class c = null;
			// read whole file
			while (sc.hasNext())
			{
				String ln = sc.nextLine().trim();
				// doesnt support package visibility, also does not support enums or interfaces
				if (ln.startsWith(Visibility.PUBLIC.getWord()) || ln.startsWith(Visibility.PRIVATE.getWord()) || ln.startsWith(Visibility.PROTECTED.getWord()))
				{
					if (ln.contains(" class "))// if this is a class
					{
						c = new Class();
						String[] keywords = ln.split(" ");
						if (ln.contains("extends")) // the last word in the class definition is usually the class name, unless there is an extends
						{
							int j = 0;
							for (; j < keywords.length; j++)// does not support classes that implement interfaces
								if (keywords[j].equals("extends"))
									break;
							c.setName(keywords[j - 1]);
						} else
							c.setName(keywords[keywords.length - 1]);
						if (ln.contains("public"))
							c.setVisibility(Visibility.PUBLIC);
						else if (ln.contains("private"))
							c.setVisibility(Visibility.PRIVATE);
						else if (ln.contains("protected"))
							c.setVisibility(Visibility.PROTECTED);

						if (ln.contains("abstract"))
							c.setAbstract(true);
					}
					// out of basic class, method, and variable definitions, variables are the only ones that end with a semicolon
					if (ln.endsWith(";") && c != null) // so this must be a variable
					{
						Visibility visibility = Visibility.PUBLIC;// does not support package visiblility
						if (ln.contains("public"))
							visibility = Visibility.PUBLIC;
						else if (ln.contains("private"))
							visibility = Visibility.PRIVATE;
						else if (ln.contains("protected"))
							visibility = Visibility.PROTECTED;

						boolean isStatic = ln.contains("static");
						boolean isFinal = ln.contains("final");
						String type = "";

						String keywords[] = ln.split(" ");
						type = keywords[1 + (isStatic ? 1 : 0) + (isFinal ? 1 : 0)];// type definition is defined after static and final are declared

						// this loop accounts for comma separated variable declarations of the same type, including if those variables are set to a value
						for (int j = 2 + (isStatic ? 1 : 0) + (isFinal ? 1 : 0); j < keywords.length; j++)
						{
							String name = keywords[j];
							if (keywords[j].contains("="))
							{
								j++;
								continue;
							}
							if (name.contains(",") || name.contains(";"))// take off any punctuation
								c.addvariable(new Variable(name.substring(0, name.length() - 1), type, visibility, isStatic, isFinal));
							else
								c.addvariable(new Variable(name, type, visibility, isStatic, isFinal));
						}
					}

					// method declarations contain ( ) so this must be a method
					if (ln.endsWith(")") && ln.contains("(") && c != null)
					{
						// isolate the method name by searching backwards for the last space before the '('
						// use that as the start and the index of the '(' as the end to get the method name
						Method m = new Method(ln.substring(ln.lastIndexOf(' ', ln.indexOf('(')), ln.indexOf('(')));
						// cut the method declaration in half by the '(', left half is visibility, static, abstract, and final, right half is parameters
						String[] keywords = ln.split("\\(");
						String modifiers = keywords[0];
						String params = keywords[1];

						// does not support package visibility
						Visibility visibility = Visibility.PUBLIC;
						if (modifiers.contains("public"))
							visibility = Visibility.PUBLIC;
						else if (modifiers.contains("private"))
							visibility = Visibility.PRIVATE;
						else if (modifiers.contains("protected"))
							visibility = Visibility.PROTECTED;

						boolean isStatic = modifiers.contains("static");
						boolean isFinal = modifiers.contains("final");
						boolean isAbstract = modifiers.contains("abstract");
						String returnType = "";
						if (DEBUG)
							System.out.println("classname  " + c.getName());
						if (modifiers.contains(c.getName()))// check if the return type is the class containing the method
						{
							if (!m.getName().equals(c.getName()))// if the method is not a constructor, the return type must be the class
								returnType = c.getName();
						} else
						{// split out the return type by getting the substring from the 2nd to last space and the last space
							int lastSpace = modifiers.lastIndexOf(' ');
							if (DEBUG)
								System.out.println(modifiers + "  " + modifiers.lastIndexOf(' ', lastSpace - 1) + " " + lastSpace);
							returnType = modifiers.substring(modifiers.lastIndexOf(' ', lastSpace - 1), lastSpace).trim();
						}

						if (DEBUG)
							System.out.println("\t\t\t" + modifiers + ": " + returnType);

						m.setType(returnType);
						m.setFinal(isFinal);
						m.setAbstract(isAbstract);
						m.setStatic(isStatic);

						// if the ')' is not directly following '(', there must be parameters
						if (ln.indexOf('(') + 1 != ln.indexOf(')'))
						{
							params.replace("\\(", "");
							params.replace("\\)", "");
							for (String p : params.split(", "))
							{
								boolean argFinal = p.contains("final");
								String paramModifiers[] = p.split(" ");
								String type = paramModifiers[0 + (argFinal ? 1 : 0)];
								String name = paramModifiers[1 + (argFinal ? 1 : 0)];
								name.trim();
								if (name.endsWith(",") || name.endsWith(";") || name.endsWith(")"))// edge case name cleanup
									name = name.substring(0, name.length() - 1);
								m.addArg(new Argument(name, type, argFinal));
							}
						}
						c.addMethod(m);
					}
				}
			}
			if (c != null)
			{
				if (DEBUG)
					System.out.println(c.getDisplay() + "\n\n\n");
				classes.add(c);
			}
		}
		return classes.toArray(new Class[0]);
	}
}
