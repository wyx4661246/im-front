package org.marzaha.im.remoting.protocol;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RemotingCommand {

    private static AtomicInteger requestId = new AtomicInteger(0);
    private static SerializeType serializeType = SerializeType.JSON;

    private int code;
    private int version = 0;
    private int opaque = requestId.getAndIncrement();
    private int flag = 0;
    private String remark;

    private transient byte[] body;

    public static RemotingCommand decode(final ByteBuffer byteBuffer) {
        int length = byteBuffer.limit();
        int oriHeaderLen = byteBuffer.getInt();
        int headerLength = getHeaderLength(oriHeaderLen);

        byte[] headerData = new byte[headerLength];
        byteBuffer.get(headerData);

        RemotingCommand cmd = headerDecode(headerData, getProtocolType(oriHeaderLen));

        int bodyLength = length - 4 - headerLength;
        byte[] bodyData = null;
        if (bodyLength > 0) {
            bodyData = new byte[bodyLength];
            byteBuffer.get(bodyData);
        }
        cmd.body = bodyData;

        return cmd;
    }

    public <T> T decodeBody(Class<T> classOfT) {
        switch (serializeType) {
            case JSON:
                T resultJson = RemotingJsonSerializable.decode(this.body, classOfT);
                return resultJson;
//            case APP:
//                RemotingCommand resultRMQ = RocketMQSerializable.rocketMQProtocolDecode(headerData);
//                resultRMQ.setSerializeTypeCurrentRPC(type);
//                return resultRMQ;
            default:
                break;
        }

        return null;
    }

    public static SerializeType getProtocolType(int source) {
        return SerializeType.valueOf((byte) ((source >> 24) & 0xFF));
    }

    public static int getHeaderLength(int length) {
        return length & 0xFFFFFF;
    }

    private static RemotingCommand headerDecode(byte[] headerData, SerializeType type) {
        switch (type) {
            case JSON:
                RemotingCommand resultJson = RemotingJsonSerializable.decode(headerData, RemotingCommand.class);
                return resultJson;
//            case APP:
//                RemotingCommand resultRMQ = RocketMQSerializable.rocketMQProtocolDecode(headerData);
//                resultRMQ.setSerializeTypeCurrentRPC(type);
//                return resultRMQ;
            default:
                break;
        }

        return null;
    }

    private byte[] headerEncode(SerializeType type) {
        switch (type) {
            case JSON:
                return RemotingJsonSerializable.encode(this);
//            case APP:
//                RemotingCommand resultRMQ = RocketMQSerializable.rocketMQProtocolDecode(headerData);
//                resultRMQ.setSerializeTypeCurrentRPC(type);
//                return resultRMQ;
            default:
                RemotingJsonSerializable.encode(this);
                break;
        }

        return null;
    }


    public ByteBuffer encodeHeader() {
        return encodeHeader(this.body != null ? this.body.length : 0);
    }

    public ByteBuffer encodeHeader(final int bodyLength) {
        // 1> header length size
        int length = 4;

        // 2> header data length
        byte[] headerData;
        headerData = this.headerEncode(serializeType);

        length += headerData.length;

        // 3> body data length
        length += bodyLength;

        ByteBuffer result = ByteBuffer.allocate(4 + length - bodyLength);

        // length
        result.putInt(length);

        // header length
        result.put(markProtocolType(headerData.length, serializeType));

        // header data
        result.put(headerData);

        result.flip();

        return result;
    }

    public static byte[] markProtocolType(int source, SerializeType type) {
        byte[] result = new byte[4];

        result[0] = type.getCode();
        result[1] = (byte) ((source >> 16) & 0xFF);
        result[2] = (byte) ((source >> 8) & 0xFF);
        result[3] = (byte) (source & 0xFF);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
