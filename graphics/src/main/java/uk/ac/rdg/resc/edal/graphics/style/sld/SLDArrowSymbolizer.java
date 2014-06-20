package uk.ac.rdg.resc.edal.graphics.style.sld;

import java.awt.Color;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import uk.ac.rdg.resc.edal.graphics.style.ArrowLayer;
import uk.ac.rdg.resc.edal.graphics.style.ArrowLayer.ArrowStyle;
import uk.ac.rdg.resc.edal.graphics.style.ImageLayer;

public class SLDArrowSymbolizer extends AbstractSLDSymbolizer1D {

	/*
	 * Parse symbolizer using XPath
	 */
	@Override
	protected ImageLayer parseSymbolizer() throws NumberFormatException, XPathExpressionException, SLDException {
		// get the arrow properties
		String arrowSizeText = (String) xPath.evaluate(
				"./resc:ArrowSize", symbolizerNode, XPathConstants.STRING);
		Integer arrowSize = 8;
		if (!(arrowSizeText == null) && !(arrowSizeText.equals(""))) {
			arrowSize = Integer.parseInt(arrowSizeText);
		}
		String arrowColourText = (String) xPath.evaluate(
				"./resc:ArrowColour", symbolizerNode, XPathConstants.STRING);
		Color arrowColour = Color.BLACK;
		if (arrowColourText != null && !(arrowColourText.equals(""))) {
			arrowColour = decodeColour(arrowColourText);
		}
		
		ArrowStyle arrowStyle = ArrowStyle.THIN_ARROW;
		String arrowStyleText = (String) xPath.evaluate(
		        "./resc:ArrowStyle", symbolizerNode, XPathConstants.STRING);
		if (arrowStyleText != null && !(arrowStyleText.equals(""))) {
		    arrowStyle = ArrowStyle.valueOf(arrowStyleText);
		}
				
		// instantiate a new arrow layer and add it to the image
		ArrowLayer arrowLayer = new ArrowLayer(layerName, arrowSize, arrowColour, arrowStyle);
		return arrowLayer;
	}

}
