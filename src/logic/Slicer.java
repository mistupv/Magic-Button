package logic;

public class Slicer
{
	private String name;
	private String path;
	private String command;
	private String input;
	private String output;
	private String scLine;
	private String scName;
	private String scOccurrence;
	private String scLength;
	private String scStartLine;
	private String scStartColumn;
	private String scEndLine;
	private String scEndColumn;
	private String scStartOffset;
	private String scEndOffset;
	private boolean flagCombination1;
	private boolean flagCombination2;
	private boolean flagCombination3;
	private boolean flagCombination4;
	private boolean flagCombination5;

	public Slicer(String name, String path, String command, String input, String output, String scLine, String scName, String scOccurrence, String scLength,
				String scStartLine, String scStartColumn, String scEndLine, String scEndColumn, String scStartOffset, String scEndOffset,
				boolean flagCombination1, boolean flagCombination2, boolean flagCombination3, boolean flagCombination4, boolean flagCombination5)
	{
		this.name = name;
		this.path = path;
		this.command = command;
		this.input = input;
		this.output = output;
		this.scLine = scLine;
		this.scName = scName;
		this.scOccurrence = scOccurrence;
		this.scLength = scLength;
		this.scStartLine = scStartLine;
		this.scStartColumn = scStartColumn;
		this.scEndLine = scEndLine;
		this.scEndColumn = scEndColumn;
		this.scStartOffset = scStartOffset;
		this.scEndOffset = scEndOffset;
		this.flagCombination1 = flagCombination1;
		this.flagCombination2 = flagCombination2;
		this.flagCombination3 = flagCombination3;
		this.flagCombination4 = flagCombination4;
		this.flagCombination5 = flagCombination5;
	}

	public String getName()
	{
		return this.name;
	}
	public String getPath()
	{
		return this.path;
	}
	public String getCommand()
	{
		return this.command;
	}
	public String getInput()
	{
		return this.input;
	}
	public String getOutput()
	{
		return this.output;
	}
	public String getSCLine()
	{
		return this.scLine;
	}
	public String getSCName()
	{
		return this.scName;
	}
	public String getSCOccurrence()
	{
		return this.scOccurrence;
	}
	public String getSCLength()
	{
		return this.scLength;
	}
	public String getSCStartLine()
	{
		return this.scStartLine;
	}
	public String getSCStartColumn()
	{
		return this.scStartColumn;
	}
	public String getSCEndLine()
	{
		return this.scEndLine;
	}
	public String getSCEndColumn()
	{
		return this.scEndColumn;
	}
	public String getSCStartOffset()
	{
		return this.scStartOffset;
	}
	public String getSCEndOffset()
	{
		return this.scEndOffset;
	}
	public boolean getFlagCombination1()
	{
		return this.flagCombination1;
	}
	public boolean getFlagCombination2()
	{
		return this.flagCombination2;
	}
	public boolean getFlagCombination3()
	{
		return this.flagCombination3;
	}
	public boolean getFlagCombination4()
	{
		return this.flagCombination4;
	}
	public boolean getFlagCombination5()
	{
		return this.flagCombination5;
	}

	public String toString()
	{
		return this.name;
	}

	public String getCommandPreview()
	{
		final SlicingCriterion slicingCriterion = new SlicingCriterion(42, "VarX", 3, 12, 123);
		final String inputPath = "./input.txt";
		final String outputPath = "./output.txt";

		return this.getCommand(slicingCriterion, inputPath, outputPath);
	}
	public String getCommand(SlicingCriterion slicingCriterion, String inputPath, String outputPath)
	{
		final boolean flagCombination1 = this.getFlagCombination1();
		final boolean flagCombination2 = this.getFlagCombination2();
		final boolean flagCombination3 = this.getFlagCombination3();
		final boolean flagCombination4 = this.getFlagCombination4();
		final boolean flagCombination5 = this.getFlagCombination5();
		final boolean scLine = flagCombination1;
		final boolean scName = flagCombination1;
		final boolean scOccurrence = flagCombination1;
		final boolean scLength = flagCombination3 || flagCombination5;
		final boolean scStartLine = flagCombination2 || flagCombination3;
		final boolean scStartColumn = flagCombination2 || flagCombination3;
		final boolean scEndLine = flagCombination2;
		final boolean scEndColumn = flagCombination2;
		final boolean scStartOffset = flagCombination4 || flagCombination5;
		final boolean scEndOffset = flagCombination4;

		String command = this.getCommand();

		command = command.replace("{" + this.getInput() + "}", "-" + this.getInput() + " \"" + inputPath + "\"");
		command = command.replace("{" + this.getOutput() + "}", "-" + this.getOutput() + " \"" + outputPath + "\"");
		command = !scLine ? command : command.replace("{" + this.getSCLine() + "}", "-" + this.getSCLine() + " " + slicingCriterion.getLine());
		command = !scName ? command : command.replace("{" + this.getSCName() + "}", "-" + this.getSCName() + " " + slicingCriterion.getName());
		command = !scOccurrence ? command : command.replace("{" + this.getSCOccurrence() + "}", "-" + this.getSCOccurrence() + " " + slicingCriterion.getOccurrence());
		command = !scLength ? command : command.replace("{" + this.getSCLength() + "}", "-" + this.getSCLength() + " " + slicingCriterion.getLength());
		command = !scStartLine ? command : command.replace("{" + this.getSCStartLine() + "}", "-" + this.getSCStartLine() + " " + slicingCriterion.getStartPoint());
		command = !scStartColumn ? command : command.replace("{" + this.getSCStartColumn() + "}", "-" + this.getSCStartColumn() + " " + slicingCriterion.getStartPoint().x);
		command = !scEndLine ? command : command.replace("{" + this.getSCEndLine() + "}", "-" + this.getSCEndLine() + " " + slicingCriterion.getEndPoint().y);
		command = !scEndColumn ? command : command.replace("{" + this.getSCEndColumn() + "}", "-" + this.getSCEndColumn() + " " + slicingCriterion.getEndPoint().x);
		command = !scStartOffset ? command : command.replace("{" + this.getSCStartOffset() + "}", "-" + this.getSCStartOffset() + " " + slicingCriterion.getStartOffset());
		command = !scEndOffset ? command : command.replace("{" + this.getSCEndOffset() + "}", "-" + this.getSCEndOffset() + " " + slicingCriterion.getEndOffset());

		command = command.replace("[" + this.getInput() + "]", "\"" + inputPath + "\"");
		command = command.replace("[" + this.getOutput() + "]", "\"" + outputPath + "\"");
		command = !scLine ? command : command.replace("[" + this.getSCLine() + "]", slicingCriterion.getLine() + "");
		command = !scName ? command : command.replace("[" + this.getSCName() + "]", slicingCriterion.getName());
		command = !scOccurrence ? command : command.replace("[" + this.getSCOccurrence() + "]", slicingCriterion.getOccurrence() + "");
		command = !scLength ? command : command.replace("[" + this.getSCLength() + "]", slicingCriterion.getLength() + "");
		command = !scStartLine ? command : command.replace("[" + this.getSCStartLine() + "]", slicingCriterion.getStartPoint() + "");
		command = !scStartColumn ? command : command.replace("[" + this.getSCStartColumn() + "]", slicingCriterion.getStartPoint().x + "");
		command = !scEndLine ? command : command.replace("[" + this.getSCEndLine() + "]", slicingCriterion.getEndPoint().y + "");
		command = !scEndColumn ? command : command.replace("[" + this.getSCEndColumn() + "]", slicingCriterion.getEndPoint().x + "");
		command = !scStartOffset ? command : command.replace("[" + this.getSCStartOffset() + "]", slicingCriterion.getStartOffset() + "");
		command = !scEndOffset ? command : command.replace("[" + this.getSCEndOffset() + "]", slicingCriterion.getEndOffset() + "");

		return command;
	}
}