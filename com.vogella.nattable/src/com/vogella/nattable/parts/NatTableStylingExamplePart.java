package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.IConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.Person.Gender;
import com.vogella.model.person.PersonService;

public class NatTableStylingExamplePart {
	
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
        IRowDataProvider<Person> bodyDataProvider =
        		new ListDataProvider<Person>(
        				personService.getPersons(50),
        				columnPropertyAccessor);
		
		// create a layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		// remember to set the regionName on the ViewportLayer to GridRegion.BODY
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// register the config label accumulator on the DataLayer
		bodyDataLayer.setConfigLabelAccumulator(new PersonConfigLabelAccumulator(bodyDataProvider));
		
		// create the NatTable instance without default configuration
		NatTable natTable = new NatTable(parent, viewportLayer, false);
		
		//add the default style configuration
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		
		//add the custom style configuration
		natTable.addConfiguration(new PersonStyleConfiguration());
		
		//call configure to populate the IConfigRegistry
		natTable.configure();
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	// create a custom IConfigLabelAccumulator to mark female persons
	static class PersonConfigLabelAccumulator implements IConfigLabelAccumulator {

		public static final String MARRIED_LABEL = "marriedLabel";
		
		public static final String FEMALE_LABEL = "femaleLabel";
		public static final String MALE_LABEL = "maleLabel";
		
		private IRowDataProvider<Person> bodyDataProvider;
		
		public PersonConfigLabelAccumulator(IRowDataProvider<Person> bodyDataProvider) {
			this.bodyDataProvider = bodyDataProvider;
		}

		@Override
		public void accumulateConfigLabels(LabelStack configLabels, int columnPosition, int rowPosition) {
			Person p = bodyDataProvider.getRowObject(rowPosition);
			if (p != null) {
				configLabels.addLabel(p.getGender().equals(Gender.FEMALE) ? FEMALE_LABEL : MALE_LABEL);
				
				if (columnPosition == 3) {
					configLabels.addLabel(MARRIED_LABEL);
				}
			}
		}

	}
	
	// create a custom style configuration
	static class PersonStyleConfiguration extends AbstractRegistryConfiguration {

		@Override
		public void configureRegistry(IConfigRegistry configRegistry) {
			//register a style containing a yellow background configuration for the femaleRowLabel
			IStyle style = new Style();
			style.setAttributeValue(CellStyleAttributes.BACKGROUND_COLOR, GUIHelper.COLOR_YELLOW);
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.CELL_STYLE, 
					style, 
					DisplayMode.NORMAL, 
					PersonConfigLabelAccumulator.FEMALE_LABEL);
			
			//register a checkbox painter for the married label
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.CELL_PAINTER, 
					new CheckBoxPainter(), 
					DisplayMode.NORMAL, 
					PersonConfigLabelAccumulator.MARRIED_LABEL);
		}

	}

}