/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static java.util.Arrays.asList;
import static org.truth0.Truth.ASSERT;

import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.testing.google.TestStringMultisetGenerator;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * Unit test for {@link LinkedHashMultiset}.
 *
 * @author Kevin Bourrillion
 */
@GwtCompatible(emulated = true)
public class LinkedHashMultisetTest extends AbstractMultisetTest {

  private static TestStringMultisetGenerator linkedHashMultisetGenerator() {
    return new TestStringMultisetGenerator() {
      @Override protected Multiset<String> create(String[] elements) {
        return LinkedHashMultiset.create(asList(elements));
      }

      @Override
      public List<String> order(List<String> insertionOrder) {
        List<String> order = Lists.newArrayList();
        for (String s : insertionOrder) {
          int index = order.indexOf(s);
          if (index == -1) {
            order.add(s);
          } else {
            order.add(index, s);
          }
        }
        return order;
      }
    };
  }

  @Override protected <E> Multiset<E> create() {
    return LinkedHashMultiset.create();
  }

  public void testCreate() {
    Multiset<String> multiset = LinkedHashMultiset.create();
    multiset.add("foo", 2);
    multiset.add("bar");
    assertEquals(3, multiset.size());
    assertEquals(2, multiset.count("foo"));
    assertEquals("[foo x 2, bar]", multiset.toString());
  }

  public void testCreateWithSize() {
    Multiset<String> multiset = LinkedHashMultiset.create(50);
    multiset.add("foo", 2);
    multiset.add("bar");
    assertEquals(3, multiset.size());
    assertEquals(2, multiset.count("foo"));
    assertEquals("[foo x 2, bar]", multiset.toString());
  }

  public void testCreateFromIterable() {
    Multiset<String> multiset
        = LinkedHashMultiset.create(Arrays.asList("foo", "bar", "foo"));
    assertEquals(3, multiset.size());
    assertEquals(2, multiset.count("foo"));
    assertEquals("[foo x 2, bar]", multiset.toString());
  }

  public void testToString() {
    ms.add("a", 3);
    ms.add("c", 1);
    ms.add("b", 2);

    assertEquals("[a x 3, c, b x 2]", ms.toString());
  }

  public void testLosesPlaceInLine() throws Exception {
    ms.add("a");
    ms.add("b", 2);
    ms.add("c");
    ASSERT.that(ms.elementSet()).has().allOf("a", "b", "c").inOrder();
    ms.remove("b");
    ASSERT.that(ms.elementSet()).has().allOf("a", "b", "c").inOrder();
    ms.add("b");
    ASSERT.that(ms.elementSet()).has().allOf("a", "b", "c").inOrder();
    ms.remove("b", 2);
    ms.add("b");
    ASSERT.that(ms.elementSet()).has().allOf("a", "c", "b").inOrder();
  }

  public void testIteratorRemoveConcurrentModification() {
    ms.add("a");
    ms.add("b");
    Iterator<String> iterator = ms.iterator();
    iterator.next();
    ms.remove("a");
    assertEquals(1, ms.size());
    assertTrue(ms.contains("b"));
    try {
      iterator.remove();
      fail();
    } catch (ConcurrentModificationException expected) {}
    assertEquals(1, ms.size());
    assertTrue(ms.contains("b"));
  }
}

