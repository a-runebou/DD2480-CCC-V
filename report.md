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


## Coverage

### Tools

Document your experience in using a "new"/different coverage tool.

How well was the tool documented? Was it possible/easy/difficult to
integrate it with your build environment?

### Your own coverage tool

Show a patch (or link to a branch) that shows the instrumented code to
gather coverage measurements.

The patch is probably too long to be copied here, so please add
the git command that is used to obtain the patch instead:

git diff ...

What kinds of constructs does your tool support, and how accurate is
its output?

### Evaluation

1. How detailed is your coverage measurement?

2. What are the limitations of your own tool?

3. Are the results of your tool consistent with existing coverage tools?

## Coverage improvement

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
