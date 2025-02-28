# PA3
Usage

Compilation

To compile the program, use the following command:

javac ConvertInfixToPostfix.java

Execution

Run the program by providing an input file containing infix expressions:

java ConvertInfixToPostfix <inputfile>

Example:

java ConvertInfixToPostfix expressions.txt

Input File Format

The input file should contain one infix expression per line. Blank lines are ignored.
Example content of expressions.txt:

A + B * C
( A + B ) * C
A + B == C && D > E

Output Format

For each expression, the program prints:

The original infix expression.

Whether the expression is valid or not.

The corresponding postfix expression (if valid).
