package dev.agiro.matriarch.parametrized_test_source.annotations;


import dev.agiro.matriarch.parametrized_test_source.MotherFactoryResourceProviders;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for parameterized tests that generates test data using Matriarch's ObjectMother.
 *
 * <p>This annotation allows you to easily generate multiple test cases with different configurations
 * without manually creating test objects.
 *
 * <h2>Basic Usage:</h2>
 * <pre>{@code
 * @ParameterizedTest
 * @MotherFactoryResource(args = {
 *     @RandomArg(targetClass = User.class)
 * })
 * void testWithRandomUser(User user) {
 *     assertNotNull(user);
 * }
 * }</pre>
 *
 * <h2>With Named Test Cases:</h2>
 * <pre>{@code
 * @ParameterizedTest
 * @MotherFactoryResource(args = {
 *     @RandomArg(
 *         name = "Valid User",
 *         targetClass = User.class,
 *         overrides = @OverrideField(field = "email", value = "valid@example.com")
 *     ),
 *     @RandomArg(
 *         name = "User with Invalid Email",
 *         targetClass = User.class,
 *         overrides = @OverrideField(field = "email", value = "invalid-email")
 *     )
 * })
 * void testEmailValidation(User user) {
 *     // Test logic
 * }
 * }</pre>
 *
 * <h2>With Complex Overrides:</h2>
 * <pre>{@code
 * @ParameterizedTest
 * @MotherFactoryResource(args = {
 *     @RandomArg(
 *         name = "Admin User",
 *         targetClass = User.class,
 *         jsonOverrides = """
 *             {
 *                 "email": "admin@example.com",
 *                 "role": "ADMIN",
 *                 "permissions": ["READ", "WRITE", "DELETE"]
 *             }
 *             """
 *     )
 * })
 * void testAdminPermissions(User user) {
 *     assertTrue(user.hasPermission("DELETE"));
 * }
 * }</pre>
 *
 * <h2>With Regex Patterns:</h2>
 * <pre>{@code
 * @ParameterizedTest
 * @MotherFactoryResource(args = {
 *     @RandomArg(
 *         name = "User with Pattern Email",
 *         targetClass = User.class,
 *         overrides = @OverrideField(
 *             field = "email",
 *             value = "[a-z]{5,10}@(gmail|outlook).com",
 *             isRegex = true
 *         )
 *     )
 * })
 * void testEmailPattern(User user) {
 *     assertTrue(user.getEmail().matches(".*@(gmail|outlook)\\.com"));
 * }
 * }</pre>
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ArgumentsSource(MotherFactoryResourceProviders.class)
public @interface MotherFactoryResource {
    /**
     * Array of test case arguments. Each RandomArg represents one test case.
     * Multiple RandomArg entries will generate multiple test cases.
     */
    RandomArg[] args();
}
