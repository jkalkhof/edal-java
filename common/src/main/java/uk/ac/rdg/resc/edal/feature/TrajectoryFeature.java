/*******************************************************************************
 * Copyright (c) 2013 The University of Reading
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the University of Reading, nor the names of the
 *    authors or contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package uk.ac.rdg.resc.edal.feature;

import java.util.Map;

import uk.ac.rdg.resc.edal.domain.TrajectoryDomain;
import uk.ac.rdg.resc.edal.metadata.Parameter;
import uk.ac.rdg.resc.edal.position.GeoPosition;
import uk.ac.rdg.resc.edal.util.Array1D;

/**
 * A {@link Feature} representing values over a 1-dimensional domain of
 * time-ordered points, where each point has a distinct value in 4-dimensional
 * space
 * 
 * @author Guy Griffiths
 * 
 */
public class TrajectoryFeature extends AbstractDiscreteFeature<GeoPosition, GeoPosition> {
    private static final long serialVersionUID = 1L;

    public TrajectoryFeature(String id, String name, String description, TrajectoryDomain domain,
            Map<String, Parameter> parameters, Map<String, ? extends Array1D<Number>> values) {
        super(id, name, description, domain, parameters, values);
    }

    @Override
    public TrajectoryDomain getDomain() {
        return (TrajectoryDomain) super.getDomain();
    }
    
    @Override
    public Array1D<Number> getValues(String paramId) {
        return (Array1D<Number>) super.getValues(paramId);
    }
}
