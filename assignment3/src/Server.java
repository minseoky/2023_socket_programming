import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {
    public static void main(String[] args) throws IOException, SQLException {
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

        //메세지 파싱
        String[] token = message.split(":");
        if(token.length == 2){
            if(token[0].equals("N")){
                // 도메인을 ip주소로
                out.println(domain2Ip(token[1]));
            }
            else if(token[0].equals("R")){
                // ip주소를 도메인으로
                out.println(ip2Domain(token[1]));
            }
            else{
                // 입력 오류
                out.println("형식 오류");
            }
        }
        else if(token.length == 3 && token[0].equals("W")){
            // DB미보유시 추가
            int result = 0;
            if((result = ipDomainAdd(token[1], token[2]))==0){
                out.println("  ip   : " + token[1]);
                out.println("domain : " + token[2]);
                out.println("위 내용이 추가되었습니다.");
            }
            else if(result == 1){
                out.println("해당 ip가 이미 DB에 존재합니다.");
            }
            else if(result == 2){
                out.println("해당 domain이 이미 DB에 존재합니다.");
            }
        }
        else{
            // 입력 오류
            out.println("형식 오류");
        }


        // 소켓 및 서버 소켓 닫기
        clientSocket.close();
        serverSocket.close();
    }
    public static String domain2Ip(String domain) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String userName = "test_user";
        String password = "Choi304706.";
        String result = null;

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select NAME, IP from new_table");

        while(resultSet.next() == true){
            if(resultSet.getString("NAME").equals(domain)){
                result = resultSet.getString("IP");
                break;
            }
        }

        resultSet.close();
        statement.close();
        connection.close();

        return result;
    }

    public static String ip2Domain(String ip) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String userName = "test_user";
        String password = "Choi304706.";
        String result = null;

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select NAME, IP from new_table");

        while(resultSet.next() == true){
            if(resultSet.getString("IP").equals(ip)){
                result = resultSet.getString("NAME");
                break;
            }
        }

        resultSet.close();
        statement.close();
        connection.close();

        return result;
    }

    public static int ipDomainAdd(String ip, String domain) throws  SQLException {
        String url = "jdbc:mysql://localhost:3306/test_db";
        String userName = "test_user";
        String password = "Choi304706.";
        int code = 0; // no error

        Connection connection = DriverManager.getConnection(url, userName, password);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select NAME, IP from new_table");


        //중복 db 검사
        while(resultSet.next() == true){
            if(resultSet.getString("IP").equals(ip)){
                code = 1; // ip error
                break;
            }
            else if (resultSet.getString("NAME").equals(domain)) {
                code = 2; // domain error
                break;
            }
        }
        // DB없으면 추가
        if (code == 0) {
            statement.executeUpdate("INSERT INTO new_table (IP, NAME) VALUES ('" + ip + "', '" + domain + "')");
        }


        resultSet.close();
        statement.close();
        connection.close();
        return code;
    }


}
