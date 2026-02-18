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

# Part 1: Complexity measurement
## Summary for function `performNextMedianCut`

### What are your results? Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?

Both of us counted the CCN to be `21`, which corresponds with the result from the tool `lizard`.
There was some uncertainty as to what counts as a decision, for example if an IF statement with a logical operation (`IF (a AND b)`) counts as 2 or 1 decisions.
This complexity was counted as `number of decisions + 1`, which is the formula that lizard uses.
Were we to use the theoretical complexity, we get CCN of ${num\_of\_decisions} - {num\_of\_returns} + 2 = 20 - 4 + 2 = 18$.

### Are the functions/methods with high CC also very long in terms of LOC?

Not necessarily, a short function can have a high complexity, an example would be a function containing a switch statement. Likewise, a very long function can have low complexity, if it does not have a large amount of decisions.

### What is the purpose of these functions? Is it related to the high CC?
The function `performNextMedianCut` performs one iteration of the Median Cut color quantization algorithm, which aims to reduce the number of colors in an image by repeatedly splitting the color space.
One iteration splits the colors into 2 regions with the best balance.
The high complexity corresponds with the function's purpose, as it requires nested for loops, and multiple conditionals to check different color components and median edge cases.

### If your programming language uses exceptions: Are they taken into account by the tool? If you think of an exception as another possible branch (to the catch block or the end of the function), how is the CC affected?

No, the tool does not take into account exceptions, as it only looks for decision points.
However, the `catch` keyword is counted as an additional decision.

### Is the documentation of the function clear about the different possible outcomes induced by different branches taken?
The function `performNextMedianCut` lacks any sort of documentation, either in the form of Javadoc or normal comments.
Therefore the different outcomes are not very clear, and had to be deduced from the source code.

# Part 2: Coverage measurement and improvement
## Task 1: DIY: manual branch coverage for `readBits`
Linked issue: [issue/12](https://github.com/a-runebou/DD2480-CCC-V/issues/12)

To visualize the code instrumentation: `git diff master issue/12`

## Task 2: Coverage improvement for `readBits`
Linked issue: [issue/18](https://github.com/a-runebou/DD2480-CCC-V/issues/18)

The class `BitInputStream.java` (located in `formats/tiff/datareaders`) did not have any dedicated unit tests, therefore expanding on existing tests was impossible. 
New tests were written in a dedicated test file.
The function is public, so it could be called directly, and no additional interfaces were needed to be implemented. 
The only data structure needed was the input stream, which was fairly easy to create.

In total, the coverage was improved from 8/28 to 27/28.

The old coverage reported by JaCoCo:
![Old Coverage](/assets/coverage/readBits/read_bits_old_coverage.png)
The new coverage reported by JaCoCo:
![New Coverage](/assets/coverage/readBits/read_bits_new_coverage.png)

To visualize the code changes: `git diff master issue/18`