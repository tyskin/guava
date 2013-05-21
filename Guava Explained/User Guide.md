用户指南
------
Guava 项目包含数个 Google 员工在 Java 项目中用到的核心库: 集合, 缓存, 原生支持, 并发库, 公共注释, 字符串处理, I/O, 等等. Google 的员工们每天都在生产环境中使用着这些工具.

查看 Javadoc 并不是学习如何使用这些库的最有效的方式, 这里只是对 Guava 最流行和最强大的一些特性作了些说明.

<i>This wiki is a work in progress, and parts of it may still be under construction.</i>

+ <b>基本工具类</b>: Make using the Java language more pleasant.

    - <b>使用和避免 null</b>: null 有语言歧义, 会产生令人费解的错误, 反正他总是让人不爽. 很多 Guava 的工具类在遇到 null 时会直接拒绝或出错, 而不是默默地接受他们.

    - <b>前提条件</b>: 更容易的对你的方法进行前提条件的测试.

    - <b>常见的对象方法</b>: 简化了Object常用方法的实现, 如 hashCode() 和 toString().

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

+ <b>Strings</b>: 一个非常非常有用的字符串工具类: 提供 splitting, joining, padding 等操作.

+ <b>Primitives</b>: 扩展 JDK 中未提供的对原生类型（如int、char等）的操作, 包括某些类型的无符号的变量.

+ <b>Ranges</b>: Guava 一个强大的 API, 提供 Comparable 类型的范围处理, 包括连续和离散的情况.

+ <b>I/O</b>: 简化 I/O 操作, 特别是对 I/O 流和文件的操作, for Java 5 and 6.

+ <b>Hashing</b>: 提供比 Object.hashCode() 更复杂的 hash 方法, 提供 Bloom filters.

+ <b>EventBus</b>: 基于发布-订阅模式的组件通信，但是不需要明确地注册在委托对象中

+ <b>Math</b>: 优化的 math 工具类，经过完整测试.

+ <b>Reflection</b>: Guava 的 Java 反射机制工具类.

+ <b>Tips</b>: Getting your application working the way you want it to with Guava.
    - <b>Philosophy</b>: what Guava is and isn't, and our goals.
    - <b>Using Guava in your build</b>, with build systems including Maven, Gradle, and more.
    - <b>Using ProGuard</b> to avoid bundling parts of Guava you don't use with your JAR.
    - <b>Apache Commons equivalents</b>, helping you translate code from using Apache Commons Collections.
    - <b>Compatibility</b>, details between Guava versions.
    - <b>Idea Graveyard</b>, feature requests that have been conclusively rejected.
    - <b>Friends</b>, open-source projects we like and admire.