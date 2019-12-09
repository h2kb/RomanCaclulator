import java.util.Scanner;
import java.util.Stack;

public class RomanCalculator {

    public static class StringConstants {
        static final String HEADLINE = "Please enter a roman expression to calculate: ";
    }

    // Translation of the expression to Reverse Polish Notation
    static String toRPN(String expression) {
        StringBuilder builder = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            //We are working with a+b and a + b
            if (expression.charAt(i) == ' ') {
                continue;
            }

            int priority = getPriority(expression.charAt(i));

            //priority 0 are any numbers
            if (priority == 0) {
                builder.append(expression.charAt(i));
            }

            //priority 1 is a opened bracket
            if (priority == 1) {
                stack.push(expression.charAt(i));
            }

            //priority 2 or 3 are operators
            if (priority > 1) {
                builder.append(" ");

                while (!stack.isEmpty()) {
                    if (getPriority(stack.peek()) >= priority) {
                        builder.append(stack.pop());
                        builder.append(" ");
                    } else {
                        break;
                    }
                }
                stack.push(expression.charAt(i));
            }

            //priority -1 is a closed bracket
            if (priority == -1) {
                builder.append(" ");

                while (getPriority(stack.peek()) != 1) {
                    builder.append(stack.pop());
                }
                stack.pop();
            }
        }

        while (!stack.isEmpty()) {
            builder.append(" ");
            builder.append(stack.pop());
        }

        return builder.toString();
    }

    // Translation of RPN to the answer
    static int calculateResult(String expressionRNP) {
        String[] tempStrings = expressionRNP.split(" ");
        Stack<Integer> stack = new Stack<>();

        for (String tempString : tempStrings) {
            if (isNumber(tempString)) {
                stack.push(Integer.parseInt(tempString));
            } else {
                int num1 = stack.pop();
                int num2 = stack.pop();

                switch (tempString) {
                    case "+":
                        stack.push(num2 + num1);
                        break;
                    case "-":
                        stack.push(num2 - num1);
                        break;
                    case "*":
                        stack.push(num2 * num1);
                        break;
                    case "/":
                        stack.push(num2 / num1);
                        break;
                }
            }
        }

        return stack.pop();
    }

    //Get a priority of symbols or numbers
    static int getPriority(char character) {
        switch (character) {
            case '*':
            case '/':
                return 3;
            case '+':
            case '-':
                return 2;
            case '(':
                return 1;
            case ')':
                return -1;
            default:
                return 0;
        }
    }

    static boolean isNumber(String numAsString) {
        try {
            Integer.parseInt(numAsString);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    static String prepareExpression(String exp) {
        StringBuilder builder = new StringBuilder();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < exp.length(); i++) {
            char ch = exp.charAt(i);

            if (ch == ' ') {
                continue;
            }

            switch (ch) {
                case '+':
                case '-':
                case '*':
                case '/':
                    builder.append(" ").append(ch).append(" ");
                    break;
                case '(':
                    builder.append(ch).append(" ");
                    break;
                case ')':
                    builder.append(" ").append(ch);
                    break;
                default:
                    builder.append(ch);
                    break;
            }
        }

        String[] arrRawData = builder.toString().split(" ");

        for (String str : arrRawData) {
            switch (str) {
                case "+":
                case "-":
                case "*":
                case "/":
                    result.append(" ").append(str).append(" ");
                    break;
                case "(":
                    result.append(str).append(" ");
                    break;
                case ")":
                    result.append(" ").append(str);
                    break;
                default:
                    result.append(new RomanNumeral(str).toInt());
                    break;
            }
        }

        return result.toString();
    }

    public static void main(String[] args) {
        System.out.println(StringConstants.HEADLINE);
        Scanner in = new Scanner(System.in);
        String rawData = in.nextLine();
        in.close();

        String rpn = toRPN(prepareExpression(rawData));
        RomanNumeral result = new RomanNumeral(calculateResult(rpn));
        System.out.println(result.toString());
    }
}
