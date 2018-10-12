/*
 * Copyright © 2018 Grzegorz Krupinski (grzegorz.krupinski@programming-automation.com)
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

package com.pa.util.function;

import org.junit.Test;

import java.util.List;

import static com.pa.util.function.NullSafeUtils.*;
import static org.junit.Assert.*;

/**
 * Tests of NullSafeUtils class
 *
 * @author Grzegorz Krupinski
 */
public class NullSafeUtilsTest {

    public static final String TEST = "test";
    public static final String DEFAULT = "default";

    private List<String> fakeList;

    private String getTestStringNull() {
        return null;
    }

    private String getTestStringThrow() {
        if (1 == 1) {
            throw new NullPointerException();
        }
        return "";
    }

    private String getTestString() {
        return TEST;
    }

    @Test
    public void testGet() {
        assertNull(get(null));
        assertNull(get(this::getTestStringNull));
        assertNull(get(this::getTestStringThrow));
        assertNull(get(() -> fakeList.get(0)));
        assertEquals(TEST, get(() -> getTestString()));
    }

    @Test
    public void testGetDefault() {
        assertEquals(DEFAULT, get(null, DEFAULT));
        assertEquals(DEFAULT, get(this::getTestStringNull, DEFAULT));
        assertEquals(DEFAULT, get(this::getTestStringThrow, DEFAULT));
        assertEquals(DEFAULT, get(() -> fakeList.get(0), DEFAULT));
        assertEquals(TEST, get(() -> getTestString(), DEFAULT));
    }

    @Test
    public void testGetIf() {
        assertNull(getIf(this::getTestString, (s) -> s.equals("x")));
        assertEquals(TEST, getIf(this::getTestString, (s) -> s.equals(TEST)));
        assertNull(getIf(this::getTestStringThrow, (s) -> s.equals(TEST)));
    }

    @Test
    public void testIsNull() {
        assertTrue(isNull(null));
        assertTrue(isNull(this::getTestStringNull));
        assertTrue(isNull(this::getTestStringThrow));
        assertTrue(isNull(() -> fakeList.get(0)));
        assertFalse(isNull(() -> getTestString()));
    }

    @Test
    public void testIsEqual() {
        assertTrue(isEqual(TEST, () -> getTestString()));
        assertFalse(isEqual(TEST, this::getTestStringNull));
        assertFalse(isEqual(TEST, this::getTestStringThrow));
    }

    @Test
    public void testIsTrue() {
        assertTrue(isTrue(() -> TEST.equals(getTestString())));

    }

    @Test
    public void testIsTrueEx() {
        assertFalse(isTrue(() -> fakeList.isEmpty()));

    }

    @Test
    public void testIsFalse() {
        assertTrue(isFalse(() -> DEFAULT.equals(getTestString())));
        assertFalse(isFalse(() -> DEFAULT.equals(getTestStringThrow())));
    }

    @Test
    public void testAllNull() {
        assertTrue(allNull(null, null));
        assertTrue(allNull(getTestStringNull()));
        assertTrue(allNull(this::getTestStringNull, this::getTestStringThrow, () -> getTestStringNull()));
        assertFalse(allNull(this::getTestStringThrow, this::getTestString));
        assertFalse(allNull("a", null));

    }

    @Test
    public void testAllNotNull() {
        assertTrue(allNotNull(getTestString()));
        assertTrue(allNotNull(this::getTestString, () -> getTestString()));
        assertFalse(allNotNull(this::getTestStringThrow, this::getTestString));
        assertFalse(allNotNull("a", null));
    }

    @Test
    public void testAnyNotNull() {
        assertTrue(anyNotNull("a", null, null));
        assertFalse(anyNotNull(null, null, null));

        assertTrue(anyNotNull(this::getTestString, () -> getTestStringNull()));
        assertTrue(anyNotNull(this::getTestStringThrow, this::getTestString));
        assertFalse(anyNotNull(this::getTestStringThrow, this::getTestStringNull));
    }

    @Test
    public void testAnyNull() {
        assertTrue(anyNull("a", null, null));
        assertFalse(anyNull("a", "b"));

        assertTrue(anyNull(this::getTestString, () -> getTestStringNull()));
        assertTrue(anyNull(this::getTestStringThrow, this::getTestString));
        assertFalse(anyNull(this::getTestString, this::getTestString));
    }
}
