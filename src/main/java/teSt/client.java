package teSt;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 8080); // Use the server's IP address and port number

            // Get the input stream of the socket
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Read the JSON response from the server
            String jsonResponse = reader.readLine();

            // Create an instance of ObjectMapper from Jackson
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert the JSON response to an instance of the Person class
            Person person = objectMapper.readValue(jsonResponse, Person.class);

            System.out.println("Received Person: " + person.getName() + ", " + person.getAge() + ", " + person.getCity());

            socket.close();
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
        // Constructors, getters, and setters
    }
}