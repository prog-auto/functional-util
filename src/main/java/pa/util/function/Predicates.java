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

import java.util.function.BiFunction;
import java.util.function.Predicate;

/**
 * Predicates is a utility class, which provides methods to operate on predicates
 * <p>
 * Logical operations: NOT, AND, OR, NAND, NOR, XAND, XOR
 * <p>
 * Extra operations: NONE(=NOR), ALL(=AND)
 * <p>
 * Multi arguments (more than two arguments) logical operations: AND, OR, NAND, NOR
 * <p>
 * Extra operations: NONE(=NOR), ALL(=AND)
 *
 * @author Grzegorz Krupinski
 */
public class Predicates {

    /**
     * Predicate TRUE - useful in tests
     *
     * @param <T>
     * @return always true
     */
    public static <T> Predicate<T> pTrue() {
        return (T t) -> true;
    }

    /**
     * Predicate FALSE - useful in tests
     *
     * @param <T> type
     * @return always false
     */
    public static <T> Predicate<T> pFalse() {
        return (T t) -> false;
    }

    /**
     * Predicate to change member reference into Predicate (you can use methods
     * of Predicate class e.g. negate())
     *
     * @param <T> type
     * @param p   predicate or member reference
     * @return Predicate
     */
    public static <T> Predicate<T> predicate(Predicate<T> p) {
        return p;
    }

    /**
     * Logical negation - NOT
     *
     * @param <T> type
     * @param p   predicate
     * @return p.negate()
     */
    public static <T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }

    /**
     * Logical NOR (not OR=none)
     *
     * @param <T> type
     * @param p1  param1
     * @param p2  param2
     * @return if none
     */
    public static <T> Predicate<T> none(Predicate<T> p1, Predicate<T> p2) {
        return (p1.or(p2)).negate();
    }

    /**
     * Logical conjunction - AND (=all)
     *
     * @param <T> type
     * @param p1  param1
     * @param p2  param2
     * @return if all
     */
    public static <T> Predicate<T> all(Predicate<T> p1, Predicate<T> p2) {
        return p1.and(p2);
    }

    /**
     * Logical implication - COND
     *
     * @param <T> type
     * @param p   param1
     * @param q   param2
     * @return if p then q
     */
    public static <T> Predicate<T> cond(Predicate<T> p, Predicate<T> q) {
        return p.negate().or(q);
    }

    /**
     * Logical conjunction - AND
     *
     * @param <T> type
     * @param p1  predicate 1
     * @param p2  predicate 2
     * @return AND predicate
     */
    public static <T> Predicate<T> and(Predicate<T> p1, Predicate<T> p2) {
        return p1.and(p2);
    }

    /**
     * Logical NAND
     *
     * @param <T> type
     * @param p1  predicate 1
     * @param p2  predicate 2
     * @return NAND predicate
     */
    public static <T> Predicate<T> nand(Predicate<T> p1, Predicate<T> p2) {
        return (p1.and(p2)).negate();
    }

    /**
     * Exclusive conjunction - XAND
     *
     * @param <T> type
     * @param p1  predicate 1
     * @param p2  predicate 2
     * @return XAND predicate
     */
    public static <T> Predicate<T> xand(Predicate<T> p1, Predicate<T> p2) {
        return or(
                all(p1, p2),
                none(p1, p2)
        );
    }

    /**
     * Logical disjunction - OR
     *
     * @param <T> type
     * @param p1  predicate 1
     * @param p2  predicate 2
     * @return OR predicate
     */
    public static <T> Predicate<T> or(Predicate<T> p1, Predicate<T> p2) {
        return p1.or(p2);
    }

    /**
     * Logical NOR
     *
     * @param <T> type
     * @param p1  predicate 1
     * @param p2  predicate 2
     * @return NOR predicate
     */
    public static <T> Predicate<T> nor(Predicate<T> p1, Predicate<T> p2) {
        return (p1.or(p2)).negate();
    }

    /**
     * Exclusive disjunction - XOR
     *
     * @param <T> type
     * @param p1  predicate 1
     * @param p2  predicate 2
     * @return XOR predicate
     */
    public static <T> Predicate<T> xor(Predicate<T> p1, Predicate<T> p2) {
        return or(
                and(p1, not(p2)),
                and(not(p1), p2)
        );
    }

    /**
     * Produces Predicate based on array of Predicates
     *
     * @param <T>        type
     * @param func       base Predicate function
     * @param predicates array of predicates
     * @return predicate
     */
    protected static <T> Predicate<T> oper(BiFunction<Predicate<T>, Predicate<T>, Predicate<T>> func, Predicate<T>... predicates) {
        checkPredicatesCount(predicates.length);
        Predicate<T> result = predicates[0];
        for (int i = 1; i < predicates.length; i++) {
            result = func.apply(result, predicates[i]);
        }
        return result;
    }

    /**
     * if count equals 0 then throws exception
     *
     * @param count predicate count
     * @throws NullPointerException
     */
    protected static void checkPredicatesCount(int count) {
        if (count == 0) {
            throw new NullPointerException();
        }
    }

    /**
     * Logical NOR (=NONE)
     *
     * @param predicates list of predicates
     * @param <T>        type
     * @return NOR predicate
     */
    public static <T> Predicate<T> none(Predicate<T>... predicates) {
        return nor(predicates);
    }

    /**
     * Logical AND (=ALL)
     *
     * @param predicates list of predicates
     * @param <T>        type
     * @return AND predicate
     */
    public static <T> Predicate<T> all(Predicate<T>... predicates) {
        return and(predicates);
    }

    /**
     * Logical AND
     *
     * @param predicates list of predicates
     * @param <T>        type
     * @return AND predicate
     */
    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        return oper(Predicates::and, predicates);
    }

    /**
     * Logical NAND
     *
     * @param predicates list of predicates
     * @param <T>        type
     * @return NAND predicate
     */
    public static <T> Predicate<T> nand(Predicate<T>... predicates) {
        return oper(Predicates::and, predicates).negate();
    }

    /**
     * Logical OR
     *
     * @param predicates list of predicates
     * @param <T>        type
     * @return OR predicate
     */
    public static <T> Predicate<T> or(Predicate<T>... predicates) {
        return oper(Predicates::or, predicates);
    }

    /**
     * Logical NOR
     *
     * @param predicates list of predicates
     * @param <T>        type
     * @return NIR predicate
     */
    public static <T> Predicate<T> nor(Predicate<T>... predicates) {
        return oper(Predicates::or, predicates).negate();
    }

}
