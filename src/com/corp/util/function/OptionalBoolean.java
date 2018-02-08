package com.corp.util.function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.google.common.base.Preconditions;

public class OptionalBoolean {
    Optional<Boolean> inner = Optional.ofNullable(null);

    /**
     * Allows an Optional<Boolean> to be treated as OptionalBoolean, allowing minor functionality enhancements
     * @param other
     */
    private OptionalBoolean(@Nonnull Optional<Boolean> other) {
        Preconditions.checkArgument(other != null, "Must initialize with Optional");
        if (other.isPresent()) {
            inner = Optional.of(other.get());
        }
    }

    /**
     * Create an Optional boolean with the provided Boolean which may be null
     */
    private OptionalBoolean(@Nullable Boolean b) {
        inner = Optional.ofNullable(b);
    }

    /**
     * coerce creates an OptionalBoolean by mapping the provided optional with the mapping function. If the Optional
     * is not present, OptionalBoolean.get() will return false.
     * @param obj an Optional to map
     * @param mapper a function to map to a boolean
     * @param <T> Any type to be mapped to boolean
     * @return the OptionalBoolean resulting from mapping the provided obj
     */
    public static <T> OptionalBoolean coerce(Optional<T> obj, Function<T, Boolean> mapper) {
        return new OptionalBoolean(obj.map(mapper));
    }

    /**
     * @return an OptionalBoolean wrapping other. if other is not present, OptionalBoolean.get() will return false.
     */
    public static OptionalBoolean wrap(Optional<Boolean> other) {
        return new OptionalBoolean(other);
    }

    /**
     * @see @link Optional<Boolean>.empty
     */
    public static OptionalBoolean empty() {
        return new OptionalBoolean(Optional.empty());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof OptionalBoolean)) {
            return false;
        }
        OptionalBoolean other = (OptionalBoolean)o;
        if (!isPresent() && !other.isPresent()) {
            return true;
        }
        return get() == other.get();
    }

    /**
     * @see @link Optional<Boolean>.filter
     */
    public OptionalBoolean filter(Predicate<Boolean> predicate) {
        return wrap(inner.filter(predicate));
    }

    /**
     * @see @link Optional<Boolean>.flatMap
     */
    public <T> Optional<?> flatMap(Function<Boolean, Optional<T>> mapper) {
        if (isPresent()) {
            return mapper.apply(get());
        }
        return Optional.empty();
    }

    /**
     * @see @link Optional<Boolean>.isPresent
     */
    public boolean isPresent() {
        return inner.isPresent();
    }

    /**
     * Only differs from Optional.get in that it returns false if not present.
     *
     * @see @link Optional<Boolean>.get
     */
    public boolean get() {
        return isPresent() && inner.get();
    }

    /**
     * @see @link Optional<Boolean>.hashCode
     */
    public int hashCode() {
        return inner.hashCode();
    }

    /**
     * @see @link Optional<Boolean>.ifPresent
     */
    public void ifPresent(Consumer<Boolean> consumer) {
        if (isPresent()) {
            consumer.accept(inner.get());
        }
    }

    /**
     * @see @link Optional<Boolean>.map
     */
    public <T> Optional<T> map(Function<Boolean, T> mapper) {
        if (inner.isPresent()) {
            return Optional.ofNullable(mapper.apply(get()));
        }
        return Optional.empty();
    }

    /**
     * @see @link Optional<Boolean>.of
     */
    public static OptionalBoolean of(boolean b) {
        return new OptionalBoolean(b);
    }

    /**
     * @see @link Optional<Boolean>.ofNullable
     */
    public static OptionalBoolean ofNullable(Boolean b) {
        return new OptionalBoolean(b);
    }

    /**
     * @see @link Optional<Boolean>.orElse
     */
    public Boolean orElse(Boolean other) {
        return inner.orElse(other);
    }

    /**
     * @see @link Optional<Boolean>.orElseGet
     */
    public Boolean orElseGet(Supplier<Boolean> other) {
        return inner.orElseGet(other);
    }

    /**
     * @see @link Optional<Boolean>.orElseThrow
     */
    public <X extends Throwable> Boolean orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        return inner.orElseThrow(exceptionSupplier);
    }

    /**
     * @see @link Optional<Boolean>.toString
     */
    public String toString() {
        return "OptionalBoolean(" + inner.toString() + ")";
    }
}
