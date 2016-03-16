package br.com.marcospcruz.test;

import java.io.IOException;
import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.IntrospectionException;
import javax.management.MBeanInfo;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import br.com.marcospcruz.ltfProcessingWatchdog.util.ConstantesEnum;

public class ThreadDumpGenerator {

	public static void main(String args[]) {

		new ThreadDumpGenerator().generateThreadDump();

	}

	public void generateThreadDump() {

		StringBuilder dump = new StringBuilder();

		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

		ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(
				threadMXBean.getAllThreadIds(), 5000);

		for (ThreadInfo threadInfo : threadInfos) {

			dump.append('"');
			dump.append(threadInfo.getThreadName());
			dump.append("\" ");
			final State state = threadInfo.getThreadState();
			dump.append("\n    java.lang.Thread.State:  ");
			dump.append(state);
			StackTraceElement[] stackTraceElements = threadInfo.getStackTrace();
			for (StackTraceElement stackTraceElement : stackTraceElements) {
				dump.append("\n     at  ");
				dump.append(stackTraceElement);
			}

			dump.append("\n\n");
		}

		System.out.println(dump.toString());

	}

	public void testeJMXConnection() throws IOException, InstanceNotFoundException, IntrospectionException, ReflectionException {
		
		Map<String, String[]> env = new HashMap<String, String[]>();
		// env.put(JMXConnector.CREDENTIALS)
		JMXServiceURL url = new JMXServiceURL(ConstantesEnum.JMXURL.getValue()
				.toString());
		JMXConnector connector = JMXConnectorFactory.connect(url, env);

		MBeanServerConnection mbs = connector.getMBeanServerConnection();

		// get all mbeans
		Set<ObjectInstance> beans = mbs.queryMBeans(null, null);

		for (ObjectInstance instance : beans) {
			MBeanInfo info = mbs.getMBeanInfo(instance.getObjectName());
		}

	}
}
