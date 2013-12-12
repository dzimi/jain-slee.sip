/*
 * Copyright (C) 2013 mateuszD.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.mobicents.slee.resource.sip11;

/**
 *
 * @author mateuszD
 */
public interface SipResourceAdaptorUsageParameters {

    // counter-type usage parameter names
    public void incrementSipMessages(long value);

    public void incrementINVITE_Requests(long value);

    public void incrementACK_Requests(long value);

    public void incrementBYE_Requests(long value);

    public void incrementCANCEL_Requests(long value);

    public void incrementOPTIONS_Requests(long value);

    public void incrementPRACK_Requests(long value);

    public void incrementResponses_180(long value);

    public void incrementResponses_181(long value);

    public void incrementResponses_183(long value);

    public void incrementResponses_18x(long value);

    public void incrementResponses_200(long value);

    public void incrementResponses_2xx(long value);

    public void incrementResponses_3xx(long value);

    public void incrementResponses_400(long value);

    public void incrementResponses_401(long value);

    public void incrementResponses_403(long value);

    public void incrementResponses_404(long value);

    public void incrementResponses_405(long value);

    public void incrementResponses_406(long value);

    public void incrementResponses_408(long value);

    public void incrementResponses_415(long value);

    public void incrementResponses_480(long value);

    public void incrementResponses_486(long value);

    public void incrementResponses_487(long value);

    public void incrementResponses_488(long value);

    public void incrementResponses_4xx(long value);

    public void incrementResponses_500(long value);

    public void incrementResponses_501(long value);

    public void incrementResponses_502(long value);

    public void incrementResponses_503(long value);

    public void incrementResponses_504(long value);

    public void incrementResponses_5xx(long value);

    // sample-type usage parameter names
    public void sampleSipCallSessionTime(long value);
}
