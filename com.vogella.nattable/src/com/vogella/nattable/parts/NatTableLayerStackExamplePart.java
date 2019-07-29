package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableLayerStackExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// property names of the Person class
        String[] propertyNames = {
                "firstName",
                "lastName",
                "gender",
                "married",
                "birthday" };
        
		// create the ReflectiveColumnPropertyAccessor
        IColumnPropertyAccessor<Person> columnPropertyAccessor =
                new ReflectiveColumnPropertyAccessor<Person>(propertyNames);

        // create a ListDataProvider
        IDataProvider bodyDataProvider =
        		new ListDataProvider<Person>(
        				personService.getPersons(50),
        				columnPropertyAccessor);
		
		// create a layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		// remember to set the regionName on the ViewportLayer to GridRegion.BODY
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
		
		// as the selection mouse bindings are registered for the region label
        // GridRegion.BODY
        // we need to set that region label to the viewport so the selection via mouse
        // is working correctly
        viewportLayer.setRegionName(GridRegion.BODY);

        // create the NatTable instance
        NatTable natTable = new NatTable(parent, viewportLayer);
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}