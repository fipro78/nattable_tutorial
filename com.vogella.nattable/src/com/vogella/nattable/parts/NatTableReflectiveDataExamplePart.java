package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableReflectiveDataExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
        // property names of the Person class
        String[] propertyNames = { "firstName", "lastName", "gender", "married", "birthday" };

        // create the ReflectiveColumnPropertyAccessor
        IColumnPropertyAccessor<Person> columnPropertyAccessor =
                new ReflectiveColumnPropertyAccessor<Person>(propertyNames);

        // create the data provider
        IDataProvider bodyDataProvider =
                new ListDataProvider<Person>(
                        personService.getPersons(10), columnPropertyAccessor);

        // create a layer stack
        // DataLayer is sufficient for this example
        DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

        // create the NatTable instance
        // use different style bits to avoid rendering of inactive scrollbars for small table
        // Note: The enabling/disabling and showing of the scrollbars is handled by the
        //       ViewportLayer. Without the ViewportLayer the scrollbars will always be
        //       visible with the default style bits of NatTable.
        NatTable natTable = new NatTable(
                parent,
                SWT.NO_REDRAW_RESIZE | SWT.DOUBLE_BUFFERED | SWT.BORDER,
                bodyDataLayer);
        
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}