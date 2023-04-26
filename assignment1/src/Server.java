import java.io.*;
import java.net.*;

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

        // 받은 메세지를 아스키코드화해서 StringBuilder asciiString에 넣기
        StringBuilder asciiString = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            int ascii = (int) message.charAt(i);
            asciiString.append(ascii);
            asciiString.append(" "); //코드 간 구분 가시화를 위한 공백
        }

        // 클라이언트로 아스키 코드 메시지 보내기
        out.println(asciiString.toString());
        System.out.println("클라이언트로 보낸 메시지(아스키 코드): " + asciiString.toString());

        // 소켓 및 서버 소켓 닫기
        clientSocket.close();
        serverSocket.close();
    }
}
