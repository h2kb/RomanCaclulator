import java.util.Stack;
import java.util.regex.Pattern;

public class RomanCalculator {

    public static class StringConstants {
        static final String HEADLINE = "Please enter a roman expression to calculate: ";
    }

    // Translation of the expression to Reverse Polish Notation
    static String toRPN(String expression) {
        StringBuilder builder = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);
            int priority = getPriority(ch);

            /*
             * priority 0 are any numbers
             * priority 1 is a opened bracket
             * priority 2 or 3 are operators
             * priority -1 is a closed bracket
             */

            if (priority == 0) {
                builder.append(ch);
            } else if (priority == 1) {
                stack.push(ch);
            } else if (priority > 1) {
                builder.append(" ");

                while (!stack.isEmpty()) {
                    if (getPriority(stack.peek()) >= priority) {
                        builder.append(stack.pop());
                        builder.append(" ");
                    } else {
                        break;
                    }
                }
                stack.push(ch);
            } else if (priority == -1) {
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

    // Get a priority of symbols or numbers
    static int getPriority(char ch) {
        switch (ch) {
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

    // Check for the number
    static boolean isNumber(String strNum) {
        if (strNum == null) {
            return false;
        }

        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");

        return pattern.matcher(strNum).matches();
    }

    // Add spaces to separate characters
    static String prepareRawString(String str) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (ch == ' ') {
                continue;
            }

            if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                builder.append(" ").append(ch).append(" ");
            } else if (ch == '(') {
                builder.append(ch).append(" ");
            } else if (ch == ')') {
                builder.append(" ").append(ch);
            } else {
                builder.append(ch);
            }
        }

        return builder.toString();
    }

    // Replace roman numeral with arabic numeral
    static String replaceRoman(String str) {
        StringBuilder builder = new StringBuilder();
        String[] arrRawData = str.split(" ");

        for (String data : arrRawData) {
            if (data.equals("+") || data.equals("-") || data.equals("*") || data.equals("/") || data.equals("(") || data.equals(")")) {
                builder.append(data);
            } else {
                builder.append(new RomanNumeral(data).toInt());
            }
        }

        return builder.toString();
    }

    public static void main(String[] args) {
//        System.out.println(StringConstants.HEADLINE);
//        Scanner in = new Scanner(System.in);
//        String rawData = in.nextLine();
//        in.close();
//
//        String rpn = toRPN(prepareExpression(rawData));
//        RomanNumeral result = new RomanNumeral(calculateResult(rpn));
//        System.out.println(result1.toString());

        System.out.print("Test 1 is: ");
        String rpn1 = toRPN(replaceRoman(prepareRawString("(V+V)*II")));
        RomanNumeral result1 = new RomanNumeral(calculateResult(rpn1));
        System.out.println(result1.toString().equals("XX"));

        System.out.print("Test 2 is: ");
        String rpn2 = toRPN(replaceRoman(prepareRawString("(V+V)*(II-I)")));
        RomanNumeral result2 = new RomanNumeral(calculateResult(rpn2));
        System.out.println(result2.toString().equals("X"));

        System.out.print("Test 3 is: ");
        String rpn3 = toRPN(replaceRoman(prepareRawString("(XL + II)*(X - III) / III")));
        RomanNumeral result3 = new RomanNumeral(calculateResult(rpn3));
        System.out.println(result3.toString().equals("XCVIII"));
    }
}
