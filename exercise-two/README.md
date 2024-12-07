## Exercise Two Prompt

There is an array `A` consisting of `N` integers. What is the maximum sum of two integers from `A` that share their first and last digits? For example, `1007` and `167` share their first (`1`) and last (`7`) digits, whereas `2002` and `55` do not.

Write a function:

```java
class Solution { 
    public int solution(int[] A); 
}
```

that, given an array `A` consisting of `N` integers, returns the maximum sum of two integers that share their first and last digits. If there are no two integers that share their first and last digits, the function should return `−1`.

## Examples

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
