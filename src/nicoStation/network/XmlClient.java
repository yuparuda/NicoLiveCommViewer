package nicoStation.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

import message.xmlLevel.IOHandler;

public class XmlClient {		// Using Nonblocking socket channel
	static final int NO_VALID_DATA = -1;
	static final int IO_BFF_SIZE = 1024000;
	static final int RCVBUF_SIZE = 200000;
	static final int SNDBUF = 2000;

	private Selector selector;
	private SocketChannel channel;
	private boolean keepReceiving = false;
	// for debugging
	private int maxCommentCount = 0;

	private void init(SocketChannel channel) throws IOException {
		channel.configureBlocking(false);
		channel.setOption(StandardSocketOptions.SO_RCVBUF, RCVBUF_SIZE);
		channel.setOption(StandardSocketOptions.SO_SNDBUF, SNDBUF);
	}

	private void receiveDataVia(SocketChannel channel, IOHandler response)
			throws IOException {
		int read = channel.read(response.mssgBox);

		switch (read) {

		case NO_VALID_DATA:
			System.out.println(" Invalid data -1, and Sockent is closed ");
			channel.close();
			break;

		case 0:
			System.out.println(" No data 0");
			break;

		default:
			response.mssgBox.flip();
			int result = response.procResponse();
			if (result == IOHandler.END) {
				this.keepReceiving = false;
				channel.close();
			} else {
				channel.register(selector, result, response);
			}
			break;
		}
	}

	private void sendDataVia(SocketChannel channel, IOHandler mssg)
			throws IOException {

		int remaining = mssg.mssgBox.remaining();
		channel.write(mssg.mssgBox);		// important !!!!!!!
		remaining = mssg.mssgBox.remaining();

		if (remaining == 0) {
			mssg.mssgBox.clear();
			channel.register(selector, SelectionKey.OP_READ, mssg);
		} else { // send this message to server again
			channel.register(selector, SelectionKey.OP_WRITE, mssg);
		}
	}

	private void handleValid(SelectionKey key) throws IOException {
		SelectableChannel channel = key.channel();
		IOHandler mssg = (IOHandler) key.attachment();

		if (key.isReadable()) {
			receiveDataVia((SocketChannel) channel, mssg);
		} else if (key.isValid() && key.isWritable()) {
			sendDataVia((SocketChannel) channel, mssg);
		}
	}

	private void procIOUsing(Set<SelectionKey> key_set) throws IOException {
		for (SelectionKey sk : key_set) {
			key_set.remove(sk);
			handleValid(sk);
		}
	}

	public void connectTo(InetSocketAddress addr, IOHandler ioMssg) {
		this.keepReceiving = true;

		try (Selector slctr = SelectorProvider.provider().openSelector();
				SocketChannel clientChannel = SocketChannel.open(addr)) {

			init(clientChannel);
			this.selector = slctr;
			this.channel = clientChannel;
			// send Message to the Nico Server
			this.channel.register(this.selector, SelectionKey.OP_WRITE, ioMssg);
			System.out.println("Connected to the Nico Server... ");

			while (keepReceiving) {
				selector.select();		// blocking a little
				Thread.sleep(10);		// important
				procIOUsing(selector.selectedKeys());
			}
		} catch (IOException | InterruptedException e1) {
			e1.printStackTrace();
		}
		System.out.println("XmlClient was Shutdowned...Properly");
	}

	public void sendReqToServer(IOHandler io_mssg)
			throws ClosedChannelException {
		System.out.println("! Send to Server ");
		this.channel.register(this.selector, SelectionKey.OP_WRITE, io_mssg);
	}

	public void shutDown() {
		System.out.println("Close Nonblocking server");
		this.keepReceiving = false;
	}
}
