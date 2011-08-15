/**
 * Copyright (c) 2009 - 2011 AppWork UG(haftungsbeschränkt) <e-mail@appwork.org>
 * 
 * This file is part of org.appwork.utils.swing.table
 * 
 * This software is licensed under the Artistic License 2.0,
 * see the LICENSE file or http://www.opensource.org/licenses/artistic-license-2.0.php
 * for details
 */
package org.appwork.utils.swing.table.columnmenu;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.appwork.resources.AWUTheme;
import org.appwork.utils.locale.APPWORKUTILS;
import org.appwork.utils.swing.table.ExtTable;

/**
 * @author thomas
 * 
 */
public class ResetColumns extends AbstractAction {

    private final ExtTable<?> table;

    /**
     * @param extTable
     */
    public ResetColumns(final ExtTable<?> extTable) {
        super(APPWORKUTILS.T.ResetColumnsAction());
        this.putValue(Action.SMALL_ICON, AWUTheme.getInstance().getIcon("refresh", 16));
        this.table = extTable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        this.table.resetColumnDimensions();
        this.table.resetColumnOrder();
        this.table.resetColumnVisibility();
        this.table.updateColumns();

    }

}
