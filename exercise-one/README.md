# Exercise One

## Prompt

2 threads ping-pong. One thread prints 'ping', another one prints 'pong'. Stop after 5 seconds.

The expected output is alternating ping-pong:

```plaintext
ping
pong
ping
pong
ping
pong
```

## Implementation Details

The `exercise-one` module implements a simple ping-pong mechanism using two threads. One thread prints "ping" and the other prints "pong" in an alternating fashion. The program runs for a specified duration and then stops. Below is a high-level description of the implementation:

1. **Configuration**:
    - The `Configuration` class contains constants used throughout the module, such as the execution duration, thread names, and messages to be printed.

2. **Main Class**:
    - The `Main` class is the entry point of the application. It initializes a `PingPong` instance and starts the ping-pong process for a specified duration.

3. **PingPong Class**:
    - The `PingPong` class manages the state and coordination between the ping and pong threads. It uses a `PingPongState` inner class to hold the shared state, including a lock, conditions for synchronization, and a running flag.
    - The `start` method starts the ping and pong threads and runs them for the specified duration.
    - The `stop` method stops the threads and ensures they are properly terminated.

4. **PingWorker and PongWorker Classes**:
    - The `PingWorker` and `PongWorker` classes implement the `Runnable` interface and define the behavior of the ping and pong threads, respectively.
    - Each worker thread waits for its turn to print its message ("ping" or "pong") and then signals the other thread to proceed.

5. **Synchronization**:
    - The synchronization between the ping and pong threads is achieved using a `ReentrantLock` and two `Condition` objects (`pingCondition` and `pongCondition`). The threads use these conditions to wait for their turn and signal each other.

6. **Execution Flow**:
    - The `PingPong` instance is created with an initial state indicating whether to start with "ping" or "pong".
    - The `start` method sets the running flag to true and starts both threads.
    - Each thread alternates printing "ping" and "pong" by acquiring the lock, checking the turn, printing the message, and signaling the other thread.
    - After the specified duration, the `stop` method sets the running flag to false, signals both conditions to wake up any waiting threads, and waits for both threads to terminate.

This implementation ensures that the "ping" and "pong" messages are printed alternately and the program stops gracefully after the specified duration.

## Compile, Run and Test

### Compile

To compile the project, navigate to the root directory of the module and run the following command:

```sh
mvn clean compile
```

### Run

To run the project, use the following command:

```sh
mvn exec:java -Dexec.mainClass="com.example.exerciseone.Main"
```

### Test

To run the tests, use the following command:

```sh
mvn test
```
