package org.marzaha.im.front.control;

import org.marzaha.im.front.processor.UserMessageProcessor;
import org.marzaha.im.remoting.common.protocol.RequestCode;
import org.marzaha.im.remoting.netty.NettyRemotingServer;
import org.marzaha.im.remoting.netty.NettyServerConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrontAppControl {

    private NettyServerConfig nettyServerConfig;
    private NettyRemotingServer remotingServer;
    private ExecutorService sendMessageExecutor = Executors.newCachedThreadPool();

    public boolean initialize() {
        NettyServerConfig serverConfig = new NettyServerConfig();
        serverConfig.setListenPort(8989);
        this.nettyServerConfig = serverConfig;
        this.remotingServer = new NettyRemotingServer(this.nettyServerConfig);
        remotingServer.registerProcessor(RequestCode.USER_MESSAGE, new UserMessageProcessor(), sendMessageExecutor);
        return true;
    }

    public boolean start() {
        remotingServer.start();
        return true;
    }

}
