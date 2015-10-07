package org.uengine.codi.common;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.*;

/**
 * Created by hoo.lim on 8/28/2015.
 */
public class ImageUtilsTest {
    @Test
    public void testCreateThumbnail(){

        try(InputStream is = new ByteArrayInputStream(FileUtils.readFileToByteArray(new File("D:\\Project\\codi\\1001\\folder\\11.jpg")));
        OutputStream os = new FileOutputStream("D:\\Project\\codi\\1001\\folder\\11.thumnail.jpg")){
            ImageUtils.createThumbnail(is, os, "jpg", 104, 104);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
