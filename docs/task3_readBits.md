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

## Refactoring plan
- The original complexity (according to lizard): 20
- Expected change in complexity: 
  - -7 decisions from the first switch
  - -7 from the second switch
  - This results in a complexity of approximately 6

Change the switch
```
switch (count) {
case 1:
    return bits & 1;
case 2:
    return bits & 3;
case 3:
    return bits & 7;
case 4:
    return bits & 15;
case 5:
    return bits & 31;
case 6:
    return bits & 63;
case 7:
    return bits & 127;
}
```
for just one line of code, as each case can be changed with `pow(2,count)-1 = (1 << count)-1`. This should reduce the complexity considerably.The first IF statement needs to be changed slightly, to not allow for counts <= 0.

The second part of `readBits`, which implements the function of reading whole bytes, can also be refactored. As this part implements a fairly separate functionality, creating a new method for reading whole bytes is a good choice, as it will also reduce the complexity.
```
if (byteOrder == ByteOrder.BIG_ENDIAN) {
    switch (count) {
    case 16:
        bytesRead += 2;
        return in.read() << 8 | in.read() << 0;
    case 24:
        bytesRead += 3;
        return in.read() << 16 | in.read() << 8 | in.read() << 0;
    case 32:
        bytesRead += 4;
        return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read() << 0;
    default:
        break;
    }
} else {
    switch (count) {
    case 16:
        bytesRead += 2;
        return in.read() << 0 | in.read() << 8;
    case 24:
        bytesRead += 3;
        return in.read() << 0 | in.read() << 8 | in.read() << 16;
    case 32:
        bytesRead += 4;
        return in.read() << 0 | in.read() << 8 | in.read() << 16 | in.read() << 24;
    default:
        break;
    }
}
```
Furthermore, the function can be rewritten in order to get rid of the switch statement. 
If we introduce another variable in which to store the result, we can read any number of bytes as 
```
int result = 0
for (int i = 0; i < numberOfBytes; i++>) {
    result = (result << 8) | in.read();
}
```
for the Big Endian, and `result = result | (in.read << (8*i))` for Little Endian.
This will further reduce the complexity of the second function.

## Final complexity
The complexity of readBits was reduced down to 7 from the original 20. Our expectation was correct, only we added one check in the form of an IF statement for valid values which was not present in the original implementation.