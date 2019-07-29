package com.vogella.nattable.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.convert.DefaultBooleanDisplayConverter;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.edit.editor.CheckBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.edit.editor.ComboBoxCellEditor;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.Person.Gender;
import com.vogella.model.person.PersonService;

public class NatTableEditingExamplePart {
	
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
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// register the ColumnLabelAccumulator
		bodyDataLayer.setConfigLabelAccumulator(new ColumnLabelAccumulator());
		
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

		// create the NatTable instance using the GridLayer instance without default configuration
		NatTable natTable = new NatTable(parent, gridLayer, false);
		
		//add the default style configuration
		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
		
		//add a custom editing configuration
		natTable.addConfiguration(new AbstractRegistryConfiguration() {
			
			@Override
			public void configureRegistry(IConfigRegistry configRegistry) {
				// - enable editing for all cells
		        configRegistry.registerConfigAttribute(
		                EditConfigAttributes.CELL_EDITABLE_RULE,
		                IEditableRule.ALWAYS_EDITABLE);
		        
		        
		        // - register a combo box cell editor for Gender

		        ComboBoxCellEditor comboBoxCellEditor =
	                    new ComboBoxCellEditor(Gender.FEMALE, Gender.MALE);
	            configRegistry.registerConfigAttribute(
	                    EditConfigAttributes.CELL_EDITOR,
	                    comboBoxCellEditor,
	                    DisplayMode.EDIT,
	                    ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 2);

		        
		        // - register a checkbox cell editor for the married field

		        // register a CheckBoxCellEditor
	            configRegistry.registerConfigAttribute(
	                    EditConfigAttributes.CELL_EDITOR,
	                    new CheckBoxCellEditor(),
	                    DisplayMode.EDIT,
	                    ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);

	            // if you want to use the CheckBoxCellEditor, you should also
	            // consider using the corresponding CheckBoxPainter to show the
	            // content like a checkbox in your NatTable
	            configRegistry.registerConfigAttribute(
	                    CellConfigAttributes.CELL_PAINTER,
	                    new CheckBoxPainter(),
	                    DisplayMode.NORMAL,
	                    ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);

	            // using a CheckBoxCellEditor also needs a Boolean conversion to
	            // work correctly
	            configRegistry.registerConfigAttribute(
	                    CellConfigAttributes.DISPLAY_CONVERTER,
	                    new DefaultBooleanDisplayConverter(),
	                    DisplayMode.NORMAL,
	                    ColumnLabelAccumulator.COLUMN_LABEL_PREFIX + 3);


			}
		});
		
		//call configure to populate the IConfigRegistry
		natTable.configure();
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
}