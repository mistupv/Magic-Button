import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import config.Config;
import edg.graph.EDG;
import edg.graph.Node;
import eknife.EDGFactory;
import eknife.EKnife.Language;
import logic.EDGMapping;

public class MapFactory
{
	public static void main(String[] args)
	{
		
	}
	private static void create(File program, File goldStandard)
	{
		final String temporaryPath = Config.getConfig().getTemporaryPath();
		final String outputPath = temporaryPath + "sliceTmp.erl";

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

		final List<Node> fictitiousSlicedNodes = MapFactory.getFictitiousNodes(slicedNodes, mapSliceOriginal);
		slicedNodes.removeAll(fictitiousSlicedNodes);
		for (Node fictitiousNode : fictitiousSlicedNodes)
			mapSliceGold.remove(fictitiousNode);
		final List<Node> fictitiousGoldNodes = MapFactory.getFictitiousNodes(goldStandardNodes, mapGoldOriginal);
		goldStandardNodes.removeAll(fictitiousGoldNodes);
	}
	private static List<Node> getFictitiousNodes(List<Node> nodes, Map<Node, Node> map)
	{
		final List<Node> fictitious = new LinkedList<Node>();

		for (Node node : nodes)
			if (!map.containsKey(node))
				fictitious.add(node);

		return fictitious;
	}
}