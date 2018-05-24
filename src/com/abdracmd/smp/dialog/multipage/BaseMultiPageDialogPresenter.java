/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.dialog.multipage;

import hu.belicza.andras.util.Utils;
import hu.belicza.andras.util.uicomponent.JXButton;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.abdracmd.Consts;
import com.abdracmd.service.icon.XIcon;
import com.abdracmd.service.language.TextKey;
import com.abdracmd.smp.dialog.BaseDialogPresenter;
import com.abdracmd.util.GuiUtils;

/**
 * Base multi-page dialog presenter.
 * 
 * <p>A multi-page dialog is a dialog which can display an {@link IPage} and has a navigation tree to choose the active page
 * from the defined page list. It also has some controls like "Previous Page" and "Next Page" to help the navigation.</p>
 * 
 * @param < Model > model type associated with this base multi-page dialog presenter
 * @param < Shell > shell type associated with this base multi-page dialog presenter
 * 
 * @author Andras Belicza
 */
public class BaseMultiPageDialogPresenter< Model extends BaseMultiPageDialogModel, Shell extends BaseMultiPageDialogShell< Model, ? extends BaseMultiPageDialogPresenter< Model, Shell > > > extends BaseDialogPresenter< Model, Shell > {
	
	/** Previous page button. */
	private final JXButton prevPageButton = new JXButton();
	/** Next page button.     */
	private final JXButton nextPageButton = new JXButton();
	
	/** Root of the navigation tree. */
	private final DefaultMutableTreeNode root = new DefaultMutableTreeNode();
	
	/** Navigation tree of pages.    */
	private final JTree tree = new JTree( root ) {
		private static final long serialVersionUID = 1L;
		
		/**
		 * Override updateUI() so we can set a new cell renderer (because the renderer caches some LaF dependent colors).
		 */
		@Override
		public void updateUI() {
			super.updateUI();
			
	    	// Renderer which renders the page icon 
	    	setCellRenderer( new DefaultTreeCellRenderer() {
				private static final long serialVersionUID = 1L;
				
				@Override
	    		public Component getTreeCellRendererComponent( final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus ) {
	    			super.getTreeCellRendererComponent( tree, value, sel, expanded, leaf, row, hasFocus );
	    			
	    			final Object userObject = ( (DefaultMutableTreeNode) value ).getUserObject();
	       			setIcon( userObject instanceof IPage ? ( (IPage) userObject ).getIcon() : null );
	       			
	    			return this;
	    		}
	    	} );
	    	
			// Forward updateUI() to page components (only 1 is added and visible at a time!)
			// Cached page components don't get updated because only 1 is added and visible at a time, so clear it!
			if ( pageKeyComponentCache != null )
				pageKeyComponentCache.clear();
		}
	};
	
	/** Scroll pane wrapping the active page. */
	private final JScrollPane pageScrollPane = new JScrollPane();
	
	/** Node of the default page to show by default.        */
	private DefaultMutableTreeNode defaultPageNodeToShow;
	
	/**
	 * Cache of already displayed page components.
	 * Mapped from page key to page component.
	 */
	private final Map< Object, JComponent > pageKeyComponentCache = new HashMap<>();
	
    /**
     * Creates a new BaseMultiPageDialogPresenter.
     * @param shell reference to the shell
     */
	protected BaseMultiPageDialogPresenter( final Shell shell ) {
    	super( shell );
    	
    	setTitle( TextKey.ABOUT$TITLE, Consts.APP_NAME );
    	setIcon( XIcon.MY_APP_ICON_BIG );
    	
    	// Split pane containing and dividing the navigation tree and the page content (along with controls and page title).
    	final JSplitPane splitPane = new JSplitPane();
    	
    	final JScrollPane treeScrollPane = new JScrollPane( tree );
    	treeScrollPane.setPreferredSize( new Dimension( 200, 500 ) );
    	splitPane.setLeftComponent( treeScrollPane );
    	
		final JPanel myContentPanel = new JPanel( new BorderLayout() );
		
		myContentPanel.add( pageScrollPane, BorderLayout.CENTER );
		
		final Box northBox = Box.createVerticalBox();
		
		// Control panel
		final JPanel controlPanel = new JPanel();
		GuiUtils.initButton( prevPageButton, XIcon.F_ARROW_180, TextKey.MULTIPAGE_DIALOG$PREVIOUS_PAGE_BTN );
		prevPageButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final TreePath selectionPath = tree.getSelectionPath();
				DefaultMutableTreeNode node = selectionPath == null ? root : ( (DefaultMutableTreeNode) selectionPath.getLastPathComponent() ).getPreviousNode();
				if ( node == root )
					node = (DefaultMutableTreeNode) root.getLastLeaf();
				selectNode( node );
			}
		} );
		controlPanel.add( prevPageButton );
		nextPageButton.setHorizontalTextPosition( SwingConstants.LEFT );
		GuiUtils.initButton( nextPageButton, XIcon.F_ARROW, TextKey.MULTIPAGE_DIALOG$NEXT_PAGE_BTN );
		nextPageButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( final ActionEvent event ) {
				final TreePath selectionPath = tree.getSelectionPath();
				DefaultMutableTreeNode node = selectionPath == null ? null : ( (DefaultMutableTreeNode) selectionPath.getLastPathComponent() ).getNextNode();
				if ( node == null )
					node = (DefaultMutableTreeNode) root.getFirstChild();
				selectNode( node );
			}
		} );
		controlPanel.add( nextPageButton );
		northBox.add( new JScrollPane( controlPanel ) );
		
		// Title
		final JLabel titleLabel = new JLabel( Utils.EMPTY_STRING, SwingConstants.CENTER );
		titleLabel.setFont( titleLabel.getFont().deriveFont( Font.BOLD, titleLabel.getFont().getSize() + 5 ) );
		final JPanel titleWrapperPanel = GuiUtils.wrapInBorderPanel( titleLabel );
		titleWrapperPanel.setBorder( BorderFactory.createEmptyBorder( 3, 3, 3, 3 ) );
		northBox.add( titleWrapperPanel );
		
		myContentPanel.add( northBox, BorderLayout.NORTH );
		
    	splitPane.setRightComponent( myContentPanel );
		component.getContentPane().add( splitPane, BorderLayout.CENTER );
		
		// Selection listener which "activates" the selected about page
		tree.addTreeSelectionListener( new TreeSelectionListener() {
			{ valueChanged( null ); } // Initialize page view
			@Override
			public void valueChanged( final TreeSelectionEvent event ) {
				if ( event != null && event.isAddedPath() ) {
					// Page selected
					final IPage page = (IPage) ( (DefaultMutableTreeNode) event.getPath().getLastPathComponent() ).getUserObject();
					
					// Cache created page components
					final Object pageKey = model.pageKeyProvider.provide( page );
					JComponent pageComponent = pageKeyComponentCache.get( pageKey );
					if ( pageComponent == null )
						pageKeyComponentCache.put( pageKey, pageComponent = page.createPage() );
					
					titleLabel.setText( page.getDisplayName() );
					titleLabel.setIcon( page.getIcon() );
					pageScrollPane.setViewportView( pageComponent );
					pageComponent.updateUI();
				}
				else {
					// Page deselected. Single selection model => nothing is selected
					titleLabel.setText( Utils.SPACE_STRING ); // Set space string instead of null or empty string to reserve space
					titleLabel.setIcon( null );
					pageScrollPane.setViewportView( null );
				}
			}
		} );
		
    	tree.setRootVisible( false );
    	tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
    	
    	rebuildPageTree();
    }
    
	/**
	 * Rebuilds the page tree.
	 */
	public void rebuildPageTree() {
		tree.clearSelection();
		
		defaultPageNodeToShow = null;
		root.removeAllChildren();
		
		buildPageTree( root, model.pageList );
		
		( (DefaultTreeModel) tree.getModel() ).reload();
		
		pageKeyComponentCache.clear();
		
    	// Expand all rows
    	for ( int i = 0; i < tree.getRowCount(); i++ )
    		tree.expandRow( i );
    	
		prevPageButton.setEnabled( !model.pageList.isEmpty() );
    	nextPageButton.setEnabled( !model.pageList.isEmpty() );
    	
		// Select default page
		if ( defaultPageNodeToShow != null )
			selectNode( defaultPageNodeToShow );
	}
	
    /**
     * Builds the page tree from the specified page list, adding the nodes to the specified parent node.
     * @param parentNode parent node to add page nodes to
     * @param pageList   page list to build the page tree from
     */
    private void buildPageTree( final DefaultMutableTreeNode parentNode, final List< ? extends IPage > pageList ) {
    	for ( final IPage page : pageList ) {
			final DefaultMutableTreeNode pageNode = new DefaultMutableTreeNode( page );
			parentNode.add( pageNode );
			
			if ( defaultPageNodeToShow == null && ( model.defaultPageKey == null || model.pageKeyProvider.provide( page ).equals( model.defaultPageKey ) ) )
				defaultPageNodeToShow = pageNode;
			
			if ( page.getChildPageList() != null && !page.getChildPageList().isEmpty() )
				buildPageTree( pageNode, page.getChildPageList() );
		}
    }
    
    /**
     * Selects the specified node in the tree.
     * Also scrolls to it if it's not visible.
     * @param node node to be selected
     */
    private void selectNode( final DefaultMutableTreeNode node ) {
    	final TreePath treePath = new TreePath( node.getPath() );
		tree.setSelectionPath( treePath );
		tree.scrollPathToVisible( treePath );
    }
    
}
