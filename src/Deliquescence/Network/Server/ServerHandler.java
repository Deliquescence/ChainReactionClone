/*
 * Copyright (c) 2014, Deliquescence <Deliquescence1@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * - Neither the name of the copyright holder nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package Deliquescence.Network.Server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author Josh
 */
@ChannelHandler.Sharable
public class ServerHandler extends ChannelHandlerAdapter {

    private ChannelHandlerContext chc;

    @Override
    public void channelActive(ChannelHandlerContext chc) {
        this.chc = chc;
    }
    /* public void write(ChannelHandlerContext ctx, String msg, ChannelPromise promise) throws Exception {
     System.out.println("SERVER WRITE : " + msg);
     ctx.write(msg);
     }*/

    /*  @Override
     public void channelRead(ChannelHandlerContext ctx, Object msg) {

     Channel incoming = ctx.channel();

     ByteBuf in = (ByteBuf) msg;
     String str = in.toString(0, in.readableBytes(), io.netty.util.CharsetUtil.US_ASCII);
     System.out.println("SERVER READ @ " + incoming.remoteAddress() + " : " + str);

     }
     */
//    @Override
//    protected void messageReceived(ChannelHandlerContext chc, String i) throws Exception {
//        System.out.println("SERVER READ @ " + chc.channel().remoteAddress() + " : " + i);
//    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("SERVER READ @ " + ctx.channel().remoteAddress() + " : " + msg.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void writeMine(final String msg) throws Exception {
        ChannelFuture future = chc.write(msg);
    }
}
