package com.vogella.nattable.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.IRowDataProvider;
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
import org.eclipse.nebula.widgets.nattable.layer.LabelStack;
import org.eclipse.nebula.widgets.nattable.layer.cell.IConfigLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.painter.NatTableBorderOverlayPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.CheckBoxPainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.IStyle;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.style.theme.IThemeExtension;
import org.eclipse.nebula.widgets.nattable.style.theme.ModernNatTableThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.style.theme.ThemeConfiguration;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.vogella.model.person.Person;
import com.vogella.model.person.Person.Gender;
import com.vogella.model.person.PersonService;

public class NatTableThemeStylingExamplePart {
	
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
		
		// create a body layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// register the config label accumulator on the DataLayer
		bodyDataLayer.setConfigLabelAccumulator(new PersonConfigLabelAccumulator(bodyDataProvider));
		
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
		
		// create a ModernNatTableThemeConfiguration
		ThemeConfiguration theme = new ModernNatTableThemeConfiguration();
		
		// add the custom theme extension
		theme.addThemeExtension(new PersonThemeExtension());
		
		// set the theme on the NatTable instance
		natTable.setTheme(theme);
		
		// add the NatTableBorderOverlayPainter to render the border on every side of NatTable
		natTable.addOverlayPainter(new NatTableBorderOverlayPainter());
		
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

	// create an IThemeExtension for special Person styling
	static class PersonThemeExtension implements IThemeExtension {

		@Override
		public void registerStyles(IConfigRegistry configRegistry) {
			//register a style containing a yellow background configuration for the femaleLabel
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

		@Override
		public void unregisterStyles(IConfigRegistry configRegistry) {
			//unregister the style containing a yellow background configuration for the femaleLabel
			configRegistry.unregisterConfigAttribute(
					CellConfigAttributes.CELL_STYLE, 
					DisplayMode.NORMAL, 
					PersonConfigLabelAccumulator.FEMALE_LABEL);
			
			//unregister the checkbox painter for the married label
			configRegistry.unregisterConfigAttribute(
					CellConfigAttributes.CELL_PAINTER, 
					DisplayMode.NORMAL, 
					PersonConfigLabelAccumulator.MARRIED_LABEL);
		}

	}
}