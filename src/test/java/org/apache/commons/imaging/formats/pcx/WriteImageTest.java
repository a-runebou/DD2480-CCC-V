/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.imaging.formats.pcx;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class PcxWriterBitDepthTest {

    private BufferedImage createSimpleRgbImage() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, Color.RED.getRGB());
        img.setRGB(1, 0, Color.GREEN.getRGB());
        img.setRGB(0, 1, Color.BLUE.getRGB());
        img.setRGB(1, 1, Color.WHITE.getRGB());
        return img;
    }

    @Test
    void testWriteImageWithVariousBitDepths() throws IOException {

        BufferedImage image = createSimpleRgbImage();

        // CASE 1: bitDepthWanted = 32 (should become 32 bit, 1 plane)
        {
            PcxImagingParameters params = new PcxImagingParameters()
                    .setBitDepth(32);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            new PcxWriter(params).writeImage(image, os);

            byte[] data = os.toByteArray();

            assertEquals(32, data[3] & 0xFF);
            assertEquals(1, data[65] & 0xFF);
        }

        // CASE 2: bitDepthWanted = 24 (should become 8 bit, 3 planes)
        {
            PcxImagingParameters params = new PcxImagingParameters()
                    .setBitDepth(24);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            new PcxWriter(params).writeImage(image, os);

            byte[] data = os.toByteArray();

            assertEquals(8, data[3] & 0xFF);
            assertEquals(3, data[65] & 0xFF);
        }

        // CASE 3: bitDepthWanted = 8 (should become 8 bit, 1 plane)
        {
            PcxImagingParameters params = new PcxImagingParameters()
                    .setBitDepth(8);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            new PcxWriter(params).writeImage(image, os);

            byte[] data = os.toByteArray();

            assertEquals(8, data[3] & 0xFF);
            assertEquals(1, data[65] & 0xFF);
        }
    }

    @Test
    void test4bit1plane() throws IOException {
        // 9 unique colors -> palette.length() > 8 -> hit 5
        // planesWanted == 1 -> hit 6
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int c = 1;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int rgb = ((c * 40) & 0xFF) << 16
                        | ((c * 80) & 0xFF) << 8
                        | ((c * 20) & 0xFF);
                img.setRGB(x, y, rgb);
                c++;
            }
        }

        PcxImagingParameters params = new PcxImagingParameters()
                .setBitDepth(4)
                .setPlanes(1);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new PcxWriter(params).writeImage(img, os);

        byte[] data = os.toByteArray();
        assertEquals(4, data[3] & 0xFF);
        assertEquals(1, data[65] & 0xFF);
    }

    @Test
    void test1bit4planes() throws IOException {
        // planesWanted != 1 -> hit 7
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int c = 1;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int rgb = ((c * 40) & 0xFF) << 16
                        | ((c * 80) & 0xFF) << 8
                        | ((c * 20) & 0xFF);
                img.setRGB(x, y, rgb);
                c++;
            }
        }

        PcxImagingParameters params = new PcxImagingParameters()
                .setBitDepth(4)
                .setPlanes(4);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new PcxWriter(params).writeImage(img, os);

        byte[] data = os.toByteArray();
        assertEquals(1, data[3] & 0xFF);
        assertEquals(4, data[65] & 0xFF);
    }

    @Test
    void testBitDepth2Planes2() throws IOException {
        // palette.length() > 2
        // planesWanted == 2 -> hit 10
        BufferedImage img = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, Color.BLACK.getRGB());
        img.setRGB(1, 0, Color.RED.getRGB());
        img.setRGB(2, 0, Color.WHITE.getRGB());

        PcxImagingParameters params = new PcxImagingParameters()
                .setBitDepth(2)
                .setPlanes(2);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        new PcxWriter(params).writeImage(img, os);

        byte[] data = os.toByteArray();
        assertEquals(1, data[3] & 0xFF);
        assertEquals(2, data[65] & 0xFF);
    }
}