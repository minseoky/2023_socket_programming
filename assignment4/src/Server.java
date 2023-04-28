import java.io.*;
import java.net.*;
import java.util.Stack;

public class Server {
    public static void main(String[] args) throws IOException {
        // 서버 소켓 생성 및 포트 번호 지정
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.setReuseAddress(true);
        serverSocket.bind(new InetSocketAddress("localhost", 2023));
        System.out.println("서버가 시작되었습니다.");

        // 클라이언트 연결 대기
        serverSocket.setSoTimeout(10000);
        System.out.println("클라이언트 연결 대기중...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("클라이언트와 연결되었습니다.");

        // 문자열로 받기 위해 BufferedReader
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        // 문자열 보내기 위해 PrintWriter
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        // 클라이언트로부터 메시지 받기
        String message = in.readLine();
        System.out.println("클라이언트로부터 받은 메시지: " + message);

        // 수식 계산
        String answer = calculate(message);
        out.println(answer);

        // 소켓 및 서버 소켓 닫기
        clientSocket.close();
        serverSocket.close();
    }
    public static String calculate(String equation) {
        Stack<Integer> numbers = new Stack<Integer>();
        Stack<Character> operators = new Stack<Character>();

        for (int i = 0; i < equation.length(); i++) {
            char ch = equation.charAt(i);

            if (Character.isDigit(ch)) {
                int num = 0;

                while (i < equation.length() && Character.isDigit(equation.charAt(i))) {
                    num = num * 10 + (equation.charAt(i) - '0');
                    i++;
                }

                i--;

                numbers.push(num);
            } else if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }

                operators.pop();
            } else if (ch == '+' || ch == '-' || ch == '*' || ch == '/') {
                while (!operators.empty() && hasPrecedence(ch, operators.peek())) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
                }

                operators.push(ch);
            }
        }

        while (!operators.empty()) {
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop().toString();
    }

    public static boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }

        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
            return false;
        }

        return true;
    }

    public static int applyOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }

                return a / b;
        }

        return 0;
    }

}
