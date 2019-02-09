// Rcon Manager for Android
// Copyright (c) 2019. Kenvix <i@kenvix.com>
//
// Licensed under GNU Affero General Public License v3.0

package com.kenvix.rconmanager.rcon.protocol;

import com.kenvix.rconmanager.rcon.exception.ConnectionException;
import com.kenvix.rconmanager.rcon.exception.IllegalAuthorizationException;
import com.kenvix.rconmanager.rcon.exception.MalformedPacketException;
import com.kenvix.rconmanager.rcon.meta.RconCommandResult;
import com.kenvix.rconmanager.rcon.meta.RconServer;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RconConnect {
    private final Charset charset = Charset.forName("UTF-8");
    private RconServer rconServer;
    private List<RconCommandResult> commandResults;
    private Status status = Status.Ready;
    private Socket socket = null;
    private int requestId = 0;

    public List<RconCommandResult> getCommandResults() {
        return commandResults;
    }

    public Status getStatus() {
        return status;
    }


    public enum Status { Ready, Connected, Working, Disconnected }

    public RconConnect(RconServer rconServer) {
        this.rconServer = rconServer;
        commandResults = new ArrayList<>();
    }

    /**
     * Connect to a rcon server
     *
     * @throws ConnectionException Wrapped IOE
     * @throws IllegalAuthorizationException MPE
     */
    public void connect() {
        try {
            this.requestId = this.hashCode();
            this.socket = new Socket(rconServer.getHost(), rconServer.getPort());

            RconPacket res = send(RconPacket.SERVERDATA_AUTH, rconServer.getPassword().getBytes());

            if(res.getRequestId() == -1) {
                throw new IllegalAuthorizationException();
            }

            setStatus(Status.Connected);
        } catch (IOException ex) {
            throw new ConnectionException(ex);
        }
    }

    /**
     * Send a Rcon packet and fetch the response
     *
     * @param type The packet type
     * @param payload The payload (password, command, etc.)
     * @return A RconPacket object containing the response
     *
     * @throws IOException IOE
     * @throws MalformedPacketException MPE
     */
    private RconPacket send(int type, byte[] payload) throws IOException {
        try {
            write(socket.getOutputStream(), requestId, type, payload);
        } catch(SocketException se) {
            // Close the socket if something happens
            disconnect();

            // Rethrow the exception
            throw se;
        }

        return read(socket.getInputStream());
    }

    /**
     * Write a rcon packet on an outputstream
     *
     * @param out The OutputStream to write on
     * @param requestId The request id
     * @param type The packet type
     * @param payload The payload
     *
     * @throws ConnectionException Wrapped IOE
     */
    private static void write(OutputStream out, int requestId, int type, byte[] payload) {
        try {
            int bodyLength = getBodyLength(payload.length);
            int packetLength = getPacketLength(bodyLength);

            ByteBuffer buffer = ByteBuffer.allocate(packetLength);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            buffer.putInt(bodyLength);
            buffer.putInt(requestId);
            buffer.putInt(type);
            buffer.put(payload);

            // Null bytes terminators
            buffer.put((byte)0);
            buffer.put((byte)0);

            // Woosh!
            out.write(buffer.array());
            out.flush();
        } catch (IOException ex) {
            throw new ConnectionException(ex);
        }
    }

    /**
     * Read an incoming rcon packet
     *
     * @param in The InputStream to read on
     * @return The read RconPacket
     *
     * ConnectionException Wrapped IOE
     * @throws ConnectionException Wrapped IOE
     * @throws MalformedPacketException MPE
     */
    private static RconPacket read(InputStream in) {
        try {
            // Header is 3 4-bytes ints
            byte[] header = new byte[4 * 3];

            // Read the 3 ints
            if(in.read(header) < 1)
                throw new MalformedPacketException("Wrong packet received");

            // Use a bytebuffer in little endian to read the first 3 ints
            ByteBuffer buffer = ByteBuffer.wrap(header);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            int length = buffer.getInt();
            int requestId = buffer.getInt();
            int type = buffer.getInt();

            // Payload size can be computed now that we have its length
            byte[] payload = new byte[length - 4 - 4 - 2];

            DataInputStream dis = new DataInputStream(in);

            // Read the full payload
            dis.readFully(payload);

            // Read the null bytes
            if(dis.read(new byte[2]) <= 0)
                throw new MalformedPacketException("Wrong packet received");

            return new RconPacket(requestId, type, payload);
        } catch (BufferUnderflowException | EOFException e) {
            throw new MalformedPacketException("Cannot read the whole packet");
        } catch (IOException ex) {
            throw new ConnectionException(ex);
        }
    }

    private static int getPacketLength(int bodyLength) {
        // 4 bytes for length + x bytes for body length
        return 4 + bodyLength;
    }

    private static int getBodyLength(int payloadLength) {
        // 4 bytes for requestId, 4 bytes for type, x bytes for payload, 2 bytes for two null bytes
        return 4 + 4 + payloadLength + 2;
    }


    public void disconnect() {
        try {
            socket.close();
            setStatus(Status.Disconnected);
        } catch (IOException ex) {
            throw new ConnectionException(ex);
        }
    }

    /**
     * Send a command to the server
     *
     * @param payload The command to send
     *
     * @throws ConnectionException Wrapped IOE
     */
    public RconCommandResult command(String payload) {
        try {
            if(payload == null || payload.trim().isEmpty()) {
                throw new IllegalArgumentException("Payload can't be null or empty");
            }

            setStatus(Status.Working);
            RconPacket response = send(RconPacket.SERVERDATA_EXECCOMMAND, payload.getBytes());
            setStatus(Status.Connected);

            RconCommandResult result = new RconCommandResult(payload, new String(response.getPayload(), charset));
            commandResults.add(result);
            return result;
        } catch (IOException ex) {
            throw new ConnectionException(ex);
        }
    }

    @Override
    public int hashCode() {
        return 0xFA02 + rconServer.hashCode() ^ commandResults.hashCode();
    }

    private void setStatus(Status status) {
        this.status = status;
    }

    public RconServer getRconServer() {
        return rconServer;
    }
}
