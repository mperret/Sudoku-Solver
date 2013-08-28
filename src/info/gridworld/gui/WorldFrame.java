/* 
 * AP(r) Computer Science GridWorld Case Study:
 * Copyright(c) 2002-2006 College Entrance Examination Board 
 * (http://www.collegeboard.com).
 *
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * @author Julie Zelenski
 * @author Chris Nevison
 * @author Cay Horstmann
 */
package info.gridworld.gui;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;
import info.gridworld.world.World;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
/**
 * The WorldFrame displays a World and allows manipulation of its occupants. <br />
 * This code is not tested on the AP CS A and AB exams. It contains GUI
 * implementation details that are not intended to be understood by AP CS
 * students.
 */
public class WorldFrame<T> extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GUIController<T> control;
	private GridPanel display;
	private JTextArea messageArea;
	private ArrayList<JMenuItem> menuItemsDisabledDuringRun;
	private World<T> world;
	private ResourceBundle resources;
	private DisplayMap displayMap;
	@SuppressWarnings("rawtypes")
	private Set<Class> gridClasses;
	private JMenu newGridMenu;
	private static int count = 0;
	/**
	 * Constructs a WorldFrame that displays the occupants of a world
	 * 
	 * @param world
	 *            the world to display
	 */
	@SuppressWarnings("rawtypes")
	public WorldFrame(World<T> world)
	{
		this.world = world;
		count++;
		resources = ResourceBundle.getBundle(getClass().getName() + "Resources");
		try
		{
			System.setProperty("sun.awt.exception.handler", GUIExceptionHandler.class.getName());
		}
		catch (SecurityException ex)
		{
			// will fail in an applet
		}
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent event)
			{
				count--;
				if (count == 0)
					System.exit(0);
			}
		});
		displayMap = new DisplayMap();
		String title = System.getProperty("info.gridworld.gui.frametitle");
		if (title == null)
			title = resources.getString("frame.title");
		setTitle(title);
		setLocation(25, 15);
		URL appIconUrl = getClass().getResource("GridWorld.gif");
		ImageIcon appIcon = new ImageIcon(appIconUrl);
		setIconImage(appIcon.getImage());
		makeMenus();
		JPanel content = new JPanel();
		content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		content.setLayout(new BorderLayout());
		setContentPane(content);
		display = new GridPanel(displayMap, resources);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher()
		{
			public boolean dispatchKeyEvent(KeyEvent event)
			{
				if (getFocusOwner() == null)
					return false;
				String text = KeyStroke.getKeyStrokeForEvent(event).toString();
				final String PRESSED = "pressed ";
				int n = text.indexOf(PRESSED);
				if (n < 0)
					return false;
				// filter out modifier keys; they are neither characters or
				// actions
				if (event.getKeyChar() == KeyEvent.CHAR_UNDEFINED && !event.isActionKey())
					return false;
				text = text.substring(0, n) + text.substring(n + PRESSED.length());
				boolean consumed = getWorld().keyPressed(text, display.getCurrentLocation());
				if (consumed)
					repaint();
				return consumed;
			}
		});
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewport(new PseudoInfiniteViewport(scrollPane));
		scrollPane.setViewportView(display);
		content.add(scrollPane, BorderLayout.CENTER);
		gridClasses = new TreeSet<Class>(new Comparator<Class>()
		{
			public int compare(Class a,Class b)
			{
				return a.getName().compareTo(b.getName());
			}
		});
		for (String name : world.getGridClasses())
			try
			{
				gridClasses.add(Class.forName(name));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		Grid<T> gr = world.getGrid();
		gridClasses.add(gr.getClass());
		makeNewGridMenu();
		control = new GUIController<T>(this, display, displayMap, resources);
		content.add(control.controlPanel(), BorderLayout.SOUTH);
		messageArea = new JTextArea(2, 35);
		messageArea.setEditable(false);
		messageArea.setFocusable(false);
		messageArea.setBackground(new Color(0xFAFAD2));
		pack();
		repaint(); // to show message
		display.setGrid(gr);
	}
	public void repaint()
	{
		String message = getWorld().getMessage();
		if (message == null)
			message = resources.getString("message.default");
		messageArea.setText(message);
		messageArea.repaint();
		display.repaint(); // for applet
		super.repaint();
	}
	/**
	 * Gets the world that this frame displays
	 * 
	 * @return the world
	 */
	public World<T> getWorld()
	{
		return world;
	}
	/**
	 * Sets a new grid for this world. Occupants are transferred from the old
	 * world to the new.
	 * 
	 * @param newGrid
	 *            the new grid
	 */
	public void setGrid(Grid<T> newGrid)
	{
		Grid<T> oldGrid = world.getGrid();
		Map<Location, T> occupants = new HashMap<Location, T>();
		for (Location loc : oldGrid.getOccupiedLocations())
			occupants.put(loc, world.remove(loc));
		world.setGrid(newGrid);
		for (Location loc : occupants.keySet())
		{
			if (newGrid.isValid(loc))
				world.add(loc, occupants.get(loc));
		}
		display.setGrid(newGrid);
		repaint();
	}
	/**
	 * Displays an error message
	 * 
	 * @param t
	 *            the throwable that describes the error
	 * @param resource
	 *            the resource whose .text/.title strings should be used in the
	 *            dialog
	 */
	public void showError(Throwable t,String resource)
	{
		String text;
		try
		{
			text = resources.getString(resource + ".text");
		}
		catch (MissingResourceException e)
		{
			text = resources.getString("error.text");
		}
		String title;
		try
		{
			title = resources.getString(resource + ".title");
		}
		catch (MissingResourceException e)
		{
			title = resources.getString("error.title");
		}
		String reason = resources.getString("error.reason");
		String message = text + "\n" + MessageFormat.format(reason, new Object[] { t });
		JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
	}
	// Creates the drop-down menus on the frame.
	private JMenu makeMenu(String resource)
	{
		JMenu menu = new JMenu();
		configureAbstractButton(menu, resource);
		return menu;
	}
	private void configureAbstractButton(AbstractButton button,String resource)
	{
		String title = resources.getString(resource);
		int i = title.indexOf('&');
		int mnemonic = 0;
		if (i >= 0)
		{
			mnemonic = title.charAt(i + 1);
			title = title.substring(0, i) + title.substring(i + 1);
			button.setText(title);
			button.setMnemonic(Character.toUpperCase(mnemonic));
			button.setDisplayedMnemonicIndex(i);
		}
		else
			button.setText(title);
	}
	private void makeMenus()
	{
		JMenuBar mbar = new JMenuBar();
		menuItemsDisabledDuringRun = new ArrayList<JMenuItem>();
		newGridMenu = makeMenu("menu.file.new");
		menuItemsDisabledDuringRun.add(newGridMenu);
		setRunMenuItemsEnabled(true);
		setJMenuBar(mbar);
	}
	private void makeNewGridMenu()
	{
		newGridMenu.removeAll();
		MenuMaker<T> maker = new MenuMaker<T>(this, resources, displayMap);
		maker.addConstructors(newGridMenu, gridClasses);
	}
	/**
	 * Sets the enabled status of those menu items that are disabled when
	 * running.
	 * 
	 * @param enable
	 *            true to enable the menus
	 */
	public void setRunMenuItemsEnabled(boolean enable)
	{
		for (JMenuItem item : menuItemsDisabledDuringRun)
			item.setEnabled(enable);
	}
	/**
	 * Nested class that is registered as the handler for exceptions on the
	 * Swing event thread. The handler will put up an alert panel and dump the
	 * stack trace to the console.
	 */
	public class GUIExceptionHandler
	{
		public void handle(Throwable e)
		{
			e.printStackTrace();
			JTextArea area = new JTextArea(10, 40);
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			area.setText(writer.toString());
			area.setCaretPosition(0);
			String copyOption = resources.getString("dialog.error.copy");
			JOptionPane pane = new JOptionPane(new JScrollPane(area), JOptionPane.ERROR_MESSAGE, JOptionPane.YES_NO_OPTION, null, new String[] { copyOption,
					resources.getString("cancel") });
			pane.createDialog(WorldFrame.this, e.toString()).setVisible(true);
			if (copyOption.equals(pane.getValue()))
			{
				area.setSelectionStart(0);
				area.setSelectionEnd(area.getText().length());
				area.copy(); // copy to clipboard
			}
		}
	}
}
