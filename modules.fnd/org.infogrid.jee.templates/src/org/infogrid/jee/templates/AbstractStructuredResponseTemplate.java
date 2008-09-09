//
// This file is part of InfoGrid(tm). You may not use this file except in
// compliance with the InfoGrid license. The InfoGrid license and important
// disclaimers are contained in the file LICENSE.InfoGrid.txt that you should
// have received with InfoGrid. If you have not received LICENSE.InfoGrid.txt
// or you do not consent to all aspects of the license and the disclaimers,
// no license is granted; do not use this file.
// 
// For more information about InfoGrid go to http://infogrid.org/
//
// Copyright 1998-2008 by R-Objects Inc. dba NetMesh Inc., Johannes Ernst
// All rights reserved.
//

package org.infogrid.jee.templates;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.infogrid.jee.CarriesHttpStatusCodeException;
import org.infogrid.jee.sane.SaneServletRequest;
import org.infogrid.util.http.SaneRequest;

/**
 * Factors out common functionality of StructuredResponseTemplates.
 */
public abstract class AbstractStructuredResponseTemplate
        implements
            StructuredResponseTemplate
{
    /**
     * Constructor for subclasses only.
     * 
     * @param request the incoming HTTP request
     * @param requestedTemplate the requested ResponseTemplate, if any
     * @param structured the StructuredResponse that contains the response
     */
    protected AbstractStructuredResponseTemplate(
            SaneServletRequest request,
            String             requestedTemplate,
            StructuredResponse structured )
    {
        theRequest           = request;
        theStructured        = structured;
        theRequestedTemplate = requestedTemplate;
    }
    
    /**
     * Obtain the incoming request.
     * 
     * @return the incoming request
     */
    public SaneRequest getRequest()
    {
        return theRequest;
    }

    /**
     * Obtain the StructuredResponse that instantiates this template.
     * 
     * @return the StructuredResponse
     */
    public StructuredResponse getStructuredResponse()
    {
        return theStructured;
    }

    /**
     * Default implementation for how to handle the HTTP status code.
     * 
     * @param delegate the underlying HttpServletResponse
     * @param structured the StructuredResponse that contains the response
     * @throws IOException thrown if an I/O error occurred
     */
    protected void outputStatusCode(
            HttpServletResponse delegate,
            StructuredResponse  structured )
        throws
            IOException
    {
        // If it is set to something not-OK, that wins
        // Then, the first exception wins
        int status = structured.getHttpResponseCode();
        Iterator<Throwable> iter = theStructured.problems().iterator();
        while( status == HttpServletResponse.SC_OK && iter.hasNext() ) {
            Throwable current = iter.next();
            if( current instanceof CarriesHttpStatusCodeException ) {
                status = ((CarriesHttpStatusCodeException)current).getDesiredHttpStatusCode();
            }
         }
        delegate.setStatus( status );
    }
    
    /**
     * Default implentation for how to handle cookies.
     * 
     * @param delegate the underlying HttpServletResponse
     * @param structured the StructuredResponse that contains the response
     * @throws IOException thrown if an I/O error occurred
     */
    protected void outputCookies(
            HttpServletResponse delegate,
            StructuredResponse  structured )
        throws
            IOException
    {
        Cookie toSend;
        if( theRequestedTemplate == null ) {
            toSend = new Cookie( LID_TEMPLATE_COOKIE_NAME, "**deleted**" );
            toSend.setMaxAge( 0 ); // delete cookie
        } else {
            toSend = new Cookie( LID_TEMPLATE_COOKIE_NAME, theRequestedTemplate );
            toSend.setPath( theRequest.getDelegate().getContextPath() );
            toSend.setMaxAge( 60 * 60 * 24 * 365 * 10 ); // 10 years
        }
        delegate.addCookie( toSend );
    }

    /**
     * Default implentation for how to handle the MIME type.
     * 
     * @param delegate the underlying HttpServletResponse
     * @param structured the StructuredResponse that contains the response
     * @throws IOException thrown if an I/O error occurred
     */
    protected void outputMimeType(
            HttpServletResponse delegate,
            StructuredResponse  structured )
        throws
            IOException
    {
        String mime = structured.getMimeType();
        if( mime == null ) {
            mime = "text/plain";
        }
        delegate.setContentType( mime );
    }

    /**
     * Default implementation for how to handle a location header.
     * 
     * @param delegate the underlying HttpServletResponse
     * @param structured the StructuredResponse that contains the response
     * @throws IOException thrown if an I/O error occurred
     */
    protected void outputLocation(
            HttpServletResponse delegate,
            StructuredResponse  structured )
        throws
            IOException
    {        
        String location = structured.getLocation();
        if( location != null ) {
            delegate.setHeader( "Location", location );
            // don't use delegate.sendRedirect as it also sets the status code
        }
    }

    /**
     * Default implementatino for how to handle additional HTTP headers.
     * 
     * @param delegate the underlying HttpServletResponse
     * @param structured the StructuredResponse that contains the response
     * @throws IOException thrown if an I/O error occurred
     */
    protected void outputAdditionalHeaders(
            HttpServletResponse delegate,
            StructuredResponse  structured )
        throws
            IOException
    {
        Map<String,String> headers = theStructured.additionalHeaders();
        if( headers != null ) {
            for( String key : headers.keySet()) {
                String value = headers.get( key );
                
                delegate.setHeader( key, value );
            }
        }
    }

    /**
     * The incoming request.
     */
    protected SaneServletRequest theRequest;

    /**
     * The structured response to process with the dispatcher
     */
    protected StructuredResponse theStructured;

    /**
     * The requested formatting template, if any.
     */
    protected String theRequestedTemplate;
}
