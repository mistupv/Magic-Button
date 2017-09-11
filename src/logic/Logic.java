package logic;

import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import config.Config;
import edg.PdfFactory;
import edg.graph.EDG;
import edg.graph.EdgeInfo;
import edg.graph.Node;
import edg.graph.NodeInfo;
import edg.graph.EdgeInfo.Type;
import edg.traverser.EDGTraverser;
import eknife.EDGFactory;
import eknife.EKnife.Language;
import misc.Misc;
import misc.util.Flusher;

public class Logic
{
	private static Logic logic = new Logic();
	public static Logic getLogic()
	{
		return Logic.logic;
	}

	private final Config config = Config.getConfig();
	private String[][] bestMetrics = this.loadBestMetrics();

	private Logic()
	{
		
	}

	public String[][] getData()
	{
		final String[][] data = this.getMetrics();

		this.saveCurrentMetrics(data);
		if (this.getDouble(data[0][6]) > this.getDouble(data[0][3]))
		{
			this.saveBestMetrics();
			this.bestMetrics = this.loadBestMetrics();
		}

		return data;
	}
	private String[][] getMetrics()
	{
		final String benchmarksPath = this.config.getBenchmarksPath();
		final Slicer slicer = this.getSlicer();

		final int benchmarkCount = this.countBenchmarks();
		final String[][] data = new String[benchmarkCount + 1][8];
		final List<File> programsFolders0 = Misc.getFolders(new File(benchmarksPath), false);
		final List<File> programsFolders = this.sortFiles(programsFolders0);
		final String[] extensions = { ".erl" };
		final double[] meanMetrics = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
		int benchmark = 0;

		for (File programFolder : programsFolders)
		{
			final String programFolderPath = programFolder.getAbsolutePath();
			final File programFile = Misc.getFiles(new File(programFolderPath), extensions, false).get(0);
			final String slicesPath = programFolderPath + File.separator + "slices";
			final List<File> goldStandards0 = Misc.getFiles(new File(slicesPath), extensions, false);
			final List<File> goldStandards = this.sortFiles(goldStandards0);

			for (File goldStandard : goldStandards)
			{
				final String benchmarkName = Misc.getFileName(goldStandard);
				final double[] bestMetrics = this.getBestMetrics(benchmarkName);
				final Object[] metrics = this.getSlicerMetrics(benchmark + 1, slicer, programFile, goldStandard);

				benchmark++;
				data[benchmark][0] = benchmarkName;
				data[benchmark][1] = Misc.round(bestMetrics[0], 2) + "%";
				data[benchmark][2] = Misc.round(bestMetrics[1], 2) + "%";
				data[benchmark][3] = Misc.round(bestMetrics[2], 2) + "%";
				data[benchmark][4] = Misc.round((Double) metrics[0], 2) + "%";
				data[benchmark][5] = Misc.round((Double) metrics[1], 2) + "%";
				data[benchmark][6] = Misc.round((Double) metrics[2], 2) + "%";
				data[benchmark][7] = (String)metrics[3];
				meanMetrics[0] += Misc.round(bestMetrics[0], 2);
				meanMetrics[1] += Misc.round(bestMetrics[1], 2);
				meanMetrics[2] += Misc.round(bestMetrics[2], 2);
				meanMetrics[3] += Misc.round((Double) metrics[0], 2);
				meanMetrics[4] += Misc.round((Double) metrics[1], 2);
				meanMetrics[5] += Misc.round((Double) metrics[2], 2);
			}
		}
		data[0][0] = "";
		data[0][1] = Misc.round(meanMetrics[0] / benchmarkCount, 2) + "%";
		data[0][2] = Misc.round(meanMetrics[1] / benchmarkCount, 2) + "%";
		data[0][3] = Misc.round(meanMetrics[2] / benchmarkCount, 2) + "%";
		data[0][4] = Misc.round(meanMetrics[3] / benchmarkCount, 2) + "%";
		data[0][5] = Misc.round(meanMetrics[4] / benchmarkCount, 2) + "%";
		data[0][6] = Misc.round(meanMetrics[5] / benchmarkCount, 2) + "%";
		data[0][7] = "";

		return data;
	}
	private double[] getBestMetrics(String benchmarkName)
	{
		for (int bestMetricsIndex = 0; bestMetricsIndex < this.bestMetrics.length; bestMetricsIndex++)
		{
			final String benchmarkName0 = this.bestMetrics[bestMetricsIndex][0];
			if (!benchmarkName.equals(benchmarkName0))
				continue;

			final String precision = this.bestMetrics[bestMetricsIndex][1];
			final String recall = this.bestMetrics[bestMetricsIndex][2];
			final String f1 = this.bestMetrics[bestMetricsIndex][3];
			final double precision0 = Double.parseDouble(precision.substring(0, precision.length() - 1));
			final double recall0 = Double.parseDouble(recall.substring(0, recall.length() - 1));
			final double f10 = Double.parseDouble(f1.substring(0, f1.length() - 1));

			return new double[] { precision0, recall0, f10 };
		}

		return new double[] { 0.0, 0.0, 0.0 };
	}
	private Object[] getSlicerMetrics(int benchmark, Slicer slicer, File program, File goldStandard)
	{
		final String temporaryPath = this.config.getTemporaryPath();
		final String programPath = program.getAbsolutePath();

		final String SCText = Misc.readLines(goldStandard).get(0); 
		final SlicingCriterion sc = this.obtainSlicingCriterion(SCText);
		final String outputPath = temporaryPath + "sliceTmp.erl";
		final File outputFile = new File(outputPath);
		Misc.copyFile(program, outputFile, true);
		this.executeSlicer(slicer, sc, programPath, outputPath);

		final EDG originalEDG = EDGFactory.createEDG(Language.Erlang, program.getAbsolutePath(), false);
		final EDG goldStandardEDG = EDGFactory.createEDG(Language.Erlang, goldStandard.getAbsolutePath(), false);
		final EDG slicedEDG = EDGFactory.createEDG(Language.Erlang, outputPath, false);

		final List<Node> goldStandardNodes = goldStandardEDG.getNodes();
		final List<Node> slicedNodes = slicedEDG.getNodes();

		final EDGMapping mappingSliceGold = new EDGMapping(slicedEDG, goldStandardEDG);
		final Map<Node, Node> mapSliceGold = mappingSliceGold.map();

		final EDGMapping mappingSliceOriginal = new EDGMapping(slicedEDG, originalEDG);
		final Map<Node, Node> mapSliceOriginal = mappingSliceOriginal.map();

		final EDGMapping mappingGoldOriginal = new EDGMapping(goldStandardEDG, originalEDG);
		final Map<Node, Node> mapGoldOriginal = mappingGoldOriginal.map();

// TODO Borrame
this.saveEDGs(benchmark, slicedEDG, goldStandardEDG, mapSliceGold);

		final List<Node> fictitiousSlicedNodes = this.getFictitiousNodes(slicedNodes, mapSliceOriginal);
		slicedNodes.removeAll(fictitiousSlicedNodes);
		for (Node fictitiousNode : fictitiousSlicedNodes)
			mapSliceGold.remove(fictitiousNode);
		final List<Node> fictitiousGoldNodes = this.getFictitiousNodes(goldStandardNodes, mapGoldOriginal);
		goldStandardNodes.removeAll(fictitiousGoldNodes);

		final String msg = this.getRemainingNodes(goldStandardNodes, mapSliceGold);
		final String msg2 = this.getLeftoverNodes(slicedNodes, mapSliceGold);

		final int correctlyRetrievedNodes = mapSliceGold.size();
		final int retrievedNodes = slicedNodes.size();
		final int correctNodes = goldStandardNodes.size();
		final double precision = 100.0 * correctlyRetrievedNodes / retrievedNodes;
		final double recall = 100.0 * correctlyRetrievedNodes / correctNodes;
		final double f1 = 2 * precision * recall / (precision + recall);

		return new Object[] { precision, recall, f1, msg + "\n" + msg2};
	}

	private Slicer getSlicer()
	{
		final String configPath = this.config.getConfigurationPath();
		final File slicersFile = new File(configPath + "Slicer.txt");
		if (!slicersFile.exists())
			return null;

		final String line = Misc.readLines(slicersFile.getAbsolutePath()).get(0);

		final StringTokenizer st = new StringTokenizer(line, "@");
		final String name = st.nextToken().trim();
		final String path = st.nextToken().trim();
		final String command = st.nextToken().trim();
		final String input = st.nextToken().trim();
		final String output = st.nextToken().trim();
		final String scLine = st.nextToken().trim();
		final String scName = st.nextToken().trim();
		final String scOccurrence = st.nextToken().trim();
		final String scLength = st.nextToken().trim();
		final String scStartLine = st.nextToken().trim();
		final String scStartColumn = st.nextToken().trim();
		final String scEndLine = st.nextToken().trim();
		final String scEndColumn = st.nextToken().trim();
		final String scStartOffset = st.nextToken().trim();
		final String scEndOffset = st.nextToken().trim();
		final boolean flagCombination1 = Boolean.parseBoolean(st.nextToken().trim());
		final boolean flagCombination2 = Boolean.parseBoolean(st.nextToken().trim());
		final boolean flagCombination3 = Boolean.parseBoolean(st.nextToken().trim());
		final boolean flagCombination4 = Boolean.parseBoolean(st.nextToken().trim());
		final boolean flagCombination5 = Boolean.parseBoolean(st.nextToken().trim());

		return new Slicer(name, path, command, input, output,
				scLine, scName, scOccurrence, scLength, scStartLine, scStartColumn, scEndLine, scEndColumn, scStartOffset, scEndOffset,
				flagCombination1, flagCombination2, flagCombination3, flagCombination4, flagCombination5);
	}
	private void executeSlicer(Slicer slicer, SlicingCriterion sc, String input, String output)
	{
		try
		{
			final String command = slicer.getCommand(sc, input, output);
			final Runtime runtime = Runtime.getRuntime();
			final String slicerPath = slicer.getPath();
			final File slicerFile = new File(slicerPath);
			final java.lang.Process process = runtime.exec(new String[] { "/bin/sh", "-c", command }, null, slicerFile);

			new Flusher(process).start();
			process.waitFor();
		}
		catch (Exception e)
		{
			throw new RuntimeException("Error");
		}
	}
	private String[][] loadBestMetrics()
	{
		try
		{
			final int startRow = 2;
			final int startColumn = 1;
			final String resultsPath = this.config.getResultsPath();
			final String currentPath = resultsPath + "best.xls";
			final File file = new File(currentPath);
			final FileInputStream fis = new FileInputStream(file);
			final Workbook workbook = new HSSFWorkbook(fis);

			// Results
			final Sheet resultsSheet = workbook.getSheet("Results");
			final int rowCount = resultsSheet.getLastRowNum();
			final int columnCount = resultsSheet.getRow(startRow).getLastCellNum();
			final String[][] data = new String[rowCount + 1 - startRow][columnCount - startColumn];

			for (int rowIndex = startRow; rowIndex <= rowCount; rowIndex++)
			{
				final Row row = resultsSheet.getRow(rowIndex);

				for (int columnIndex = startColumn; columnIndex < columnCount; columnIndex++)
				{
					final Cell cell = row.getCell(columnIndex);
					final String value = cell.toString();

					data[rowIndex - startRow][columnIndex - startColumn] = value;
				}
			}

			workbook.close();

			return data;
		}
		catch (IOException e)
		{
			
		}

		return new String[0][0];
	}
	private void saveCurrentMetrics(String[][] data)
	{
		try
		{
			final int startRow = 2;
			final int startColumn = 1;
			final Workbook workbook = new HSSFWorkbook();
			final String resultsPath = this.config.getResultsPath();
			final String currentPath = resultsPath + "current.xls";
			final File file = new File(currentPath);

			// Results
			final Sheet resultsSheet = workbook.createSheet("Results");

				// Create header
			final Row headerRow = resultsSheet.createRow(startRow - 1);
			final Cell benchmarkNameCell = headerRow.createCell(startColumn);
			final Cell precisionCell = headerRow.createCell(startColumn + 1);
			final Cell recallCell = headerRow.createCell(startColumn + 2);
			final Cell f1Cell = headerRow.createCell(startColumn + 3);
			benchmarkNameCell.setCellValue("Benchmark");
			precisionCell.setCellValue("Precision");
			recallCell.setCellValue("Recall");
			f1Cell.setCellValue("F1");

				// Create benchmarks' results
			final int rowCount = data.length;
			final int columnCount = data[0].length;
			for (int rowIndex = 0; rowIndex < rowCount; rowIndex++)
			{
				final Row benchmarkRow = resultsSheet.createRow(startRow + rowIndex);

				for (int columnIndex = 0; columnIndex < columnCount - 1; columnIndex++)
				{
					if (1 <= columnIndex && columnIndex <= 3)
						continue;
					final String cellValue = data[rowIndex][columnIndex];
					final int colIndex = columnIndex == 0 ? 0 : columnIndex - 3;
					final Cell resultCell = benchmarkRow.createCell(startColumn + colIndex);

					resultCell.setCellValue(cellValue);
				}
			}

			// Save file
			final FileOutputStream fos = new FileOutputStream(file);
			workbook.write(fos);
			fos.close();
			workbook.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private void saveBestMetrics()
	{
		final String resultsPath = this.config.getResultsPath();
		final String currentPath = resultsPath + "current.xls";
		final File currentFile = new File(currentPath);
		final String bestPath = resultsPath + "best.xls";
		final File bestFile = new File(bestPath);
		final String now = Misc.getCurrentTime("yyyy-MM-dd_HH''mm");
		final String bestHistoryPath = resultsPath + "bests" + File.separator + now + ".xls";
		final File bestHistoryFile = new File(bestHistoryPath);

		Misc.copyFile(currentFile, bestFile, false);
		Misc.copyFile(currentFile, bestHistoryFile, false);
	}

	private List<File> sortFiles(List<File> files)
	{
		final List<File> filesSorted = new LinkedList<File>();

		for (File file : files)
		{
			final String fileName = Misc.getFileName(file);
			final int fileNumber = this.getNumber(fileName);
			int fileSortedIndex = 0;

			for (; fileSortedIndex < filesSorted.size(); fileSortedIndex++)
			{
				final File fileSortedFile = filesSorted.get(fileSortedIndex);
				final String fileSortedName = Misc.getFileName(fileSortedFile);
				final int fileSortedNumber = this.getNumber(fileSortedName);

				if (fileNumber < fileSortedNumber)
					break;
			}

			filesSorted.add(fileSortedIndex, file);
		}

		return filesSorted;
	}
	private int getNumber(String fileName)
	{
		int number = 0;

		for (int index = fileName.length() - 1; index >= 0; index--)
		{
			try
			{
				final String text = fileName.substring(index);
				number = Integer.parseInt(text);
			}
			catch (Exception e)
			{
				break;
			}
		}

		return number;
	}
	private double getDouble(String percentage)
	{
		try
		{
			final String number = percentage.substring(0, percentage.length() - 1);
			return Double.parseDouble(number);
		}
		catch (Exception e)
		{
			return 0.0;
		}
	}
	private int countBenchmarks() 
	{
		final String benchmarksPath = this.config.getBenchmarksPath();
		final List<File> programsFolders = Misc.getFolders(new File(benchmarksPath), false);
		final String[] extensions = { ".erl" };
		int benchmarkCount = 0;

		for (File programFolder : programsFolders)
		{
			final String programFolderPath = programFolder.getAbsolutePath();
			final String slicesPath = programFolderPath + File.separator + "slices";
			final List<File> slicesFiles = Misc.getFiles(new File(slicesPath), extensions, false);

			benchmarkCount += slicesFiles.size();
		}

		return benchmarkCount;
	}
	private SlicingCriterion obtainSlicingCriterion(String SCText)
	{
		int line = 0;
		String var = "";
		int oc = 0;
		int offset = 0;
		int col = 0;

		final StringTokenizer st = new StringTokenizer(SCText, " ");

		while (st.hasMoreTokens())
		{
			final String token = st.nextToken(); 

			if (token.startsWith("line="))
				line = Integer.parseInt(token.substring(5));
			else if (token.startsWith("var="))
				var = token.substring(4);
			else if (token.startsWith("oc="))
				oc = Integer.parseInt(token.substring(3));
			else if (token.startsWith("offset="))
				offset = Integer.parseInt(token.substring(7));
			else if (token.startsWith("col="))
				col = Integer.parseInt(token.substring(4));
		}

		return new SlicingCriterion(line, var, oc, offset, col); 
	}

	private String getRemainingNodes(List<Node> nodes, Map<Node, Node> map)
	{
		final List<Node> unMappedNodes = new LinkedList<Node>();

		for (Node node : nodes)
			if (!map.containsValue(node))
				unMappedNodes.add(node);
		if (unMappedNodes.isEmpty())
			return "Your slice is complete\n";

		String msg = "Necessary nodes deleted:\n";
		for (Node unMappedNode : unMappedNodes)
		{	
			String name = unMappedNode.getData().getName();
			name = name == null ? unMappedNode.getName() : name;
			msg += "ln: " + unMappedNode.getData().getInfo().getLine() + ", text: " + name + "\n";
		}
		return msg;
	}
	private String getLeftoverNodes(List<Node> nodes, Map<Node, Node> map)
	{
		final List<Node> unMappedNodes = new LinkedList<Node>();

		for (Node node : nodes)
			if (!map.containsKey(node))
				unMappedNodes.add(node);
		if (unMappedNodes.isEmpty())
			return "Your slice is correct\n";

		String msg = "Non-sliced nodes:\n";
		for (Node unMappedNode : unMappedNodes)
		{	
			String name = unMappedNode.getData().getName();
			name = name == null ? unMappedNode.getName() : name;
			msg += "ln: " + unMappedNode.getData().getInfo().getLine() + ", text: " + name + "\n";
		}
		return msg;
	}
	private List<Node> getFictitiousNodes(List<Node> nodes, Map<Node, Node> map)
	{
		final List<Node> fictitious = new LinkedList<Node>();

		for (Node node : nodes)
			if (!map.containsKey(node))
				fictitious.add(node);

		return fictitious;
	}

// TODO Borrame
private int id = -1;
private Map<Node, Node> map = new Hashtable<Node, Node>();
private void saveEDGs(int benchmark, EDG edg1, EDG edg2, Map<Node, Node> map)
{
	if (benchmark != -1)
		return;
	final List<Integer> nodes = Arrays.asList(338, 339, 355);

	this.id = -1;
	final String codebase = "/Users/Fenix/Desktop/Slicing/";
	final String outputPath = codebase + "map.pdf";
	final File outputFile = new File(outputPath);
	final Set<Entry<Node, Node>> entrySet = map.entrySet();
	final NodeInfo rootNodeInfo = new NodeInfo(this.id++, NodeInfo.Type.Root, "EDG", null);
	final Node rootNode = new Node("EDG", rootNodeInfo);
	final EDG edg = new EDG();

	edg.setRootNode(rootNode);
	this.addSubEDG(edg, rootNode, edg1.getRootNode());
	this.addSubEDG(edg, rootNode, edg2.getRootNode());
	for (Entry<Node, Node> entry : entrySet)
	{
		final Node from = this.map.get(entry.getKey());
		final Node to = this.map.get(entry.getValue());
		if (nodes.contains(from.getData().getId()) || nodes.contains(to.getData().getId()))
			edg.addEdge(from, to, 0, new EdgeInfo(Type.Value));
	}
	PdfFactory.createPdf(outputFile, edg);
}
private void addSubEDG(EDG edg, Node parent, Node node)
{
	final List<Node> children = EDGTraverser.getChildren(node);
	final Node node0 = new Node(node.getName(), new NodeInfo(this.id++, node.getData().getType(), node.getData().getName(), null));

	edg.addNode(node0);
	edg.addEdge(parent, node0, 0, new EdgeInfo(Type.Structural));
	this.map.put(node, node0);
	for (Node child : children)
		this.addSubEDG(edg, node0, child);
}
}