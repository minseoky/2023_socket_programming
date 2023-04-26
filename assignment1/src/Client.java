import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        int portNumber = 2023;
        String serverIP="127.0.0.1"; //localhost
        try{
            Socket socket = new Socket(serverIP, portNumber);
            System.out.println("서버에 연결되었습니다.");

            //문자열로 받기 위해 BufferedReader
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //문자열 보내기 위해 PrintWriter
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner input = new Scanner(System.in);
            System.out.print("서버로 보낼 메세지 : ");
            String message = input.next();
            out.println(message);
            System.out.println("서버로 보낸 메시지: " + message);

            String response = in.readLine();
            System.out.println("서버로부터 받은 메시지(아스키 코드): " + response);

            socket.close();
        }catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("연결 실패");
            e.printStackTrace();
        }
    }
}