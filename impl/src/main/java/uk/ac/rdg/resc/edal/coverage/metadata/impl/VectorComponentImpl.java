package uk.ac.rdg.resc.edal.coverage.metadata.impl;

import java.util.List;

import uk.ac.rdg.resc.edal.Phenomenon;
import uk.ac.rdg.resc.edal.Unit;
import uk.ac.rdg.resc.edal.coverage.metadata.PlotStyle;
import uk.ac.rdg.resc.edal.coverage.metadata.VectorComponent;
import uk.ac.rdg.resc.edal.coverage.metadata.VectorMetadata;

public class VectorComponentImpl extends ScalarMetadataImpl implements VectorComponent {

    private VectorComponentType direction;

    public VectorComponentImpl(String name, String description,
            Phenomenon parameter, Unit units, Class<?> clazz, VectorComponentType direction, List<PlotStyle> allowedPlotStyles) {
        super(name, description, parameter, units, clazz, allowedPlotStyles);
        this.direction = direction;
    }
    
    @Override
    public VectorMetadata getParent() {
        return (VectorMetadata) super.getParent();
    }

    @Override
    public VectorComponentType getComponentType() {
        return direction;
    }
    
    @Override
    public VectorComponent clone() throws CloneNotSupportedException {
        return new VectorComponentImpl(getName(), getDescription(), getParameter(),
                getUnits(), getValueType(), direction, getAllowedPlotStyles());
    }

}
