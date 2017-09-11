package presentation;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import logic.Logic;

public class Results extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final Logic logic = Logic.getLogic();
	private final DefaultTableCellRenderer redRenderer = new DefaultTableCellRenderer();
	private final DefaultTableCellRenderer greenRenderer = new DefaultTableCellRenderer();
	private String[][] data = new String[0][7];

	public Results()
	{
		this.initComponents();
		this.addComponents();
	}
	private void initComponents()
	{
		this.setLayout(null);

		this.redRenderer.setForeground(Color.RED);				// Red
		this.greenRenderer.setForeground(new Color(0, 150, 0));	// Green
	}
	private void addComponents()
	{
		final JScrollPane resultsTable = this.createResultsTable();
		final JButton magicButton = new JButton("Voy a tener suerte");

		resultsTable.setBounds(10, 10, 525, 225);
		magicButton.setBounds(200, 240, 150, 30);

		magicButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				Results.this.updateData();
			}
		});

		this.add(resultsTable);
		this.add(magicButton);
	}

	private JScrollPane createResultsTable()
	{
		final Object[] columns = this.getColumns();
		final Object[][] data = this.getData();

		final JTable resultsTable = new JTable(data, columns) {
			private static final long serialVersionUID = 1L;

			public TableCellRenderer getCellRenderer(int arg0, int arg1) {
				if (arg1 < 4)
					return super.getCellRenderer(arg0, arg1);

				final String text1 = (String) getValueAt(arg0, arg1 - 3);
				final String text2 = (String) getValueAt(arg0, arg1);
				final DefaultTableCellRenderer renderer = Results.this.getRenderer(text1, text2);

				return renderer != null ? renderer : super.getCellRenderer(arg0, arg1);
			}
			public boolean isCellEditable(int row, int column) { return false; }
		};
		final JScrollPane scrollPane = new JScrollPane(resultsTable);

		resultsTable.setFillsViewportHeight(true);
		resultsTable.setAutoCreateRowSorter(true);
		resultsTable.getColumnModel().getColumn(0).setPreferredWidth(125);
		resultsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e)
			{
				if (e.getButton() != 1 || e.getClickCount() != 2)
					return;
				final int selectedRow = resultsTable.getSelectedRow();
				final String message = (String) data[selectedRow][7];

				JOptionPane.showMessageDialog(null, message);
			}
		});
		scrollPane.setViewportView(resultsTable);

		return scrollPane;
	}
	private String[] getColumns()
	{
		return new String[] { "Benchmark", "P Mejor", "R Mejor", "F1 Mejor", "P Actual", "R Actual", "F1 Actual" };
	}
	private String[][] getData()
	{
		return this.data;
	}
	private DefaultTableCellRenderer getRenderer(String best, String current)
	{
		if (best == null || current == null || best == "" || current == "")
			return null;

		try
		{
			final double percentage1 = Double.parseDouble(best.substring(0, best.indexOf("%")));
			final double percentage2 = Double.parseDouble(current.substring(0, current.indexOf("%")));

			if (percentage1 > percentage2)
				return this.redRenderer;
			if (percentage1 < percentage2)
				return this.greenRenderer;
		}
		catch (Exception e)
		{
			
		}

		return null;
	}

	private void reload()
	{
		this.clear();
		this.addComponents();
		this.refresh();
	}
	private void clear()
	{
		this.removeAll();
	}
	private void refresh()
	{
		this.revalidate();
		this.repaint();
	}

	private void updateData()
	{
		this.data = this.logic.getData();
		this.reload();
	}
}