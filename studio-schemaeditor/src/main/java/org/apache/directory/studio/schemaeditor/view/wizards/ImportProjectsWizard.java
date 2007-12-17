/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *  
 *    http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License. 
 *  
 */
package org.apache.directory.studio.schemaeditor.view.wizards;


import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.apache.directory.studio.schemaeditor.Activator;
import org.apache.directory.studio.schemaeditor.PluginUtils;
import org.apache.directory.studio.schemaeditor.controller.ProjectsHandler;
import org.apache.directory.studio.schemaeditor.model.Project;
import org.apache.directory.studio.schemaeditor.model.io.ProjectsImportException;
import org.apache.directory.studio.schemaeditor.model.io.ProjectsImporter;
import org.apache.directory.studio.schemaeditor.view.ViewUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;


/**
 * This class represents the wizard to import schema projects.
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ImportProjectsWizard extends Wizard implements IImportWizard
{
    public static final String ID = Activator.PLUGIN_ID + ".wizards.ImportProjectsWizard";

    // The pages of the wizard
    private ImportProjectsWizardPage page;

    /** The ProjectsHandler */
    private ProjectsHandler projectsHandler;


    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#addPages()
     */
    public void addPages()
    {
        // Creating pages
        page = new ImportProjectsWizardPage();

        // Adding pages
        addPage( page );
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.wizard.Wizard#performFinish()
     */
    public boolean performFinish()
    {
        // Saving the dialog settings
        page.saveDialogSettings();

        // Getting the projects to be imported
        final File[] selectedProjectFiles = page.getSelectedProjectFiles();
        try
        {
            getContainer().run( true, false, new IRunnableWithProgress()
            {
                public void run( IProgressMonitor monitor )
                {
                    monitor.beginTask( "Importing projects: ", selectedProjectFiles.length );

                    for ( File projectFile : selectedProjectFiles )
                    {
                        monitor.subTask( projectFile.getName() );
                        try
                        {
                            Project project = ProjectsImporter.getProject( projectFile.getAbsolutePath() );
                            projectsHandler.addProject( project );
                        }
                        catch ( ProjectsImportException e )
                        {
                            PluginUtils.logError( "An error occured when importing project " + projectFile.getName()
                                + ".", e );
                            ViewUtils.displayErrorMessageBox( "Import Error",
                                "An error occured when importing project " + projectFile.getName() + "." );
                        }
                        monitor.worked( 1 );
                    }

                    monitor.done();
                }
            } );
        }
        catch ( InvocationTargetException e )
        {
            // Nothing to do (it will never occur)
            e.printStackTrace();
        }
        catch ( InterruptedException e )
        {
            // Nothing to do.
        }

        return true;
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init( IWorkbench workbench, IStructuredSelection selection )
    {
        setNeedsProgressMonitor( true );

        projectsHandler = Activator.getDefault().getProjectsHandler();
    }
}
