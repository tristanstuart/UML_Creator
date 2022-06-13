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
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class UMLData
{

	private static String path;

	public static void main(String[] args) throws IOException
	{
		path = "E:\\_I HATE UML\\" + args[0].substring(args[0].lastIndexOf("/", args[0].lastIndexOf("/") - 1) + 1, args[0].lastIndexOf("/")) + "\\UML\\" + "game\\";

		Class[] classes = getClasses(args);

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
			new File(path).mkdirs();
			File asdf = new File(path + c.getName() + ".png");
			asdf.createNewFile();
			ImageIO.write(img, "png", asdf);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

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

	public static Class[] getClasses(String args[]) throws IOException
	{
		ArrayList<Class> classes = new ArrayList<Class>();
		if (args.length < 1)
		{
			System.err.println("not enough args, exiting...");
			System.exit(1);
		}

		String directory = args[0];
		File dir = new File(directory);
		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++)
		{
			if (!files[i].getName().endsWith(".java"))
				continue;
			Scanner sc = new Scanner(files[i]);
			Class c = null;
			while (sc.hasNext())
			{
				String ln = sc.nextLine().trim();
				if (ln.startsWith(Visibility.PUBLIC.getWord()) || ln.startsWith(Visibility.PRIVATE.getWord()) || ln.startsWith(Visibility.PROTECTED.getWord()))
				{
					if (ln.contains(" class "))
					{
						c = new Class();
						String[] keywords = ln.split(" ");
						if (ln.contains("extends"))
						{
							int j = 0;
							for (; j < keywords.length; j++)
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

					// you dont make good money do

					if (ln.endsWith(";") && c != null)
					{
						Visibility visibility = Visibility.PUBLIC;
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
						type = keywords[1 + (isStatic ? 1 : 0) + (isFinal ? 1 : 0)];

						for (int j = 2 + (isStatic ? 1 : 0) + (isFinal ? 1 : 0); j < keywords.length; j++)
						{
							String name = keywords[j];
							if (keywords[j].contains("="))
							{
								j++;
								continue;
							}
							if (name.contains(",") || name.contains(";"))
								c.addvariable(new Variable(name.substring(0, name.length() - 1), type, visibility, isStatic, isFinal));
							else
								c.addvariable(new Variable(name, type, visibility, isStatic, isFinal));
						}
					}

					if (ln.endsWith(")") && ln.contains("(") && c != null)
					{
						Method m = new Method(ln.substring(ln.lastIndexOf(' ', ln.indexOf('(')), ln.indexOf('(')));
						String[] keywords = ln.split("\\(");
						String modifiers = keywords[0];
						String params = keywords[1];

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
						System.out.println("classname  " + c.getName());
						if (modifiers.contains(c.getName()))
						{
							if (!m.getName().equals(c.getName()))
								returnType = c.getName();
						} else
						{
							System.out.println(modifiers + "  " + modifiers.lastIndexOf(' ', modifiers.lastIndexOf(' ') - 1) + " " + modifiers.lastIndexOf(' '));
							returnType = modifiers.substring(modifiers.lastIndexOf(' ', modifiers.lastIndexOf(' ') - 1), modifiers.lastIndexOf(' ')).trim();
						}

						System.out.println("\t\t\t" + modifiers + ": " + returnType);

						m.setType(returnType);
						m.setFinal(isFinal);
						m.setAbstract(isAbstract);
						m.setStatic(isStatic);

						if (ln.indexOf('(') + 1 != ln.indexOf(')'))
						{
							params.replace("\\(", "");
							params.replace("\\)", "");
							for (String p : params.split(", "))
							{
								boolean argFinal = p.contains("final");
								String asdf[] = p.split(" ");
								String type = asdf[0 + (argFinal ? 1 : 0)];
								String name = asdf[1 + (argFinal ? 1 : 0)];
								name.trim();
								if (name.endsWith(",") || name.endsWith(";") || name.endsWith(")"))
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
				System.out.println(c.getDisplay() + "\n\n\n");
				classes.add(c);
			}
		}
		return classes.toArray(new Class[0]);
	}
}
