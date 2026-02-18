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

package org.apache.commons.imaging.formats.png;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.imaging.common.AllocationRequestException;
import org.junit.jupiter.api.Test;

class PngImageParserTest extends AbstractPngTest {

    /**
     * Test GII_01_TRUE_chunksEmpty: getImageInfo throws exception when no relevant chunks are found.
     * This creates a PNG with valid signature but only an IEND chunk (no IHDR or other expected chunks).
     */
    @Test
    void testChunksEmpty() {
        // PNG signature followed immediately by IEND chunk
        final byte[] pngWithNoChunks = {
                // PNG Signature 8 bytes
                (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n',
                // IEND chunk: length=0, type=IEND, CRC
                0x00, 0x00, 0x00, 0x00,
                'I', 'E', 'N', 'D',
                (byte) 0xAE, 0x42, 0x60, (byte) 0x82
        };

        final ImagingException exception = assertThrows(ImagingException.class,
                () -> new PngImageParser().getImageInfo(pngWithNoChunks, null));
        assertEquals("PNG: no chunks", exception.getMessage());
    }

    /**
     * Test GII_02_TRUE_badIHDRCount: getImageInfo throws exception when IHDR count != 1.
     * This creates a PNG with two IHDR chunks to trigger the "more than one Header" exception.
     */
    @Test
    void testbadIHDRCount() {
        // PNG with two IHDR chunks
        final byte[] pngWithTwoIHDR = {
                (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n',
                // First IHDR chunk (length=13, type=IHDR, data, CRC)
                0x00, 0x00, 0x00, 0x0D,  
                'I', 'H', 'D', 'R',       
                0x00, 0x00, 0x00, 0x01,   // Width: 1
                0x00, 0x00, 0x00, 0x01,   // Height: 1
                0x08,                      // Bit depth: 8
                0x02,                      // Color type: 2 (RGB)
                0x00,                      // Compression: 0
                0x00,                      // Filter: 0
                0x00,                      // Interlace: 0
                (byte) 0x90, 0x77, 0x53, (byte) 0xDE,  
                // Second IHDR chunk 
                0x00, 0x00, 0x00, 0x0D,  
                'I', 'H', 'D', 'R',       
                0x00, 0x00, 0x00, 0x01,   // Width: 1
                0x00, 0x00, 0x00, 0x01,   // Height: 1
                0x08,                      // Bit depth: 8
                0x02,                      // Color type: 2 (RGB)
                0x00,                      // Compression: 0
                0x00,                      // Filter: 0
                0x00,                      // Interlace: 0
                (byte) 0x90, 0x77, 0x53, (byte) 0xDE, 
                // IEND chunk
                0x00, 0x00, 0x00, 0x00,
                'I', 'E', 'N', 'D',
                (byte) 0xAE, 0x42, 0x60, (byte) 0x82
        };

        final ImagingException exception = assertThrows(ImagingException.class,
                () -> new PngImageParser().getImageInfo(pngWithTwoIHDR, null));
        assertEquals("PNG contains more than one Header", exception.getMessage());
    }

    private static byte[] getPngImageBytes(final BufferedImage image, final PngImagingParameters params) throws IOException {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            new PngWriter().writeImage(image, os, params, null);
            return os.toByteArray();
        }
    }

    @Test
    void testGetImageSize() {
        final byte[] bytes = {
                // Header
                (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n',
                // (Too large) Length
                (byte) 0b0111_1111, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF - 10,
                // Chunk type
                'I', 'H', 'D', 'R', };
        assertThrows(AllocationRequestException.class, () -> new PngImageParser().getImageSize(bytes));
    }

    @Test
    void testNoPalette() throws IOException {
        final BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        image.setRGB(1, 1, 0x00FFffFF);
        final PngImagingParameters params = new PngImagingParameters();

        final byte[] bytes = getPngImageBytes(image, params);
        final ImageInfo imageInfo = new PngImageParser().getImageInfo(bytes, null);
        assertFalse(imageInfo.usesPalette());
    }

    @Test
    void testPalette() throws IOException {
        final BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        image.setRGB(1, 1, 0x00FFffFF);
        final PngImagingParameters params = new PngImagingParameters();
        params.setForceIndexedColor(true);

        final byte[] bytes = getPngImageBytes(image, params);
        final ImageInfo imageInfo = new PngImageParser().getImageInfo(bytes, null);
        assertTrue(imageInfo.usesPalette());
    }
}
