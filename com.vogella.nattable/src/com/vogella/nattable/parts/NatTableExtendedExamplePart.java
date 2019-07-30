package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.PersonService;

public class NatTableExtendedExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ExtendedReflectiveColumnPropertyAccessor for ExtendedPersonWithAddress
		
		// create a ListDataProvider
		
		// create a body layer stack with a sorted collection by last name
		
		// create a column header layer stack with column grouping
		
		// configure the column groups

		// create a row header layer stack with row grouping
		
		// configure the row groups
		// collect containing last names and the number of persons with that last name

		// create a corner layer stack
		
		// create a GridLayer using the created layer stacks
		
		// create the NatTable instance using the GridLayer instance
		NatTable natTable = null;
		
		// register a column group header menu

		// register a row group header menu

		// register display persistence dialog
		
		// register column chooser
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	// create an AbstractLayerTransform to encapsulate the body layer stack
}