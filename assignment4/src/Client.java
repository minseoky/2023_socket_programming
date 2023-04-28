import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
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
            System.out.print("서버로 보낼 수식 : ");
            String message = input.next();
            out.println(message);
            System.out.println("서버로부터 받은 메세지: ");
            while((message = in.readLine()) != null){
                String response = message;
                System.out.println(response);
            }




            socket.close();
        }catch (IOException e) {
            System.out.println("연결 실패");
            e.printStackTrace();
        }
    }
}