package br.com.marcospcruz.ltfProcessingWatchdog.util;

import static java.lang.management.ManagementFactory.THREAD_MXBEAN_NAME;
import static java.lang.management.ManagementFactory.getThreadMXBean;
import static java.lang.management.ManagementFactory.newPlatformMXBeanProxy;

import java.io.IOException;
import java.lang.management.LockInfo;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/*
 * @(#)ThreadMonitor.java 1.6 05/12/22
 * 
 * Copyright (c) 2006 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * Neither the name of Sun Microsystems, Inc. or the names of contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN") AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility.
 */

/*
 * @(#)ThreadMonitor.java 1.6 05/12/22
 */

/**
 * Example of using the java.lang.management API to dump stack trace and to
 * perform deadlock detection.
 * 
 * @author Mandy Chung
 * @version %% 12/22/05
 */
class ThreadMonitor {
	private MBeanServerConnection server;

	private ThreadMXBean tmbean;

	private ObjectName objname;

	// default - JDK 6+ VM
	private String findDeadlocksMethodName = "findDeadlockedThreads";

	private boolean canDumpLocks = true;

	private StringBuffer threadDumpStringBuffer;

	/**
	 * Constructs a ThreadMonitor object to get thread information in a remote
	 * JVM.
	 */
	public ThreadMonitor(MBeanServerConnection server) throws IOException {
		System.out.println(server);
		this.server = server;
		this.tmbean = newPlatformMXBeanProxy(server, THREAD_MXBEAN_NAME,
				ThreadMXBean.class);
		try {
			objname = new ObjectName(THREAD_MXBEAN_NAME);
		} catch (MalformedObjectNameException e) {
			// should not reach here
			InternalError ie = new InternalError(e.getMessage());
			ie.initCause(e);
			throw ie;
		}
		parseMBeanInfo();
	}

	/**
	 * Constructs a ThreadMonitor object to get thread information in the local
	 * JVM.
	 */
	public ThreadMonitor() {
		this.tmbean = getThreadMXBean();
	}

	/**
	 * Prints the thread dump information to System.out.
	 * 
	 * @return
	 */
	public StringBuffer threadDump() {
		if (canDumpLocks) {
			if (tmbean.isObjectMonitorUsageSupported()
					&& tmbean.isSynchronizerUsageSupported()) {
				// Print lock info if both object monitor usage
				// and synchronizer usage are supported.
				// This sample code can be modified to handle if
				// either monitor usage or synchronizer usage is supported.
				dumpThreadInfoWithLocks();
			}
		} else {
			dumpThreadInfo();
		}

		return threadDumpStringBuffer;
	}

	private void dumpThreadInfo() {
		collectThreadDump("Full Java thread dump");
		long[] tids = tmbean.getAllThreadIds();
		ThreadInfo[] tinfos = tmbean.getThreadInfo(tids, Integer.MAX_VALUE);
		for (ThreadInfo ti : tinfos) {
			printThreadInfo(ti);
		}
	}

	/**
	 * Prints the thread dump information with locks info to System.out.
	 */
	private void dumpThreadInfoWithLocks() {
		collectThreadDump("Full Java thread dump with locks info");

		ThreadInfo[] tinfos = tmbean.dumpAllThreads(true, true);
		for (ThreadInfo ti : tinfos) {
			printThreadInfo(ti);
			LockInfo[] syncs = ti.getLockedSynchronizers();
			printLockInfo(syncs);
		}
		collectThreadDump("");
	}

	/**
	 * 
	 * @param string
	 */
	private void collectThreadDump(String string) {
		// TODO Auto-generated method stub

		if (threadDumpStringBuffer == null)

			threadDumpStringBuffer = new StringBuffer();

		threadDumpStringBuffer.append(string
				+ ConstantesEnum.LineSeparator.getValue().toString());

	}

	private static String INDENT = "    ";

	private void printThreadInfo(ThreadInfo ti) {
		// print thread information
		printThread(ti);

		// print stack trace with locks
		StackTraceElement[] stacktrace = ti.getStackTrace();
		MonitorInfo[] monitors = ti.getLockedMonitors();
		for (int i = 0; i < stacktrace.length; i++) {
			StackTraceElement ste = stacktrace[i];
			collectThreadDump(INDENT + "at " + ste.toString());
			for (MonitorInfo mi : monitors) {
				if (mi.getLockedStackDepth() == i) {
					collectThreadDump(INDENT + "  - locked " + mi);
				}
			}
		}
		collectThreadDump("");
	}

	private void printThread(ThreadInfo ti) {
		StringBuilder sb = new StringBuilder("\"" + ti.getThreadName() + "\""
				+ " Id=" + ti.getThreadId() + " in " + ti.getThreadState());
		if (ti.getLockName() != null) {
			sb.append(" on lock=" + ti.getLockName());
		}
		if (ti.isSuspended()) {
			sb.append(" (suspended)");
		}
		if (ti.isInNative()) {
			sb.append(" (running in native)");
		}
		collectThreadDump(sb.toString());
		if (ti.getLockOwnerName() != null) {
			collectThreadDump(INDENT + " owned by " + ti.getLockOwnerName()
					+ " Id=" + ti.getLockOwnerId());
		}
	}

	private void printMonitorInfo(ThreadInfo ti, MonitorInfo[] monitors) {
		collectThreadDump(INDENT + "Locked monitors: count = "
				+ monitors.length);
		for (MonitorInfo mi : monitors) {
			collectThreadDump(INDENT + "  - " + mi + " locked at ");
			collectThreadDump(INDENT + "      " + mi.getLockedStackDepth()
					+ " " + mi.getLockedStackFrame());
		}
	}

	private void printLockInfo(LockInfo[] locks) {
		collectThreadDump(INDENT + "Locked synchronizers: count = "
				+ locks.length);
		for (LockInfo li : locks) {
			collectThreadDump(INDENT + "  - " + li);
		}
		collectThreadDump("");
	}

	/**
	 * Checks if any threads are deadlocked. If any, print the thread dump
	 * information.
	 */
	public boolean findDeadlock() {
		long[] tids;
		if (findDeadlocksMethodName.equals("findDeadlockedThreads")
				&& tmbean.isSynchronizerUsageSupported()) {
			tids = tmbean.findDeadlockedThreads();
			if (tids == null) {
				return false;
			}

			collectThreadDump("Deadlock found :-");
			ThreadInfo[] infos = tmbean.getThreadInfo(tids, true, true);
			for (ThreadInfo ti : infos) {
				printThreadInfo(ti);
				printLockInfo(ti.getLockedSynchronizers());
				collectThreadDump("");
			}
		} else {
			tids = tmbean.findMonitorDeadlockedThreads();
			if (tids == null) {
				return false;
			}
			ThreadInfo[] infos = tmbean.getThreadInfo(tids, Integer.MAX_VALUE);
			for (ThreadInfo ti : infos) {
				// print thread information
				printThreadInfo(ti);
			}
		}

		return true;
	}

	private void parseMBeanInfo() throws IOException {
		try {
			MBeanOperationInfo[] mopis = server.getMBeanInfo(objname)
					.getOperations();

			// look for findDeadlockedThreads operations;
			boolean found = false;
			for (MBeanOperationInfo op : mopis) {
				if (op.getName().equals(findDeadlocksMethodName)) {
					found = true;
					break;
				}
			}
			if (!found) {
				// if findDeadlockedThreads operation doesn't exist,
				// the target VM is running on JDK 5 and details about
				// synchronizers and locks cannot be dumped.
				findDeadlocksMethodName = "findMonitorDeadlockedThreads";
				canDumpLocks = false;
			}
		} catch (IntrospectionException e) {
			InternalError ie = new InternalError(e.getMessage());
			ie.initCause(e);
			throw ie;
		} catch (InstanceNotFoundException e) {
			InternalError ie = new InternalError(e.getMessage());
			ie.initCause(e);
			throw ie;
		} catch (ReflectionException e) {
			InternalError ie = new InternalError(e.getMessage());
			ie.initCause(e);
			throw ie;
		}
	}
}