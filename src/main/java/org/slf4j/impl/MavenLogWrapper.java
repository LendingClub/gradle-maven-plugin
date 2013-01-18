package org.slf4j.impl;

import org.apache.maven.plugin.logging.Log;

public class MavenLogWrapper implements Log {

	Log myLog;
	
	MavenLogWrapper(Log log) {
		super();
		myLog = log;
	}
	public void fallback(CharSequence arg0) {
		System.err.println("SLF4J: "+arg0);
	}
	public void fallback(CharSequence arg0, Throwable t) {
		System.err.println("SLF4J: "+arg0 +" "+t.toString());
	}
	public void debug(CharSequence arg0) {
		if (myLog!=null) {
			myLog.debug(arg0);
		}
		else {
			fallback(arg0);
		}
	}

	@Override
	public void debug(Throwable arg0) {
		if (myLog!=null) {
			myLog.debug(arg0);
		}
		else {
			fallback("",arg0);
		}
	}

	@Override
	public void debug(CharSequence arg0, Throwable arg1) {
		if (myLog!=null) {
			myLog.debug(arg0,arg1);
		}
		else {
			fallback(arg0,arg1);
		}
	}

	public void error(CharSequence arg0) {
		if (myLog!=null) {
			myLog.error(arg0);
		}
		else {
			fallback(arg0);
		}
	}

	@Override
	public void error(Throwable arg0) {
		if (myLog!=null) {
			myLog.error(arg0);
		}
		else {
			fallback("",arg0);
		}
	}

	@Override
	public void error(CharSequence arg0, Throwable arg1) {
		if (myLog!=null) {
			myLog.error(arg0,arg1);
		}
		else {
			fallback(arg0,arg1);
		}
	}

	
	
	public void warn(CharSequence arg0) {
		if (myLog!=null) {
			myLog.warn(arg0);
		}
		else {
			fallback(arg0);
		}
	}

	@Override
	public void warn(Throwable arg0) {
		if (myLog!=null) {
			myLog.warn(arg0);
		}
		else {
			fallback("",arg0);
		}
	}

	@Override
	public void warn(CharSequence arg0, Throwable arg1) {
		if (myLog!=null) {
			myLog.warn(arg0,arg1);
		}
		else {
			fallback(arg0,arg1);
		}
	}

	
	public void info(CharSequence arg0) {
		if (myLog!=null) {
			myLog.info(arg0);
		}
		else {
			fallback(arg0);
		}
	}

	@Override
	public void info(Throwable arg0) {
		if (myLog!=null) {
			myLog.info(arg0);
		}
		else {
			fallback("",arg0);
		}
	}

	@Override
	public void info(CharSequence arg0, Throwable arg1) {
		if (myLog!=null) {
			myLog.info(arg0,arg1);
		}
		else {
			fallback(arg0,arg1);
		}
	}
	@Override
	public boolean isDebugEnabled() {
		if (myLog!=null) {
			return myLog.isDebugEnabled();
		}
		else {
			return true;
		}
	}
	@Override
	public boolean isErrorEnabled() {
		if (myLog!=null) {
			return myLog.isErrorEnabled();
		}
		else {
			return true;
		}
	}
	@Override
	public boolean isInfoEnabled() {
		if (myLog!=null) {
			return myLog.isInfoEnabled();
		}
		else {
			return true;
		}
	}
	@Override
	public boolean isWarnEnabled() {
		if (myLog!=null) {
			return myLog.isWarnEnabled();
		}
		else {
			return true;
		}
	}

	
}
