package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.PersonService;

public class NatTableStylingExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ReflectiveColumnPropertyAccessor
		
		// create a ListDataProvider
		
		// create a layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		// remember to set the regionName on the ViewportLayer to GridRegion.BODY
		
		// register the config label accumulator on the DataLayer
		
		// create the NatTable instance without default configuration
		NatTable natTable = null;
		
		//add the default style configuration
		
		//add the custom style configuration
		
		//call configure to populate the IConfigRegistry
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	// create a custom IConfigLabelAccumulator to mark female persons
	
	// create a custom style configuration
}