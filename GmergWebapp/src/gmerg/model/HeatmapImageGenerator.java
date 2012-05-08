/**
 * 
 */
package gmerg.model;

import gmerg.utils.Utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class HeatmapImageGenerator {
	
	public BufferedImage getHeatmapImage(double[][] values, int tileSize) {
		return getHeatmapImage(values, tileSize, -1, -1);
	}
	
	public BufferedImage getHeatmapImage(double[][] values, int width, int height, boolean imageDims) {
		return getHeatmapImage(values, -1, width, height);
	}
	
	/**
	 * <p>modified by xingjun - 17/09/2009
	 * - check the nullability in the first place</p>
	 */
	public BufferedImage getHeatmapImage(double[][] values, int tileSize, int width, int height) {
		if (values == null || values[0] == null) {
//			return 	getHeatmapImage(null, 0, 0);	// this is to avoid display of a red cross in IE when there is no image data 	
			return null;
		}
		int rowNum = values.length;
		int colNum = values[0].length;
		int tileWidth = Math.max(0, Math.round(width/colNum)); 
		int tileHeight = Math.max(0, Math.round(height/rowNum)); 
		if (width <= 0) 
			tileWidth = tileHeight;
		if (height <= 0) 
			tileHeight = tileWidth;
		if (tileSize > 0) 
			tileWidth = tileHeight = tileSize;
		return getHeatmapImage(values, tileWidth, tileHeight);
	}
		
	public BufferedImage getHeatmapImage(double[][] values, int tileWidth, int tileHeight) {
		if (values == null || values[0]==null) {
/*			
			BufferedImage dumyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = dumyImage.getGraphics();
			graphics.setColor(new Color(0, 0, 0, 0));
			graphics.fillRect(0, 0, 1, 1);
			return dumyImage;
*/			
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		}			
		
		int rowNum = values.length;
		int colNum = values[0].length;
		int heatmapWidth = tileWidth * colNum;
		int heatmapHeight = tileHeight * rowNum;
		
		BufferedImage heatmap = new BufferedImage(heatmapWidth, heatmapHeight, BufferedImage.TYPE_INT_RGB);
		Graphics graphics = heatmap.getGraphics();
		Color expressedColor = Utility.getHeatmapExpressedColor(); 
		Color notExpressedColor = Utility.getHeatmapNotExpressedColor(); 
		for (int i=0; i<rowNum; i++)
			for (int j=0; j<colNum; j++) {
				double value = values[i][j];
				Color baseColor;
				if (value < 0) {  
					baseColor = notExpressedColor; 
					value = -value;
				}
				else
					baseColor = expressedColor; 
				int r = Math.min(255, (int)Math.round(baseColor.getRed()*value));
				int g = Math.min(255, (int)Math.round(baseColor.getGreen()*value));
				int b = Math.min(255, (int)Math.round(baseColor.getBlue()*value));
				graphics.setColor(new Color(r, g, b));
				graphics.fillRect(j*tileWidth, i*tileHeight, tileWidth, tileHeight);
			}

		return heatmap;
	}

}
