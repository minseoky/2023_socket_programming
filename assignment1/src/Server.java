import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2023);
        System.out.println("서버가 시작되었습니다.");

        Socket clientSocket = serverSocket.accept();
        System.out.println("클라이언트와 연결되었습니다.");
        //문자열로 받기 위해 BufferedReader
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //문자열 보내기 위해 PrintWriter
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String message = in.readLine();
        System.out.println("클라이언트로부터 받은 메시지: " + message);

        StringBuilder asciiString = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            int ascii = (int) message.charAt(i);
            asciiString.append(ascii);
            asciiString.append(" ");
        }
        out.println(asciiString);
        System.out.println("클라이언트로 보낸 메시지(아스키 코드): " + asciiString);


        clientSocket.close();
        serverSocket.close();
    }
}