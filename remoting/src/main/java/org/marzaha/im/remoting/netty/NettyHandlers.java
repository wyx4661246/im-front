package org.marzaha.im.remoting.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.marzaha.im.remoting.RemotingServer;
import org.marzaha.im.remoting.protocol.RemotingCommand;
import org.marzaha.im.remoting.utils.NettyUtil;


public class NettyHandlers {

    private final RemotingServer remotingServer;

    public NettyHandlers(RemotingServer remotingServer) {
        this.remotingServer = remotingServer;
    }

    @ChannelHandler.Sharable
    class NettyServerHandler extends SimpleChannelInboundHandler<RemotingCommand> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, RemotingCommand msg) throws Exception {
            remotingServer.processRequestCommand(ctx, msg);
        }
    }

    @ChannelHandler.Sharable
    class NettyConnectManageHandler extends ChannelDuplexHandler {
        @Override
        public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = NettyUtil.parseChannelRemoteAddr(ctx.channel());
//            log.info("NETTY SERVER PIPELINE: channelRegistered {}", remoteAddress);
            super.channelRegistered(ctx);
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = NettyUtil.parseChannelRemoteAddr(ctx.channel());
//            log.info("NETTY SERVER PIPELINE: channelUnregistered, the channel[{}]", remoteAddress);
            super.channelUnregistered(ctx);
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = NettyUtil.parseChannelRemoteAddr(ctx.channel());
//            log.info("NETTY SERVER PIPELINE: channelActive, the channel[{}]", remoteAddress);
            super.channelActive(ctx);

//            if (NettyRemotingServer.this.channelEventListener != null) {
//                NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.CONNECT, remoteAddress, ctx.channel()));
//            }
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            final String remoteAddress = NettyUtil.parseChannelRemoteAddr(ctx.channel());
//            log.info("NETTY SERVER PIPELINE: channelInactive, the channel[{}]", remoteAddress);
            super.channelInactive(ctx);

//            if (NettyRemotingServer.this.channelEventListener != null) {
//                NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.CLOSE, remoteAddress, ctx.channel()));
//            }
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state().equals(IdleState.ALL_IDLE)) {
                    final String remoteAddress = NettyUtil.parseChannelRemoteAddr(ctx.channel());
//                    log.warn("NETTY SERVER PIPELINE: IDLE exception [{}]", remoteAddress);
                    NettyUtil.closeChannel(ctx.channel());
//                    if (NettyRemotingServer.this.channelEventListener != null) {
//                        NettyRemotingServer.this
//                                .putNettyEvent(new NettyEvent(NettyEventType.IDLE, remoteAddress, ctx.channel()));
//                    }
                }
            }

            ctx.fireUserEventTriggered(evt);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            final String remoteAddress = NettyUtil.parseChannelRemoteAddr(ctx.channel());
//            log.warn("NETTY SERVER PIPELINE: exceptionCaught {}", remoteAddress);
//            log.warn("NETTY SERVER PIPELINE: exceptionCaught exception.", cause);

//            if (NettyRemotingServer.this.channelEventListener != null) {
//                NettyRemotingServer.this.putNettyEvent(new NettyEvent(NettyEventType.EXCEPTION, remoteAddress, ctx.channel()));
//            }

            NettyUtil.closeChannel(ctx.channel());
        }
    }


}
