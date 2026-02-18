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

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

            PcxWriter writer = new PcxWriter(params);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writer.writeImage(image, os);

            byte[] data = os.toByteArray();

            int headerBitDepth = data[3] & 0xFF;   // bits per pixel

            assertEquals(32, headerBitDepth);
            
        }

        // CASE 2: bitDepthWanted = 24 (should become 8 bit, 3 planes)
        {
            PcxImagingParameters params = new PcxImagingParameters()
                    .setBitDepth(24);

            PcxWriter writer = new PcxWriter(params);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writer.writeImage(image, os);

            byte[] data = os.toByteArray();

            int headerBitDepth = data[3] & 0xFF;

            assertEquals(8, headerBitDepth);
        }

        // CASE 3: bitDepthWanted = 8 (should become 8 bit, 1 plane)
        {
            PcxImagingParameters params = new PcxImagingParameters()
                    .setBitDepth(8);

            PcxWriter writer = new PcxWriter(params);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            writer.writeImage(image, os);

            byte[] data = os.toByteArray();

            int headerBitDepth = data[3] & 0xFF;

            assertEquals(8, headerBitDepth);
        }
    }
}
