package org.uengine.codi.common;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

public class ImageUtils {
	public static void createThumbnail(String load,String save,String type,int w,int h) throws Exception {
		BufferedInputStream stream_file = new BufferedInputStream(new FileInputStream(load));
		createThumbnail(stream_file,save,type,w,h);
	}

	public static void createThumbnail(BufferedInputStream stream_file,String save,String type,int w,int h) throws Exception {
		getImageThumbnail(stream_file,save,type,w,h);
	}

	public static void getImageThumbnail(BufferedInputStream stream_file,String save,String type,int width, int height) throws Exception  {

		File  file = new File(save);
		BufferedImage bi = ImageIO.read(stream_file);

		// 원본 이미지의 비율에 맞게 썸네일 이미지 비율을 정하는 부분.
		float cvtWidth = 0.0f;
		float cvtHeight = 0.0f;

		if(bi.getWidth() > bi.getHeight()) {
			cvtHeight = (float)height * ((float)bi.getHeight() / (float)bi.getWidth());
			cvtWidth = width;
		} else if(bi.getWidth() < bi.getHeight()) {
			cvtWidth = (float)width * ((float)bi.getWidth() / (float)bi.getHeight());
			cvtHeight = height;
		} else {
			cvtWidth = width;
			cvtHeight = height;
		}



		BufferedImage thumb = new BufferedImage
				((int)cvtWidth, (int)cvtHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D  g2 = thumb.createGraphics();

		g2.drawImage(bi, 0, 0, (int)cvtWidth, (int)cvtHeight, null);

		ImageIO.write(thumb, type, file); 
	}
}