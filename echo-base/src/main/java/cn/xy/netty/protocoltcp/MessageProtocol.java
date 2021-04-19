package cn.xy.netty.protocoltcp;


/**
 * 协议包，解决拆包粘包问题
 * @author  xiangyu
 */
public class MessageProtocol {
    /**
     *  数据长度
     */
    private int len;
    /**
     *  数据
     */
    private byte[] content;


    public MessageProtocol() {
    }

    public MessageProtocol(int len, byte[] content) {
        this.len = len;
        this.content = content;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
