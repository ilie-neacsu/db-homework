import com.example.exercisetwo.Solution;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SolutionTest {

    private static Stream<Arguments> provideTestData() {
        return Stream.of(
                Arguments.of(new int[]{130, 191, 200, 10}, 140), // Example 1
                Arguments.of(new int[]{405, 45, 300, 300}, 600), // Example 2
                Arguments.of(new int[]{50, 222, 49, 52, 25}, -1), // Example 3
                Arguments.of(new int[]{30, 909, 3190, 99, 3990, 9009}, 9918), // Example 4
                Arguments.of(new int[]{-11, 200, 300, -101}, -1) // Skip negative numbers
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testSolution(int[] array, int expected) {
        Solution solution = new Solution();
        assertEquals(expected, solution.solution(array));
    }

}
