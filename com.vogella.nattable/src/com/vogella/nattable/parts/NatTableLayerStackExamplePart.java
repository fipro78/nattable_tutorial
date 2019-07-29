package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.PersonService;

public class NatTableLayerStackExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ReflectiveColumnPropertyAccessor
		
		// create a ListDataProvider
		
		// create a layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		// remember to set the regionName on the ViewportLayer to GridRegion.BODY
		
		// create the NatTable instance
		NatTable natTable = null;
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}