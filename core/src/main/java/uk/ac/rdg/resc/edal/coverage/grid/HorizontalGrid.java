/*******************************************************************************
 * Copyright (c) 2011 The University of Reading
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
 *******************************************************************************/

package uk.ac.rdg.resc.edal.coverage.grid;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import uk.ac.rdg.resc.edal.coverage.domain.GridDomain;
import uk.ac.rdg.resc.edal.geometry.BoundingBox;
import uk.ac.rdg.resc.edal.position.HorizontalPosition;
import uk.ac.rdg.resc.edal.util.BigList;

/**
 * A two-dimensional grid in the horizontal plane that is referenced to a 2D
 * horizontal coordinate reference system. If each individual grid axis is
 * aligned with the axes of the CRS, the {@link RectilinearGrid} class can be
 * used.
 * 
 * @author Jon Blower
 */
public interface HorizontalGrid extends GridDomain<HorizontalPosition, GridCell2D> {

    /**
     * {@inheritDoc}
     * <p>
     * This may or may not be aligned with any of the real-world coordinate axes
     * in the {@link #getCoordinateReferenceSystem() coordinate reference
     * system}.
     * </p>
     */
    public GridAxis getXAxis();

    /**
     * {@inheritDoc}
     * <p>
     * This may or may not be aligned with any of the real-world coordinate axes
     * in the {@link #getCoordinateReferenceSystem() coordinate reference
     * system}.
     * </p>
     */
    public GridAxis getYAxis();

    /**
     * {@inheritDoc}
     * <p>
     * Grids can have a large number of values, hence we specialize the return
     * type to a {@link BigList}.
     * </p>
     * 
     * @return
     */
    @Override
    public BigList<GridCell2D> getDomainObjects();

    /**
     * Returns a two-dimensional horizontal coordinate reference system.
     * 
     * @return a two-dimensional horizontal coordinate reference system.
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem();

    /**
     * Gets the bounding box for the coordinates of the grid in the grid's
     * coordinate reference system.
     * 
     * @return A {@link BoundingBox} containing the co-ordinates of the grid
     */
    public BoundingBox getCoordinateExtent();

    /**
     * Returns the {@link HorizontalPosition} associated with the given
     * coordinates
     * 
     * @param xIndex
     *            the index of the x-coordinate
     * @param yIndex
     *            the index of the y-coordinate
     * @return the {@link HorizontalPosition} at the given coordinates
     */
    public HorizontalPosition transformCoordinates(int xIndex, int yIndex);

    /**
     * Returns the {@link HorizontalPosition} associated with the given
     * coordinates
     * 
     * @param coords
     *            the indices of the coordinates
     * @return the {@link HorizontalPosition} at the given coordinates
     */
    public HorizontalPosition transformCoordinates(GridCoordinates2D coords);

    /**
     * Returns the coordinates of the GridCell that contains the given
     * horizontal position
     * 
     * @param pos
     *            the desired {@link HorizontalPosition}
     * @return the {@link GridCoordinates2D} containing the desired coordinates,
     *         or <code>null</code> if the position is outside of the grid
     */
    public GridCell2D findContainingCell(HorizontalPosition pos);

    /**
     * Returns the {@link GridCell2D} containing the given coordinates
     * 
     * @param coords
     *            the indices of the coordinates
     * @return the {@link GridCell2D} containing the given coordinates
     */
    public GridCell2D getGridCell(GridCoordinates2D coords);

    /**
     * Returns the {@link GridCell2D} containing the given coordinates
     * 
     * @param xIndex
     *            the index of the x-coordinate
     * @param yIndex
     *            the index of the y-coordinate
     * @return the {@link GridCell2D} containing the given coordinates
     */
    public GridCell2D getGridCell(int xIndex, int yIndex);

    @Override
    public GridCoordinates2D getCoords(long index);
}