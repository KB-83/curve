package teSt;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class serVer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8080); // Choose an appropriate port number

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Create an instance of ObjectMapper from Jackson
                ObjectMapper objectMapper = new ObjectMapper();

                // Create an object representing the data to be sent
                Person person = new Person("John", 30, "New York");

                // Convert the object to JSON string
                String json = objectMapper.writeValueAsString(person);

                // Convert the JSON string to bytes
                byte[] jsonBytes = json.getBytes();

                // Get the output stream of the client socket
                OutputStream outputStream = clientSocket.getOutputStream();

                // Send the JSON bytes to the client
                outputStream.write(jsonBytes);
                outputStream.flush();
                outputStream.close();

                System.out.println("Data sent to client.");
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class Person {
        private String name;
        private int age;
        private String city;

        public Person() {
        }

        public Person(String name, int age, String city) {
            this.name = name;
            this.age = age;
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
        // Getters and setters
    }
}
