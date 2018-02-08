package com.corp.util.function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Enclosed.class)
public class OptionalBooleanTest {

    @RunWith(Parameterized.class)
    public static class ParameterizedTests {
        @Parameters
        public static Collection<Object[]> data() {
            return Arrays.asList(new Object[][]{
                {true, true},
                {false, false},
                {null, false}
            });
        }

        @Parameter(0)
        public Boolean input;

        @Parameter(1)
        public boolean expected;

        @Test
        public void test_regularBooleans() {
            assertThat(OptionalBoolean.ofNullable(input).get(), is(expected));
        }
    }

    public static class ExplicitTests {
        @Test
        public void test_mapping_Empty() {
            Optional<String> opt = Optional.empty();
            OptionalBoolean bool = OptionalBoolean.coerce(opt, s -> s.contains("hello"));
            assertThat(bool.get(), is(false));
        }

        @Test
        public void test_mapping_nonEmpty() {
            Optional<String> opt = Optional.of("hello world");
            OptionalBoolean bool = OptionalBoolean.coerce(opt, s -> s.contains("hello"));
            assertThat(bool.get(), is(true));
        }

        @Test
        public void test_wrapping_empty() {
            assertThat(OptionalBoolean.wrap(Optional.empty()).get(), is(false));
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main(OptionalBooleanTest.class.getName());
    }
}
