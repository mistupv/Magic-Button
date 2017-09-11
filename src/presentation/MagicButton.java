package presentation;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MagicButton extends JFrame
{
	private static final long serialVersionUID = 1L;

	private final JPanel content = new JPanel();
	private final JPanel panel = new Results();

	public MagicButton()
	{
		this.initComponents();
		this.addComponents();

		this.setVisible(true);
	}
	private void initComponents()
	{
		this.setMinimumSize(new Dimension(550, 300));
		this.setSize(new Dimension(550, 300));
		this.setResizable(false);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setTitle("Magic button");

		this.add(this.content);
		this.content.setLayout(new GridBagLayout());
	}
	private void addComponents()
	{
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridy = 0;
		constraints.gridx = 0;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.insets = new Insets(0, 0, 0, 0);
		this.content.add(this.panel, constraints);
	}

	@SuppressWarnings("unused")
	private void reload()
	{
		this.clear();
		this.addComponents();
		this.refresh();
	}
	private void clear()
	{
		this.content.removeAll();
	}
	private void refresh()
	{
		this.content.revalidate();
		this.content.repaint();
	}
}