TEST_PACKAGE = "test/"

def _get_java_dotted_package_path():
    return PACKAGE_NAME[len(TEST_PACKAGE):].replace('/','.')

def java_test_suite(name, srcs, deps, package=None, test_class=None, timeout='moderate', resources=[], nonTestSrcs=[], **kwargs):
    """Rule for Java test binaries. Can be used for tests that have a Suite class as well as those that don't.

    To specify a Suite class, populate test_class. Otherwise, the package should be populated with the
    Java package of the src files.

    Examples:

    java_test_suite(
        name = "example",
        srcs = ["Example.java"],
        package = "com.corp.example",
        deps = ["//thirdparty:junit"],
    )

    java_test_suite(
        name = "example2",
        srcs = glob(["**/*.java"]),
        test_class = "com.corp.example.ExampleSuite",
       deps = ["//thirdparty:junit"],
    )"""

    if test_class == None:
        if package == None:
            fail("Must declare a test_class for the Java Suite or a package for the test files")
        cmd = ["(",
            "echo 'package %s;'" % package,
            "echo 'import org.junit.runner.RunWith;'",
            "echo 'import org.junit.runners.Suite;'",
            "echo 'import org.junit.runners.Suite.SuiteClasses;'",
        ]
        parsed_srcs = []
        for src in srcs:
            spl = src.rsplit('/', 1)
            class_name = spl[-1].split('.')[0]
            parsed_srcs.append(class_name)
            if '/' in src:
                path = spl[0].replace('/', '.') + "." + class_name
                cmd.append("echo 'import %s.%s;'" % (package, path))

        cmd += [
            "echo '@RunWith(Suite.class)'",
            "echo '@SuiteClasses({'"
        ]
        for src in parsed_srcs:
            cmd.append("echo '  %s.class,'" % src)
        cmd.append("echo '})'")
        cmd.append("echo 'public class %s {}'" % name)
        cmd.append(") >$@")
        native.genrule(
            name=name + "_testsuite",
            outs=[name + ".java"],
            cmd="\n".join(cmd),
            testonly=1)
        srcs = srcs + [":" + name + "_testsuite"] + nonTestSrcs
        test_class =  _get_java_dotted_package_path() + "." +  name

    native.java_test(
        name = name,
        srcs = srcs,
        resource_strip_prefix = TEST_PACKAGE,
        test_class =  test_class,
        deps = deps,
        resources = resources,
        timeout = timeout,
        **kwargs)
