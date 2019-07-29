package com.vogella.nattable.parts;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.columnChooser.command.DisplayColumnChooserCommandHandler;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.data.ExtendedReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.performance.ColumnGroupExpandCollapseLayer;
import org.eclipse.nebula.widgets.nattable.group.performance.ColumnGroupHeaderLayer;
import org.eclipse.nebula.widgets.nattable.group.performance.RowGroupExpandCollapseLayer;
import org.eclipse.nebula.widgets.nattable.group.performance.RowGroupHeaderLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.ColumnHideShowLayer;
import org.eclipse.nebula.widgets.nattable.hideshow.RowHideShowLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayerTransform;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.persistence.command.DisplayPersistenceDialogCommandHandler;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.reorder.RowReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.AbstractHeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.ui.menu.VisibleColumnsRemaining;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import com.vogella.model.person.ExtendedPersonWithAddress;
import com.vogella.model.person.PersonService;

public class NatTableExtendedExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent, PersonService personService) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// property names of the ExtendedPersonWithAddress class
        String[] propertyNames = { "firstName", "lastName", "gender",
                "married", "address.street", "address.housenumber",
                "address.postalCode", "address.city", "age", "birthday",
                "money", "description", "favouriteFood", "favouriteDrinks" };
        
		// create the ExtendedReflectiveColumnPropertyAccessor
        IColumnPropertyAccessor<ExtendedPersonWithAddress> columnPropertyAccessor =
                new ExtendedReflectiveColumnPropertyAccessor<>(propertyNames);

		// create a body layer stack with a sorted collection by last name
        List<ExtendedPersonWithAddress> persons = personService.getExtendedPersonsWithAddress(50);
        Collections.sort(persons, (o1, o2) -> {
            return o1.getLastName().compareTo(o2.getLastName());
        });

        BodyLayerStack<ExtendedPersonWithAddress> bodyLayerStack =
        		new BodyLayerStack<>(persons, columnPropertyAccessor);
        
		// create a column header layer stack with column grouping
        Map<String, String> propertyToLabelMap = new HashMap<>();
        propertyToLabelMap.put("firstName", "Firstname");
        propertyToLabelMap.put("lastName", "Lastname");
        propertyToLabelMap.put("gender", "Gender");
        propertyToLabelMap.put("married", "Married");
        propertyToLabelMap.put("address.street", "Street");
        propertyToLabelMap.put("address.housenumber", "Housenumber");
        propertyToLabelMap.put("address.postalCode", "Postalcode");
        propertyToLabelMap.put("address.city", "City");
        propertyToLabelMap.put("age", "Age");
        propertyToLabelMap.put("birthday", "Birthday");
        propertyToLabelMap.put("money", "Money");
        propertyToLabelMap.put("description", "Description");
        propertyToLabelMap.put("favouriteFood", "Food");
        propertyToLabelMap.put("favouriteDrinks", "Drinks");
        
		IDataProvider columnHeaderDataProvider = 
				new DefaultColumnHeaderDataProvider(propertyNames, propertyToLabelMap);
		DataLayer columnHeaderDataLayer = 
				new DataLayer(columnHeaderDataProvider);
		ColumnHeaderLayer columnHeaderLayer = 
				new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayerStack.getViewportLayer(), bodyLayerStack.getSelectionLayer());
        ColumnGroupHeaderLayer columnGroupHeaderLayer =
                new ColumnGroupHeaderLayer(columnHeaderLayer, bodyLayerStack.getSelectionLayer());

        // configure the column groups
        columnGroupHeaderLayer.addGroup("Person", 0, 4);
        columnGroupHeaderLayer.addGroup("Address", 4, 4);
        columnGroupHeaderLayer.addGroup("Facts", 8, 3);
        columnGroupHeaderLayer.addGroup("Personal", 11, 3);

		// create a row header layer stack with row grouping
		IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyLayerStack.getBodyDataProvider());
		DataLayer rowHeaderDataLayer = new DataLayer(rowHeaderDataProvider, 40, 20);
		RowHeaderLayer rowHeaderLayer = new RowHeaderLayer(
		    rowHeaderDataLayer,
		    bodyLayerStack.getViewportLayer(),
		    bodyLayerStack.getSelectionLayer());
        RowGroupHeaderLayer rowGroupHeaderLayer =
                new RowGroupHeaderLayer(rowHeaderLayer, bodyLayerStack.getSelectionLayer());

        // configure the row groups
        // collect containing last names and the number of persons with that
        // last name
        Map<String, Long> counted = persons.stream()
                .collect(Collectors.groupingBy(ExtendedPersonWithAddress::getLastName, Collectors.counting()));
        counted.entrySet().stream().forEach(e -> {
            rowGroupHeaderLayer.addGroup(
                    e.getKey(),
                    // retrieve the index of the first element with the given
                    // last name
                    IntStream.range(0, persons.size())
                            .filter(index -> persons.get(index).getLastName().equals(e.getKey()))
                            .findFirst()
                            .getAsInt(),
                    e.getValue().intValue());
        });

		// create a corner layer stack
		IDataProvider cornerDataProvider =
			    new DefaultCornerDataProvider(
			        columnHeaderDataProvider,
			        rowHeaderDataProvider);
			DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
			ILayer cornerLayer = new CornerLayer(
			    cornerDataLayer,
			    rowGroupHeaderLayer,
			    columnGroupHeaderLayer);

		// create a GridLayer using the created layer stacks
		GridLayer gridLayer = 
				new GridLayer(
						bodyLayerStack, 
						columnGroupHeaderLayer, 
						rowGroupHeaderLayer, 
						cornerLayer);
		
		// create the NatTable instance using the GridLayer instance
        NatTable natTable = new NatTable(parent, gridLayer, false);

        // as the autoconfiguration of the NatTable is turned off, we have to
        // add the DefaultNatTableStyleConfiguration manually
        natTable.addConfiguration(new DefaultNatTableStyleConfiguration());

        natTable.addConfiguration(new AbstractHeaderMenuConfiguration(natTable) {
            @Override
            protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
                PopupMenuBuilder builder = super.createColumnHeaderMenu(natTable)
                        .withHideColumnMenuItem()
                        .withShowAllColumnsMenuItem()
                        // the performance column group menu, not the old one
                        .withCreateColumnGroupMenuItem()
                        .withUngroupColumnsMenuItem()
                        .withAutoResizeSelectedColumnsMenuItem()
                        .withColumnRenameDialog()
                        .withColumnChooserMenuItem()
                        .withInspectLabelsMenuItem();
                builder.withEnabledState(
                        PopupMenuBuilder.HIDE_COLUMN_MENU_ITEM_ID,
                        new VisibleColumnsRemaining(bodyLayerStack.getSelectionLayer()));
                return builder;
            }

            @Override
            protected PopupMenuBuilder createRowHeaderMenu(NatTable natTable) {
                return super.createRowHeaderMenu(natTable)
                        .withHideRowMenuItem()
                        .withShowAllRowsMenuItem()
                        .withCreateRowGroupMenuItem()
                        .withUngroupRowsMenuItem()
                        .withAutoResizeSelectedRowsMenuItem()
                        .withInspectLabelsMenuItem();
            }

            @Override
            protected PopupMenuBuilder createCornerMenu(NatTable natTable) {
                return super.createCornerMenu(natTable)
                		.withShowAllColumnsMenuItem()
                		.withShowAllRowsMenuItem()
                		.withStateManagerMenuItemProvider();
            }
        });

        // register a column group header menu
        final Menu columnGroupHeaderMenu = new PopupMenuBuilder(natTable)
                .withRenameColumnGroupMenuItem()
                .withRemoveColumnGroupMenuItem()
                .withInspectLabelsMenuItem()
                .build();

        natTable.addConfiguration(new AbstractUiBindingConfiguration() {
            @Override
            public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
                uiBindingRegistry.registerFirstMouseDownBinding(
                        new MouseEventMatcher(
                                SWT.NONE,
                                GridRegion.COLUMN_GROUP_HEADER,
                                MouseEventMatcher.RIGHT_BUTTON),
                        new PopupMenuAction(columnGroupHeaderMenu));
            }
        });

        // register a row group header menu
        final Menu rowGroupHeaderMenu = new PopupMenuBuilder(natTable)
                .withRenameRowGroupMenuItem()
                .withRemoveRowGroupMenuItem()
                .withInspectLabelsMenuItem()
                .build();

        natTable.addConfiguration(new AbstractUiBindingConfiguration() {
            @Override
            public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
                uiBindingRegistry.registerFirstMouseDownBinding(
                        new MouseEventMatcher(
                                SWT.NONE,
                                GridRegion.ROW_GROUP_HEADER,
                                MouseEventMatcher.RIGHT_BUTTON),
                        new PopupMenuAction(rowGroupHeaderMenu));
            }
        });

        // register display persistence dialog
        gridLayer.registerCommandHandler(
                new DisplayPersistenceDialogCommandHandler(natTable));

        // register column chooser
        gridLayer.registerCommandHandler(
        		new DisplayColumnChooserCommandHandler(
                        bodyLayerStack.getColumnHideShowLayer(),
                        columnHeaderLayer,
                        columnHeaderDataLayer,
                        columnGroupHeaderLayer,
                        false));

        natTable.configure();
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
    static class BodyLayerStack<T> extends AbstractLayerTransform {

        private final ListDataProvider<T> bodyDataProvider;

        private ColumnHideShowLayer columnHideShowLayer;
        private SelectionLayer selectionLayer;
        private ViewportLayer viewportLayer;

        public BodyLayerStack(List<T> values, IColumnPropertyAccessor<T> columnPropertyAccessor) {

            this.bodyDataProvider =
                    new ListDataProvider<>(values, columnPropertyAccessor);
            DataLayer bodyDataLayer = 
            		new DataLayer(this.bodyDataProvider);

            ColumnReorderLayer columnReorderLayer =
                    new ColumnReorderLayer(bodyDataLayer);
            this.columnHideShowLayer =
                    new ColumnHideShowLayer(columnReorderLayer);
            ColumnGroupExpandCollapseLayer columnGroupExpandCollapseLayer =
                    new ColumnGroupExpandCollapseLayer(columnHideShowLayer);
            RowReorderLayer rowReorderLayer =
                    new RowReorderLayer(columnGroupExpandCollapseLayer);
            RowHideShowLayer rowHideShowLayer =
                    new RowHideShowLayer(rowReorderLayer);
            RowGroupExpandCollapseLayer rowGroupExpandCollapseLayer =
                    new RowGroupExpandCollapseLayer(rowHideShowLayer);

            this.selectionLayer =
                    new SelectionLayer(rowGroupExpandCollapseLayer);
            this.viewportLayer =
                    new ViewportLayer(selectionLayer);

            setUnderlyingLayer(viewportLayer);
        }

        public IDataProvider getBodyDataProvider() {
            return this.bodyDataProvider;
        }

        public ColumnHideShowLayer getColumnHideShowLayer() {
            return this.columnHideShowLayer;
        }
        
        public SelectionLayer getSelectionLayer() {
        	return this.selectionLayer;
        }

        public ViewportLayer getViewportLayer() {
            return this.viewportLayer;
        }

    }

}