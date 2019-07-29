package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.PersonService;

public class NatTableEditingExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ReflectiveColumnPropertyAccessor
		
		// create a ListDataProvider
		
		// create a body layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		
		// register the ColumnLabelAccumulator
		
		// create a column header layer stack

		// create a row header layer stack

		// create a corner layer stack
		
		// create a GridLayer using the created layer stacks
		
		// create the NatTable instance using the GridLayer instance without default configuration
		NatTable natTable = null;
		
		//add the default style configuration
		
		//add a custom editing configuration
		// - enable editing for all cells
		// - register a combo box cell editor for Gender
		// - register a checkbox editor, checkbox painter and a boolean converter for the married column
		
		//call configure to populate the IConfigRegistry
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}