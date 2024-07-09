# Class Structure
My standard class structure is as follows:
1. Static methods and variables first, then instance methods and variables.
2. Variables first, then enums & classes, then methods
3. Private first, then package-private, then public
    * Exception: the class constructor(s) go first among the methods
4. final variables first, then other variables

Other notes:
* There should be one empty line between each method declared.
* `@Override` functions go at the bottom.