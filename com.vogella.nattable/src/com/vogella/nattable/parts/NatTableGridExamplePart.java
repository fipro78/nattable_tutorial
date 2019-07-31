package com.vogella.nattable.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableGridExamplePart {
	
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
		
		// create a body layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
		SelectionLayer selectionLayer = new SelectionLayer(columnReorderLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// create a column header layer stack
        Map<String, String> propertyToLabelMap = new HashMap<String, String>();
        propertyToLabelMap.put("firstName", "Firstname");
        propertyToLabelMap.put("lastName", "Lastname");
        propertyToLabelMap.put("gender", "Gender");
        propertyToLabelMap.put("married", "Married");
        propertyToLabelMap.put("birthday", "Birthday");
        
		IDataProvider columnHeaderDataProvider = 
				new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
		DataLayer columnHeaderDataLayer = 
				new DataLayer(columnHeaderDataProvider);
		ILayer columnHeaderLayer = 
				new ColumnHeaderLayer(columnHeaderDataLayer, viewportLayer, selectionLayer);

		// create a row header layer stack
		IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
		DataLayer rowHeaderDataLayer = new DataLayer(rowHeaderDataProvider, 40, 20);
		ILayer rowHeaderLayer = new RowHeaderLayer(
		    rowHeaderDataLayer,
		    viewportLayer,
		    selectionLayer);
		
		// create a corner layer stack
		IDataProvider cornerDataProvider =
			    new DefaultCornerDataProvider(
			        columnHeaderDataProvider,
			        rowHeaderDataProvider);
			DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
			ILayer cornerLayer = new CornerLayer(
			    cornerDataLayer,
			    rowHeaderLayer,
			    columnHeaderLayer);
			
		// create a GridLayer using the created layer stacks
		GridLayer gridLayer = 
				new GridLayer(
						viewportLayer, 
						columnHeaderLayer, 
						rowHeaderLayer, 
						cornerLayer);
			
		// create the NatTable instance using the GridLayer instance
		NatTable natTable = new NatTable(parent, gridLayer);
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}