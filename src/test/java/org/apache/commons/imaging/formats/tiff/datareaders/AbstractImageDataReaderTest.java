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
package org.apache.commons.imaging.formats.tiff.datareaders;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.Rectangle;
import java.nio.ByteOrder;

import org.apache.commons.imaging.common.ImageBuilder;
import org.apache.commons.imaging.formats.tiff.AbstractTiffRasterData;
import org.apache.commons.imaging.formats.tiff.constants.TiffPlanarConfiguration;
import org.apache.commons.imaging.formats.tiff.constants.TiffTagConstants;
import org.junit.jupiter.api.Test;

class AbstractImageDataReaderTest {
    /**
     * Minimal concrete subclass only for testing.
     */
    static class TestReader extends AbstractImageDataReader {
        TestReader(int samplesPerPixel, TiffPlanarConfiguration config) {
            super(null, null, new int[]{32}, 1, samplesPerPixel, 3, 100, 100, config);
        }
        @Override
        public ImageBuilder readImageData(Rectangle subImageSpecification, boolean hasAlpha, boolean isAlphaPremultiplied) {
            return null;
        }
        @Override
        public AbstractTiffRasterData readRasterData(Rectangle subImage) {
            return null;
        }
    }
    /**
     * Contract:
     * Given 16-bit samples stored in LITTLE_ENDIAN byte order,
     * the method shall decode each pair of bytes into a signed integer.
     *
     * Expected behaviour:
     * Bytes [1,0] and [2,0] should decode to integer values 1 and 2.
     */
    @Test
    void unpackIntSamples_16bit_littleEndian() {
        final TestReader reader = new TestReader(1, TiffPlanarConfiguration.CHUNKY);
        final byte[] bytes = new byte[]{1, 0, 2, 0};
        final int[] result = reader.unpackIntSamples(2, 1, 2, bytes, 0, 16, ByteOrder.LITTLE_ENDIAN);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    /**
     * Contract:
     * Given 16-bit samples stored in BIG_ENDIAN byte order,
     * the method shall correctly interpret byte order when decoding.
     *
     * Expected behaviour:
     * Bytes [0,1] and [0,2] should decode to integer values 1 and 2.
     */
    @Test
    void unpackIntSamples_16bit_bigEndian() {
        final TestReader reader = new TestReader(1, TiffPlanarConfiguration.CHUNKY);
        final byte[] bytes = new byte[]{0, 1, 0, 2};
        final int[] result = reader.unpackIntSamples(2, 1, 2, bytes, 0, 16, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    /**
     * Contract:
     * Given 32-bit samples stored in LITTLE_ENDIAN byte order,
     * the method shall decode each group of four bytes into a signed integer.
     *
     * Expected behaviour:
     * Bytes [1,0,0,0] and [2,0,0,0] should decode to integer values 1 and 2.
     */
    @Test
    void unpackIntSamples_32bit_littleEndian() {
        final TestReader reader = new TestReader(1, TiffPlanarConfiguration.CHUNKY);
        final byte[] bytes = new byte[]{1, 0, 0, 0, 2, 0, 0, 0};
        final int[] result = reader.unpackIntSamples(2, 1, 2, bytes, 0, 32, ByteOrder.LITTLE_ENDIAN);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    /**
     * Contract:
     * Given 32-bit samples stored in BIG_ENDIAN byte order,
     * the method shall correctly interpret byte order when decoding.
     *
     * Expected behaviour:
     * Bytes [0,0,0,1] and [0,0,0,2] should decode to integer values 1 and 2.
     */
    @Test
    void unpackIntSamples_32bit_bigEndian() {
        final TestReader reader = new TestReader(1, TiffPlanarConfiguration.CHUNKY);
        final byte[] bytes = new byte[]{0, 0, 0, 1, 0, 0, 0, 2};
        final int[] result = reader.unpackIntSamples(2, 1, 2, bytes, 0, 32, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    /**
     * Contract:
     * When the predictor is set to horizontal differencing, the method shall
     * reconstruct original sample values by cumulatively adding differences.
     *
     * Expected behaviour:
     * Given differences [1,0] and [1,0], the method should reconstruct original values 1 and 2.
     */
    @Test
    void unpackIntSamples_withDifferencing() {
        final TestReader reader = new TestReader(1, TiffPlanarConfiguration.CHUNKY);
        final byte[] bytes = new byte[]{1, 0, 1, 0};
        final int[] result = reader.unpackIntSamples(2, 1, 2, bytes, TiffTagConstants.PREDICTOR_VALUE_HORIZONTAL_DIFFERENCING, 16, ByteOrder.LITTLE_ENDIAN);
        assertArrayEquals(new int[]{1, 2}, result);
    }

    /**
     * Contract:
     * If bitsPerSample is not 16, or 32, the method should return an array of length 1.
     *
     * Expected behaviour:
     * Given bitsPerSample = 1, the method should return an array of length 1 regardless of input bytes.
     */
    @Test
    void unpackIntSamples_invalidBitsPerSample_branch() {
        final TestReader reader = new TestReader(1, TiffPlanarConfiguration.CHUNKY);
        final byte[] bytes = new byte[]{1, 2, 3, 4};
        final int[] result = reader.unpackIntSamples(1, 1, 1, bytes, 0, 8, ByteOrder.LITTLE_ENDIAN);
        assertEquals(1, result.length);
    }

    /**
     * Contract:
     * If the input byte array is shorter than required for the specified bitsPerSample and samplesPerPixel,
     * the method should throw an ArrayIndexOutOfBoundsException.
     *
     * Expected behaviour:
     * Given bitsPerSample = 16 and samplesPerPixel = 2, if the byte array has only 2 bytes
     * (enough for one sample), the method should throw an exception
     */
    @Test
    void unpackIntSamples_lengthTernary_trueBranch() {
        final TestReader reader = new TestReader(1, TiffPlanarConfiguration.CHUNKY);
        final byte[] bytes = new byte[]{1, 0}; // shorter than required
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> reader.unpackIntSamples(2, 1, 2, bytes, 0, 16, ByteOrder.LITTLE_ENDIAN));
    }



}
