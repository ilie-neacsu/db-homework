# Exercise Two

## Prompt

There is an array `A` consisting of `N` integers. What is the maximum sum of two integers from `A` that share their first and last digits? For example, `1007` and `167` share their first (`1`) and last (`7`) digits, whereas `2002` and `55` do not.

Write a function:

```java
class Solution { 
    public int solution(int[] A); 
}
```

that, given an array `A` consisting of `N` integers, returns the maximum sum of two integers that share their first and last digits. If there are no two integers that share their first and last digits, the function should return `−1`.

### Examples

1. **Input:** `A = [130, 191, 200, 10]`  
   **Output:** `140`  
   **Explanation:** The only integers in `A` that share first and last digits are `130` and `10`.

2. **Input:** `A = [405, 45, 300, 300]`  
   **Output:** `600`  
   **Explanation:** There are two pairs of integers that share first and last digits: `(405, 45)` and `(300, 300)`. The sum of the two `300`s is bigger than the sum of `405` and `45`.

3. **Input:** `A = [50, 222, 49, 52, 25]`  
   **Output:** `−1`  
   **Explanation:** There are no two integers that share their first and last digits.

4. **Input:** `A = [30, 909, 3190, 99, 3990, 9009]`  
   **Output:** `9918`

## Implementation Details

The algorithm aims to find the maximum sum of two integers from an array that share their first and last digits. Here's a step-by-step explanation of how the algorithm works:

1. **Initialization**:
   - A `HashMap` named `digitMap` is used to store pairs of integers that share the same first and last digits. The key is a `DigitPair` (a record containing the first and last digits), and the value is an array of two integers representing the two largest numbers found so far with those digits.
   - A constant `MIN_VALUE` is set to `-1` to represent an invalid or non-existent value.

2. **Processing the Array**:
   - Iterate through each number in the input array.
   - Skip negative numbers.
   - Extract the first and last digits of the current number.
   - Create a `DigitPair` key using these digits.
   - Retrieve the current top two numbers for this digit pair from `digitMap`. If none exist, initialize with `MIN_VALUE`.
   - Update the top two numbers if the current number is larger than either of them.
   - Store the updated top two numbers back in `digitMap`.

3. **Calculating the Maximum Sum**:
   - Initialize `maxSum` to `MIN_VALUE`.
   - Iterate through the values in `digitMap`.
   - For each pair of top numbers, if both are valid (greater than `MIN_VALUE`), calculate their sum.
   - Update `maxSum` if the current sum is greater than the previous `maxSum`.

4. **Return the Result**:
   - Return `maxSum`, which is the maximum sum of two integers sharing the same first and last digits, or `-1` if no such pair exists.

### Time Complexity

- **O(N)**: The algorithm processes each element of the array exactly once, where `N` is the number of elements in the array.

### Space Complexity

- **O(U)**: The space complexity is determined by the number of unique first and last digit pairs, denoted as `U`. In the worst case, `U` can be at most 100 (10 possible first digits * 10 possible last digits). Therefore, the space complexity is constant, O(1), with respect to the input size.

## Compile, Run and Test

### Compile

To compile the project, navigate to the root directory of the module and run the following command:

```sh
mvn clean compile
```

### Run

To run the project, use the following command:

```sh
mvn exec:java -Dexec.mainClass="com.example.exercisetwo.Main"
```

### Test

To run the tests, use the following command:

```sh
mvn test
```