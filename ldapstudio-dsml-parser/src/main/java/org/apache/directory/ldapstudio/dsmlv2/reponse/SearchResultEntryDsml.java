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

package org.apache.directory.ldapstudio.dsmlv2.reponse;


import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.directory.shared.ldap.codec.LdapMessage;
import org.apache.directory.shared.ldap.codec.search.SearchResultEntry;
import org.dom4j.Element;


/**
 * DSML Decorator for SearchResultEntry
 */
public class SearchResultEntryDsml extends LdapResponseDecorator implements DsmlDecorator
{
    /**
     * Default constructor
     * @param ldapMessage the message to decorate
     */
    public SearchResultEntryDsml( LdapMessage ldapMessage )
    {
        super( ldapMessage );
    }


    /**
     * Get the message type
     * @return Returns the type.
     */
    public int getMessageType()
    {
        return instance.getSearchResultEntry().getMessageType();
    }


    /**
     * Convert the request to its XML representation in the DSMLv2 format.
     * @param root the root dom4j Element
     * @return the dom4j Element corresponding to the entry.
     */
    public Element toDsml( Element root )
    {
        Element element = root.addElement( "searchResultEntry" );
        SearchResultEntry searchResultEntry = instance.getSearchResultEntry();
        element.addAttribute( "dn", searchResultEntry.getObjectName().toString() );

        Attributes attributes = searchResultEntry.getPartialAttributeList();
        NamingEnumeration ne = attributes.getAll();

        // Looping on Attributes Enumeration
        while ( ne.hasMoreElements() )
        {
            Attribute attribute = ( Attribute ) ne.nextElement();

            Element attributeElement = element.addElement( "attr" );
            attributeElement.addAttribute( "name", attribute.getID() );

            // Loopint on Values Enumeration
            try
            {
                NamingEnumeration ne2 = attribute.getAll();

                while ( ne2.hasMoreElements() )
                {
                    String str = ne2.nextElement().toString();
                    attributeElement.addElement( "value" ).addText( str );
                }
            }
            catch ( NamingException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return element;
    }

}
