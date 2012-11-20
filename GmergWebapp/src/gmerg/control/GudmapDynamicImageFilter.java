package gmerg.control;

import gmerg.assemblers.MasterTableAssembler;
import gmerg.entities.HeatmapData;
import gmerg.model.HeatmapImageGenerator;
import gmerg.model.TitleImageGenerator;
import gmerg.utils.Utility;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.*;

public class GudmapDynamicImageFilter implements Filter{
    protected boolean debug = false;

	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String requestURI = httpServletRequest.getRequestURI();
			String imageName = requestURI.substring(requestURI.lastIndexOf("/")+1); 
			BufferedImage image = getDynamicImage(imageName, httpServletRequest);
			if (image == null)
				return;
			HttpServletResponse httpResponse = (HttpServletResponse)response;
			httpResponse.setHeader("Cache-Control", "no-store");
			httpResponse.addHeader("Pragma", "no-cache");
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			httpServletResponse.setContentType("image/jpg");
			OutputStream outputStream = httpServletResponse.getOutputStream();
			ImageWriter imageWriter = ImageIO.getImageWritersByFormatName("jpeg").next();
			ImageWriteParam iwp = imageWriter.getDefaultWriteParam();
			iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			iwp.setCompressionQuality(0.96f);	// set compression quality 1.00 is the best quality with largest size
			ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
			imageWriter.setOutput(ios);
			imageWriter.write(null, new IIOImage(image, null, null), iwp);
			ios.flush();
			imageWriter.dispose();
			ios.close();
//			ImageIO.write(image, "jpeg", outputStream); 	// generate low quality images 
			outputStream.close();
			return;
		}
		filterChain.doFilter(request, response);
	}

	public void destroy() {
		
	}

	public BufferedImage getDynamicImage(String name, HttpServletRequest request) {
	    if (debug)
		System.out.println("GudmapDynamicImageFilter.getDynamicImage name = "+name);

		String name1 = name.substring(0, name.lastIndexOf('.'));
		int index = name1.indexOf("_");
		String prefix = "";
		String suffix = "";
		if (index <0 ) 
			suffix = name1;
		else {
			prefix = name1.substring(0, index);
			suffix = name1.substring(index+1);
		}
//System.out.println(index+" name1"+name1+"    suffix="+suffix+"   prefix="+prefix);		
		
		int height = Utility.getIntValue(request.getParameter("height"), 0);
		int width = Utility.getIntValue(request.getParameter("width"), 0);
		String masterTableId = request.getParameter("masterTableId");
//System.out.println("nameParts[0]"+nameParts[0]+"    nameParts[1]"+nameParts[1]);
		
		if (debug)
		    System.out.println("GudmapDynamicImageFilter.getDynamicImage prefix = "+prefix+" suffix = "+suffix+" height = "+height+" width = "+width+" masterTableId ="+masterTableId);

		BufferedImage ret = null;
		if (prefix.equalsIgnoreCase("heatmap")) {
//			System.out.println("DynamicImagefilter-----> : masterTableId="+masterTableId);		
			
			MasterTableAssembler assembler = new MasterTableAssembler(masterTableId);
			HeatmapData heatmapData = assembler.retrieveGeneExpressions(suffix); 
			if (null != heatmapData) {
			    MasterTableAssembler.getDisplayTransform().adjustExpressionsForDisplay(heatmapData);
			    HeatmapImageGenerator heatmapImageGenerator = new HeatmapImageGenerator();
			    int tileSize = Utility.getIntValue(request.getParameter("tile"), 0);
			    if (tileSize==0 && height==0 && width==0)
				tileSize = 3;
			    ret = heatmapImageGenerator.getHeatmapImage(heatmapData.getExpression(), tileSize, width, height);
			}
		}

		if (prefix.equalsIgnoreCase("title")) {
			boolean vertical = false;
			Color background = null;
			TitleImageGenerator titleGenerator = new TitleImageGenerator();
			if (request.getParameter("masterTable") != null) {
				if (height==0)
					height = 12;
				if (width==0)
				    width = 160;
				vertical = true;
				background = new Color(0xE6, 0xE8, 0xFA); //Stripy color from gudmap css
			}
			ret = titleGenerator.getTitleImage(suffix, width, height, 0, new Font("SansSerif", Font.PLAIN,  9), background, vertical, "center");
		}
		
		return ret;
	}

}
