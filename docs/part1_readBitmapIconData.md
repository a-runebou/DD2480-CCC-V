# Summary for function `readBitmapIconData`

### What are your results? Did everyone get the same result? Is there something that is unclear? If you have a tool, is its result the same as yours?

Both of us got the same result, which is a CCN of `21`. We counted the exact same lines and operations which solidifies our result. However, this result simply counts the number of decision points in the code +1. This matches the result from the tool `lizard`, but it does not consider early returns which can reduce the CC. 

Considering the theoretical CNN we instead get $\pi = 17$ (the number of decision points) and $s = 4$ exit points (3 throw + 1 return) which gives us a CCN of $\pi - s + 2 = 15$. This is a significant difference from the result of `21` and shows that the tool does not consider early returns in its calculation.

### Are the functions/methods with high CC also very long in terms of LOC?

For this function the NLOC is `110` which is quite long and reflects the high CC. However, one does not necessarily need to imply the other. For example, a function could have a high CC but be short if it has many decision points in a small amount of code such as switch statements. Similarly, a function could be long but have a low CC if it has few decision points.

### What is the purpose of these functions? Is it related to the high CC?



### If your programming language uses exceptions: Are they taken into account by the tool? If you think of an exception as another possible branch (to the catch block or the end of the function), how is the CC affected?


### Is the documentation of the function clear about the different possible outcomes induced by different branches taken?