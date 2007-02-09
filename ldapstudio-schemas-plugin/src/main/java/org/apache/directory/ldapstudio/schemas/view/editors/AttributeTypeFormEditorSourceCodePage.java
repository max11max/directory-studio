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

package org.apache.directory.ldapstudio.schemas.view.editors;


import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.apache.directory.ldapstudio.schemas.model.AttributeType;
import org.apache.directory.ldapstudio.schemas.model.Schema.SchemaType;
import org.apache.directory.ldapstudio.schemas.view.viewers.SchemaSourceViewer;
import org.apache.directory.server.core.tools.schema.AttributeTypeLiteral;
import org.apache.directory.server.core.tools.schema.OpenLdapSchemaParser;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;


/**
 * This class is the Source Code Page of the Attribute Type Editor
 */
public class AttributeTypeFormEditorSourceCodePage extends FormPage
{
    /** The page ID*/
    public static final String ID = AttributeTypeFormEditor.ID + "sourceCodePage";

    /** The page title */
    public static String TITLE = Messages.getString( "AttributeTypeFormEditor.Source_Code" );

    /** The modified attribute type */
    private AttributeType modifiedAttributeType;

    /** The Schema Source Viewer */
    private SchemaSourceViewer schemaSourceViewer;

    /** The flag to indicate if the user can leave the Source Code page */
    private boolean canLeaveThePage = true;

    /** The listener of the Schema Source Editor Widget */
    private ModifyListener schemaSourceViewerListener = new ModifyListener()
    {
        public void modifyText( ModifyEvent e )
        {
            canLeaveThePage = true;
            try
            {
                ( ( AttributeTypeFormEditor ) getEditor() ).setDirty( true );
                OpenLdapSchemaParser parser = new OpenLdapSchemaParser();
                parser.parse( schemaSourceViewer.getTextWidget().getText() );
                List attributeTypes = parser.getAttributeTypes();
                if ( attributeTypes.size() != 1 )
                {
                    // Throw an exception and return
                }
                else
                {
                    updateAttributeType( ( AttributeTypeLiteral ) attributeTypes.get( 0 ) );
                }
            }
            catch ( IOException e1 )
            {
                canLeaveThePage = false;
            }
            catch ( ParseException exception )
            {
                canLeaveThePage = false;
                System.err.println( exception.getMessage() );
            }
        }
    };


    /**
     * Default constructor
     * 
     * @param editor
     *      the associated editor
     */
    public AttributeTypeFormEditorSourceCodePage( FormEditor editor )
    {
        super( editor, ID, TITLE );
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.forms.editor.FormPage#createFormContent(org.eclipse.ui.forms.IManagedForm)
     */
    protected void createFormContent( IManagedForm managedForm )
    {
        ScrolledForm form = managedForm.getForm();
        FormToolkit toolkit = managedForm.getToolkit();
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        form.getBody().setLayout( layout );
        toolkit.paintBordersFor( form.getBody() );

        modifiedAttributeType = ( ( AttributeTypeFormEditor ) getEditor() ).getModifiedAttributeType();

        // SOURCE CODE Field
        schemaSourceViewer = new SchemaSourceViewer( form.getBody(), null, null, false, SWT.BORDER | SWT.H_SCROLL
            | SWT.V_SCROLL );
        GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
        gd.heightHint = 10;
        schemaSourceViewer.getTextWidget().setLayoutData( gd );
        if ( modifiedAttributeType.getOriginatingSchema().type == SchemaType.coreSchema )
        {
            schemaSourceViewer.setEditable( false );
        }

        // set text font
        Font font = JFaceResources.getFont( JFaceResources.TEXT_FONT );
        schemaSourceViewer.getTextWidget().setFont( font );

        IDocument document = new Document();
        schemaSourceViewer.setDocument( document );

        // Initialization from the "input" attribute type
        fillInUiFields();

        schemaSourceViewer.getTextWidget().addModifyListener( schemaSourceViewerListener );
    }


    /**
     * Fills in the User Interface.
     */
    private void fillInUiFields()
    {
        // SOURCE CODE Field
        schemaSourceViewer.getDocument().set( modifiedAttributeType.write() );
    }


    /* (non-Javadoc)
     * @see org.eclipse.ui.forms.editor.FormPage#canLeaveThePage()
     */
    public boolean canLeaveThePage()
    {
        return canLeaveThePage;
    }


    /**
     * Updates the Modified Attribute Type from the given Attribute Type Literal.
     *
     * @param atl
     *      the Attribute Type Literal
     */
    private void updateAttributeType( AttributeTypeLiteral atl )
    {
        modifiedAttributeType.setCollective( atl.isCollective() );
        modifiedAttributeType.setDescription( atl.getDescription() );
        modifiedAttributeType.setEquality( atl.getEquality() );
        modifiedAttributeType.setNames( atl.getNames() );
        modifiedAttributeType.setNoUserModification( atl.isNoUserModification() );
        modifiedAttributeType.setObsolete( atl.isObsolete() );
        // TODO Need to update the Shared LDAP lirary to be able to modify the OID
        modifiedAttributeType.setOrdering( atl.getOrdering() );
        modifiedAttributeType.setSingleValue( atl.isSingleValue() );
        modifiedAttributeType.setSubstr( atl.getSubstr() );
        modifiedAttributeType.setSuperior( atl.getSuperior() );
        modifiedAttributeType.setSyntax( atl.getSyntax() );
        modifiedAttributeType.setUsage( atl.getUsage() );
    }


    /**
     * Refreshes the UI.
     */
    public void refreshUI()
    {
        fillInUiFields();
    }
}
