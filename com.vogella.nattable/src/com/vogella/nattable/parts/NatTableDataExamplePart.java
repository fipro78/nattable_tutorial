package com.vogella.nattable.parts;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.Person.Gender;
import com.vogella.model.person.PersonService;

public class NatTableDataExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create a ListDataProvider using the custom IColumnAccessor
        IColumnAccessor<Person> columnPropertyAccessor =
                new PersonColumnPropertyAccessor();

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
	
	// create a custom IColumnAccessor to access column data per row object
	static class PersonColumnPropertyAccessor implements IColumnAccessor<Person> {

		@Override
		public Object getDataValue(Person person, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return person.getFirstName();
			case 1:
				return person.getLastName();
			case 2:
				return person.getGender();
			case 3:
				return person.isMarried();
			case 4:
				return person.getBirthday();
			}
			return person;
		}

		@Override
		public void setDataValue(Person person, int columnIndex, Object newValue) {
			switch (columnIndex) {
			case 0:
				String firstName = String.valueOf(newValue);
				person.setFirstName(firstName);
				break;
			case 1:
				String lastName = String.valueOf(newValue);
				person.setLastName(lastName);
				break;
			case 2:
				person.setGender((Gender) newValue);
				break;
			case 3:
				person.setMarried((boolean) newValue);
				break;
			case 4:
				person.setBirthday((Date) newValue);
				break;
			}
		}

		@Override
		public int getColumnCount() {
			return 5;
		}

	}
}