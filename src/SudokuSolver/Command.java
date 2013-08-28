package SudokuSolver;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
@SuppressWarnings("serial")
public class Command extends JFrame
{
	JPanel contentPane;
	JLabel imageLabel = new JLabel();
	JLabel headerLabel = new JLabel();
	/**
	 * Hey Mr. Smith, or whoever else may be looking at this code! You seem to
	 * be trying to figure out what the Command class is. Sorry, but we
	 * purposely didn't include the source code for it, because it would ruin
	 * the surprise! Unless you have a decompiler, sorry!
	 */
	public Command(SudokuGrid grid)
	{
		String line = JOptionPane.showInputDialog(null, ">>command", "", 1);
		try
		{
			if (line.equals("System.exit(0)") || line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("exit"))//////
				System.exit(0);
			if (line.equalsIgnoreCase("does fabric taste good?") || line.equalsIgnoreCase("does cloth taste good?"))//
				magicMagicWizardMagicParty();
			if (line.equalsIgnoreCase("oh, it's nothing special..."))//
				superFunAwesomeHappyFunTime();
			if (line.equalsIgnoreCase("derpy") || line.equalsIgnoreCase("derp") || line.equalsIgnoreCase("derps"))//
				niceLittleSupriseInTheMiddleOfTheCodeYay();
			if (line.equals("printValidity()"))//
				grid.printValidity();
			if (line.equals("clearArray()"))//
				grid.clearArray();
			if (line.equals("step()"))//
				grid.step();
			if (line.equals("fillPuzzleArray()"))//
				grid.fillPuzzleArray();
			if (line.equals("printPuzzleArray()"))//
				grid.printPuzzleArray();
			if (line.equals("puzzleIsValid()"))//
				JOptionPane.showMessageDialog(null, grid.puzzleIsValid());
			if (line.equals("solve()"))//
			{
				grid.solve(0);
			}
			if (line.equals("isSolved()"))//
				JOptionPane.showMessageDialog(null, grid.isSolved());
			if (line.equals("numIsValid()"))//
			{
				int r = Integer.parseInt(JOptionPane.showInputDialog(null, "r", "", 1));
				int c = Integer.parseInt(JOptionPane.showInputDialog(null, "c", "", 1));
				int n = Integer.parseInt(JOptionPane.showInputDialog(null, "num", "", 1));
				JOptionPane.showMessageDialog(null, grid.numIsValid(r, c, n));
			}
			if (line.equalsIgnoreCase("h") || line.equalsIgnoreCase("help")|| line.equalsIgnoreCase("/?"))//
			{
				JOptionPane.showMessageDialog(null, "/?\nclearArray()\nderp\nderps\nderpy\ndoes cloth taste good?\n"
						+ "does fabric taste good?\nexit\nfillPuzzleArray()\nh\nhelp\nisSolved()\nnumIsValid()\n" +
						"oh, it's nothing special...\nprintPuzzleArray()\nprintValidity()\npuzzleIsValid()\nquit\n" +
						"solve()\nstep()\nSystem.exit(0)","Help",1);
			}
		}
		catch (NullPointerException e)
		{
		}
	}
	private void magicMagicWizardMagicParty()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		setSize(new Dimension(500, 550));
		setTitle("Does Fabric Taste Good?");
		headerLabel.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 40));
		headerLabel.setText("Why yes! Yes it does.");
		contentPane.add(headerLabel, java.awt.BorderLayout.NORTH);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("a.pez"));
		imageLabel.setIcon(ii);
		contentPane.add(imageLabel, java.awt.BorderLayout.CENTER);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	private void superFunAwesomeHappyFunTime()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		setSize(new Dimension(320, 255));
		setTitle("Oh, it's Nothing Special...");
		headerLabel.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 25));
		headerLabel.setText("It's Just My Bass Cannon!");
		contentPane.add(headerLabel, java.awt.BorderLayout.NORTH);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("b.pez"));
		imageLabel.setIcon(ii);
		contentPane.add(imageLabel, java.awt.BorderLayout.CENTER);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	private void niceLittleSupriseInTheMiddleOfTheCodeYay()
	{
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BorderLayout());
		setSize(new Dimension(731, 700));
		setTitle("Derpy");
		headerLabel.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 25));
		headerLabel.setText("I Just Don't Know What Went Wrong!");
		contentPane.add(headerLabel, java.awt.BorderLayout.NORTH);
		ImageIcon ii = new ImageIcon(this.getClass().getResource("c.pez"));
		imageLabel.setIcon(ii);
		contentPane.add(imageLabel, java.awt.BorderLayout.CENTER);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
