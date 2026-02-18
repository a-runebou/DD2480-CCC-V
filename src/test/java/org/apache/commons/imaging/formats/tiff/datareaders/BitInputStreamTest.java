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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;

import org.junit.jupiter.api.Test;

public class BitInputStreamTest {
    /**
     * Contract:
     * The readBits function shall allow for reading 1 to 8 bits from the datastream,
     * if these bits are not read across the byte boundary.
     *
     * Expected behavior:
     * Reading first byte as 1 + 7 bits, second byte as 2 + 6 bits, third byte as 3 + 5 bits, fourth byte as 4 + 4 bytes, and finally
     * fifth byte as 2 + 3 + 3 bits, every function call shall return the expected result and not throw any exception.
     * At the end, the number of read bytes should be equal to 5.
     */
    @Test
    void readingBitsWithinByteBoundaryWorksCorrectly() {
        final byte[] data = new byte[] {
            (byte) 0b10101101, // 1 and 00101101 = 1 + 4 + 8 + 32 = 45
            (byte) 0b11010011, // 11 = 3 and 010011 = 1 + 2 + 16 = 19
            (byte) 0b10101101, // 101 = 5 and 01101 = 1 + 4 + 8 = 13
            (byte) 0b11010011, // 1101 = 13 and 0011 = 3
            (byte) 0b01101011 // 01 = 1 and 101 = 5 and 011 = 3
        };
        final ByteArrayInputStream bais = new ByteArrayInputStream(data);
        final BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.LITTLE_ENDIAN);
        int result;
        // first byte
        result = assertDoesNotThrow(() -> bitInputStream.readBits(1));
        assertEquals(1, result);
        result = assertDoesNotThrow(() -> bitInputStream.readBits(7));
        assertEquals(45, result);
        // second byte
        result = assertDoesNotThrow(() -> bitInputStream.readBits(2));
        assertEquals(3, result);
        result = assertDoesNotThrow(() -> bitInputStream.readBits(6));
        assertEquals(19, result);
        // third byte
        result = assertDoesNotThrow(() -> bitInputStream.readBits(3));
        assertEquals(5, result);
        result = assertDoesNotThrow(() -> bitInputStream.readBits(5));
        assertEquals(13, result);
        // fourth byte
        result = assertDoesNotThrow(() -> bitInputStream.readBits(4));
        assertEquals(13, result);
        result = assertDoesNotThrow(() -> bitInputStream.readBits(4));
        assertEquals(3, result);
        // fifth byte
        result = assertDoesNotThrow(() -> bitInputStream.readBits(2));
        assertEquals(1, result);
        result = assertDoesNotThrow(() -> bitInputStream.readBits(3));
        assertEquals(5, result);
        result = assertDoesNotThrow(() -> bitInputStream.readBits(3));
        assertEquals(3, result);
        // check the number of read bytes
        assertEquals(5, bitInputStream.getBytesRead());
    }


    /**
     * Contract:
     * The readBits function shall not allow for reading bits across the byte boundary.
     *
     * Expected behavior:
     * Given an Input Stream with 2 bytes, reading firstly 5 bits, and then trying to read 4 bits shall throw an IOException, as the function cannot read bits
     * across byte boundaries.
     */
    @Test
    void readingAcrossByteBoundaryThrowsException() {
        final byte[] data = new byte[] {
            (byte) 0b10101101,
            (byte) 0b11110011
        };
        final ByteArrayInputStream bais = new ByteArrayInputStream(data);
        // initialiize BitInputStream
        final BitInputStream bitInputStream = new BitInputStream(bais, ByteOrder.BIG_ENDIAN);
        // read 5 bits
        final int result;
        result = assertDoesNotThrow(() -> bitInputStream.readBits(5));
        assertThrows(IOException.class, () -> {
            bitInputStream.readBits(4);
        });
    }
}
