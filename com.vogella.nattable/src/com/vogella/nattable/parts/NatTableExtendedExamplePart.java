package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NatTableExtendedExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ReflectiveColumnPropertyAccessor
		
		// create a ListDataProvider
		
		// create a body layer stack
		
		// create a column header layer stack with column grouping

		// create a row header layer stack with row grouping

		// create a corner layer stack
		
		// create a GridLayer using the created layer stacks
		
		// create the NatTable instance using the GridLayer instance
		NatTable natTable = null;
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}