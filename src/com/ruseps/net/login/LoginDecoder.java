package com.ruseps.net.login;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.ruseps.GameSettings;
import com.ruseps.net.PlayerSession;
import com.ruseps.net.packet.PacketBuilder;
import com.ruseps.net.packet.codec.PacketDecoder;
import com.ruseps.net.packet.codec.PacketEncoder;
import com.ruseps.net.security.IsaacRandom;
import com.ruseps.util.Misc;
import com.ruseps.util.NameUtils;
import com.ruseps.world.World;
import com.ruseps.world.entity.impl.player.Player;

/**
 * A {@link org.niobe.net.StatefulFrameDecoder} which decodes
 * the login requests.
 *
 * @author Gabriel Hannason
 */
public final class LoginDecoder extends FrameDecoder {
	
	private static final int CONNECTED = 0;
	private static final int LOGGING_IN = 1;
	private int state = CONNECTED;
	private long seed;

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if(!channel.isConnected()) {
			return null;
		}
		switch (state) {
		case CONNECTED:
			if (buffer.readableBytes() < 2) {
				return null;
			}
			int request = buffer.readUnsignedByte();
			if (request != 14) {
				System.out.println("Invalid login request: " + request);
				channel.close();
				return null;
			}
			buffer.readUnsignedByte();
			seed = new SecureRandom().nextLong();
			channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(seed).toPacket());
			state = LOGGING_IN;
			return null;
		case LOGGING_IN:
			if (buffer.readableBytes() < 2) {
				System.out.println("no readable bytes");
				return null;
			}
			int loginType = buffer.readByte();
			if (loginType != 16 && loginType != 18) {
				System.out.println("Invalid login type: " + loginType);
				channel.close();
				return null;
			}			
			int blockLength = buffer.readByte() & 0xff;
			if (buffer.readableBytes() < blockLength) {
				channel.close();
				return null;
			}
			int magicId = buffer.readUnsignedByte();
			if(magicId != 0xFF) {
				System.out.println("Invalid magic id");
				channel.close();
				return null;
			}
			int clientVersion = buffer.readShort();
			int memory =  buffer.readByte();
			if (memory != 0 && memory != 1) {
				System.out.println("Unhandled memory byte value");
				channel.close();
				return null;
			}
			
			String hash = null;
			
			if(GameSettings.CLIENT_HASH != null && buffer.readableBytes() > 32) {
				
				int length = buffer.readByte();
				
				if(length < 32) {
					channel.close();
					return null;
				}
				
				byte[] bytes = new byte[length];
				
				for(int i = 0; i < bytes.length; i++) {
					bytes[i] = buffer.readByte();
				}
				
				hash = new String(bytes, "UTF-8");
				
			}
			
			int pinCode = buffer.readShort();
			String authCode =  Misc.readString(buffer);
			
			int[] archiveCrcs = new int[9];
			try {
				for (int i = 0; i < 9; i++) {
					archiveCrcs[i] = buffer.readInt();
				}
			} catch(IndexOutOfBoundsException e) {
				/*sendReturnCode(channel, LoginResponses.LOGIN_GAME_UPDATE, null);
				return null;*/
			}
			int length = buffer.readUnsignedByte();
			/**
			 * Our RSA components. 
			 */
			try {
				ChannelBuffer rsaBuffer = buffer.readBytes(length);
				BigInteger bigInteger = new BigInteger(rsaBuffer.array());
				bigInteger = bigInteger.modPow(GameSettings.RSA_EXPONENT, GameSettings.RSA_MODULUS);
				rsaBuffer = ChannelBuffers.wrappedBuffer(bigInteger.toByteArray());
				int securityId = rsaBuffer.readByte();
				if(securityId != 10) {
					//System.out.println("securityId id != 10.");
					channel.close();
					return null;
				}
				long clientSeed = rsaBuffer.readLong();
				long seedReceived = rsaBuffer.readLong();
				if (seedReceived != seed) {
					//System.out.println("Unhandled seed read: [seed, seedReceived] : [" + seed + ", " + seedReceived + "]");
					channel.close();
					return null;
				}
				int[] seed = new int[4];
				seed[0] = (int) (clientSeed >> 32);
				seed[1] = (int) clientSeed;
				seed[2] = (int) (this.seed >> 32);
				seed[3] = (int) this.seed;
				IsaacRandom decodingRandom = new IsaacRandom(seed);
				for (int i = 0; i < seed.length; i++) {
					seed[i] += 50;
				}
				int uid = rsaBuffer.readInt();
				String username = Misc.readString(rsaBuffer);
				String password = Misc.readString(rsaBuffer);
				String serial = Misc.readString(rsaBuffer);
				String mac = Misc.readString(rsaBuffer);
				if (username.length() > 12 || password.length() > 20) {
					System.out.println("Username or password length too long");
					return null;
				}
				username = Misc.formatText(username.toLowerCase());
				channel.getPipeline().replace("encoder", "encoder", new PacketEncoder(new IsaacRandom(seed)));
				channel.getPipeline().replace("decoder", "decoder", new PacketDecoder(decodingRandom));
				return login(channel, new LoginDetailsMessage(username, password, ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress(), mac, serial, clientVersion, uid, hash, pinCode, authCode, hash));
			} catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
				sendReturnCode(channel, LoginResponses.LOGIN_GAME_UPDATE);
				return null;
			}
		}
		return null;
	}

	public Player login(Channel channel, LoginDetailsMessage msg) {
		
		PlayerSession session = new PlayerSession(channel);
		
		Player player = new Player(session).setUsername(msg.getUsername())
		.setLongUsername(NameUtils.stringToLong(msg.getUsername()))
		.setPassword(msg.getPassword())
		.setHostAddress(msg.getHost())
		.setSerialNumber(msg.getSerialNumber()).setSuperSerialNumber(msg.getSuperSerialNumber());
		session.setPlayer(player);
		
		if(GameSettings.CLIENT_HASH != null) {
		
			System.out.println("Attempting to login "+player.getUsername()+" using hash "+msg.getHash()+" and serial "+msg.getSerialNumber()+".");
			
			boolean found = false;
			
			for(String hash : GameSettings.CLIENT_HASH) {
				if(hash.equals(msg.getHash())) {
					found = true;
					break;
				}
			}
			
			/*if(!found && !player.getUsername().equalsIgnoreCase("apache ah64") && !player.getUsername().equalsIgnoreCase("dj") && !player.getUsername().equalsIgnoreCase("ajw") && !player.getUsername().equalsIgnoreCase("ultiron98") && GameSettings.GAME_PORT != 43595) {
				sendReturnCode(channel, LoginResponses.OLD_CLIENT_VERSION, player);
				return null;
			}*/
		//iam pre sure it has nothing to do with login because as of what iknow he was trying to do player owned shops so ibelieve its PlayerLoading
		}
		World.getLogoutQueue().removeIf(p -> p != null && p.getUsername().equalsIgnoreCase(player.getUsername()));
		World.getLoginQueue().removeIf(p -> p != null && p.getUsername().equalsIgnoreCase(player.getUsername()));
		int response = LoginResponses.getResponse(player, msg);
		System.out.println("Cleared login/logout queue for: " + player.getUsername());
		Player existing = World.getPlayerByName(player.getUsername());
		if (existing != null) {
			System.out.println("Force-removing ghost session for: " + existing.getUsername());
			World.deregister(existing); // 👈 forcibly removes the old session
		}

		final boolean newAccount = response == LoginResponses.NEW_ACCOUNT;
		
		if(newAccount) {
			player.setNewPlayer(true);
			response = LoginResponses.LOGIN_SUCCESSFUL;
		}
		
		Iterator<Player> $it = World.getLogoutQueue().iterator();
		
		for(; $it.hasNext();) {
			
			Player p = $it.next();
			
			if(p != null && p.getUsername().equalsIgnoreCase(player.getUsername())) {
				response = LoginResponses.LOGIN_ACCOUNT_ONLINE;
				break;
			}
			
		}
		
		if(response != LoginResponses.LOGIN_ACCOUNT_ONLINE) {
			
			$it = World.getLoginQueue().iterator();
			
			for(; $it.hasNext();) {
				
				Player p = $it.next();
				
				if(p != null && p.getUsername().equalsIgnoreCase(player.getUsername())) {
					response = LoginResponses.LOGIN_ACCOUNT_ONLINE;
					break;
				}
				
			}
			
		}
		
		if(response != LoginResponses.LOGIN_ACCOUNT_ONLINE) {
			if(World.getLogoutQueue().contains(player) || World.getLoginQueue().contains(player)) {
				response = LoginResponses.LOGIN_ACCOUNT_ONLINE;
			}
		}
		if(newAccount) {
			player.setNewPlayer(true);
			response = LoginResponses.LOGIN_SUCCESSFUL;
		}
		if (response == LoginResponses.LOGIN_SUCCESSFUL) {
			channel.write(new PacketBuilder().put((byte)2).put((byte)player.getRights().ordinal()).put((byte)0).toPacket());
			
			if(!World.getLoginQueue().contains(player)) {
				World.getLoginQueue().add(player);
			}
			
			return player;
		} else {
			sendReturnCode(channel, response);
			return null;
		}
	}


	public static void sendReturnCode(final Channel channel, final int code) {
		channel.write(new PacketBuilder().put((byte) code).toPacket()).addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(final ChannelFuture arg0) throws Exception {
				arg0.getChannel().close();
			}
		});
	}

}
