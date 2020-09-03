package org.marzaha.im.remoting.netty;

import io.netty.channel.ChannelHandlerContext;
import org.marzaha.im.remoting.protocol.RemotingCommand;

public interface NettyRequestProcessor {

    RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request)
            throws Exception;
}
