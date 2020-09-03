package org.marzaha.im.front.processor;

import io.netty.channel.ChannelHandlerContext;
import org.marzaha.im.front.entity.AppMessage;
import org.marzaha.im.remoting.netty.NettyRequestProcessor;
import org.marzaha.im.remoting.protocol.RemotingCommand;

public class UserLoProcessor implements NettyRequestProcessor {


    @Override
    public RemotingCommand processRequest(ChannelHandlerContext ctx, RemotingCommand request) throws Exception {
        AppMessage appMessage = request.decodeBody(AppMessage.class);
        //校验
        //发送离线数据
        return null;
    }
}
