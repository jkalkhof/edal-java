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

package uk.ac.rdg.resc.edal.util;

import java.sql.Connection;
import java.sql.SQLException;

import org.geotoolkit.factory.Hints;
import org.geotoolkit.referencing.CRS;
import org.geotoolkit.referencing.crs.DefaultGeographicCRS;
import org.geotoolkit.referencing.factory.epsg.EpsgInstaller;
import org.h2.jdbcx.JdbcDataSource;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.FactoryException;

import uk.ac.rdg.resc.edal.exceptions.EdalException;
import uk.ac.rdg.resc.edal.exceptions.InvalidCrsException;
import uk.ac.rdg.resc.edal.geometry.BoundingBox;
import uk.ac.rdg.resc.edal.geometry.BoundingBoxImpl;
import uk.ac.rdg.resc.edal.position.HorizontalPosition;

/**
 * A class containing static methods which are useful for GIS operations.
 * 
 * @author Guy
 * 
 */
public final class GISUtils {

    private GISUtils() {
    }

    public static boolean isWgs84LonLat(CoordinateReferenceSystem coordinateReferenceSystem) {
        try {
            return CRS.findMathTransform(coordinateReferenceSystem, DefaultGeographicCRS.WGS84)
                    .isIdentity();
        } catch (Exception e) {
            return false;
        }
    }

    public static double getNextEquivalentLongitude(double reference, double target) {
        // Find the clockwise distance from the first value on this axis
        // to the target value. This will be a positive number from 0 to
        // 360 degrees
        double clockDiff = constrainLongitude360(target - reference);
        return reference + clockDiff;
    }

    public static double constrainLongitude180(double value) {
        double val = constrainLongitude360(value);
        return val > 180.0 ? val - 360.0 : val;
    }

    /**
     * Returns a longitude value in degrees that is equal to the given value but
     * in the range [0:360]
     */
    public static double constrainLongitude360(double value) {
        double val = value % 360.0;
        return val < 0.0 ? val + 360.0 : val;
    }

    /**
     * Transforms the given HorizontalPosition to a new position in the given
     * coordinate reference system.
     * 
     * @param pos
     *            The position to translate.
     * @param targetCrs
     *            The CRS to translate into
     * @return a new position in the given CRS, or the same position if the new
     *         CRS is the same as the point's CRS. The returned point's CRS will
     *         be set to {@code targetCrs}. If the CRS of the position is null,
     *         the CRS will simply be set to the targetCrs.
     * @throws NullPointerException
     *             if {@code targetCrs} is null.
     * @todo error handling
     */
    public static HorizontalPosition transformPosition(HorizontalPosition pos,
            CoordinateReferenceSystem targetCrs) {
        if (targetCrs == null) {
            throw new NullPointerException("Target CRS cannot be null");
        }
        CoordinateReferenceSystem sourceCrs = pos.getCoordinateReferenceSystem();
        if (sourceCrs == null) {
            return new HorizontalPosition(pos.getX(), pos.getY(), targetCrs);
        }
        /*
         * CRS.findMathTransform() caches recently-used transform objects so we
         * should incur no large penalty for multiple invocations
         */
        try {
            MathTransform transform = CRS.findMathTransform(sourceCrs, targetCrs);
            if (transform.isIdentity())
                return pos;
            double[] point = new double[] { pos.getX(), pos.getY() };
            transform.transform(point, 0, point, 0, 1);
            return new HorizontalPosition(point[0], point[1], targetCrs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean crsMatch(CoordinateReferenceSystem sourceCrs,
            CoordinateReferenceSystem targetCrs) {
        MathTransform transform;
        try {
            transform = CRS.findMathTransform(sourceCrs, targetCrs);
            return transform.isIdentity();
        } catch (FactoryException e) {
            /*
             * There is a problem performing the transfer. Say that these CRSs
             * do not match (since we can't be sure)
             */
            return false;
        }
    }

    /**
     * Finds a {@link CoordinateReferenceSystem} with the given code, forcing
     * longitude-first axis order.
     * 
     * @param crsCode
     *            The code for the CRS
     * @return a coordinate reference system with the longitude axis first
     * @throws InvalidCrsException
     *             if a CRS matching the code cannot be found
     * @throws NullPointerException
     *             if {@code crsCode} is null
     */
    public static CoordinateReferenceSystem getCrs(String crsCode) throws InvalidCrsException {
        if (crsCode == null)
            throw new NullPointerException("CRS code cannot be null");
        try {
            /* The "true" means "force longitude first" */
            return CRS.decode(crsCode, true);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidCrsException(crsCode);
        }
    }

    /**
     * Converts a string of the form "x1,y1,x2,y2" into a {@link BoundingBox}
     * 
     * @throws EdalException
     *             if the format of the bounding box is invalid
     */
    public static BoundingBox parseBbox(String bboxStr, boolean lonFirst, String crs)
            throws EdalException {
        String[] bboxEls = bboxStr.split(",");
        /* Check the validity of the bounding box */
        if (bboxEls.length != 4) {
            throw new EdalException("Invalid bounding box format: need four elements");
        }
        double minx, miny, maxx, maxy;
        try {
            if (lonFirst) {
                minx = Double.parseDouble(bboxEls[0]);
                miny = Double.parseDouble(bboxEls[1]);
                maxx = Double.parseDouble(bboxEls[2]);
                maxy = Double.parseDouble(bboxEls[3]);
            } else {
                minx = Double.parseDouble(bboxEls[1]);
                miny = Double.parseDouble(bboxEls[0]);
                maxx = Double.parseDouble(bboxEls[3]);
                maxy = Double.parseDouble(bboxEls[2]);
            }
        } catch (NumberFormatException nfe) {
            throw new EdalException("Invalid bounding box format: all elements must be numeric");
        }
        if (minx >= maxx || miny >= maxy) {
            throw new EdalException("Invalid bounding box format");
        }
        return new BoundingBoxImpl(minx, miny, maxx, maxy, getCrs(crs));
    }

    static {
        /*
         * Initialise the EPSG database if necessary
         */
        try {
            Class.forName("org.h2.Driver");
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:.h2/epsg.db");
            Connection conn = dataSource.getConnection();
            conn.setAutoCommit(true);
            Hints.putSystemDefault(Hints.EPSG_DATA_SOURCE, dataSource);
            EpsgInstaller i = new EpsgInstaller();
            i.setDatabase(conn);
            if (!i.exists()) {
                i.call();
            }
        } catch (FactoryException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
