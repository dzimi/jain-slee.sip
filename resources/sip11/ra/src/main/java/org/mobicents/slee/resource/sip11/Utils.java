/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.mobicents.slee.resource.sip11;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;

import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.CSeqHeader;
import javax.sip.header.CallIdHeader;
import javax.sip.header.ContactHeader;
import javax.sip.header.ContentLengthHeader;
import javax.sip.header.FromHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.header.RecordRouteHeader;
import javax.sip.header.RouteHeader;
import javax.sip.header.ToHeader;
import javax.sip.header.ViaHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;

public class Utils {

    private static Set<String> DIALOG_CREATING_METHODS;

    /**
     *
     * @return
     */
    public static Set<String> getDialogCreatingMethods() {
        if (DIALOG_CREATING_METHODS == null) {
            final Set<String> set = new HashSet<String>();
            set.add(Request.INVITE);
            set.add(Request.REFER);
            set.add(Request.SUBSCRIBE);
            DIALOG_CREATING_METHODS = Collections.unmodifiableSet(set);
        }
        return DIALOG_CREATING_METHODS;
    }
    private static Set<String> HEADERS_TO_OMMIT_ON_REQUEST_COPY;

    /**
     *
     * @return
     */
    public static Set<String> getHeadersToOmmitOnRequestCopy() {
        if (HEADERS_TO_OMMIT_ON_REQUEST_COPY == null) {
            final Set<String> set = new HashSet<String>();
            set.add(RouteHeader.NAME);
            set.add(RecordRouteHeader.NAME);
            set.add(ViaHeader.NAME);
            set.add(CallIdHeader.NAME);
            set.add(CSeqHeader.NAME);
            set.add(FromHeader.NAME);
            set.add(ToHeader.NAME);
            set.add(ContentLengthHeader.NAME);
            HEADERS_TO_OMMIT_ON_REQUEST_COPY = Collections.unmodifiableSet(set);
        }
        return HEADERS_TO_OMMIT_ON_REQUEST_COPY;
    }
    private static Set<String> HEADERS_TO_OMMIT_ON_RESPONSE_COPY;

    /**
     *
     * @return
     */
    public static Set<String> getHeadersToOmmitOnResponseCopy() {
        if (HEADERS_TO_OMMIT_ON_RESPONSE_COPY == null) {
            final Set<String> set = new HashSet<String>();
            set.add(RouteHeader.NAME);
            set.add(RecordRouteHeader.NAME);
            set.add(ViaHeader.NAME);
            set.add(CallIdHeader.NAME);
            set.add(CSeqHeader.NAME);
            set.add(ContactHeader.NAME);
            set.add(FromHeader.NAME);
            set.add(ToHeader.NAME);
            set.add(ContentLengthHeader.NAME);
            HEADERS_TO_OMMIT_ON_RESPONSE_COPY = Collections.unmodifiableSet(set);
        }
        return HEADERS_TO_OMMIT_ON_RESPONSE_COPY;
    }

    /**
     * Generates route list the same way dialog does.
     *
     * @param response
     * @return
     * @throws ParseException
     */
    public static List<RouteHeader> getRouteList(Response response, HeaderFactory headerFactory) throws ParseException {
        // we have record route set, as we are client, this is reversed
        final ArrayList<RouteHeader> routeList = new ArrayList<RouteHeader>();
        final ListIterator<?> rrLit = response.getHeaders(RecordRouteHeader.NAME);
        while (rrLit.hasNext()) {
            final RecordRouteHeader rrh = (RecordRouteHeader) rrLit.next();
            final RouteHeader rh = headerFactory.createRouteHeader(rrh.getAddress());
            final Iterator<?> pIt = rrh.getParameterNames();
            while (pIt.hasNext()) {
                final String pName = (String) pIt.next();
                rh.setParameter(pName, rrh.getParameter(pName));
            }
            routeList.add(0, rh);
        }
        return routeList;
    }

    /**
     * Forges Request-URI using contact and To name par to address URI, this is
     * required on dialog fork, this is how target is determined
     *
     * @param response
     * @return
     * @throws ParseException
     */
    public static URI getRequestUri(Response response, AddressFactory addressFactory) throws ParseException {
        final ContactHeader contact = ((ContactHeader) response.getHeader(ContactHeader.NAME));
        return (contact != null) ? (URI) contact.getAddress().getURI().clone() : null;
    }

    public static void incrementResponseUsageParameters(Response resp, SipResourceAdaptorUsageParameters usageParams) {
        int code = resp.getStatusCode();
        usageParams.incrementSipMessages(1);
        if (code > 179 && code < 190) {
            usageParams.incrementResponses_18x(1);
            switch (code) {
                case 180:
                    usageParams.incrementResponses_180(1);
                    break;
                case 181:
                    usageParams.incrementResponses_181(1);
                    break;
                case 183:
                    usageParams.incrementResponses_183(1);
                    break;
            }
        } else if (code > 199 && code < 300) {
            usageParams.incrementResponses_2xx(1);
            switch (code) {
                case 200:
                    usageParams.incrementResponses_200(1);
                    break;
            }

        } else if (code > 299 && code < 400) {
            usageParams.incrementResponses_3xx(1);
        } else if (code > 399 && code < 500) {
            usageParams.incrementResponses_4xx(1);
            usageParams.incrementSipMessages(1);
            usageParams.incrementACK_Requests(1);
            switch (code) {                 
                case 400:
                    usageParams.incrementResponses_400(1);
                    break;
                case 401:
                    usageParams.incrementResponses_401(1);
                    break;
                case 403:
                    usageParams.incrementResponses_403(1);
                    break;
                case 404:
                    usageParams.incrementResponses_404(1);
                    break;
                case 405:
                    usageParams.incrementResponses_405(1);
                    break;
                case 406:
                    usageParams.incrementResponses_406(1);
                    break;
                case 408:
                    usageParams.incrementResponses_408(1);
                    break;
                case 415:
                    usageParams.incrementResponses_415(1);
                    break;
                case 480:
                    usageParams.incrementResponses_480(1);
                    break;
                case 486:
                    usageParams.incrementResponses_486(1);
                    break;
                case 487:
                    usageParams.incrementResponses_487(1);
                    break;
                case 488:
                    usageParams.incrementResponses_488(1);
                    break;
            }
        } else if (code > 499 && code < 600) {
            usageParams.incrementResponses_5xx(1);
            usageParams.incrementSipMessages(1);
            usageParams.incrementACK_Requests(1);
            switch (code) {
                case 500:
                    usageParams.incrementResponses_500(1);
                    break;
                case 501:
                    usageParams.incrementResponses_501(1);
                    break;
                case 502:
                    usageParams.incrementResponses_502(1);
                    break;
                case 503:
                    usageParams.incrementResponses_503(1);
                    break;
                case 504:
                    usageParams.incrementResponses_504(1);
                    break;
            }

        }
    }

    public static void incrementRequestUsageParameters(Request req, SipResourceAdaptorUsageParameters usageParams) {
        incrementRequestUsageParameters(req.getMethod(), usageParams);
    }

    public static void incrementRequestUsageParameters(String method, SipResourceAdaptorUsageParameters usageParams) {
        usageParams.incrementSipMessages(1);
        if (method.equals(Request.INVITE)) {
            usageParams.incrementINVITE_Requests(1);
        } else if (method.equals(Request.ACK)) {
            usageParams.incrementACK_Requests(1);
        } else if (method.equals(Request.BYE)) {
            usageParams.incrementBYE_Requests(1);
        } else if (method.equals(Request.CANCEL)) {
            usageParams.incrementCANCEL_Requests(1);
        } else if (method.equals(Request.OPTIONS)) {
            usageParams.incrementOPTIONS_Requests(1);
        } else if (method.equals(Request.PRACK)) {
            usageParams.incrementPRACK_Requests(1);
        }
    }
}
