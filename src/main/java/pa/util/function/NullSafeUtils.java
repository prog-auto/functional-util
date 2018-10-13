
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

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * NullSafeUtils is a utility class, which provides methods to safety return value without throwing an exception
 * (for example NullPointerException).
 * Typical use is:
 * <p>
 * return get(()-&gt;object.getX().getY().getZ());
 * <p>
 * instead of
 * <p>
 * if (object!=null &amp;&amp; object.getX()!=null &amp;&amp; object.getX().getY()!=null){
 * <p>
 * return object.getX().getY().getZ();
 * <p>
 * }
 *
 * @author Grzegorz Krupinski
 */
public final class NullSafeUtils {

    /**
     * Returns a value provided by Supplier or null
     *
     * @param supplier supplier of a value
     * @param <S>      type
     * @return returns a value given by supplier (including null ) or returns null if RuntimeException is thrown
     */
    public static <S> S get(Supplier<S> supplier) {
        try {
            return supplier.get();
        } catch (RuntimeException ex) {
            return null;
        }
    }

    /**
     * Returns a value provided by Supplier or default value
     *
     * @param supplier     supplier of a value
     * @param <S>          type
     * @param defaultValue default value
     * @return returns a value given by supplier or
     * returns defaultValue if a value given by supplier is null or RuntimeException is thrown
     */
    public static <S> S get(Supplier<S> supplier, S defaultValue) {
        try {
            S result = supplier.get();
            return result != null ? result : defaultValue;
        } catch (RuntimeException ex) {
            return defaultValue;
        }
    }

    /**
     * Returns a value provided by Supplier if condition is met, or null
     *
     * @param supplier  supplier of a value
     * @param <S>       type
     * @param condition check condition
     * @return returns a value given by supplier if ccondition is met
     */
    public static <S> S getIf(Supplier<S> supplier, Predicate<S> condition) {
        try {
            S result = supplier.get();
            if (result == null || !condition.test(result)) {
                return null;
            }
            return result;
        } catch (RuntimeException ex) {
            return null;
        }
    }

    /**
     * Returns if Supplier's value is null or exception happened
     *
     * @param supplier supplier of a value
     * @param <S>      type
     * @return returns true if Supplier returned true else false
     */
    public static <S> boolean isNull(Supplier<S> supplier) {
        try {
            S result = supplier.get();
            return result == null;
        } catch (RuntimeException ex) {
            return true;
        }
    }

    /**
     * Returns true if Supplier's returned values is equal to expected value
     *
     * @param expected expected value of type S
     * @param supplier supplier of a value
     * @param <S>      type
     * @return returns true if Supplier returned value as expected
     */
    public static <S> boolean isEqual(S expected, Supplier<S> supplier) {
        try {
            S result = supplier.get();
            return expected.equals(result);
        } catch (RuntimeException ex) {
            return false;
        }
    }

    /**
     * Returns true only if Supplier returned true
     *
     * @param supplier supplier of a value
     * @return returns true only if Supplier returned true
     */
    public static boolean isTrue(Supplier<Boolean> supplier) {
        try {
            Boolean result = supplier.get();
            return result != null && result;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    /**
     * Returns true only if Supplier returned false
     *
     * @param supplier supplier of a value
     * @return returns true only if Supplier returned false
     */
    public static boolean isFalse(Supplier<Boolean> supplier) {
        try {
            Boolean result = supplier.get();
            return result != null && !result;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    /**
     * Returns true if all Objects are null
     *
     * @param objects list of objects
     * @return true if all Objects are null
     */
    public static boolean allNull(Object... objects) {
        for (Object o : objects) {
            if (o != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if all Suppliers returned null
     *
     * @param suppliers list of suppliers
     * @return true if all Suppliers returned null
     */
    public static boolean allNull(Supplier<Object>... suppliers) {
        for (Supplier<Object> supplier : suppliers) {
            Object o = get(supplier);
            if (o != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if all Objects are not null
     *
     * @param objects list of objects
     * @return true if all Objects are not null
     */
    public static boolean allNotNull(Object... objects) {
        for (Object o : objects) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if all Suppliers returned not null
     *
     * @param suppliers list of suppliers
     * @return true if all Suppliers returned not null
     */
    public static boolean allNotNull(Supplier<Object>... suppliers) {
        for (Supplier<Object> supplier : suppliers) {
            Object o = get(supplier);
            if (o == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if any Object is null
     *
     * @param objects list of objects
     * @return true if at least one object is null
     */
    public static boolean anyNull(Object... objects) {
        return !allNotNull(objects);
    }

    /**
     * Returns true if any Supplier returned null
     *
     * @param suppliers list of suppliers
     * @return true if at least one Supplier returned null
     */
    public static boolean anyNull(Supplier<Object>... suppliers) {
        return !allNotNull(suppliers);
    }

    /**
     * Returns true if any Object is not null
     *
     * @param objects list of objects
     * @return true if at least one object is not null
     */
    public static boolean anyNotNull(Object... objects) {
        return !allNull(objects);
    }

    /**
     * Returns true if any Supplier returned not null
     *
     * @param suppliers list of suppliers
     * @return true if at least one Supplier returned not null
     */
    public static boolean anyNotNull(Supplier<Object>... suppliers) {
        return !allNull(suppliers);
    }

}
