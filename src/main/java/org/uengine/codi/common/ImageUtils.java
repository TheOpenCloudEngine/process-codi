package org.uengine.codi.common;

import scala.ScalaReflectionException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static void createThumbnail(InputStream inputStream,OutputStream outputStream,String type,int width, int height) throws Exception {
		BufferedImage image = ImageIO.read(inputStream);

		float cvtWidth = 0.0f;
		float cvtHeight = 0.0f;

		if(image.getWidth() > image.getHeight()) {
			cvtHeight = (float)height * ((float)image.getHeight() / (float)image.getWidth());
			cvtWidth = width;
		} else if(image.getWidth() < image.getHeight()) {
			cvtWidth = (float)width * ((float)image.getWidth() / (float)image.getHeight());
			cvtHeight = height;
		} else {
			cvtWidth = width;
			cvtHeight = height;
		}

		image = createResizedCopy(image, (int)cvtWidth, (int)cvtHeight, true);

		ImageIO.write(image, type, outputStream);
	}

	private static BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
		BufferedImage thumb = new BufferedImage
				(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D  g2 = thumb.createGraphics();

		g2.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		return thumb;
	}
}