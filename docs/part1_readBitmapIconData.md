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


# Summary for function `readBitmapIconData`

### What are your results? Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?

Both of us got the same result, which is a CCN of `21`. We counted the exact same lines and operations which solidifies our result. However, this result simply counts the number of decision points in the code +1. This matches the result from the tool `lizard`, but it does not consider early returns which can reduce the CC. 

Considering the theoretical CNN we instead get $\pi = 17$ (the number of decision points) and $s = 4$ exit points (3 throw + 1 return) which gives us a CCN of $\pi - s + 2 = 15$. This is a significant difference from the result of `21` and shows that the tool does not consider early returns in its calculation.

### Are the functions/methods with high CC also very long in terms of LOC?

For this function the NLOC is `110` which is quite long and reflects the high CC. However, one does not necessarily need to imply the other. For example, a function could have a high CC but be short if it has many decision points in a small amount of code such as switch statements. Similarly, a function could be long but have a low CC if it has few decision points.

### What is the purpose of these functions? Is it related to the high CC?
The function `readBitmapIconData` is responsible for reading bitmap icon data and converts it into a usable `BufferedImage`. The high CC is directly related to its purpose since it must handle many conditional cases (different bit depths, compression modes, transparency formats and error checks). Essentially, the complexity comes from supporting multiple formats and ensuring that the function can handle various edge cases and errors gracefully.

### If your programming language uses exceptions: Are they taken into account by the tool? If you think of an exception as another possible branch (to the catch block or the end of the function), how is the CC affected?

No, the tool does not take exceptions into account in its calculation of CC. This is discussed in the first question where we calculated the theoretical CCN considering the exit points from exceptions. If we consider exceptions as branches, then the CCN is no longer `21` but `15`. However, we do count `catch` blocks as decision points in our manual calculation.

### Is the documentation of the function clear about the different possible outcomes induced by different branches taken?
The documentation of the function is not very clear about the different possible outcomes induced by different branches. There is no Javadoc comment so we only know the return type (`IconData`) and possible errors (`ImagingException`, `IOException`). 

