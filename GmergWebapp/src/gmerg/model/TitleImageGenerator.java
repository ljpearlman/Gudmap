/**
 * 
 */
package gmerg.model;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.AffineTransform;


/**
 * @author xingjun
 * 
 * used for processed gene list display page
 *
 */
public class TitleImageGenerator {
	
	public BufferedImage getTitleImage(String title, int width, int height, int margin) {
		return getTitleImage(title, width, height, margin, margin, null, null, null, false, null);
	}
	
	public BufferedImage getTitleImage(String title, int width, int height, Color background, boolean vertical, String align) {
		return getTitleImage(title, width, height, 1, 1, null, null, background, vertical, align);
	}
	
	public BufferedImage getTitleImage(String title, int width, int height, int margin, Font font, Color background, boolean vertical, String align) { 
		return getTitleImage(title, width, height, margin, margin, font, null, background, vertical, align);
	}
	
	public BufferedImage getTitleImage(String title, int heightInput, int widthInput, int marginX, int marginY, Font font, Color color, Color background, boolean vertical, String align) {
		if (heightInput<=0 || widthInput<=0)
			return null;
		
		int width = widthInput;
		int height = heightInput;
		if (vertical) {
			width = heightInput;
			height = widthInput;
		}

		BufferedImage titleImage;
		titleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = titleImage.createGraphics();

		if (font != null)
			graphics.setFont(font);
		else 
			graphics.setFont(new Font("SansSerif", Font.PLAIN,  12));
		
		if (color != null)
			graphics.setColor(color);
		else
			graphics.setColor(Color.BLACK);
		if (background != null) 
			graphics.setBackground(background);
		else
			graphics.setBackground(Color.WHITE);

		graphics.clearRect(0, 0, width, height);
		if (align == null)
			align = "center";
		
		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D titleBox = fm.getStringBounds(title, graphics);
		int boxWidth = (int)Math.round(titleBox.getWidth());
		int boxHeight = (int)Math.round(titleBox.getHeight());
		int x = 0;
		int y = 0;

		if (vertical) {
		    x = marginX;
		    y = ((int)Math.round((height + boxHeight)/2) - fm.getDescent() - fm.getLeading());
		} else {
		    x = (int)Math.round((width - boxWidth)/2);
		    if (align.equalsIgnoreCase("left"))
			x = marginX;
		    else if (align.equalsIgnoreCase("right"))
			x = Math.max (0, width - boxWidth - marginX);
		    y = ((int)Math.round((height + boxHeight)/2) - fm.getDescent() - fm.getLeading());
		}

		graphics.drawString(title, x, y);
		
		if (vertical) {
		    width  = titleImage.getWidth();  
		    height = titleImage.getHeight();  
		    BufferedImage   rotatedImage = new BufferedImage( height, width, titleImage.getType() );  
  
		    for( int i=0 ; i < width ; i++ )  
			for( int j=0 ; j < height ; j++ )  
			    rotatedImage.setRGB( height-1-j, i, titleImage.getRGB(i,j) );  

		    titleImage = rotatedImage;
		}

		return titleImage;
	}

}
