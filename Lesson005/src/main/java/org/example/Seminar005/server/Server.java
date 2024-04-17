package org.example.Seminar005.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class Server {
    public static final int PORT = 8181;

    public static void main(String[] args) {
        final Map<String, ClientHandler> clients = new HashMap<>();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущен");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Подключился новый клиент: " + clientSocket);

                    PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                    clientOut.println("Подключение успешно!");

                    Scanner clientIn =  new Scanner(clientSocket.getInputStream());
                    String clientId = clientIn.nextLine();
                    System.out.println("Идентификатор клиента " + clientSocket + ": " + clientId);

                    String allClients = clients.entrySet().stream()
                            .map(it -> "id = " + it.getKey() + ", client = " + it.getValue().getClientSocket())
                            .collect(Collectors.joining("\n"));

                    clientOut.println("Список доступных клиентов: \n" + allClients);

                    ClientHandler clientHandler = new ClientHandler(clientSocket, clientId, clients);
                    new Thread(clientHandler).start();

                    for (ClientHandler client : clients.values()) {
                        client.send("Подключился новый клиент: " + clientSocket + ", id = " + clientId);
                    }
                    clients.put(clientId,clientHandler);


                } catch (IOException e) {
                    System.err.println("Произошла ошибка " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class ClientHandler implements Runnable{
    private final Socket clientSocket;
    private final String clientId;
    private final PrintWriter out;
    private final Map <String, ClientHandler> clients;

    public ClientHandler(Socket clientSocket, String clientId, Map<String, ClientHandler> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.clientId = clientId;
        this.clients = clients;
    }
    public String getClientId() {
        return clientId;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(clientSocket.getInputStream())) {

            while (true) {
                if (clientSocket.isClosed()) {
                    send("Клиент " + clientSocket + " отключился");
                    break;
                }
                String input = in.nextLine();
//                send("Получено сообщение от " + clientSocket + ": " + input);

                String toClientId = null;
                if(input.startsWith("@")) {
                    String[] parts = input.split("\\s+");
                    if (parts.length > 0) {
                        toClientId = parts[0].substring(1);
                    }
                }



//                out.println("Сообщение от "+ this.clientId + " :" + input);
                if (input.charAt(0) == '/') {
                    if(input.equals("/exit")) {
                        for (ClientHandler client : clients.values()) {
                            client.send("Клиент " + this.clientId + " отключился");
                        }
                        clients.remove(this.clientId);
                        break;
                    } else if (input.equals("/all")) {
                        String allClients = clients.entrySet().stream()
                                .map(it -> "id = " + it.getKey() + ", client = " + it.getValue().getClientSocket())
                                .collect(Collectors.joining("\n"));

                        send("Список доступных клиентов: \n" + allClients);

                    } else {
                        send("Команда не найдена");
                    }
                } else {
                    if (toClientId == null) {
                        clients.values().forEach(it -> it.send("Сообщение от " + this.clientId + ": " + input));
                    } else {
                        ClientHandler toClient = clients.get(toClientId);
                        if (toClient !=  null) {
                            toClient.send("Персональное сообщение от " + this.clientId + ": " + input.replace("@" + toClientId + " ", ""));
                        } else {
                            System.err.println("Не найден клиент с идентификатором: " + toClientId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Произошла ошибка клиента " + clientSocket + ": " + e.getMessage());
        }
        // FIXME: При отключенни удалять из мапы и оповещать остальных
        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Ошибка при отключении " + e.getMessage());
        }
    }
    public void send(String msg) {
        out.println(msg);
    }

}
