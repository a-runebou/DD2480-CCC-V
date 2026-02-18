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


# Summary for function `decompressT4_2D`

### What are your results? Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?

Both of us got the same result, which is a CCN of `20`. This aligned with the result from using Lizard. 

### Are the functions/methods with high CC also very long in terms of LOC?

For this function the NLOC is `90` which is quite long and reflects the high CC.

### What is the purpose of these functions? Is it related to the high CC?
The function `decompressT4_2D` is responsible for decoding T.4 compressed data. It has many checks regarding the data and many comparisons to T4_T6_Tables which adds to the CC.

### If your programming language uses exceptions: Are they taken into account by the tool? If you think of an exception as another possible branch (to the catch block or the end of the function), how is the CC affected?

No, the tool does not take exceptions into account in its calculation of CC.

### Is the documentation of the function clear about the different possible outcomes induced by different branches taken?
The documentation of the function is not very clear about the different possible outcomes induced by different branches. There is the Javadoc comment does not specify the what type of variable the return value is, however it does mention the exception. 


## Refactoring

Plan for refactoring complex code:
One main contributer to the complexity of the code is a chain of else if. The plan is to refactor it into its own function.

Estimated impact of refactoring (lower CC, but other drawbacks?).
This will lower the CC of the function however the same amount of unit tests will be required for branch coverage. I would not necessarily say that this refactoring makes the code any more legibile. The only plus is that the CC is lower, though I would not claim it is an improvement on the code.

Carried out refactoring (optional, P+):

[Refactoring](https://github.com/a-runebou/DD2480-CCC-V/commit/efbb358b6d168ad418ae7e2f4aded6750a4f8a60)

![Improved CC](DD2480-CCC-V/assets/Refactor.png)

### Your own coverage tool

[Own implementation](https://github.com/a-runebou/DD2480-CCC-V/commit/1c994dba9230adb857e080ab5c5f6967587259b9)

What kinds of constructs does your tool support, and how accurate is
its output?

### Evaluation

1. How detailed is your coverage measurement?

It IDs each branch and stores the branch ID global if it was reached during unit tests.
An else statement was added for each if missing one aswell as a catch for each try missing one

2. What are the limitations of your own tool?

The limitations are that it does not explicitly say what blocks of code were not reached, rather only the ID of the block.
Furthermore it does not neatly present the results of coverage at the end of the run, but is spread out during the run.

3. Are the results of your tool consistent with existing coverage tools?

It is consitent with which branches are reached, however jacoco counts many more branches than the self implemented coverage measurement. I believe it is due to jacoco double counting branches in the case of elif statements.

## Coverage improvement

Show the comments that describe the requirements for the coverage.

Report of old coverage: ![Old coverage](DD2480-CCC-V/assets/default.png)

Report of new coverage: ![New coverage](DD2480-CCC-V/assets/ImprovedBranchCoverage.png)

Test cases added:

[Added Unit tests](https://github.com/a-runebou/DD2480-CCC-V/commit/cad443df6da8e34ebc328ff8644aba0c8de2a725)

