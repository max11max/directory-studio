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
package org.apache.directory.studio.apacheds.schemaeditor.view.editors.attributetype;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.directory.studio.apacheds.schemaeditor.Activator;
import org.apache.directory.studio.apacheds.schemaeditor.controller.SchemaHandler;
import org.apache.directory.studio.apacheds.schemaeditor.model.AttributeTypeImpl;
import org.apache.directory.studio.apacheds.schemaeditor.model.ObjectClassImpl;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


/**
 * This class is the Content Provider for the Mandatory Table of the Attribute Type Editor (Used By Page).
 *
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Rev$, $Date$
 */
public class ATEUsedByMandatoryTableContentProvider implements IStructuredContentProvider
{
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements( Object inputElement )
    {
        if ( inputElement instanceof AttributeTypeImpl )
        {
            List<ObjectClassImpl> results = new ArrayList<ObjectClassImpl>();
            AttributeTypeImpl inputAT = ( AttributeTypeImpl ) inputElement;
            SchemaHandler schemaHandler = Activator.getDefault().getSchemaHandler();

            List<String> names = new ArrayList<String>();

            String[] atNames = inputAT.getNames();
            if ( atNames != null )
            {
                for ( String name : atNames )
                {
                    names.add( name.toLowerCase() );
                }
            }

            List<ObjectClassImpl> objectClasses = schemaHandler.getObjectClasses();
            for ( ObjectClassImpl oc : objectClasses )
            {
                String[] musts = oc.getMustNamesList();
                if ( musts != null )
                {
                    for ( String must : musts )
                    {
                        if ( names.contains( must.toLowerCase() ) )
                        {
                            results.add( oc );
                        }
                    }
                }
            }

            // Sorting Results
            Collections.sort( results, new Comparator<ObjectClassImpl>()
            {
                public int compare( ObjectClassImpl oc1, ObjectClassImpl oc2 )
                {
                    if ( oc1 instanceof ObjectClassImpl && oc1 instanceof ObjectClassImpl )
                    {
                        return ( ( ObjectClassImpl ) oc1 ).getNames()[0]
                            .compareToIgnoreCase( ( ( ObjectClassImpl ) oc2 ).getNames()[0] );
                    }

                    return 0;
                }
            } );

            return results.toArray();
        }

        // Default
        return null;
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose()
    {
    }


    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
    {
    }
}
