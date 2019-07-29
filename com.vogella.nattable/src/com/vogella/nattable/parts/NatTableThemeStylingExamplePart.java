package com.vogella.nattable.parts;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NatTableThemeStylingExamplePart {
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		// create the layout
		parent.setLayout(new GridLayout());
		
		// create the ReflectiveColumnPropertyAccessor
		
		// create a ListDataProvider
		
		// create a body layer stack out of
		// DataLayer, SelectionLayer, ViepwortLayer
		
		// register the config label accumulator on the DataLayer

		// create a column header layer stack

		// create a row header layer stack

		// create a corner layer stack
		
		// create a GridLayer using the created layer stacks
		
		// create the NatTable instance using the GridLayer instance
		NatTable natTable = null;
		
		// create a ModernNatTableThemeConfiguration
		
		// add the custom theme extension
		
		// set the theme on the NatTable instance
		
		// add the NatTableBorderOverlayPainter to render the border on every side of NatTable
		
		// set the layout data
		GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);
	}
	
	// create an IThemeExtension for special Person styling
}