import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2023);
        System.out.println("서버가 시작되었습니다.");

        Socket clientSocket = serverSocket.accept();
        System.out.println("클라이언트와 연결되었습니다.");
        // 문자열로 받기 위해 BufferedReader
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        // 문자열 보내기 위해 PrintWriter
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

        String input = in.readLine();
        String[] elements = input.split(":");
        String type = elements[0];

        System.out.println("클라이언트로부터 받은 내용: " + input);
        // READ일때
        if(type.equals("R") && elements.length == 2){
            String fileName = elements[1];
            try {
                File file = new File("./files/" + fileName);
                BufferedReader br = new BufferedReader(new FileReader(file));

                StringBuilder fileContent = new StringBuilder();

                String line;
                while ((line = br.readLine()) != null) {
                    fileContent.append(line);
                    if(br.ready()) {
                        fileContent.append(System.lineSeparator());
                    }
                }

                out.println(fileContent);
                System.out.println("클라이언트로 보낸 파일 내용: " + fileContent);
            } catch (FileNotFoundException e) {
                System.err.println("파일을 찾을 수 없습니다.");
                out.println("해당 파일을 찾을 수 없습니다.");
            } catch (IOException e) {
                System.err.println("파일을 읽을 수 없습니다.");
                out.println("해당 파일을 읽을 수 없습니다.");
            }
        }
        // WRITE일때
        else if(type.equals("W") && elements.length == 3){
            String fileName = elements[1];
            String append = elements[2];
            try {
                File file = new File("./files/" + fileName);
                BufferedWriter writer = new BufferedWriter(new FileWriter(file,true));

                writer.append("\n");
                writer.append(append);

                writer.close(); // 파일 쓰기 종료

                out.println("문자열이 파일에 추가되었습니다.");

                BufferedReader br = new BufferedReader(new FileReader(file));
                StringBuilder fileContent = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    fileContent.append(line);
                    fileContent.append(System.lineSeparator()); // 각 줄 끝에 줄바꿈 문자 추가
                }

                out.println(fileContent); // StringBuilder 객체를 문자열로 변환하여 출력
                System.out.println("클라이언트로 보낸 파일 내용: " + fileContent);


            } catch (FileNotFoundException e) {
                System.err.println("파일을 찾을 수 없습니다.");
                out.println("해당 파일을 찾을 수 없습니다.");
            } catch (IOException e) {
                System.err.println("파일을 읽을 수 없습니다.");
                out.println("해당 파일을 읽을 수 없습니다.");
            }
        }
        // W,R 모두 아닐 때
        else{
            out.println("형식이 올바르지 않습니다.");
        }
        clientSocket.close();
        serverSocket.close();
    }
}
