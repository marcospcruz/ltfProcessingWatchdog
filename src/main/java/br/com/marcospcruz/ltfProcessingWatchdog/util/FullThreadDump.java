package br.com.marcospcruz.ltfProcessingWatchdog.util;

/*
 * @(#)FullThreadDump.java  1.5 05/11/17
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.
 */

/*
 * @(#)FullThreadDump.java  1.5 05/11/17
 */

import static java.lang.management.ManagementFactory.THREAD_MXBEAN_NAME;
import static java.lang.management.ManagementFactory.getThreadMXBean;
import static java.lang.management.ManagementFactory.newPlatformMXBeanProxy;

import java.io.IOException;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.net.MalformedURLException;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

/**
 * This FullThreadDump class demonstrates the capability to get a full thread
 * dump and also detect deadlock remotely.
 */
// System.out.println("Connecting to " + hostname + ":" + port);
// Create an RMI connector client and connect it to
// the RMI connector server
// TODO Auto-generated method stub
// TODO Auto-generated method stub
/**
 * Connect to a JMX agent of a given URL.
 */
// should not reach here
// public static void main(String[] args) {
// if (args.length != 1) {
// usage();
// }
//
// String[] arg2 = args[0].split(":");
// if (arg2.length != 2) {
// usage();
// }
// String hostname = arg2[0];
// int port = -1;
// try {
// port = Integer.parseInt(arg2[1]);
// } catch (NumberFormatException x) {
// usage();
// }
// if (port < 0) {
// usage();
// }
//
// // get full thread dump and perform deadlock detection
// FullThreadDump ftd = new FullThreadDump(hostname, port);
// ftd.dump();
// }
/**
 * This FullThreadDump class demonstrates the capability to get a full thread
 * dump and also detect deadlock remotely.
 */
public class FullThreadDump {
	private MBeanServerConnection server;

	private JMXConnector jmxc;

	private StringBuffer threadDumpStringBuffer;

	public FullThreadDump(String urlPath) {

		connect(urlPath);

		threadDumpStringBuffer = new StringBuffer();
	}

	public FullThreadDump(String hostname, int port) {
		// System.out.println("Connecting to " + hostname + ":" + port);

		// Create an RMI connector client and connect it to
		// the RMI connector server

		this("/jndi/rmi://" + hostname + ":" + port + "/jmxrmi");
	}

	public StringBuffer dump() {
		try {
			ThreadMonitor monitor = new ThreadMonitor(server);
			collectDump(monitor.threadDump());
			if (!monitor.findDeadlock()) {
				collectDump("No deadlock found.");
			}
		} catch (IOException e) {
			collectDump("\nCommunication error: " + e.getMessage());
			System.exit(1);
		}

		return threadDumpStringBuffer;
	}

	private void collectDump(StringBuffer threadDump) {
		// TODO Auto-generated method stub

		threadDumpStringBuffer = threadDump;

	}

	private void collectDump(String string) {
		// TODO Auto-generated method stub
		threadDumpStringBuffer.append(string
				+ ConstantesEnum.LineSeparator.getValue().toString());

	}

	/**
	 * Connect to a JMX agent of a given URL.
	 */
	private void connect(String urlPath) {

		JMXServiceURL url;
		try {
			url = new JMXServiceURL("rmi", "", 0, urlPath);
			this.jmxc = JMXConnectorFactory.connect(url);
			this.server = jmxc.getMBeanServerConnection();
		} catch (MalformedURLException e) {
			// should not reach here
		} catch (IOException e) {
			System.err.println("\nCommunication error: " + e.getMessage());
			System.exit(1);
		}
	}

	// public static void main(String[] args) {
	// if (args.length != 1) {
	// usage();
	// }
	//
	// String[] arg2 = args[0].split(":");
	// if (arg2.length != 2) {
	// usage();
	// }
	// String hostname = arg2[0];
	// int port = -1;
	// try {
	// port = Integer.parseInt(arg2[1]);
	// } catch (NumberFormatException x) {
	// usage();
	// }
	// if (port < 0) {
	// usage();
	// }
	//
	// // get full thread dump and perform deadlock detection
	// FullThreadDump ftd = new FullThreadDump(hostname, port);
	// ftd.dump();
	// }

	private static void usage() {
		System.out.println("Usage: java FullThreadDump <hostname>:<port>");
	}
}

