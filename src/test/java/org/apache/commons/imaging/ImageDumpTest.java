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
package org.apache.commons.imaging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.imaging.formats.tiff.itu_t4.T4AndT6Compression;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ImageDumpTest {

    @Test
    void testDump() throws IOException {
        final ImageDump imageDump = new ImageDump();
        final BufferedImage bufferedImage = new BufferedImage(10, 10, 10);
        imageDump.dump(bufferedImage);

        assertEquals(10, bufferedImage.getHeight());
    }

    @Test
    void testDumpColorSpace() throws IOException {
        final ImageDump imageDump = new ImageDump();
        final ColorSpace colorSpace = ColorSpace.getInstance(1004);
        imageDump.dumpColorSpace("Ku&]N>!4'C#Jzn+", colorSpace);

        assertEquals(3, colorSpace.getNumComponents());
    }
    // New branch coverage
    //[Branch 2, Branch 18]
    @Test
    public void testDecompressT4_2D_DecompressionError() throws ImagingException {
        byte[] compressed = {0b00000000, 0b00000001}; // Example compressed data
        int width = 5;
        int height = 2;
        boolean hasFill = true;

        ImagingException exception = assertThrows(ImagingException.class, () -> {
            T4AndT6Compression.decompressT4_2D(compressed, width, height, hasFill);
        });
        assertTrue(exception.getMessage().contains("Decompression error"));
    }
    //New branch coverage
    //[Branch 1, Branch 18]
    @Test
    public void testDecompressT4_2D_EOLError() throws ImagingException {
        byte[] compressed = {(byte) 0b10101010, (byte) 0b11001100}; // Example invalid data
        int width = 5;
        int height = 2;
        boolean hasFill = true;

        ImagingException exception = assertThrows(ImagingException.class, () -> {
            T4AndT6Compression.decompressT4_2D(compressed, width, height, hasFill);
        });
        assertTrue(exception.getMessage().contains("Decompression error"));
    }
    // New branch coverage
    //[Branch 4, Branch 2, Branch 19]
    @Test
    public void testDecompressT4_2D_1D() throws ImagingException {
        byte[] compressed = new byte[] {
            (byte)0x00,
            (byte)0x18,
            (byte)0xE8
        };
        int width = 2;
        int height = 1;
        boolean hasFill = false;

        byte[] result = T4AndT6Compression.decompressT4_2D(compressed, width, height, hasFill);

        assertNotNull(result);
        assertEquals(1, result.length);
    }
    //New path coverage
    //[Branch 4, Branch 2, Branch 3, Branch 19, Branch 17, Branch 8, Branch 7]
    @Test
    public void testDecompressT4_2D_new_path() throws ImagingException {
        
        int width = 4;
        int height = 2;
        boolean hasFill = true;
        byte[] compressed = T4AndT6Compression.compressT4_2D(new byte[]{1,0,0,1,1,1,4,2}, width, height, hasFill,5);
        byte[] result = T4AndT6Compression.decompressT4_2D(compressed, width, height, hasFill);

        assertNotNull(result);
        assertEquals(2, result.length);
    }



    @AfterEach
    public void writeAccessedBranches() {
        try {
            final Set<String> accessedBranches = T4AndT6Compression.getAccessedBranches();
            System.out.println(accessedBranches);
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            T4AndT6Compression.clearAccessedBranches();
        }
    }

}
