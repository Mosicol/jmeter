/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
package org.apache.jmeter.config.gui;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import junit.framework.TestCase;

import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.ObjectTableModel;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

/****************************************
 * Title: JMeter Description: Copyright: Copyright (c) 2000 Company: Apache
 *
 *@author    Michael Stover
 *@created   March 13, 2001
 *@version   1.0
 ***************************************/

public class ArgumentsPanel extends AbstractConfigGui implements FocusListener, ActionListener, CellEditorListener
{
    transient private static Logger log = Hierarchy.getDefaultHierarchy().getLoggerFor(JMeterUtils.GUI);
    transient JTable table;
    JButton add;
    JButton delete;
    protected transient ObjectTableModel tableModel;
    String name;
    JLabel tableLabel;

    private static String ADD = "add";
    private static String DELETE = "delete";

    /****************************************
     * Constructor for the ArgumentsPanel object
     ***************************************/
    public ArgumentsPanel()
    {
        this(JMeterUtils.getResString("paramtable"));
    }

    public void editingCanceled(ChangeEvent e)
    {}

    public void editingStopped(ChangeEvent e)
    {}

    public ArgumentsPanel(String label)
    {
        tableLabel = new JLabel(label);
        init();
    }

    protected JTable getTable()
    {
        return table;
    }

    protected JLabel getTableLabel()
    {
        return tableLabel;
    }

    protected JButton getDeleteButton()
    {
        return delete;
    }

    protected JButton getAddButton()
    {
        return add;
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public Collection getMenuCategories()
    {
        return null;
    }

    /****************************************
     * !ToDoo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public String getStaticLabel()
    {
        return "Argument List";
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@return   !ToDo (Return description)
     ***************************************/
    public TestElement createTestElement()
    {
        Arguments args = new Arguments();
        modifyTestElement(args);
        return (TestElement) args.clone();
    }

    /**
     * Modifies a given TestElement to mirror the data in the gui components.
     * @see org.apache.jmeter.gui.JMeterGUIComponent#modifyTestElement(TestElement)
     */
    public void modifyTestElement(TestElement args)
    {
        Iterator modelData = tableModel.iterator();
        Arguments arguments = null;
        if (args instanceof Arguments)
        {
            arguments = (Arguments) args;
            arguments.clear();
            while (modelData.hasNext())
            {
                Argument arg = (Argument) modelData.next();
                arg.setMetaData("=");
                arguments.addArgument(arg);
            }
        }
        this.configureTestElement(args);
    }

    /****************************************
     * !ToDo (Method description)
     *
     *@param el  !ToDo (Parameter description)
     ***************************************/
    public void configure(TestElement el)
    {
        super.configure(el);
        if (el instanceof Arguments)
        {
            tableModel.clearData();
            PropertyIterator iter = ((Arguments) el).iterator();
            while (iter.hasNext())
            {
                Argument arg = (Argument) iter.next().getObjectValue();
                tableModel.addRow(arg);
            }
        }
        checkDeleteStatus();
    }

    /****************************************
     * Description of the Method
     *
     *@param e  Description of Parameter
     ***************************************/
    public void focusLost(FocusEvent e)
    {
        log.debug("Focus lost on table");
        stopTableEditing();
    }

    /****************************************
     * Description of the Method
     *
     *@param e  Description of Parameter
     ***************************************/
    public void focusGained(FocusEvent e)
    {}

    /****************************************
     * Description of the Method
     *
     *@param e  Description of Parameter
     ***************************************/
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();
        if (action.equals(DELETE))
        {
            deleteArgument();
        }
        else if (action.equals(ADD))
        {
            addArgument();
        }
    }

    protected void deleteArgument()
    {
        // If a table cell is being edited, we must cancel the editing before
        // deleting the row
        if (table.isEditing())
        {
            TableCellEditor cellEditor = table.getCellEditor(table.getEditingRow(), table.getEditingColumn());
            cellEditor.cancelCellEditing();
        }

        int rowSelected = table.getSelectedRow();
        if (rowSelected >= 0)
        {
            tableModel.removeRow(rowSelected);
            tableModel.fireTableDataChanged();

            // Disable DELETE if there are no rows in the table to delete.
            if (tableModel.getRowCount() == 0)
            {
                delete.setEnabled(false);
            }

            // Table still contains one or more rows, so highlight (select)
            // the appropriate one.
            else
            {
                int rowToSelect = rowSelected;

                if (rowSelected >= tableModel.getRowCount())
                {
                    rowToSelect = rowSelected - 1;
                }

                table.setRowSelectionInterval(rowToSelect, rowToSelect);
            }
        }
    }

    protected void addArgument()
    {
        // If a table cell is being edited, we should accept the current value
        // and stop the editing before adding a new row.
        stopTableEditing();

        tableModel.addRow(makeNewArgument());

        // Enable DELETE (which may already be enabled, but it won't hurt)
        delete.setEnabled(true);

        // Highlight (select) the appropriate row.
        int rowToSelect = tableModel.getRowCount() - 1;
        table.setRowSelectionInterval(rowToSelect, rowToSelect);
    }

    protected Object makeNewArgument()
    {
        return new Argument("","");
    }

    private void stopTableEditing()
    {
        if (table.isEditing())
        {
            TableCellEditor cellEditor = table.getCellEditor(table.getEditingRow(), table.getEditingColumn());
            cellEditor.stopCellEditing();
        }
    }

    /****************************************
     * !ToDo
     ***************************************/
    private Component makeMainPanel()
    {
        initializeTableModel();
        table = new JTable(tableModel);
        table.addFocusListener(this);
        // use default editor/renderer to fix bug #16058
        //		TextAreaTableCellEditor editor = new TextAreaTableCellEditor();
        //		table.setDefaultEditor(String.class,
        //				editor);
        //		editor.addCellEditorListener(this);
        //		TextAreaCellRenderer renderer = new TextAreaCellRenderer();
        //		table.setRowHeight(renderer.getPreferredHeight());
        //		table.setDefaultRenderer(String.class,renderer);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(pane.getMinimumSize());
        return pane;
    }

    private JPanel makeButtonPanel() {
        add = new JButton(JMeterUtils.getResString("add"));
        add.setActionCommand(ADD);
        add.setEnabled(true);
        
        delete = new JButton(JMeterUtils.getResString("delete"));
        delete.setActionCommand(DELETE);
        
        checkDeleteStatus();
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add.addActionListener(this);
        delete.addActionListener(this);
        buttonPanel.add(add);
        buttonPanel.add(delete);
        return buttonPanel;
    }

    protected void initializeTableModel()
    {
        tableModel = new ObjectTableModel(new String[] { Arguments.COLUMN_NAMES[0], Arguments.COLUMN_NAMES[1] }, 
                new String[]{"name","value"},new Class[]{String.class,String.class},new Class[] { String.class, String.class },
                new Argument());
    }

    protected void checkDeleteStatus()
    {
        // Disable DELETE if there are no rows in the table to delete.
        if (tableModel.getRowCount() == 0)
        {
            delete.setEnabled(false);
        }
        else
        {
            delete.setEnabled(true);
        }
    }

    /****************************************
     * !ToDo (Method description)
     **************************************
    public void removeInnerPanel()
    {
    	table.setEnabled(false);
    	add.setEnabled(false);
    	delete.setEnabled(false);
    	tableModel.removeAllRows();
    	tableModel.fireTableDataChanged();
    
    	this.remove(innerPanel);
    	innerPanel = null;
    }*/

    private void init()
    {
        setLayout(new BorderLayout());

        add(makeLabelPanel(), BorderLayout.NORTH);
        add(makeMainPanel(), BorderLayout.CENTER);
        // Force a minimum table height of 70 pixels
        add(Box.createVerticalStrut(70), BorderLayout.WEST);
        add(makeButtonPanel(), BorderLayout.SOUTH);

        table.revalidate();
        sizeColumns(table);
    }

    protected Component makeLabelPanel()
    {
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(tableLabel);
        return labelPanel;
    }

    protected void sizeColumns(JTable table)
    {}

    public static class Test extends TestCase
    {

        public Test(String name)
        {
            super(name);
        }

        public void testArgumentCreation() throws Exception
        {
            ArgumentsPanel gui = new ArgumentsPanel();
            gui.tableModel.addRow(new Argument());
            gui.tableModel.setValueAt("howdy", 0, 0);
            gui.tableModel.addRow(new Argument());
            gui.tableModel.setValueAt("doody", 0, 1);
            assertEquals("=", ((Argument) ((Arguments) gui.createTestElement()).getArguments().get(0).getObjectValue()).getMetaData());
        }
    }
}
