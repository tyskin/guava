/*
 * Copyright (C) 2010 The Guava Authors
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

package com.google.common.base;

import com.google.caliper.legacy.Benchmark;
import com.google.caliper.runner.CaliperMain;
import com.google.common.base.Objects;

/**
 * Some microbenchmarks for the {@link Objects} class.
 *
 * @author Ben L. Titzer
 */
public class ObjectsBenchmark extends Benchmark {

  private static final Integer I0 = -45;
  private static final Integer I1 = -1;
  private static final Integer I2 = 3;
  private static final String S0 = "3";
  private static final String S1 = "Ninety five";
  private static final String S2 = "44 one million";
  private static final String S3 = "Lowly laundry lefties";
  private static final String S4 = "89273487U#*&#";
  private static final Double D0 = 9.234d;
  private static final Double D1 = -1.2e55;

  public int timeHashString_2(int reps) {
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += Objects.hashCode(S0, S1);
    }
    return dummy;
  }

  public int timeHashString_3(int reps) {
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += Objects.hashCode(S0, S1, S2);
    }
    return dummy;
  }

  public int timeHashString_4(int reps) {
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += Objects.hashCode(S0, S1, S2, S3);
    }
    return dummy;
  }

  public int timeHashString_5(int reps) {
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += Objects.hashCode(S0, S1, S2, S3, S4);
    }
    return dummy;
  }

  public int timeHashMixed_5(int reps) {
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += Objects.hashCode(I2, S1, D1, S2, I0);
      dummy += Objects.hashCode(D0, I1, S3, I2, S0);
    }
    return dummy;
  }

  public static void main(String[] args) {
    CaliperMain.main(ObjectsBenchmark.class, args);
  }
}
