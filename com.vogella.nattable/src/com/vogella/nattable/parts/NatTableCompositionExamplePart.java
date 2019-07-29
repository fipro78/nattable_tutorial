package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NatTableCompositionExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ReflectiveColumnPropertyAccessor
		
		// create a ListDataProvider
		
		// create a body layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		
		// create a column header layer stack using using the ColumnHeaderLayer
		
		// create a CompositeLayer with 1 column and 2 rows
		// register the column header layer for GridRegion.COLUMN_HEADER on the first row
		// register the body layer for GridRegion.BODY on the second row
		
		// create the NatTable instance using the CompositeLayer instance
		NatTable natTable = null;
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}