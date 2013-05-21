用户指南
------
Guava 项目包含数个 Google 员工在 Java 项目中用到的核心库: 集合, 缓存, 原生支持, 并发库, 公共注释, 字符串处理, I/O, 等等. Google 的员工们每天都在生产环境中使用着这些工具.

查看 Javadoc 并不是学习如何使用这些库的最有效的方式, 这里只是对 Guava 最流行和最强大的一些特性作了些说明.

<i>This wiki is a work in progress, and parts of it may still be under construction.</i>

+ <b>基本工具类</b>: Make using the Java language more pleasant.

    - <b>使用和避免 null</b>: null 有语言歧义, 会产生令人费解的错误, 反正他总是让人不爽. 很多 Guava 的工具类在遇到 null 时会直接拒绝或出错, 而不是默默地接受他们.

    - <b>前提条件</b>: 更容易的对你的方法进行前提条件的测试.

    - <b>常见的对象方法</b>: 简化了Object常见方法的实现, 如 hashCode() 和 toString().

    - <b>排序</b>: Guava 强大的 "fluent Comparator"比较器, 提供多关键字排序.

    - <b>Throwable类</b>: 简化了异常检查和错误传播.

+ <b>集合类</b>: Guava 对 JDK 集合类的扩展. 这是 Guava 项目最完善和为人所知的部分.
    - <b>Immutable collections</b>, 防御性编程, 不可修改的集合, and improved efficiency.
    
    - <b>New collection types</b>, for use cases that the JDK collections don't address as well as they could: multisets, multimaps, tables, bidirectional maps, and more.
    
    - <b>Powerful collection utilities</b>, java.util.Collections 中未包含的常用操作.
    
    - <b>Extension utilities</b>: 给 Collection 对象添加一个装饰器? 实现迭代器? 还有更容易的方法.
    
+ <b>缓存</b>: 局部缓存, done right, and supporting a wide variety of expiration behaviors.

+ <b>Functional idioms</b>: Used sparingly, Guava's functional idioms can significantly simplify code.

+ <b>Concurrency</b>: Powerful, simple abstractions to make it easier to write correct concurrent code.
    
    - <b>ListenableFuture</b>: Futures, with callbacks when they are finished.
    
    - <b>Service</b>: Things that start up and shut down, taking care of the difficult state logic for you.

+ <b>Strings</b>: A few extremely useful string utilities: splitting, joining, padding, and more.

+ <b>Primitives</b>: operations on primitive types, like int and char, not provided by the JDK, including unsigned variants for some types.

+ <b>Ranges</b>: Guava's powerful API for dealing with ranges on Comparable types, both continuous and discrete.

+ <b>I/O</b>: Simplified I/O operations, especially on whole I/O streams and files, for Java 5 and 6.

+ <b>Hashing</b>: Tools for more sophisticated hashes than what's provided by Object.hashCode(), including Bloom filters.

+ <b>EventBus</b>: Publish-subscribe-style communication between components without requiring the components to explicitly register with one another.

+ <b>Math</b>: Optimized, thoroughly tested math utilities not provided by the JDK.

+ <b>Reflection</b>: Guava utilities for Java's reflective capabilities.

+ <b>Tips</b>: Getting your application working the way you want it to with Guava.
    - <b>Philosophy</b>: what Guava is and isn't, and our goals.
    - <b>Using Guava in your build</b>, with build systems including Maven, Gradle, and more.
    - <b>Using ProGuard</b> to avoid bundling parts of Guava you don't use with your JAR.
    - <b>Apache Commons equivalents</b>, helping you translate code from using Apache Commons Collections.
    - <b>Compatibility</b>, details between Guava versions.
    - <b>Idea Graveyard</b>, feature requests that have been conclusively rejected.
    - <b>Friends</b>, open-source projects we like and admire.