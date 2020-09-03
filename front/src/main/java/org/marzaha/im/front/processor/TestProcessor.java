package org.marzaha.im.front.processor;

import io.netty.channel.ChannelHandlerContext;
import org.marzaha.im.remoting.netty.NettyRequestProcessor;
import org.marzaha.im.remoting.protocol.RemotingCommand;

public class TestProcessor implements NettyRequestProcessor {

    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        return null;
    }
}
