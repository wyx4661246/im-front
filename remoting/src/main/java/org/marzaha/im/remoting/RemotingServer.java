package org.marzaha.im.remoting;

import io.netty.channel.ChannelHandlerContext;
import org.marzaha.im.remoting.netty.NettyRequestProcessor;
import org.marzaha.im.remoting.protocol.RemotingCommand;

import java.util.concurrent.ExecutorService;

public interface RemotingServer extends RemotingService{

    void registerProcessor(final int requestCode, final NettyRequestProcessor processor,
                           final ExecutorService executor);

    void processRequestCommand(ChannelHandlerContext ctx, RemotingCommand msg);
}
