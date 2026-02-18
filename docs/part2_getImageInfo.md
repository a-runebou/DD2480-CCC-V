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

### What is the quality of your own coverage measurement? Does it take into account ternary operators(condition ? yes : no) and exceptions, if available in your language?
> My manual coverage measurement is reasonably accurate for the exact branches I explicitly instrumented in the code directly, but it is not a complete “branch coverage” measurement of the whole function/program.

> It does not automatically account for ternary operators (`cond ? yes : no`) unless I manually instrument both outcomes by re-writing into `if/else`. For exceptions, it only “counts” exceptional paths if I place `hit()` before the `throw`; unexpected exceptions that happen earlier won’t be recorded.


### What are the limitations of your tool? How would the instrumentation change if you modify the program?

> The big limitation is that this tool is **manual, incomplete, and fragile**: it only measures what I decided to label, and it can miss branches created by the compiler (ternaries, short-circuit logic, synthetic paths). 

> If the program changes, I must update IDs and move/add/remove `hit()` calls, so they still represent the real branch outcomes. In other words, instrumentation has to be changed alongside the code, and it’s quite easy to get out of sync compared to automated tools.


### If you have an automated tool, are your results consistent with the ones produced by existing tool(s)?

> My manual tracker and JaCoCo are consistent at a high level: if my report shows an outcome was hit, JaCoCo will generally show execution in that region too. But they won’t match exactly, because JaCoCo measures bytecode-level branches automatically, while my tool measures only the labeled outcomes I chose.
