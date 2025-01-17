/*
*  $Id$
*
*  This is open-source software written by ILEX Engineering, Inc., under
*  contract to the federal government. You are free to copy and use this
*  source code for your own purposes, except that no part of the information
*  contained in this file may be claimed to be proprietary.
*
*  Except for specific contractual terms between ILEX and the federal 
*  government, this source code is provided completely without warranty.
*  For more information contact: info@ilexeng.com
*
*  $Log$
*  Revision 1.3  2008/07/23 20:44:03  dlittle
*  Included finishedJob() method
*
*  Revision 1.2  2008/07/23 20:20:11  dlittle
*  changes made to allow test dialog to add northern panel
*
*  Revision 1.1  2008/04/04 18:21:09  cvs
*  Added legacy code to repository
*
*  Revision 1.4  2008/02/10 20:17:35  mmaloney
*  dev
*
*  Revision 1.2  2008/01/31 21:43:40  cvs
*  added code for internationalization
*
*  Revision 1.3  2006/08/19 01:19:46  mmaloney
*  dev
*
*  Revision 1.2  2005/08/21 14:18:40  mjmaloney
*  LRGS 5.1
*
*  Revision 1.1  2005/08/03 21:50:45  mjmaloney
*  Implementing new RtStat application.
*
*/
package ilex.gui;

import ilex.util.LoadResourceBundle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

/**
 * This dialog is used when a lengthy job must be done and we don't want
 * to freeze the GUI. It contains a scrolling text area showing the progress
 * of the job.
 * @author mjmaloney
 *
 */
public class JobDialog
	extends JDialog
{
	private static ResourceBundle labels = null;
	private JPanel panel1 = new JPanel();
	private BorderLayout borderLayout1 = new BorderLayout();
	private JPanel jPanel1 = new JPanel();
	private JLabel oneMomentPleaseLabel = new JLabel();
	private FlowLayout flowLayout1 = new FlowLayout();
	private JPanel jPanel2 = new JPanel();
	private TitledBorder titledBorder1;
	private BorderLayout borderLayout2 = new BorderLayout();
	private JScrollPane progressScrollPane = new JScrollPane();
	private JTextArea progressArea = new JTextArea();
	private JPanel jPanel3 = new JPanel();
	private JButton cancelButton = new JButton();

	private boolean cancelled;

	/**
	 * Constructor.
	 * @param frame parent frame or null if none.
	 * @param title Title bar string
	 * @param modal true if this should be model.
	 */
	public JobDialog(Frame frame, String title, boolean modal)
	{
		super(frame, title, modal);
		doInit(title);
	}

	public JobDialog(JDialog parent, String title, boolean modal)
	{
		super(parent, title, modal);
		doInit(title);
	}
	
	/**
	 * when called the cancel button will change its text to
	 * "Done"
	 *
	 */
	public void finishedJob()
	{
		cancelButton.setText("Done");
	}
	
	public void setNorthPanel(JPanel newNorth)
	{
		panel1.remove(jPanel1);
		jPanel1=newNorth;
		panel1.add(jPanel1,BorderLayout.NORTH);
	}

	public void doInit(String title)
	{
		getLabels();
		try
		{
			jbInit();
			pack();
			progressArea.setEditable(false);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		cancelled = false;
		addToProgress(title);
		
		this.setSize(600,280);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		if (frameSize.height > screenSize.height)
		{
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width)
		{
			frameSize.width = screenSize.width;
		}
		this.setLocation( (screenSize.width - frameSize.width) / 2,
						  (screenSize.height - frameSize.height) / 2);

	}
	
	public static ResourceBundle getLabels() 
	{
		if (labels == null)
			//Load the labels properties file
			labels = LoadResourceBundle.getLabelDescriptions(
					"ilex/resources/gui", null);
		return labels;
	}
	/** no-args constructor for JBuilder. */
//	public JobDialog()
//	{
//		this(null, "", false);
//	}

	/**
	 * GUI component initialization
	 * @throws Exception
	 */
	private void jbInit()
		throws Exception
	{
		titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.
			white, new Color(165, 163, 151)), labels.getString(
					"JobDialog.progress"));
		panel1.setLayout(borderLayout1);
		this.setModal(true);
		oneMomentPleaseLabel.setText(labels.getString("JobDialog.oneMoment"));
		jPanel1.setLayout(flowLayout1);
		flowLayout1.setVgap(10);
		jPanel2.setBorder(titledBorder1);
		jPanel2.setLayout(borderLayout2);
		progressArea.setLineWrap(true);
		progressArea.setWrapStyleWord(true);
		progressScrollPane.setHorizontalScrollBarPolicy(JScrollPane.
			HORIZONTAL_SCROLLBAR_NEVER);
		cancelButton.setText(labels.getString("EditPropsAction.cancel"));
		cancelButton.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelButton_actionPerformed(e);
			}
		});
		getContentPane().add(panel1);
		panel1.add(jPanel1, BorderLayout.NORTH);
		jPanel1.add(oneMomentPleaseLabel, null);
		panel1.add(jPanel2, BorderLayout.CENTER);
		jPanel2.add(progressScrollPane, BorderLayout.CENTER);
		panel1.add(jPanel3, BorderLayout.SOUTH);
		jPanel3.add(cancelButton, null);
		progressScrollPane.getViewport().add(progressArea, null);
	}

	/**
	 * Called when Cancel button is pressed.
	 * @param e ActionEvent
	 */
	void cancelButton_actionPerformed(ActionEvent e)
	{
		cancelled = true;
		closeDlg();
	}

	/**
	 * @return true if job was cancelled, else false.
	 */
	public boolean wasCancelled()
	{
		return cancelled;
	}
	/**
	 * Adds a string to the end of the progress text area.
	 * @param str String
	 */
	public void addToProgress(final String str)
	{
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					progressArea.append(str + "\n");
					progressArea.setCaretPosition(
						progressArea.getText().length());
				}
		  	});
	}
	
	/**
	 * adds a string to the end of the progress area without
	 * adding a line feed to the end.
	 * @param str
	 */
	public void addToProgressNLF(final String str)
	{
		SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					progressArea.append(str);
					progressArea.setCaretPosition(
						progressArea.getText().length());
				}
		  	});
	}

	/**
	 * Call this method from another thread when the job has finished.
	 */
	public void allDone()
	{
		SwingUtilities.invokeLater(
		    new Runnable()
			{
				public void run()
				{
					cancelled = false;
					closeDlg();
				}
			});
	}

	/** Closes the dialog. */
	void closeDlg()
	{
		setVisible(false);
		dispose();
	}

	/**
	 * The default is for the cancel button to be enabled. Call this method
	 * with false for cases where the job may not be cancelled.
	 * @param canCancel
	 */
	public void setCanCancel(boolean canCancel)
	{
	    cancelButton.setEnabled(canCancel);
	}
}
