package org.slf4j.impl;

import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public class NewMojoLogger extends org.slf4j.helpers.MarkerIgnoringBase {

	static LinkedList<AbstractMojo> mojoStack = new LinkedList<AbstractMojo>();

	public synchronized static void detachMojo() {
		mojoStack.pop();
	}

	public synchronized static void attachMojo(AbstractMojo mojo) {
		mojoStack.addFirst(mojo);

	}

	Log getLog() {
		if (mojoStack.peek() != null) {
			return new MavenLogWrapper(mojoStack.peek().getLog());
		} else {
			return new MavenLogWrapper(null);
		}
	}

	
	////// 
	@Override
	public void debug(String arg0) {

		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().debug(tp.getMessage());

	}

	@Override
	public void debug(String arg0, Object arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, arg1);
		getLog().debug(tp.getMessage());
	}

	@Override
	public void debug(String arg0, Object[] arg1) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, arg1);
		getLog().debug(tp.getMessage());
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().debug(tp.getMessage(),arg1);
	}

	@Override
	public void debug(String arg0, Object arg1, Object arg2) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, new Object[] {arg1,arg2});
		getLog().debug(tp.getMessage());
	}

	// ////////// ERROR
//////
	@Override
	public void error(String arg0) {

		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().error(tp.getMessage());

	}

	@Override
	public void error(String arg0, Object arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, arg1);
		getLog().error(tp.getMessage());
	}

	@Override
	public void error(String arg0, Object[] arg1) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, arg1);
		getLog().error(tp.getMessage());
	}

	@Override
	public void error(String arg0, Throwable arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().error(tp.getMessage(),arg1);
	}

	@Override
	public void error(String arg0, Object arg1, Object arg2) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, new Object[] {arg1,arg2});
		getLog().error(tp.getMessage());
	}
	
	
	
	
//////
	@Override
	public void info(String arg0) {

		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().info(tp.getMessage());

	}

	@Override
	public void info(String arg0, Object arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, arg1);
		getLog().info(tp.getMessage());
	}

	@Override
	public void info(String arg0, Object[] arg1) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, arg1);
		getLog().info(tp.getMessage());
	}

	@Override
	public void info(String arg0, Throwable arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().info(tp.getMessage(),arg1);
	}

	@Override
	public void info(String arg0, Object arg1, Object arg2) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, new Object[] {arg1,arg2});
		getLog().info(tp.getMessage());
	}
	
	
	
	@Override
	public boolean isDebugEnabled() {

		return getLog().isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {

		return getLog().isErrorEnabled();
	}

	@Override
	public boolean isInfoEnabled() {

		return getLog().isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {

		return getLog().isDebugEnabled();
	}

	@Override
	public boolean isWarnEnabled() {

		return getLog().isWarnEnabled();
	}

	@Override
	public void trace(String arg0) {

		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().debug(tp.getMessage());

	}

	@Override
	public void trace(String arg0, Object arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, arg1);
		getLog().debug(tp.getMessage());
	}

	@Override
	public void trace(String arg0, Object[] arg1) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, arg1);
		getLog().debug(tp.getMessage());
	}

	@Override
	public void trace(String arg0, Throwable arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().debug(tp.getMessage(),arg1);
	}

	@Override
	public void trace(String arg0, Object arg1, Object arg2) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, new Object[] {arg1,arg2});
		getLog().debug(tp.getMessage());
	}
	
//////
	@Override
	public void warn(String arg0) {

		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().warn(tp.getMessage());

	}

	@Override
	public void warn(String arg0, Object arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, arg1);
		getLog().warn(tp.getMessage());
	}

	@Override
	public void warn(String arg0, Object[] arg1) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, arg1);
		getLog().warn(tp.getMessage());
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
		FormattingTuple tp = MessageFormatter.format(arg0, null);
		getLog().warn(tp.getMessage(),arg1);
	}

	@Override
	public void warn(String arg0, Object arg1, Object arg2) {
		FormattingTuple tp = MessageFormatter.arrayFormat(arg0, new Object[] {arg1,arg2});
		getLog().warn(tp.getMessage());
	}
	

}
