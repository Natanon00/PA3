//672115010 Natanon Somboon

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertInfixToPostfix {
    static class Node {
        String data;
        Node next;

        Node(String data) {
            this.data = data;
            this.next = null;
        }
    }

    // Stack class implemented with a linked list; works with String tokens.
    static class Stack {
        Node top;

        public Stack() {
            top = null;
        }

        public void push(String s) {
            Node newNode = new Node(s);
            newNode.next = top;
            top = newNode;
        }

        public String pop() {
            if (isEmpty()) {
                return null;
            }
            String data = top.data;
            top = top.next;
            return data;
        }

        public String peek() {
            if (isEmpty()) {
                return null;
            }
            return top.data;
        }

        public boolean isEmpty() {
            return top == null;
        }
    }

    // Returns true if the token is one of the operators.
    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") ||
                token.equals("/") || token.equals("^") || token.equals("==") ||
                token.equals("!=") || token.equals(">") || token.equals("<") ||
                token.equals("&&");
    }

    // Returns the precedence of the operator.
    public static int precedence(String token) {
        return switch (token) {
            case "^" -> 3;
            case "*", "/" -> 2;
            case "+", "-" -> 1;
            case "==", "!=", ">", "<" -> 0;
            case "&&" -> -1;
            default -> -1;
        };
    }

    public static List<String> tokenize(String exp) {
        List<String> tokens = new ArrayList<>();
        Pattern tokenPattern = Pattern.compile("==|!=|&&|[><]|\\d+|[A-Za-z]+|[+\\-*/^()]");
        Matcher matcher = tokenPattern.matcher(exp);
        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }


    public static boolean isValidInfix(String exp) {
        List<String> tokens = tokenize(exp);
        Stack parenStack = new Stack();
        boolean prevIsOperator = false;

        for (int i = 0; i < tokens.size(); i++) {
            String token = tokens.get(i);
            if (token.equals("(")) {
                parenStack.push(token);
                prevIsOperator = false;
            } else if (token.equals(")")) {
                if (parenStack.isEmpty()) {
                    return false;
                }
                parenStack.pop();
                prevIsOperator = false;
            } else if (isOperator(token)) {
                if (i == 0) {
                    return false;
                }
                if (prevIsOperator) {
                    return false;
                }
                prevIsOperator = true;
            } else {
                prevIsOperator = false;
            }
        }
        if (!parenStack.isEmpty()) {
            return false;
        }
        return !prevIsOperator;
    }

    public static String infixToPostfix(String exp) {
        List<String> tokens = tokenize(exp);
        StringBuilder postfix = new StringBuilder();
        Stack stack = new Stack();

        for (String token : tokens) {
            if (Character.isLetterOrDigit(token.charAt(0))) {
                postfix.append(token).append(" ");
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    postfix.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek().equals("(")) {
                    stack.pop();
                }
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && isOperator(stack.peek())) {
                    if ((precedence(token) < precedence(stack.peek())) ||
                            (precedence(token) == precedence(stack.peek()) && !token.equals("^"))) {
                        postfix.append(stack.pop()).append(" ");
                    } else {
                        break;
                    }
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            String topToken = stack.pop();
            if (topToken.equals("(") || topToken.equals(")")) {
                continue;
            }
            postfix.append(topToken).append(" ");
        }

        return postfix.toString().trim();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java InfixToPostfix <inputfile>");
            return;
        }

        String filename = args[0];

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            int exprCount = 1;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                System.out.println("Expression " + exprCount + ":");
                System.out.println("Infix exp: " + line);

                if (isValidInfix(line)) {
                    System.out.println("Valid");
                    String postfix = infixToPostfix(line);
                    System.out.println("Postfix exp: " + postfix);
                } else {
                    System.out.println("Not-Valid");
                }
                System.out.println();
                exprCount++;
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
}
