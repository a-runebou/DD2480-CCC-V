# Part 2: Coverage measurement and improvement
## Task 1: DIY: manual branch coverage for `writeImage`
Linked issue: [issue/14](https://github.com/a-runebou/DD2480-CCC-V/issues/14)

To visualize the code instrumentation: `git diff master issue/14`

## Task 2: Coverage improvement for `writeImage`
Linked issue: [issue/25](https://github.com/a-runebou/DD2480-CCC-V/issues/25)

The class PcxWriter (located in formats/pcx) did not have any dedicated unit tests targeting the writeImage method directly. While PCX writing functionality was indirectly exercised through higher-level API tests, the internal decision logic in writeImage was not explicitly verified.

Because writeImage is public, it could be called directly from a new dedicated test class without introducing additional interfaces or modifying existing APIs. A new test file was created specifically to target different execution paths within the method.

The only required data structures were a BufferedImage and an OutputStream. These were straightforward to construct using small in-memory test images and ByteArrayOutputStream, allowing direct inspection of the generated PCX header fields such as bit depth and number of planes.

In total, 4 tests were created and the coverage was improved from 21/33 to 28/33. 

The old coverage reported by JaCoCo:
![Old Coverage](/assets/coverage/writeImage_old_coverage.png)
The new coverage reported by JaCoCo:
![New Coverage](/assets/coverage//writeImage_new_coverage.png)

To visualize the code changes: `git diff master issue/25`

## Task 3: Refactoring of `writeImage`
Linked issue: [issue/28](https://github.com/a-runebou/DD2480-CCC-V/issues/28)

The original cyclomatic complexity (as reported by lizard) was 33.

### Refactoring plan
The majority of the complexity was concentrated in the large conditional block responsible for determining bitDepth and planes:

if (palette == null || bitDepthWanted == 24 || bitDepthWanted == 32) {
    ...
} else if (palette.length() > 16 || bitDepthWanted == 8) {
    ...
} else if (palette.length() > 8 || bitDepthWanted == 4) {
    ...
}
...

To reduce complexity, this entire decision structure was moved into a separate helper method:

private int[] determineBitDepthAndPlanes(SimplePalette palette)

This method covers all branching related to bit depth and planes and returns the result as an integer array
 {bitDepth, planes}. By moving the full decision tree into a separate method, the cyclomatic complexity of writeImage was expected to drop significantly, since all major conditional branches were relocated.

## Final complexity
 The cyclomatic complexity of writeImage was reduced from 33 to 12, according to lizard.

 To see the refactoring changes: `git diff master issue/28`






