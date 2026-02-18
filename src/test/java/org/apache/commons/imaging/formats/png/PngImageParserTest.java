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
     * Creates a PNG with valid signature but only an IEND chunk (no IHDR or other expected chunks).
     */
    @Test
    void testChunksEmpty() {
        final byte[] pngWithNoChunks = {
                (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n',
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
     * Creates a PNG with two IHDR chunks to trigger the "more than one Header" exception.
     */
    @Test
    void testbadIHDRCount() {
        final byte[] pngWithTwoIHDR = {
                (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n',
                0x00, 0x00, 0x00, 0x0D,
                'I', 'H', 'D', 'R',
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
                0x08, 0x02, 0x00, 0x00, 0x00,
                (byte) 0x90, 0x77, 0x53, (byte) 0xDE,
                0x00, 0x00, 0x00, 0x0D,
                'I', 'H', 'D', 'R',
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
                0x08, 0x02, 0x00, 0x00, 0x00,
                (byte) 0x90, 0x77, 0x53, (byte) 0xDE,
                0x00, 0x00, 0x00, 0x00,
                'I', 'E', 'N', 'D',
                (byte) 0xAE, 0x42, 0x60, (byte) 0x82
        };

        final ImagingException exception = assertThrows(ImagingException.class,
                () -> new PngImageParser().getImageInfo(pngWithTwoIHDR, null));
        assertEquals("PNG contains more than one Header", exception.getMessage());
    }

    /**
     * Test GII_04_TRUE_multiPHYS_throw: getImageInfo throws exception when more than one pHYs chunk exists.
     * Creates a PNG with two pHYs chunks to trigger the exception.
     */
    @Test
    void testMultiPHYS() {
        final byte[] pngWithTwoPHYs = {
                (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n',
                0x00, 0x00, 0x00, 0x0D,
                'I', 'H', 'D', 'R',
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
                0x08, 0x02, 0x00, 0x00, 0x00,
                (byte) 0x90, 0x77, 0x53, (byte) 0xDE,
                0x00, 0x00, 0x00, 0x09,
                'p', 'H', 'Y', 's',
                0x00, 0x00, 0x0E, (byte) 0xC3, 0x00, 0x00, 0x0E, (byte) 0xC3, 0x01,
                (byte) 0xC7, 0x6F, (byte) 0xA8, 0x64,
                0x00, 0x00, 0x00, 0x09,
                'p', 'H', 'Y', 's',
                0x00, 0x00, 0x0E, (byte) 0xC3, 0x00, 0x00, 0x0E, (byte) 0xC3, 0x01,
                (byte) 0xC7, 0x6F, (byte) 0xA8, 0x64,
                0x00, 0x00, 0x00, 0x00,
                'I', 'E', 'N', 'D',
                (byte) 0xAE, 0x42, 0x60, (byte) 0x82
        };

        final ImagingException exception = assertThrows(ImagingException.class,
                () -> new PngImageParser().getImageInfo(pngWithTwoPHYs, null));
        assertEquals("PNG contains more than one pHYs: 2", exception.getMessage());
    }

    /**
     * Test GII_06_TRUE_multiSCAL_throw: getImageInfo throws exception when more than one sCAL chunk exists.
     * Creates a PNG with two sCAL chunks to trigger the exception.
     */
    @Test
    void testMultiSCAL() {
        final byte[] pngWithTwoSCAL = {
                (byte) 0x89, 'P', 'N', 'G', '\r', '\n', 0x1A, '\n',
                0x00, 0x00, 0x00, 0x0D,
                'I', 'H', 'D', 'R',
                0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x01,
                0x08, 0x02, 0x00, 0x00, 0x00,
                (byte) 0x90, 0x77, 0x53, (byte) 0xDE,
                0x00, 0x00, 0x00, 0x09,
                's', 'C', 'A', 'L',
                0x01, '1', 0x00, '1', 0x00, 0x00, 0x00, 0x00, 0x00,
                (byte) 0xC2, (byte) 0x96, 0x49, (byte) 0xAC,
                0x00, 0x00, 0x00, 0x09,
                's', 'C', 'A', 'L',
                0x01, '1', 0x00, '1', 0x00, 0x00, 0x00, 0x00, 0x00,
                (byte) 0xC2, (byte) 0x96, 0x49, (byte) 0xAC,
                0x00, 0x00, 0x00, 0x00,
                'I', 'E', 'N', 'D',
                (byte) 0xAE, 0x42, 0x60, (byte) 0x82
        };

        final ImagingException exception = assertThrows(ImagingException.class,
                () -> new PngImageParser().getImageInfo(pngWithTwoSCAL, null));
        assertEquals("PNG contains more than one sCAL:2", exception.getMessage());
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
