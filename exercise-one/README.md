# Exercise One Prompt

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

## Compile and Run 

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
