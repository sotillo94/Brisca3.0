/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.brisca.moviles.uva.brisca30.Conect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Clase encargada de llevar la logica del chat
 */
public class ChatConnection {


    private Handler mUpdateHandler;
    private ChatServer mChatServer;
    private ChatClient mChatClient;
    public static String MyIP;
    public static  String TAG = "ChatConnection";

    private Socket mSocket;
    //Elegimos -1 para que sea el so el que decida el puerto
    private int mPort = -1;

    /**
     * Se encarga de controlar el chat de la conexion
     * @param handler controlador de la conexion
     */
    public ChatConnection(Handler handler) {
        mUpdateHandler = handler;
        mChatServer = new ChatServer(handler);
    }

    /**
     * Acaba con la conexion
     */
    public void tearDown() {
        mChatServer.tearDown();
        mChatClient.tearDown();
    }

    /**
     * Se conecta a un servidor dados una IP y un puerto
     * @param address direccion IP del servidor
     * @param port puerto del servidor
     */
    public void connectToServer(InetAddress address, int port) {
        mChatClient = new ChatClient(address, port);
    }

    /**
     * Envia el mensaje
     * @param msg mensaje aenviar
     */
    public void sendMessage(String msg) {
        if (mChatClient != null) {
            mChatClient.sendMessage(msg);
        }
    }

    /**
     * Obtiene el puerto local
     * @return devuelve el puerto local
     */
    public int getLocalPort() {
        return mPort;
    }

    /**
     * Actualiza el puerto local
     * @param port puerto al que deseamos cambiar
     */
    public void setLocalPort(int port) {
        mPort = port;
    }

    /**
     * Diferencia los mensajes enviados localmente de los externos.
     * Envia mensajes
     * @param msg mensaje a enviar
     * @param local decide si el mensaje es local o externo
     * @param mAddress mi direccion IP
     */
    public synchronized void updateMessages(String msg, boolean local, InetAddress mAddress) {
        Log.e(TAG, "Updating message: " + msg);
        MyIP=mAddress.toString();
        if (local) {
            msg = "me: "+ msg;
        } else {
            msg = "them: "+ msg;
        }

        Bundle messageBundle = new Bundle();
        messageBundle.putString("msg", msg);

        Message message = new Message();
        message.setData(messageBundle);
        //Enviamos el mensaje
        mUpdateHandler.sendMessage(message);

    }

    /**
     * inicia la conexion con un socket(IP+puerto+protocolo)
     * @param socket socket con el cual queremos iniciar la conexion
     */
    private synchronized void setSocket(Socket socket) {
        Log.d(TAG, "setSocket being called.");
        if (socket == null) {
            Log.d(TAG, "Setting a null socket.");
        }
        if (mSocket != null) {
            if (mSocket.isConnected()) {
                try {
                    mSocket.close();
                } catch (IOException e) {
                    // TODO(alexlucas): Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        mSocket = socket;
    }

    /**
     * Obtener socket actual
     * @return socket actual
     */
    private Socket getSocket() {
        return mSocket;
    }

    /**
     * Se encarga de la logica del servidor del chat
     */
    private class ChatServer {
        ServerSocket mServerSocket = null;
        Thread mThread = null;

        public ChatServer(Handler handler) {
            mThread = new Thread(new ServerThread());
            mThread.start();
        }

        public void tearDown() {
            mThread.interrupt();
            try {
                mServerSocket.close();
            } catch (IOException ioe) {
                Log.e(TAG, "Error when closing server socket.");
            }
        }

        class ServerThread implements Runnable {

            @Override
            public void run() {

                try {
                    // Since discovery will happen via Nsd, we don't need to care which port is
                    // used.  Just grab an available one  and advertise it via Nsd.
                    mServerSocket = new ServerSocket(0);
                    setLocalPort(mServerSocket.getLocalPort());
                    
                    while (!Thread.currentThread().isInterrupted()) {
                        Log.d(TAG, "ServerSocket Created, awaiting connection");
                        setSocket(mServerSocket.accept());
                        Log.d(TAG, "Connected.");
                        if (mChatClient == null) {
                            int port = mSocket.getPort();
                            InetAddress address = mSocket.getInetAddress();
                            connectToServer(address, port);
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error creating ServerSocket: ", e);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Se encarga de la logica del cliente en el chat
     */
    private class ChatClient {

        private InetAddress mAddress;
        private int PORT;

        private final String CLIENT_TAG = "ChatClient";

        private Thread mSendThread;
        private Thread mRecThread;

        public ChatClient(InetAddress address, int port) {

            Log.d(CLIENT_TAG, "Creating chatClient");
            this.mAddress = address;
            this.PORT = port;

            mSendThread = new Thread(new SendingThread());
            mSendThread.start();
        }

        class SendingThread implements Runnable {

            BlockingQueue<String> mMessageQueue;
            private int QUEUE_CAPACITY = 10;

            public SendingThread() {
                mMessageQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
            }

            @Override
            public void run() {
                try {
                    if (getSocket() == null) {
                        setSocket(new Socket(mAddress, PORT));
                        Log.d(CLIENT_TAG, "Client-side socket initialized.");

                    } else {
                        Log.d(CLIENT_TAG, "Socket already initialized. skipping!");
                    }

                    mRecThread = new Thread(new ReceivingThread());
                    mRecThread.start();



                } catch (UnknownHostException e) {
                    Log.d(CLIENT_TAG, "Initializing socket failed, UHE", e);
                } catch (IOException e) {
                    Log.d(CLIENT_TAG, "Initializing socket failed, IOE.", e);
                }

                while (true) {
                    try {
                        String msg = mMessageQueue.take();
                        sendMessage(msg);
                    } catch (InterruptedException ie) {
                        Log.d(CLIENT_TAG, "Message sending loop interrupted, exiting");
                    }
                }
            }
        }

        class ReceivingThread implements Runnable {

            @Override
            public void run() {

                BufferedReader input;
                try {
                    input = new BufferedReader(new InputStreamReader(
                            mSocket.getInputStream()));
                    while (!Thread.currentThread().isInterrupted()) {

                        String messageStr;
                        messageStr = input.readLine();
                        if (messageStr != null) {
                            Log.d(CLIENT_TAG, "Read from the stream: " + messageStr);
                            updateMessages(messageStr, false, mAddress);
                        } else {
                            Log.d(CLIENT_TAG, "The nulls! The nulls!");
                            break;
                        }
                    }
                    input.close();

                } catch (IOException e) {
                    Log.e(CLIENT_TAG, "Server loop error: ", e);
                }
            }
        }

        public void tearDown() {
            try {
                getSocket().close();
            } catch (IOException ioe) {
                Log.e(CLIENT_TAG, "Error when closing server socket.");
            }
        }

        public void sendMessage(String msg) {
            try {
                Socket socket = getSocket();
                if (socket == null) {
                    Log.d(CLIENT_TAG, "Socket is null, wtf?");
                } else if (socket.getOutputStream() == null) {
                    Log.d(CLIENT_TAG, "Socket output stream is null, wtf?");
                }

                PrintWriter out = new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(getSocket().getOutputStream())), true);
                out.println(msg);
                out.flush();
                updateMessages(msg, true,mAddress);
            } catch (UnknownHostException e) {
                Log.d(CLIENT_TAG, "Unknown Host", e);
            } catch (IOException e) {
                Log.d(CLIENT_TAG, "I/O Exception", e);
            } catch (Exception e) {
                Log.d(CLIENT_TAG, "Error3", e);
            }
            Log.d(CLIENT_TAG, "Client sent message: " + msg);
        }
    }
}
