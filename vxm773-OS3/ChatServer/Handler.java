package ChatServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import Protocol.KeyTool;
import Protocol.Protocol;
import Protocol.SimpleProtocol;

public class Handler implements Runnable {

	private Socket socket = null;
	private Protocol protocol = new SimpleProtocol();
	private BufferedReader in;
	private DataOutputStream out;
	private Server server;
	private String username;
	private Key key1;
	private Key key2;

	public Handler(Socket socket) {
		this.socket = socket;
	}

	public void sendToClient(String... args) throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		try {

			// Create AES cipher & Decrypt with key2
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key2);

			out.writeBytes(
					Base64.getEncoder().encodeToString(cipher.doFinal(protocol.createMessage(args).getBytes())) + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getFromClient() throws Exception {

		// Create AES cipher & Decrypt with key2
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, key2);

		return protocol.decodeMessage(new String(cipher.doFinal(Base64.getDecoder().decode(in.readLine()))));
	}

	@Override
	public void run() {
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			server = Server.getInstance();

			// To do: Key exchange

			Cipher decryptRSA = Cipher.getInstance("RSA");
			decryptRSA.init(Cipher.DECRYPT_MODE, KeyTool.getRSAPrivateKey());
			String key1String = in.readLine();
			byte[] key1Decode = Base64.getDecoder().decode(key1String);
			byte[] ket1Decrypt = decryptRSA.doFinal(key1Decode);
			key1 = new SecretKeySpec(ket1Decrypt, "AES");
			Cipher encryptAES = Cipher.getInstance("AES");
			encryptAES.init(Cipher.ENCRYPT_MODE, key1);
			key2 = KeyTool.getAESKey();
			byte[] secondKeyEncrypted = encryptAES.doFinal(key2.getEncoded());
			Cipher decryptAES = Cipher.getInstance("AES");
			decryptAES.init(Cipher.DECRYPT_MODE, key2);
			encryptAES.init(Cipher.ENCRYPT_MODE, key2);
			String key2Encoded = Base64.getEncoder().encodeToString(secondKeyEncrypted);
			out.writeBytes(key2Encoded + "\n");

			// Remove the following line
			// out.writeBytes(new SimpleProtocol().createMessage("welcome", "welcome") +
			// "\n");

			// Sign in or create account
			String[] message = getFromClient();
			switch (message[0]) {
			case "sign-in": {
				if (server.users.containsKey(message[1])) {
					if (server.users.get(message[1]).equals(message[2])) {
						this.username = message[1];
						sendToClient("sign-in", "true", "welcome");
					} else {
						sendToClient("sign-in", "false", "Username and password do not match");
						return;
					}
				} else {
					sendToClient("sign-in", "false", "Username does not exist");
					return;
				}
				break;
			}
			case "sign-up": {
				if (false == server.users.containsKey(message[1])) {
					server.users.put(message[1], message[2]);
					sendToClient("sign-up", "true", "Registration successfully!");
				} else {
					sendToClient("sign-up", "false", "Username exists.");
				}
				return;
			}
			default:
				return;
			}
			SimpleDateFormat dFormat = new SimpleDateFormat("hh:mm");
			while (true) {
				message = getFromClient();
				switch (message[0]) {
				case "send-message": {
					server.messages.add(new Message(username, new Date(), message[1]));
					sendToClient("send-message", "true", "ok!");
					break;
				}
				case "get-message": {
					int offset = Integer.parseInt(message[1]);
					if (offset < -1)
						offset = -1;
					ArrayList<String> newMessages = new ArrayList<>();
					newMessages.add("get-message");
					for (int i = offset + 1; i < server.messages.size(); i++) {
						newMessages.add(Integer.toString(i));
						newMessages.add(server.messages.get(i).getUsername());
						newMessages.add(dFormat.format(server.messages.get(i).getTimestamp()));
						newMessages.add(server.messages.get(i).getContent());
					}
					if (newMessages.size() < 1) {
						out.writeBytes("\n");
					}
					sendToClient(newMessages.toArray(new String[newMessages.size()]));
					break;
				}
				default:
					return;
				}
			}

		} catch (Exception e) {
			try {
				socket.close();
				e.printStackTrace();
				return;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
