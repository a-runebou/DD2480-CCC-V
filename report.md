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


# Report for assignment 3

This is a template for your report. You are free to modify it as needed.
It is not required to use markdown for your report either, but the report
has to be delivered in a standard, cross-platform format.

## Project

Name: **commons-imaging**

URL: [https://github.com/apache/commons-imaging](https://github.com/apache/commons-imaging)

Description: Apache Commons Imaging is an open-source Java library that provides functionality for reading, writing, and analysing different image formats and their metadata. The project focuses on format-aware image parsing and processing (for example TIFF, JPEG, and PNG) and is part of the Apache Commons ecosystem.

## Onboarding experience

Did it build and run as documented?
    
The project builds and runs as expected using `mvn`. However, we had some struggles with finding suitable functions for the later parts of the assignment. For example, there were dead branches which can't possibly be covered by tests, and many functions uses complex input which is hard to generate for testing.


## Complexity

1. What are your results for five complex functions?
   * Did all methods (tools vs. manual count) get the same result?
   * Are the results clear?
2. Are the functions just complex, or also long?
3. What is the purpose of the functions?
4. Are exceptions taken into account in the given measurements?
5. Is the documentation clear w.r.t. all the possible outcomes?

### Function 1: `readBitmapIconData` (Fabian Williams & Alexander Runebou)

Related issue: [issues/5](https://github.com/a-runebou/DD2480-CCC-V/issues/5)

**What are your results? Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?**
> Both of us got the same result, which is a CCN of `21`. We counted the exact same lines and operations which solidifies our result. However, this result simply counts the number of decision points in the code +1. This matches the result from the tool `lizard`, but it does not consider early returns which can reduce the CC. 

>Considering the theoretical CNN we instead get $\pi = 17$ (the number of decision points) and $s = 4$ exit points (3 throw + 1 return) which gives us a CCN of $\pi - s + 2 = 15$. This is a significant difference from the result of `21` and shows that the tool does not consider early returns in its calculation.

**Are the functions/methods with high CC also very long in terms of LOC?**
> For this function the NLOC is `110` which is quite long and reflects the high CC. However, one does not necessarily need to imply the other. For example, a function could have a high CC but be short if it has many decision points in a small amount of code such as switch statements. Similarly, a function could be long but have a low CC if it has few decision points.

**What is the purpose of these functions? Is it related to the high CC?**
>The function `readBitmapIconData` is responsible for reading bitmap icon data and converts it into a usable `BufferedImage`. The high CC is directly related to its purpose since it must handle many conditional cases (different bit depths, compression modes, transparency formats and error checks). Essentially, the complexity comes from supporting multiple formats and ensuring that the function can handle various edge cases and errors gracefully.

**If your programming language uses exceptions: Are they taken into account by the tool? If you think of an exception as another possible branch (to the catch block or the end of the function), how is the CC affected?**
> No, the tool does not take exceptions into account in its calculation of CC. This is discussed in the first question where we calculated the theoretical CCN considering the exit points from exceptions. If we consider exceptions as branches, then the CCN is no longer `21` but `15`. However, we do count `catch` blocks as decision points in our manual calculation.

**Is the documentation of the function clear about the different possible outcomes induced by different branches taken?**
> The documentation of the function is not very clear about the different possible outcomes induced by different branches. There is no Javadoc comment so we only know the return type (`IconData`) and possible errors (`ImagingException`, `IOException`). 

### Function 2: `unpackFloatingPointSamples` ( Apeel Subedi & Josef Kahoun)

Related issue: [issues/7](https://github.com/a-runebou/DD2480-CCC-V/issues/7)

**What are your results? Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?**
> Both of us got the same result, which is a CCN of `20`. We counted the exact same lines and operations which solidifies our result. However, this result simply counts the number of decision points in the code `+1`. This matches the result from the tool `lizard`, but it does not consider early returns which can reduce the CC. 

>Considering the theoretical CNN we instead get $\pi = 19$ (the number of decision points) and $s = 4$ exit points which gives us a CCN of $\pi - s + 2 = 17$. This is a significant difference from the result of `20` and shows that the tool does not consider early returns in its calculation.

**Are the functions/methods with high CC also very long in terms of LOC?**
> For this function the NLOC is `104` which is quite long and reflects the high CC. However, one does not necessarily need to imply the other. For example, a function could have a high CC but be short if it has many decision points in a small amount of code such as switch statements. Similarly, a function could be long but have a low CC if it has few decision points.

**What is the purpose of these functions? Is it related to the high CC?**
>The function `unpackFloatingPointSamples` is responsible for unpacking raw bytes from TIFF files containing floating-point data and converting them into IEEE-754 32-bit float format. The high CC is directly related to its complex purpose of handling multiple data formats (32-bit and 64-bit floating point), different byte orders (little-endian vs big-endian), both tile and strip TIFF formats, various scan sizes, and extensive validation of input parameters. The complexity stems from supporting multiple floating-point representations and ensuring proper data conversion across different TIFF file configurations.

**If your programming language uses exceptions: Are they taken into account by the tool? If you think of an exception as another possible branch (to the catch block or the end of the function), how is the CC affected?**
> No, the lizard tool does not take exceptions into account in its calculation of CC. This is discussed in the first question where we calculated the theoretical CCN considering the exit points from exceptions. If we consider exceptions as branches, then the CCN is no longer `20` but `17`. However, we do count `catch` blocks as decision points in our manual calculation.

**Is the documentation of the function clear about the different possible outcomes induced by different branches taken?**
> The documentation of the `unpackFloatingPointSamples` function is quite comprehensive and clear. It has extensive Javadoc comments that explain the purpose (unpacking floating-point data from TIFF files), detailed parameter descriptions (width, height, scanSize, bytes, bitsPerPixel, byteOrder), return value explanation (array of integers in IEEE-754 32-bit float format), and potential exceptions (`ImagingException` for invalid formats). The documentation also explains the different supported formats (32-bit and 64-bit floating point) and processing modes (tile vs strip formats).

### Function 3: `performNextMedianCut` (Josef Kahoun & Carl Isaksson)

Related issue: [issue/4](https://github.com/a-runebou/DD2480-CCC-V/issues/4)

**What are your results? Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?**

> Both of us counted the CCN to be `21`, which corresponds with the result from the tool `lizard`.
There was some uncertainty as to what counts as a decision, for example if an IF statement with a logical operation (`IF (a AND b)`) counts as 2 or 1 decisions.
This complexity was counted as `number of decisions + 1`, which is the formula that lizard uses.
Were we to use the theoretical complexity, we get CCN of ${num\_of\_decisions} - {num\_of\_returns} + 2 = 20 - 4 + 2 = 18$.

**Are the functions/methods with high CC also very long in terms of LOC?**

> Not necessarily, a short function can have a high complexity, an example would be a function containing a switch statement. Likewise, a very long function can have low complexity, if it does not have a large amount of decisions.

**What is the purpose of these functions? Is it related to the high CC?**
> The function `performNextMedianCut` performs one iteration of the Median Cut color quantization algorithm, which aims to reduce the number of colors in an image by repeatedly splitting the color space.
One iteration splits the colors into 2 regions with the best balance.
The high complexity corresponds with the function's purpose, as it requires nested for loops, and multiple conditionals to check different color components and median edge cases.

**If your programming language uses exceptions: Are they taken into account by the tool? If you think of an exception as another possible branch (to the catch block or the end of the function), how is the CC affected?**

> No, the tool does not take into account exceptions, as it only looks for decision points.
However, the `catch` keyword is counted as an additional decision.
If the tool took exceptions and returns into account, we would obtain the theoretical complexity of 18, as discussed in the first question.

**Is the documentation of the function clear about the different possible outcomes induced by different branches taken?**
> The function `performNextMedianCut` lacks any sort of documentation, either in the form of Javadoc or normal comments.
Therefore the different outcomes are not very clear, and had to be deduced from the source code.

## Refactoring

### 1. `PngImageParser#getImageInfo`
   - **Author:** Apeel Subedi
   - **ISSUE:** [#Issue 26](https://github.com/a-runebou/DD2480-CCC-V/issues/26)
   

   #### Refactoring Plan:
   - Extracted helper methods to isolate responsibilities:
      - `getRequiredSingleChunk`, `getSingleOptionalChunk`, `isTransparent`, `readPhysicalScale`, `collectTextChunks`, `computePhysicalInfo`
   - Replaced an inline chunk-type array with a named constant: `IMAGE_INFO_CHUNKS`
   >Note: The Refactoring was carried out to be considered for P+

   #### Impact:
- **Cyclomatic complexity (CCN):** reduced from **20 to 8**
- **Maintainability:** improved readability and reduced nested branching
- **Coverage:** existing tests still pass; coverage improvement is documented in the Coverage Improvement section
#### Drawbacks:
- Slight increase in number of small helper methods,
  but each method is easier to test and reason about.

#### Re-production:
- Branch: `issue/26`
- Diff command:
  ```bash
  git diff origin/master..issue/26 -- \
    src/main/java/org/apache/commons/imaging/formats/png/PngImageParser.java
   ```

### 2. `BitInputStream::readBits`
   - **Author:** Josef Kahoun
   - **ISSUE:** [#Issue 24](https://github.com/a-runebou/DD2480-CCC-V/issues/24)
   

   #### Refactoring Plan:
   - The switch for individual bits `case 1:
    return bits & 1;` can be rewritten as `retyrn bits & (1<<count)-1`.
   - Isolate the last part which reads whole bytes into a self-contained method.
     - Rewrite the switch with a `for` cycle, which will read a single byte in each cycle and apply left shift accordingly.

   #### Impact:
- **Cyclomatic complexity (CCN):** reduced from **20 to 6**
- **Maintainability:** Improved readability, increased generalization of the function, added protection against negative number of bits.

#### Re-production:
- Branch: `issue/24`
- Diff command:
  ```bash
  git diff master issue/24 src/main/java/org/apache/commons/imaging/formats/tiff/datareaders/BitInputStream.java
   ```


### 3. `AbstractImageDataReader::unpackIntSamples`
   - **Author:** Alexander Runebou
   - **ISSUE:** [#Issue 16](https://github.com/a-runebou/DD2480-CCC-V/issues/16)

**Summary**
After analyzing the `unpackIntSamples` function, it is clear that the cyclomatic complexity is only partially necessary. The function combines multiple responsibilities, including byte decoding, endianness handling, bit-depth selection, row iteration, and post-processing. While each step is logically required, the function can be split into smaller, more focused functions to improve readability and maintainability.

**Causes of complexity**
- Conditional branches for different `bitsPerSample` values (16-bit and 32-bit).
- Nested conditionals for byte order (big-endian vs. little-endian).
- Loop logic that includes both decoding and post-processing steps.
- Calculation of offsets and indexing togeteher with decoding.

**Refactoring plan**
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

**Estimated impact**
The refactored code will have a lower cyclomatic complexity, as the function will be broken down into smaller, more focused functions. This will improve readability and maintainability, as each function will have a single responsibility. However, it may introduce some overhead due to additional function calls, but this is generally acceptable for improved code quality.

**Impact**
- The refactoring reduced the cyclomatic complexity from **13 to 4**.
- The code is now more modular, with clear separation of concerns between decoding logic, endianness handling, and predictor processing.

**Reproduction:**
- Branch: `issue/16`
- Diff command:
  ```bash
  git diff master issue/16 src/main/java/org/apache/commons/imaging/formats/tiff/datareaders/AbstractImageDataReader.java
   ```

## Coverage

### Tools

The project came with JaCoCo tool integrated within.
We were already pretty familiar with this tool from our previous projects, so it was easy to use.
The project README even came with a shell command to generate the JaCoCo coverage, so there were no problems.

To generate the report we used the existing Maven workflow (as also described in the project documentation), e.g.:

- `mvn clean test`
- `mvn jacoco:report`

The HTML report is produced under:
- `target/site/jacoco/index.html`

Overall, no additional configuration was required beyond running the documented commands.


### Your own coverage tool

All of us implemented a similar manual branch coverage, which consisted of a static boolean array, and hit markers written into each branch.

**Function 1: `readBits`**

Related issue: [issue/12](https://github.com/a-runebou/DD2480-CCC-V/issues/12)

To visualize the code instrumentation: 
```bash
git diff master issue/12 src/main/java/org/apache/commons/imaging/formats/tiff/datareaders/BitInputStream.java
```


### Evaluation

1. How detailed is your coverage measurement?

   Our tool is limited only for the most basic functionality, as it requires for the branch marker to be added inside the source code (call marker function with the ID of each branch). 
   It can work with exceptions, as we can add the marker function inside `catch`. 
   However, it can't work with ternary operators, as the function cannot be called. 
   Ternary operators would need to rewritten using an `if` statement.

2. What are the limitations of your own tool?

   Our branch coverage by manual instrumentation is very limited, as it requires changing the source code directly. 
   The individual branch markers need to be added manually, which often requires adding `else` to lone `if` statements.
   Furthermore, it requires to know the total number of branches when creating the boolean array.

3. Are the results of your tool consistent with existing coverage tools?

   Yes, the results are consistent with the JaCoCo coverage. 

## Coverage improvement
### Function 1: `readBits`
Related issue: [issue/18](https://github.com/a-runebou/DD2480-CCC-V/issues/18)

The class `BitInputStream.java` (located in `formats/tiff/datareaders`) did not have any dedicated unit tests, therefore expanding on existing tests was impossible.
New tests were written in a dedicated test file.
The function is public, so it could be called directly, and no additional interfaces were needed to be implemented. 
The only data structure needed was the input stream, which was fairly easy to create.

In total, **6 tests** were created and the coverage was improved from **8/28 to 27/28**. 

The old coverage reported by JaCoCo:
![Old Coverage](/assets/coverage/readBits/read_bits_old_coverage.png)
The new coverage reported by JaCoCo:
![New Coverage](/assets/coverage/readBits/read_bits_new_coverage.png)

To visualize the code changes: `git diff master issue/18`
### 
Show the comments that describe the requirements for the coverage.

Report of old coverage: [link]

Report of new coverage: [link]

Test cases added:

git diff ...

Number of test cases added: two per team member (P) or at least four (P+).

## Self-assessment: Way of working

Right now, our team is in the `Working Well` state. By changing our method of operation to suit the current situation, we are making the anticipated progress. Our tools complement our work rather than obstruct it, practices are applied seamlessly in our daily tasks (documentation frequently follows immediately), and we continuously improve both practices and tools as we gain knowledge. The majority of the self-evaluations are in agreement. Overall, the indicators fit `Working Well`, with some minor doubts about consistency (e.g., documenting decisions immediately vs. after implementation) and whether all members apply the workflow equally across tasks. Clearer task ownership, more seamless change integration, faster feedback through testing and reviews, and a workflow that more closely reflects our actual operations are some of the ways we increased coordination and predictability.

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?
