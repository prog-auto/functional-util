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

package pa.util.function;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static pa.util.function.Predicates.*;

/**
 * Tests of Predicates class
 *
 * @author Grzegorz Krupinski
 */
public class PredicatesTest {

    private final List<String> list = Arrays.asList("A", "B", "C", "DD");

    private final Predicate<String> equalsA = s -> s.equals("A");
    private final Predicate<String> equalsB = s -> s.equals("B");
    private final Predicate<String> equalsC = s -> s.equals("C");
    private final Predicate<String> startsWithA = s -> s.startsWith("A");
    private final Predicate<String> lengthOne = s -> s.length() == 1;
    private final Predicate<String> lengthTwo = s -> s.length() == 2;

    private boolean equalsA(String s) {
        return s.equals("A");
    }

    @Test
    public void testPredicate() {
        assertEquals(equalsA, predicate(equalsA));
        assertEquals(equalsA.test("A"), predicate(equalsA).test("A"));
        assertEquals(equalsA.test("B"), predicate(equalsA).test("B"));

        List<String> expected = Arrays.asList("A");
        assertPredicate(expected, this::equalsA);

        expected = Arrays.asList("B", "C", "DD");
        //assertPredicate(expected, this::equalsA);//negate()? - how to use negate without predicate()
        assertPredicate(expected, predicate(this::equalsA).negate());
        assertPredicate(expected, not(this::equalsA));
    }

    @Test
    public void testPFalse() {
        assertFalse(pFalse().test("a"));
        assertFalse(pFalse().test(1));
    }

    @Test
    public void testPTrue() {
        assertTrue(pTrue().test("a"));
        assertTrue(pTrue().test(1));
    }

    @Test
    public void testOr() {
        List<String> expected = Arrays.asList("A", "C");
        assertPredicate(expected, or(equalsA, equalsC));
    }

    @Test
    public void testOrExt() {
        List<String> expected = Arrays.asList("A", "B", "C");
        assertPredicate(expected, or(equalsA, equalsB, equalsC));
    }

    @Test
    public void testNorNone() {
        List<String> expected = Arrays.asList("DD");
        assertPredicate(expected, nor(equalsA, lengthOne));
        assertPredicate(expected, none(equalsA, lengthOne));
    }

    @Test
    public void testNorNoneExt() {
        List<String> expected = Arrays.asList("DD");
        assertPredicate(expected, nor(equalsA, equalsB, equalsC));
        assertPredicate(expected, none(equalsA, equalsB, equalsC));

        expected = Arrays.asList("B", "C", "DD");
        assertPredicate(expected, nor(equalsA));
        assertPredicate(expected, none(equalsA));
    }

    @Test
    public void testNot() {
        List<String> expected = Arrays.asList("B", "C", "DD");

        assertPredicate(expected, not(equalsA));
        assertPredicate(expected, equalsA.negate());
        assertPredicate(expected, not(this::equalsA));
        assertPredicate(expected, predicate(this::equalsA).negate());
    }

    @Test
    public void testAndAll() {
        List<String> expected = Arrays.asList("A");

        assertPredicate(expected, and(equalsA, lengthOne));
        assertPredicate(expected, all(equalsA, lengthOne));

        List<String> l = list.stream()
                .filter(equalsA)
                .filter(lengthOne)
                .collect(Collectors.toList());
        assertEquals(expected, l);
    }

    @Test
    public void testAndAllExt() {
        List<String> expected = Arrays.asList("A");

        assertPredicate(expected, and(equalsA, lengthOne, startsWithA));
        assertPredicate(expected, all(equalsA, lengthOne, startsWithA));

        List<String> l = list.stream()
                .filter(equalsA)
                .filter(lengthOne)
                .filter(startsWithA)
                .collect(Collectors.toList());
        assertEquals(expected, l);
    }

    @Test
    public void testNand() {
        List<String> expected = Arrays.asList("B", "C", "DD");

        assertPredicate(expected, nand(equalsA, lengthOne));
        assertPredicate(expected, equalsA.negate().or(lengthOne.negate()));
    }

    @Test
    public void testNandExt() {
        List<String> expected = Arrays.asList("B", "C", "DD");

        assertPredicate(expected, nand(equalsA, lengthOne, startsWithA));
        assertPredicate(expected, equalsA.negate().or(lengthOne.negate().or(startsWithA.negate())));
    }

    @Test
    public void testXand() {
        List<String> expected = Arrays.asList("A", "DD");
        assertPredicate(expected, xand(equalsA, lengthOne));
    }

    @Test
    public void testXor() {
        List<String> expected = Arrays.asList("A", "DD");
        assertPredicate(expected, xor(equalsA, lengthTwo));

        expected = Arrays.asList("B", "C");
        assertPredicate(expected, xor(equalsA, lengthOne));

        assertPredicate(expected, xor(equalsA, lengthOne));
    }

    @Test
    public void testCond() {
        List<String> expected = Arrays.asList("A", "B", "C", "DD");
        assertPredicate(expected, cond(equalsA, lengthOne));

        expected = Arrays.asList("A", "DD");
        assertPredicate(expected, cond(lengthOne, equalsA));
    }

    @Test
    public void testCheckPredicatesCount() {
        checkPredicatesCount(1);
    }

    @Test(expected = NullPointerException.class)
    public void testCheckPredicatesCountEx() {
        checkPredicatesCount(0);
    }

    /**
     * filter test list using predicate in param
     *
     * @param p predicate used in filtering
     * @return filtered list
     */
    private List<String> filterList(Predicate<String> p) {
        return list.stream().filter(p).collect(Collectors.toList());
    }

    private void assertPredicate(List<String> expected, Predicate<String> p) {
        assertEquals(expected, filterList(p));
    }


}
