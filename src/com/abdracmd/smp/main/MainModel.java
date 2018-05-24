/*
 * Project Abdra Commander
 * 
 * Copyright (c) 2012 Andras Belicza <iczaaa@gmail.com>
 * 
 * This software is the property of Andras Belicza.
 * Copying, modifying, distributing, refactoring without the author's permission
 * is prohibited and protected by Law.
 */
package com.abdracmd.smp.main;

import hu.belicza.andras.util.objectregistry.ObjectRegistry;

import java.util.ArrayList;
import java.util.List;

import com.abdracmd.fileop.IFileOp;
import com.abdracmd.service.icon.Icons;
import com.abdracmd.service.language.Language;
import com.abdracmd.service.log.Logging;
import com.abdracmd.service.settings.Settings;
import com.abdracmd.smp.BaseModel;
import com.abdracmd.smp.dialog.multipage.IPage;
import com.abdracmd.smp.mainframe.MainFrameShell;
import com.abdracmd.smp.mainframe.folderstabbed.folder.column.IColumn;
import com.abdracmd.smp.mainframe.folderstabbed.folder.theme.IFolderPresenterTheme;
import com.abdracmd.smp.shutdown.ShutdownPhase;
import com.abdracmd.smp.startup.StartupPhase;
import com.abdracmd.util.bean.reginfo.RegistrationInfo;

/**
 * This is the main model (SMP architecture) of Abdra Commander.
 * 
 * @author Andras Belicza
 */
public class MainModel extends BaseModel {
	
	/** Application wide logging. */
	private Logging logging;
	
	/** Application icons. */
	private Icons icons;
	
	/** Phase of the startup process. */
	private StartupPhase startupPhase;
	
	/** Application settings. */
	private Settings settings;
	
	/** Application language. */
	private Language language;
	
	/** List of main frame shells. */
	private final List< MainFrameShell > mainFrameShellList = new ArrayList<>();
	
	/** Phase of the shutdown process. */
	private ShutdownPhase shutdownPhase;
	
	/** Registration info. */
	private RegistrationInfo registrationInfo;
	
	
	// OBJECT REGISTRIES
	
	/**
	 * Folder presenter column registry.
	 * Available column implementations mapped from class name string to column instance.
	 */
	private final ObjectRegistry< String, IColumn< ? > >          folderPresenterColumnRegistry = ObjectRegistry.newClassNameAsKeyObjectRegistry();
	/**
	 * Folder presenter theme registry.
	 * Available themes mapped from class name string to theme instance.
	 */
	private final ObjectRegistry< String, IFolderPresenterTheme > folderPresenterThemeRegistry  = ObjectRegistry.newClassNameAsKeyObjectRegistry();
	/**
	 * About page registry.
	 * Pages mapped from class name string to page instances.
	 */
	private final ObjectRegistry< String, IPage >                 aboutPageRegistry             = ObjectRegistry.newClassNameAsKeyObjectRegistry();
	/**
	 * File operations registry.
	 * File ops mapped from class name string to file op instances.
	 */
	private final ObjectRegistry< String, IFileOp >               fileOpRegistry                = ObjectRegistry.newClassNameAsKeyObjectRegistry();
	
	// END OF OBJECT REGISTRIES
	
	
	/**
	 * Creates a new MainModel.
	 */
	protected MainModel() {
		// BaseModel stores a reference to the main model (us) which did not exist when assigned.
		// Set it for consistency.
		mainModel = this;
		
		enterStartupPhase( StartupPhase.NOT_STARTED );
		enterShutdownPhase( ShutdownPhase.NOT_STARTED );
	}
	
	/**
	 * Returns the application logging.
	 * @return the application logging
	 */
	public Logging getLogging() {
	    return logging;
    }
	
	/**
	 * Sets the application wide logging.
	 * @param logging application wide logging to be set
	 */
	public void setLogging( final Logging logging ) {
	    this.logging = logging;
    }
	
	/**
	 * Enters the specified startup phase.
	 * @param startupPhase startup phase to be entered
	 */
	public void enterStartupPhase( final StartupPhase startupPhase ) {
		synchronized ( StartupPhase.class ) {
			this.startupPhase = startupPhase;
		}
	}
	
	/**
	 * Returns the startup phase.
	 * @return the startup phase
	 */
	public StartupPhase getStartupPhase() {
		synchronized ( StartupPhase.class ) {
			return startupPhase;
		}
	}
	
	/**
	 * Returns the application settings.
	 * @return the application settings
	 */
	public Settings getSettings() {
		return settings;
	}
	
	/**
	 * Sets the application settings.
	 * @param settings settings to be set
	 */
	public void setSettings( final Settings settings ) {
		this.settings = settings;
	}
	
	/**
	 * Returns the application language.
	 * @return the application language
	 */
	public Language getLanguage() {
	    return language;
    }
	
	/**
	 * Sets the application language.
	 * @param language application language to be set
	 */
	public void setLanguage( final Language language ) {
	    this.language = language;
    }
	
	/**
	 * Enters the specified shutdown phase.
	 * @param shutdownPhase shutdown phase to be entered
	 */
	public void enterShutdownPhase( final ShutdownPhase shutdownPhase ) {
		synchronized ( ShutdownPhase.class ) {
			this.shutdownPhase = shutdownPhase;
		}
    }
	
	/**
	 * Returns the shutdown phase.
	 * @return the shutdown phase
	 */
	public ShutdownPhase getShutdownPhase() {
		synchronized ( ShutdownPhase.class ) {
			return shutdownPhase;
		}
    }
	
	/**
	 * Returns the application icons.
	 * @return the application icons
	 */
	public Icons getIcons() {
	    return icons;
    }
	
	/**
	 * Sets the application icons.
	 * @param icons application icons to be set
	 */
	public void setIcons( final Icons icons ) {
	    this.icons = icons;
    }
	
	/**
	 * Returns the list of main frame shells.
	 * @return the list of main frame shells
	 */
	public List< MainFrameShell > getMainFrameShellList() {
	    return mainFrameShellList;
    }
	
	/**
	 * Returns the registration info.
	 * @return the registration info; <code>null</code> means this copy is not registered
	 */
	public RegistrationInfo getRegistrationInfo() {
		return registrationInfo;
	}

	/**
	 * Sets the registration info.
	 * @param registrationInfo registration info to be set
	 */
	public void setRegistrationInfo(RegistrationInfo registrationInfo) {
		this.registrationInfo = registrationInfo;
	}
	
	/**
	 * Returns the folder presenter column registry.
	 * @return the folder presenter column registry
	 */
	public ObjectRegistry< String, IColumn< ? > > getFolderPresenterColumnRegistry() {
		return folderPresenterColumnRegistry;
	}
	
	/**
	 * Returns the folder presenter theme registry.
	 * @return the folder presenter theme registry
	 */
	public ObjectRegistry< String, IFolderPresenterTheme > getFolderPresenterThemeRegistry() {
		return folderPresenterThemeRegistry;
	}
	
	/**
	 * Returns the about page registry.
	 * @return the about page registry
	 */
	public ObjectRegistry< String, IPage > getAboutPageRegistry() {
		return aboutPageRegistry;
	}
	
	/**
	 * Returns the file operations registry.
	 * @return the file operations registry
	 */
	public ObjectRegistry< String, IFileOp > getFileOpRegistry() {
		return fileOpRegistry;
	}
	
}
