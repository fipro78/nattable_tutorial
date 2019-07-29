package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NatTableDataExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create a ListDataProvider using the custom IColumnAccessor
		
		// create a layer stack
		// DataLayer is sufficient for this example

		// create the NatTable instance
		NatTable natTable = null;
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	// create a custom IColumnAccessor to access column data per row object
}