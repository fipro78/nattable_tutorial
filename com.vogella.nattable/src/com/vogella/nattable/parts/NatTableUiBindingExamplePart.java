package com.vogella.nattable.parts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.AbstractMultiRowCommand;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommandHandler;
import org.eclipse.nebula.widgets.nattable.config.AbstractUiBindingConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.coordinate.PositionUtil;
import org.eclipse.nebula.widgets.nattable.data.IColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ListDataProvider;
import org.eclipse.nebula.widgets.nattable.data.ReflectiveColumnPropertyAccessor;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.LayerUtil;
import org.eclipse.nebula.widgets.nattable.layer.event.RowObjectDeleteEvent;
import org.eclipse.nebula.widgets.nattable.reorder.ColumnReorderLayer;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.selection.command.SelectRowsCommand;
import org.eclipse.nebula.widgets.nattable.ui.binding.UiBindingRegistry;
import org.eclipse.nebula.widgets.nattable.ui.matcher.MouseEventMatcher;
import org.eclipse.nebula.widgets.nattable.ui.menu.IMenuItemProvider;
import org.eclipse.nebula.widgets.nattable.ui.menu.MenuItemProviders;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuAction;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.vogella.model.person.Person;
import com.vogella.model.person.PersonService;

public class NatTableUiBindingExamplePart {
	
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
        ListDataProvider<Person> bodyDataProvider =
        		new ListDataProvider<Person>(
        				personService.getPersons(50),
        				columnPropertyAccessor);
		
		// create a body layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
		ColumnReorderLayer columnReorderLayer = new ColumnReorderLayer(bodyDataLayer);
		SelectionLayer selectionLayer = new SelectionLayer(columnReorderLayer);
		ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);

		// register the command handler
		bodyDataLayer.registerCommandHandler(new DeleteRowCommandHandler<>(bodyDataProvider.getList()));
		
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
		NatTable natTable = new NatTable(parent, gridLayer, false);

		natTable.addConfiguration(new DefaultNatTableStyleConfiguration());
        // register the menu configurations
		natTable.addConfiguration(new BodyMenuConfiguration(natTable, selectionLayer));
        
        natTable.configure();
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	// create a command to delete a row
	static class DeleteRowCommand extends AbstractMultiRowCommand {

		protected DeleteRowCommand(ILayer layer, int... rowPositions) {
			super(layer, rowPositions);
		}

		protected DeleteRowCommand(DeleteRowCommand command) {
	        super(command);
	    }
		
		@Override
		public ILayerCommand cloneCommand() {
			return new DeleteRowCommand(this);
		}
		
	}
	
	// create a command handler to delete a row
	static class DeleteRowCommandHandler<T> implements ILayerCommandHandler<DeleteRowCommand> {

	    private List<T> bodyData;

	    /**
	     *
	     * @param bodyData
	     *            The backing data list on which the delete operation should be
	     *            performed. Should be the same list that is used by the data
	     *            provider.
	     */
	    public DeleteRowCommandHandler(List<T> bodyData) {
	        this.bodyData = bodyData;
	    }

	    @Override
	    public boolean doCommand(ILayer targetLayer, DeleteRowCommand command) {
	        // convert the transported position to the target layer
	        if (command.convertToTargetLayer(targetLayer)) {
	            int[] positions = command.getRowPositions().stream().sorted().mapToInt(i -> i).toArray();

	            Map<Integer, T> deleted = new HashMap<Integer, T>();
	            for (int i = positions.length - 1; i >= 0; i--) {
	                // remove the element
	                int pos = positions[i];
	                deleted.put(pos, this.bodyData.remove(pos));
	            }
	            // fire the event to refresh
	            targetLayer.fireLayerEvent(new RowObjectDeleteEvent(targetLayer, deleted));
	            return true;
	        }
	        return false;
	    }

	    @Override
	    public Class<DeleteRowCommand> getCommandClass() {
	        return DeleteRowCommand.class;
	    }

	}
	
	// create an IMouseAction to select a row if not selected yet and open a body menu
	static class BodyPopupMenuAction extends PopupMenuAction {

		private final SelectionLayer selectionLayer;

		public BodyPopupMenuAction(Menu menu, SelectionLayer selectionLayer) {
			super(menu);
			this.selectionLayer = selectionLayer;
		}

		@Override
        public void run(NatTable natTable, MouseEvent event) {
            int columnPosition = natTable.getColumnPositionByX(event.x);
            int rowPosition = natTable.getRowPositionByY(event.y);

            int bodyRowPosition = LayerUtil.convertRowPosition(natTable, rowPosition, selectionLayer);

            if (!selectionLayer.isRowPositionFullySelected(bodyRowPosition)
                    && !selectionLayer.isRowPositionSelected(bodyRowPosition)) {
                natTable.doCommand(
                        new SelectRowsCommand(
                                natTable,
                                columnPosition,
                                rowPosition,
                                false,
                                false));
            }

            super.run(natTable, event);
        }

	}
	
	// create a configuration to open a body menu that
	// - selects a row if not selected yet
	// - opens a menu with a menu entry to delete the selected row
	static class BodyMenuConfiguration extends AbstractUiBindingConfiguration {
		
		private final SelectionLayer selectionLayer;
		private final Menu bodyMenu;
		

		public BodyMenuConfiguration(NatTable natTable, SelectionLayer selectionLayer) {
			this.selectionLayer = selectionLayer;
			
			//define a menu for the body region
			bodyMenu = new PopupMenuBuilder(natTable)
                    .withMenuItemProvider(new IMenuItemProvider() {

                        @Override
                        public void addMenuItem(NatTable natTable, Menu popupMenu) {
                            MenuItem deleteRow = new MenuItem(popupMenu, SWT.PUSH);
                            deleteRow.setText("Delete");
                            deleteRow.setEnabled(true);

                            deleteRow.addSelectionListener(new SelectionAdapter() {
                                @Override
                                public void widgetSelected(SelectionEvent event) {
                                    int[] selectedRowPositions = PositionUtil.getPositions(selectionLayer.getSelectedRowPositions());

                                    if (selectedRowPositions.length > 0) {
                                        selectionLayer.doCommand(new DeleteRowCommand(selectionLayer, selectedRowPositions));
                                    } else {
                                        int rowPosition = MenuItemProviders.getNatEventData(event).getRowPosition();
                                        natTable.doCommand(new DeleteRowCommand(natTable, rowPosition));
                                    }
                                }
                            });
                        }
                    })
                    .build();
		}


		@Override
		public void configureUiBindings(UiBindingRegistry uiBindingRegistry) {
			uiBindingRegistry.registerMouseDownBinding(
                    new MouseEventMatcher(
                            SWT.NONE,
                            GridRegion.BODY,
                            MouseEventMatcher.RIGHT_BUTTON),
                    new BodyPopupMenuAction(bodyMenu, selectionLayer));
		}
	}
	
}