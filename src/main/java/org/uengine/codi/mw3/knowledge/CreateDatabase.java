package org.uengine.codi.mw3.knowledge;

import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class CreateDatabase {

	public void create(String user, String host, String passwd, String companyName, String sqlPath) throws Exception{
		try {
			JSch jsch = new JSch();

			Session session = jsch.getSession(user, host, 22);
			session.setPassword(passwd);

			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			Channel channel = session.openChannel("exec");

			// cubrid 초기화

			String command = "sh startUp.sh " + companyName + " " + sqlPath;

			((ChannelExec) channel).setCommand(command);
			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();
			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				System.out.println(channel.isClosed());
				System.out.println(channel.isConnected());
				while (in.available() != 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0){
						channel.disconnect();
						break;
					}
					System.out.print(new String(tmp, 0, i));
				}
//				if (channel.isConnected()) {
//					System.out.println("exit-status: "
//							+ channel.getExitStatus());
					break;
//				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
