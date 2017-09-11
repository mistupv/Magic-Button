package logic;

import java.awt.Point;

public class SlicingCriterion
{
	private final int line;
	private final String name;
	private final int occurrence;
	private final int column;
	private final int offset;

	public SlicingCriterion(int line, String name)
	{
		this(line, name, 1, -1, -1);
	}
	public SlicingCriterion(int line, String name, int occurrence)
	{
		this(line, name, occurrence, -1, -1);
	}
	public SlicingCriterion(int line, String name, int column, int offset)
	{
		this(line, name, 1, column, offset);
	}
	public SlicingCriterion(int line, String name, int occurrence, int column, int offset)
	{
		this.line = line;
		this.name = name;
		this.occurrence = occurrence;
		this.column = column;
		this.offset = offset;
	}

	public int getLine()
	{
		return this.line;
	}
	public String getName()
	{
		return this.name;
	}
	public int getOccurrence()
	{
		return this.occurrence;
	}
	public Point getStartPoint()
	{
		return new Point(this.column, this.line);
	}
	public Point getEndPoint()
	{
		return new Point(this.column + this.getLength(), this.line);
	}
	public int getStartOffset()
	{
		return this.offset;
	}
	public int getEndOffset()
	{
		return this.offset + this.getLength();
	}
	public int getLength()
	{
		return this.name.length();
	}

	public boolean equals(Object obj)
	{
		if (obj == this)
			return true;
		if (!(obj instanceof SlicingCriterion))
			return false;

		final SlicingCriterion sc = (SlicingCriterion) obj;

		if (this.line != sc.line)
			return false;
		if (!this.name.equals(sc.name))
			return false;
		if (this.occurrence != sc.occurrence)
			return false;
		if (this.column != sc.column)
			return false;
		if (this.offset != sc.offset)
			return false;
		return true;
	}
	public String toString()
	{
		return "[" + this.line + ", " + this.name + ", " + this.occurrence + ", " + this.column + ", " + this.offset + "]";
	}
}