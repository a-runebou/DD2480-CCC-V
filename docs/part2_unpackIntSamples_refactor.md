<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

# Refactoring `unpackIntSamples`

## Summary
After analyzing the `unpackIntSamples` function, it is clear that the cyclomatic complexity is only partially necessary. The function combines multiple responsibilities, including byte decoding, endianness handling, bit-depth selection, row iteration, and post-processing. While each step is logically required, the function can be split into smaller, more focused functions to improve readability and maintainability.

## Causes of complexity
- Conditional branches for different `bitsPerSample` values (16-bit and 32-bit).
- Nested conditionals for byte order (big-endian vs. little-endian).
- Loop logic that includes both decoding and post-processing steps.
- Calculation of offsets and indexing togeteher with decoding.

## Refactoring plan
1. Extract decoding logic per bit depth into separate functions:
   - `decode16BitSamples(...)`
   - `decode32BitSamples(...)`
2. Create a helper function to handle endianness:
    - `readInt16LittleEndian(...)`
    - `readInt16BigEndian(...)`
    - `readInt32LittleEndian(...)`
    - `readInt32BigEndian(...)`
3. Extract predictor processing:
    - `applyDifferencing(...)`
4. Simplify main loop
