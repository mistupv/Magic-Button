package logic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edg.graph.EDG;
import edg.graph.Node;
import edg.graph.NodeInfo;
import edg.traverser.EDGTraverser;

public class EDGMapping
{
	private final EDG edg1;
	private final EDG edg2;
	private final List<NodeInfo.Type> ignore = Arrays.asList(NodeInfo.Type.Return);

	public EDGMapping(EDG edg1, EDG edg2)
	{
		this.edg1 = edg1;
		this.edg2 = edg2;
	}

	public Map<Node, Node> map()
	{
		final Node rootT1 = this.edg1.getRootNode();
		final Node rootT2 = this.edg2.getRootNode();
		final Result result = this.RTDMTD(rootT1, rootT2);
		final Map<Node, Node> map = new HashMap<Node, Node>();
		final Backtracking[][] B = result.getB();

		map.put(rootT1, rootT2);
		this.getMap(map, B);

		return map;
	}
	private Result RTDMTD(Node rootT1, Node rootT2)
	{
		final List<Node> rootT1Children = this.getChildren(rootT1);
		final List<Node> rootT2Children = this.getChildren(rootT2);
		final int m = rootT1Children.size();
		final int n = rootT2Children.size();
		final Backtracking[][] b = new Backtracking[m + 1][n + 1];
		final int[][] M = new int[m + 1][n + 1];

		// Matrix initialization
		for (int i = 1; i <= m; i++)
		{
			final Node childT1 = rootT1Children.get(i - 1);
			final List<Node> Ci = this.descendants(childT1);

			M[i][0] = M[i - 1][0];
			M[i][0] += this.getDeleteCost(childT1);
			for (int k = 0; k < Ci.size(); k++)
				M[i][0] += this.getDeleteCost(Ci.get(k));
		}
		for (int j = 1; j <= n; j++)
		{
			final Node childT2 = rootT2Children.get(j - 1);
			final List<Node> Cj = this.descendants(childT2);

			M[0][j] = M[0][j - 1];
			M[0][j] += this.getInsertCost(childT2);
			for (int k = 0; k < Cj.size(); k++)
				M[0][j] += this.getInsertCost(Cj.get(k));
		}

		// Matrix analysis
		for (int i = 1; i <= m; i++)
		{
			final Node childT1 = rootT1Children.get(i - 1);
			final List<Node> Ci = this.descendants(childT1);

			for (int j = 1; j <= n; j++)
			{
				final Node childT2 = rootT2Children.get(j - 1);
				final List<Node> Cj = this.descendants(childT2);

				int D = M[i - 1][j] + this.getDeleteCost(childT1);
				for (int k = 0; k < Ci.size(); k++)
					D += this.getDeleteCost(Ci.get(k));
				int I = M[i][j - 1] + this.getInsertCost(childT2);
				for (int k = 0; k < Cj.size(); k++)
					I += this.getInsertCost(Cj.get(k));
				int S = M[i - 1][j - 1];
				int N = Integer.MAX_VALUE;
				Backtracking[][] b1 = null;

				if (this.equals(childT1, childT2))
				{
					final Result r = this.RTDMTD(childT1, childT2);
					b1 = r.getB();
					N = S + r.getCost();
				}
				S += this.getUpdateCost(childT1, childT2);
				for (int k = 0; k < Cj.size(); k++)
					S += this.getInsertCost(Cj.get(k));
				for (int k = 0; k < Ci.size(); k++)
					S += this.getDeleteCost(Ci.get(k));

				M[i][j] = Math.min(Math.min(D, I), Math.min(S, N));
				final Backtracking.Operation operation = M[i][j] == D ? Backtracking.Operation.Delete : M[i][j] == I ? Backtracking.Operation.Insert : M[i][j] == S ? Backtracking.Operation.Substitute : Backtracking.Operation.NoOp;
				final Backtracking[][] b2 = operation == Backtracking.Operation.NoOp ? b1 : null;
				b[i][j] = new Backtracking(childT1, childT2, b2, operation);
			}
		}

		return new Result(b, M[m][n]);
	}

	private boolean equals(Node node1, Node node2)
	{
		final String name1 = node1.getData().getName();
		final String name2 = node2.getData().getName();
		if (name1 == null && name2 == null)
			return true;
		if (name1 == null || name2 == null)
			return false;
		return name1.equals(name2);
	}
	private List<Node> getChildren(Node node)
	{
		final List<Node> descendants = new LinkedList<Node>();
		final List<Node> children = EDGTraverser.getChildren(node);

		for (Node child : children)
		{
			if (this.ignore.contains(child.getData().getType()))
				descendants.addAll(this.getChildren(child));
			else
				descendants.add(child);
		}

		return descendants;
	}
	private List<Node> descendants(Node node)
	{
		final List<Node> descendants = EDGTraverser.getDescendants(node);

		descendants.removeIf(descendant -> this.ignore.contains(descendant.getData().getType()));

		return descendants;
	}
	private int getDeleteCost(Node node)
	{
		return 1;
	}
	private int getInsertCost(Node node)
	{
		return 1;
	}
	private int getUpdateCost(Node node1, Node node2)
	{
		return 1;
	}

	private void getMap(Map<Node, Node> map, Backtracking[][] B)
	{
		int rowIndex = B.length - 1;
		int colIndex = B[rowIndex].length - 1;

		while (rowIndex > 0 && colIndex > 0)
		{
			final Backtracking b = B[rowIndex][colIndex];
			final Node src = b.getSrc();
			final Node dst = b.getDst();
			final Backtracking[][] next = b.getNext();
			final Backtracking.Operation operation = b.getOperation();

			switch (operation)
			{
				case Delete:
					rowIndex--;
					break;
				case Insert:
					colIndex--;
					break;
				case NoOp:
					map.put(src, dst);
					this.getMap(map, next);
				case Substitute:
					rowIndex--;
					colIndex--;
					break;
				default:
					break;
			}
		}
	}

	private static class Result
	{
		private final Backtracking[][] B;
		private final int cost;

		public Result(Backtracking[][] B, int cost)
		{
			this.B = B;
			this.cost = cost;
		}

		public Backtracking[][] getB()
		{
			return this.B;
		}
		public int getCost()
		{
			return this.cost;
		}
	}
	private static class Backtracking
	{
		public enum Operation { Delete, Insert, Substitute, NoOp }

		private final Node src;
		private final Node dst;
		private final Backtracking[][] next;
		private final Operation operation;

		public Backtracking(Node src, Node dst, Backtracking[][] next, Operation operation)
		{
			this.src = src;
			this.dst = dst;
			this.next = next;
			this.operation = operation;
		}

		public Node getSrc()
		{
			return this.src;
		}
		public Node getDst()
		{
			return this.dst;
		}
		public Backtracking[][] getNext()
		{
			return this.next;
		}
		public Operation getOperation()
		{
			return this.operation;
		}
	}
}