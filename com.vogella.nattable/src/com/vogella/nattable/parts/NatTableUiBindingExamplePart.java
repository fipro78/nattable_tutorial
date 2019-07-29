package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NatTableUiBindingExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ReflectiveColumnPropertyAccessor
		
		// create a ListDataProvider
		
		// create a body layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		
		// register the command handler
		
		// create a column header layer stack

		// create a row header layer stack

		// create a corner layer stack
		
		// create a GridLayer using the created layer stacks
		
		// register the menu configurations
		
		// create the NatTable instance using the GridLayer instance
		NatTable natTable = null;
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	// create a command to delete a row
	
	// create a command handler to delete a row
	
	// create a configuration to open a body menu that
	// - selects a row if not selected yet
	// - opens a menu with a menu entry to delete the selected row
}