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
	
	public BufferedImage getTitleImage(String title, int height, int width, int marginX, int marginY, Font font, Color color, Color background, boolean vertical, String align) {
		if (height<=0 || width<=0)
			return null;
		
		BufferedImage titleImage;
		titleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		if (vertical) {
			int temp = width;
			width = height;
			height = temp;
		}
		Graphics2D graphics = titleImage.createGraphics();
		Font defaultFont = new Font("SansSerif", Font.PLAIN,  12);
		if (font != null)
			graphics.setFont(font);
		else 
			graphics.setFont(defaultFont);
		
		if (color != null)
			graphics.setColor(color);
		else
			graphics.setColor(Color.BLACK);
		if (background != null) 
			graphics.setBackground(background);
		else
			graphics.setBackground(Color.WHITE);
		if (vertical) 
			graphics.clearRect(0, 0, height, width);
		else
			graphics.clearRect(0, 0, width, height);
		if (align == null)
			align = "centre";
		
		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D titleBox = fm.getStringBounds(title, graphics);
		int boxWidth = (int)Math.round(titleBox.getWidth());
		int boxHeight = (int)Math.round(titleBox.getHeight());
		
		if (vertical) {
			graphics.rotate(-Math.PI/2.0, height/2.0, width/2.0);
			graphics.translate((height-width)/2.0, (width-height)/2.0);
		}
		
		if (boxWidth<=width-2*marginX && boxHeight<=height-2*marginY) { //Fits in the specified box
			int x = (int)Math.round((width - boxWidth)/2);
			if (align.equalsIgnoreCase("left"))
				x = marginX;
			else if (align.equalsIgnoreCase("right"))
				x = Math.max (0, width - boxWidth - marginX);
			int y = ((int)Math.round((height + boxHeight)/2) - fm.getDescent() - fm.getLeading());
			graphics.drawString(title, x, y);
		}
		else {	// Need scaling down to fit in the required dimensions
			BufferedImage scaledTitleImage = new BufferedImage(boxWidth, boxHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = scaledTitleImage.createGraphics();
			if (font != null)
				g.setFont(font);
			else 
				g.setFont(defaultFont);
			if (color != null)
				g.setColor(color);
			else
				g.setColor(Color.BLACK);
			if (background != null) 
				g.setBackground(background);
			else
				g.setBackground(Color.WHITE);
			g.clearRect(0, 0, boxWidth, boxHeight);
			g.drawString(title, 0, boxHeight - fm.getDescent() - fm.getLeading());
			int scaledWidth = Math.min(boxWidth, width-2*marginX+1);
			int scaledHeight = Math.min(boxHeight, height-2*marginY+1);
			scaledWidth = Math.min(width, scaledWidth);
			scaledHeight = Math.min(height, scaledHeight);
			Image scaledImage = scaledTitleImage.getScaledInstance(scaledWidth, scaledHeight, java.awt.Image.SCALE_DEFAULT);
			int scaledX = (int)Math.round((width - scaledWidth) / 2.0);
			if (align.equalsIgnoreCase("left"))
				scaledX = marginX;
			else if (align.equalsIgnoreCase("right"))
				scaledX = Math.max (0, width - scaledWidth - marginX);
			int scaledY = (int)Math.round((height - scaledHeight) / 2.0);
			graphics.drawImage(scaledImage, scaledX, scaledY, null);
		}
		
		return titleImage;
	}

}
