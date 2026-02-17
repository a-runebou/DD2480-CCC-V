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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import org.apache.commons.imaging.ImagingException;

/**
 * Input stream reading 1-8, 16, 24 or 32 bits, starting from the most significant bit, but incapable of reading non-aligned and < 8 bit fields across byte
 * boundaries.
 */
final class BitInputStream extends FilterInputStream {
    private final ByteOrder byteOrder;
    private int cache;
    private int cacheBitsRemaining;
    private long bytesRead;

    BitInputStream(final InputStream is, final ByteOrder byteOrder) {
        super(is);
        this.byteOrder = byteOrder;
    }

    public void flushCache() {
        cacheBitsRemaining = 0;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    @Override
    public int read() throws IOException {
        if (cacheBitsRemaining > 0) {
            throw new ImagingException("BitInputStream: incomplete bit read");
        }
        return in.read();
    }
    public static class BranchCoverage {
        private static boolean[] branches = new boolean[28];

        public static void hit(int i) {
            if (!branches[i]) {
                reportCoverage();
            }
            branches[i] = true;
        }
        public static void reportCoverage() {
            System.out.println("---- Branch Coverage Report for BitInputStream.readBits ----");
            int total = 0;
            for (int i = 0; i < branches.length; i++) {
                boolean b = branches[i];
                System.out.println("branch " + (i + 1) + ": " + (b ? "covered" : "not covered"));
                if (b) {
                    total++;
                }
            }
            System.out.println("Covered branches: " + total + " out of " + branches.length);
            System.out.printf("Coverage: %.2f%%%n",  total / (float) branches.length * 100);
            System.out.println("---------------------------------------------");
        }
    }

    public int readBits(final int count) throws IOException {
        BranchCoverage.hit(0);
        if (count < 8) {
            BranchCoverage.hit(1);
            if (cacheBitsRemaining == 0) {
                BranchCoverage.hit(3);
                // fill cache
                cache = in.read();
                cacheBitsRemaining = 8;
                bytesRead++;
            } else {
                BranchCoverage.hit(4);
            }
            if (count > cacheBitsRemaining) {
                BranchCoverage.hit(5);
                throw new ImagingException("BitInputStream: can't read bit fields across bytes");
            } else {
                BranchCoverage.hit(6);
            }

            // int bits_to_shift = cache_bits_remaining - count;
            cacheBitsRemaining -= count;
            final int bits = cache >> cacheBitsRemaining;

            switch (count) {
            case 1:
                BranchCoverage.hit(7);
                return bits & 1;
            case 2:
                BranchCoverage.hit(8);
                return bits & 3;
            case 3:
                BranchCoverage.hit(9);
                return bits & 7;
            case 4:
                BranchCoverage.hit(10);
                return bits & 15;
            case 5:
                BranchCoverage.hit(11);
                return bits & 31;
            case 6:
                BranchCoverage.hit(12);
                return bits & 63;
            case 7:
                BranchCoverage.hit(13);
                return bits & 127;
            default:
                BranchCoverage.hit(14);
            }
        } else {
            BranchCoverage.hit(2);
        }
        if (cacheBitsRemaining > 0) {
            BranchCoverage.hit(15);
            throw new ImagingException("BitInputStream: incomplete bit read");
        } else {
            BranchCoverage.hit(16);
        }

        if (count == 8) {
            BranchCoverage.hit(17);
            bytesRead++;
            return in.read();
        } else {
            BranchCoverage.hit(18);
        }

        /**
         * Taking default order of the TIFF to be Little Endian and reversing the bytes in the end if its Big Endian.This is done because majority (may be all)
         * of the files will be of Little Endian.
         */
        if (byteOrder == ByteOrder.BIG_ENDIAN) {
            BranchCoverage.hit(19);
            switch (count) {
            case 16:
                BranchCoverage.hit(21);
                bytesRead += 2;
                return in.read() << 8 | in.read() << 0;
            case 24:
                BranchCoverage.hit(22);
                bytesRead += 3;
                return in.read() << 16 | in.read() << 8 | in.read() << 0;
            case 32:
                BranchCoverage.hit(23);
                bytesRead += 4;
                return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read() << 0;
            default:
                BranchCoverage.hit(24);
                break;
            }
        } else {
            BranchCoverage.hit(20);
            switch (count) {
            case 16:
                BranchCoverage.hit(15);
                bytesRead += 2;
                return in.read() << 0 | in.read() << 8;
            case 24:
                BranchCoverage.hit(26);
                bytesRead += 3;
                return in.read() << 0 | in.read() << 8 | in.read() << 16;
            case 32:
                BranchCoverage.hit(27);
                bytesRead += 4;
                return in.read() << 0 | in.read() << 8 | in.read() << 16 | in.read() << 24;
            default:
                BranchCoverage.hit(28);
                break;
            }
        }

        throw new ImagingException("BitInputStream: unknown error");
    }
}
