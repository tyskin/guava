TODO: rewrite with more examples

Guava's Throwables utility can frequently simplify dealing with exceptions.

## Propagation
Sometimes, when you catch an exception, you want to throw it back up to the next try/catch block. This is frequently the case for RuntimeException or Error instances, which do not require try/catch blocks, but can be caught by try/catch blocks when you don't mean them to.

Guava provides several utilities to simplify propagating exceptions. For example:
```
   try {
     someMethodThatCouldThrowAnything();
   } catch (IKnowWhatToDoWithThisException e) {
     handle(e);
   } catch (Throwable t) {
     Throwables.propagateIfInstanceOf(t, IOException.class);
     Throwables.propagateIfInstanceOf(t, SQLException.class);
     throw Throwables.propagate(t);
   }
```
Each of these methods throw the exception themselves, but throwing the result -- e.g. throw Throwables.propagate(t) -- can be useful to prove to the compiler that an exception will be thrown.

Here are quick summaries of the propagation methods provided by Guava:

Signature  |   Explanation
:---|:---
RuntimeException propagate(Throwable)|	 Propagates the throwable as-is if it is a RuntimeException or an Error, or wraps it in a RuntimeException and throws it otherwise. Guaranteed to throw. The return type is a RuntimeException so you can write throw Throwables.propagate(t) as above, and Java will realize that that line is guaranteed to throw an exception.
void propagateIfInstanceOf(Throwable, Class<X extends Exception>) throws X	 |Propagates the throwable as-is, if and only if it is an instance of X.
void propagateIfPossible(Throwable)|	 Throws throwable as-is only if it is a RuntimeException or an Error.
void propagateIfPossible(Throwable, Class<X extends Throwable>) throws X|	 Throws throwable as-is only if it is a RuntimeException, an Error, or an X.

### Uses for Throwables.propagate
### Emulating Java 7 multi-catch and rethrow

Typically, if a caller wants to let an exception propagate up the stack, he doesn't need a catch block at all. Since he's not going to recover from the exception, he probably shouldn't be logging it or taking other action. He may want to perform some cleanup, but usually that cleanup needs to happen regardless of whether the operation succeeded, so it ends up in a finally block. However, a catch block with a rethrow is sometimes useful: Maybe the caller must update a failure count before propagating an exception, or maybe he will propagate the exception only conditionally,

Catching and rethrowing an exception is straightforward when dealing with only one kind of exception. Where it gets messy is when dealing with multiple kinds of exceptions:
```
@Override public void run() {
  try {
    delegate.run();
  } catch (RuntimeException e) {
    failures.increment();
    throw e;
  } catch (Error e) {
    failures.increment();
    throw e;
  }
}
```
Java 7 solves this problem with multicatch:
```
  } catch (RuntimeException | Error e) {
    failures.increment();
    throw e;
  }
```
Non-Java 7 users are stuck. They'd like to write code like the following, but the compiler won't permit them to throw a variable of type Throwable:
```
  } catch (Throwable t) {
    failures.increment();
    throw t;
  }
```
The solution is to replace throw t with throw Throwables.propagate(t). In this limited circumstance, Throwables.propagate behaves identically to the original code. However, it's easy to write code with Throwables.propagate that has other, hidden behavior. In particular, note that the above pattern works only with RuntimeException and Error. If the catch block may catch checked exceptions, you'll also need some calls to propagateIfInstanceOf in order to preserve behavior, as Throwables.propagate can't directly propagate a checked exception.

Overall, this use of propagate is so-so. It's unnecessary under Java 7. Under other versions, it saves a small amount of duplication, but so could a simple Extract Method refactoring. Additionally, use of propagate makes it easy to accidentally wrap checked exceptions.

### Unnecessary: Converting from throws Throwable to throws Exception
A few APIs, notably the Java reflection API and (as a result) JUnit, declare methods that throw Throwable. Interacting with these APIs can be a pain, as even the most general-purpose APIs typically only declare throws Exception. Throwables.propagate is used by some callers who know they have a non-Exception, non-Error Throwable. Here's an example of declaring a Callable that executes a JUnit test:
```
public Void call() throws Exception {
  try {
    FooTest.super.runTest();
  } catch (Throwable t) {
    Throwables.propagateIfPossible(t, Exception.class);
    Throwables.propagate(t);
  }

  return null;
}
```
There's no need for propagate() here, as the second line is equivalent to "throw new RuntimeException(t)." (Digression: This example also reminds me that propagateIfPossible is potentially confusing, since it propagates not just arguments of the given type but also Errors and RuntimeExceptions.)

This pattern (or similar variants like "throw new RuntimeException(t)") shows up ~30 times in Google's codebase. (Search for 'propagateIfPossible[^;]* Exception.class[)];'.) A slight majority of them take the explicit "throw new RuntimeException(t)" approach. It's possible that we would want a "throwWrappingWeirdThrowable" method for Throwable-to-Exception conversions, but given the two-line alternative, there's probably not much need unless we were to also deprecate propagateIfPossible.

### Controversial uses for Throwables.propagate
### Controversial: Converting checked exceptions to unchecked exceptions
In principle, unchecked exceptions indicate bugs, and checked exceptions indicate problems outside your control. In practice, even the JDK sometimes gets it wrong (or at least, for some methods, no answer is right for everyone).

As a result, callers occasionally have to translate between exception types:
```
try {
  return Integer.parseInt(userInput);
} catch (NumberFormatException e) {
  throw new InvalidInputException(e);
}
```

```
try {
  return publicInterfaceMethod.invoke();
} catch (IllegalAccessException e) {
  throw new AssertionError(e);
}
```
Sometimes, those callers use Throwables.propagate. What are the disadvantages? The main one is that the meaning of the code is less obvious. What does throw Throwables.propagate(ioException) do? What does throw new RuntimeException(ioException) do? The two do the same thing, but the latter is more straightforward. The former raises questions: "What does this do? It isn't just wrapping in RuntimeException, is it? If it were, why would they write a method wrapper?" Part of the problem here, admittedly, is that "propagate" is a vague name. (Is it a way of throwing undeclared exceptions?) Perhaps "wrapIfChecked" would have worked better. Even if the method were called that, though, there would be no advantage to calling it on a known checked exception. There may even be additional downsides: Perhaps there's a more suitable type than a plain RuntimeException for you to throw -- say, IllegalArgumentException.

We sometimes also see propagate used when the exception only might be a checked exception. The result is slightly smaller and slightly less straightforward than the alternative:
```
} catch (RuntimeException e) {
  throw e;
} catch (Exception e) {
  throw new RuntimeException(e);
}
```

```
} catch (Exception e) {
  throw Throwables.propagate(e);
}
```
However, the elephant in the room here is the general practice of converting checked exceptions to unchecked exceptions. It is unquestionably the right thing in some cases, but more frequently it's used to avoid dealing with a legitimate checked exception. This leads us to the debate over whether checked exceptions are bad idea in general. I don't wish to go into all that here. Suffice it to say that Throwables.propagate does not exist for the purpose of encouraging Java users to ignore IOException and the like.

### Controversial: Exception tunneling
But what about when you're implementing a method that isn't allowed to throw exceptions? Sometimes you need to wrap your exception in an unchecked exception. This is fine, but again, propagate is unnecessary for simple wrapping. In fact, manual wrapping may be preferable: If you wrap every exception (instead of just checked exceptions), then you can unwrap every exception on the other end, making for fewer special cases. Additionally, you may wish to use a custom exception type for the wrapping.

Controversial: Rethrowing exceptions from other threads
```
try {
  return future.get();
} catch (ExecutionException e) {
  throw Throwables.propagate(e.getCause());
}
```
There are multiple things to consider here:

1. The cause may be a checked exception. See "Converting checked exceptions to unchecked exceptions" above. But what if the task is known not to throw a checked exception? (Maybe it's the result of a Runnable.) As discussed above, you can catch the exception and throw AssertionError; propagate has little more to offer. For Future in particular, also consider Futures.get. (TODO: say something about its additional InterruptedException behavior)
2. The cause may be a non-Exception, non-Error Throwable. (Well, it's unlikely to actually be one, but the compiler would force you to consider the possibility if you tried to rethrow it directly.) See "Converting from throws Throwable to throws Exception" above.
3. The cause may be an unchecked exception or error. If so, it will be rethrown directly. Unfortunately, its stack trace will reflect the thread in which the exception was originally created, not the thread in which it is currently propagating. It's typically best to have include both threads' stack traces available in the exception chain, as in the ExecutionException thrown by get. (This problem isn't really about propagate; it's about any code that rethrows an exception in a different thread.)


## Causal Chain
Guava makes it somewhat simpler to study the causal chain of an exception, providing three useful methods whose signatures are self-explanatory:
<table>
<tr>
    <td>Throwable getRootCause(Throwable)</td>
</tr>
<tr>
    <td>List<Throwable> getCausalChain(Throwable)</td>
</tr>
<tr>
    <td>String getStackTraceAsString(Throwable)</td>
</tr>
