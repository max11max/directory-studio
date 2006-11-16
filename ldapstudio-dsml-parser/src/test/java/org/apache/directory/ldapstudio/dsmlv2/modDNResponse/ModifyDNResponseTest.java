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

package org.apache.directory.ldapstudio.dsmlv2.modDNResponse;

import java.util.List;

import javax.naming.NamingException;

import org.apache.directory.ldapstudio.dsmlv2.AbstractResponseTest;
import org.apache.directory.ldapstudio.dsmlv2.Dsmlv2ResponseParser;
import org.apache.directory.shared.ldap.codec.Control;
import org.apache.directory.shared.ldap.codec.LdapResult;
import org.apache.directory.shared.ldap.codec.modifyDn.ModifyDNResponse;
import org.apache.directory.shared.ldap.util.StringTools;

import com.sun.jndi.ldap.LdapURL;

/**
 * Tests for the Modify DN Response parsing
 */
public class ModifyDNResponseTest extends AbstractResponseTest
{
    
    
    /**
     * Test parsing of a Response with the (optional) requestID attribute
     */
    public void testResponseWithRequestId()
    {
        Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_requestID_attribute.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
        
        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        assertEquals( 456, modifyDNResponse.getMessageId() );
    }
    

    /**
     * Test parsing of a response with a (optional) Control element
     */
    public void testResponseWith1Control()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_1_control.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        assertEquals( 1, modifyDNResponse.getControls().size() );
        
        Control control = modifyDNResponse.getCurrentControl();
        
        assertTrue( control.getCriticality() );
        
        assertEquals( "1.2.840.113556.1.4.643", control.getControlType() );
        
        assertEquals( "Some text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }
    

    /**
     * Test parsing of a response with 2 (optional) Control elements
     */
    public void testResponseWith2Controls()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_2_controls.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }
        
        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        assertEquals( 2, modifyDNResponse.getControls().size() );
        
        Control control = modifyDNResponse.getCurrentControl();
        
        assertFalse( control.getCriticality() );
        
        assertEquals( "1.2.840.113556.1.4.789", control.getControlType() );
        
        assertEquals( "Some other text", StringTools.utf8ToString( ( byte[] ) control.getControlValue() ) );
    }
    
    /**
     * Test parsing of a response with 3 (optional) Control elements without value
     */
    public void testResponseWith3ControlsWithoutValue()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_3_controls_without_value.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        assertEquals( 3, modifyDNResponse.getControls().size() );
        
        Control control = modifyDNResponse.getCurrentControl();
        
        assertTrue( control.getCriticality() );
        
        assertEquals( "1.2.840.113556.1.4.456", control.getControlType() );
        
        assertEquals( StringTools.EMPTY_BYTES, control.getControlValue() );
    }
       
    /**
     * Test parsing of a response without Result Code element
     */
    public void testResponseWithoutResultCode()
    {
        testParsingFail( ModifyDNResponseTest.class, "response_without_result_code.xml");
    }
    
    /**
     * Test parsing of a response with Result Code element but a not integer value
     */
    public void testResponseWithResultCodeNotInteger()
    {
        testParsingFail( ModifyDNResponseTest.class, "response_with_result_code_not_integer.xml");
    }
    
    /**
     * Test parsing of a response with Result Code 
     */
    public void testResponseWithResultCode()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_result_code.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        LdapResult ldapResult = modifyDNResponse.getLdapResult();
        
        assertEquals( 2, ldapResult.getResultCode() );
    }
    
    /**
     * Test parsing of a response with Error Message
     */
    public void testResponseWithErrorMessage()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_error_message.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        LdapResult ldapResult = modifyDNResponse.getLdapResult();
        
        assertEquals( "Unrecognized extended operation EXTENSION_OID: 1.2.6.1.4.1.18060.1.1.1.100.2", ldapResult.getErrorMessage() );
    }
    
    /**
     * Test parsing of a response with a Referral
     */
    public void testResponseWith1Referral()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_1_referral.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        LdapResult ldapResult = modifyDNResponse.getLdapResult();
        
        List referrals = ldapResult.getReferrals();
        
        assertEquals( 1, referrals.size() );
        
        Object referral = referrals.get( 0 );
        
        try 
        {
			assertEquals( new LdapURL( "ldap://www.apache.org/" ).toString(), referral.toString() );
		} 
        catch (NamingException e)
        {
			fail();
		}
    }
    
    /**
     * Test parsing of a response with 2 Referral elements
     */
    public void testResponseWith2Referrals()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_2_referrals.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        LdapResult ldapResult = modifyDNResponse.getLdapResult();
        
        List referrals = ldapResult.getReferrals();
        
        assertEquals( 2, referrals.size() );
        
        Object referral = referrals.get( 0 );
        
        try 
        {
			assertEquals( new LdapURL( "ldap://www.apache.org/" ).toString(), referral.toString() );
		} 
        catch (NamingException e)
        {
			fail();
		}
        
        Object referral2 = referrals.get( 1 );
        
        try 
        {
			assertEquals( new LdapURL( "ldap://www.apple.com/" ).toString(), referral2.toString() );
		} 
        catch (NamingException e)
        {
			fail();
		}
    }
    
    /**
     * Test parsing of a response with a Referral and an Error Message
     */
    public void testResponseWith1ReferralAndAnErrorMessage()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_1_referral_and_error_message.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        LdapResult ldapResult = modifyDNResponse.getLdapResult();
        
        List referrals = ldapResult.getReferrals();
        
        assertEquals( 1, referrals.size() );
        
        Object referral = referrals.get( 0 );
        
        try 
        {
			assertEquals( new LdapURL( "ldap://www.apache.org/" ).toString(), referral.toString() );
		} 
        catch (NamingException e)
        {
			fail();
		}
    }
    
    /**
     * Test parsing of a response with MatchedDN attribute
     */
    public void testResponseWithMatchedDNAttribute()
    {
    	Dsmlv2ResponseParser parser = null;
        try
        {
            parser = new Dsmlv2ResponseParser();
            
            parser.setInputFile( ModifyDNResponseTest.class.getResource( "response_with_matchedDN_attribute.xml" ).getFile() );
        
            parser.parse();
        }
        catch ( Exception e )
        {
            fail( e.getMessage() );
        }

        ModifyDNResponse modifyDNResponse = ( ModifyDNResponse ) parser.getBatchResponse().getCurrentResponse();
        
        LdapResult ldapResult = modifyDNResponse.getLdapResult();
        
        assertEquals( "cn=Bob Rush,ou=Dev,dc=Example,dc=COM", ldapResult.getMatchedDN() );
    }
    
    /**
     * Test parsing of a response with wrong matched DN
     */
    public void testResponseWithWrongMatchedDN()
    {
        testParsingFail( ModifyDNResponseTest.class, "response_with_wrong_matchedDN_attribute.xml");
    }

    /**
     * Test parsing of a response with wrong Descr attribute
     */
    public void testResponseWithWrongDescr()
    {
        testParsingFail( ModifyDNResponseTest.class, "response_with_wrong_descr.xml");
    }
}
