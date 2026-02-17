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

# Part 2 - Task 1 - function `readBits`

### What is the quality of your own coverage measurement? Does it take into account ternary operators (condition ? yes : no) and exceptions, if available in your language?
As the coverage measurement is done by manual instrumentation (adding check markers into the source code), the quality is deffinitely not acceptable for a full scale project, but for this task, it is sufficient, as it provides the same results.

The function can work with exceptions, as we can add the check mark (function `hit`) inside `catch`. The manual coverage can not take into account the ternary operators, as we cannot add the call of the `hit` function. The ternary operator would need to be rewritten using an IF statement 

### What are the limitations of your tool? How would the instrumentation change if you modify the program?
The limitations lie in the manual instrumentation. Firstly, it modifies the source code, which is not ideal. We needed to add else statements to lone IFs, and default statements to switches which did not have one. 

Should the function be changed in the future, the manual branch checking would have to be changed alongside it, which introduces additional work.

### If you have an automated tool, are your results consistent with the ones produced by existing tool(s)?
We manually counted 28 branches for the `readBits` function, and the final coverage of 8 out of 28, which is consistent with the result from JaCoCo.