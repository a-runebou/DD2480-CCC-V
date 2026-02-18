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