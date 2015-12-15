// Copyright (C) 2013 Rob Schoening - http://www.github.com/if6was9
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//        http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.fortasoft.maven.plugin.gradle;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnectionException;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProgressEvent;
import org.gradle.tooling.ProgressListener;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.ResultHandler;
import org.slf4j.impl.MavenLogWrapper;
import org.slf4j.impl.NewMojoLogger;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.Mojo;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;

/**
 * Goal which invokes gradle!
 * 
 */
@Mojo(name="invoke")
public class GradleMojo extends AbstractMojo {

	@Parameter(defaultValue="2.4", required=true)
	private String gradleVersion;


	@Parameter(defaultValue="${tasks}")
	private String[] tasks;


	@Parameter(defaultValue="${task}")
	private String task;


	@Parameter(defaultValue="${project.basedir}")
	private File gradleProjectDirectory;

	@Parameter
	private String checkInvokeScript;

	@Parameter
	private String[] args;

	@Parameter
	private String[] jvmArgs;

	@Parameter
	private File javaHome;

	@Parameter(defaultValue="${project.basedir}",
		required=true)
	private File mavenBaseDir;
	
	
	// http://www.gradle.org/docs/current/javadoc/org/gradle/tooling/GradleConnector.html#useDistribution(java.net.URI)
	@Parameter
	private String gradleDistribution;

	// http://www.gradle.org/docs/current/javadoc/org/gradle/tooling/GradleConnector.html#useGradleUserHomeDir(java.io.File)
	@Parameter
	private File gradleUserHomeDir;

	// http://www.gradle.org/docs/current/javadoc/org/gradle/tooling/GradleConnector.html#useInstallation(java.io.File)
	@Parameter
	private File gradleInstallationDir;
	
	@Parameter( defaultValue = "${session}", readonly = true )
    private MavenSession session;
	
	@Parameter( defaultValue = "${project}", readonly = true )
    private MavenProject project;

	File getGradleProjectDirectory() {
		return gradleProjectDirectory;
	}

	

	String[] getTasks() throws MojoFailureException {

		String[] theTasks;

		if (task != null) {
			if (tasks != null) {
				throw new MojoFailureException(
						"<task> and <tasks> are mutually exclusive");
			}
			theTasks = new String[] { task };
		} else if (tasks != null) {
			if (tasks.length > 0) {
				theTasks = tasks;
			} else {
				throw new MojoFailureException(
						"No <task> elements specified within <tasks>");
			}
		} else {
			throw new MojoFailureException(
					"<task> or <tasks> elements are mandatory");
		}

		return theTasks;

	}

	protected GradleConnectionException gradleConnectionException;

	volatile boolean invocationComplete = false;

	class MyProgressListener implements ProgressListener {
		@Override
		public void statusChanged(ProgressEvent arg0) {
			getLog().info(arg0.getDescription());

		}
	}

	class MyResultHandler implements ResultHandler<Void> {
		@Override
		public void onComplete(Void arg0) {

			invocationComplete = true;

			getLog().info("gradle execution complete");

		}

		@Override
		public void onFailure(GradleConnectionException arg0) {
			synchronized (this) {
				invocationComplete = true;
			}
			gradleConnectionException = arg0;
		}
	}

	protected boolean shouldExecute() throws MojoFailureException {
		boolean shouldExecute = true;
		if (checkInvokeScript != null && checkInvokeScript.trim().length() > 0) {
			Binding b = new Binding();

			b.setVariable("mavenBaseDir", mavenBaseDir);

			// add common maven model objects to binding
			b.setVariable("session", session);
			b.setVariable("project", project);

			GroovyShell gs = new GroovyShell(b);

			Object rval = gs.evaluate(checkInvokeScript);

			if (rval != null && rval instanceof Boolean) {
				Boolean boolRval = (Boolean) rval;
				shouldExecute = boolRval.booleanValue();
			} else {
				throw new MojoFailureException(
						"checkScript must return boolean");
			}
		}

		return shouldExecute;
	}

	public void execute() throws MojoExecutionException, MojoFailureException {

		ProjectConnection connection = null;
		try {
			NewMojoLogger.attachMojo(this);

			if (!shouldExecute()) {
				return;
			}

			GradleConnector c = GradleConnector.newConnector();
			getLog().info("jvmArgs: " + args);
			getLog().info(
					"gradleProjectDirectory: "
							+ getGradleProjectDirectory().getAbsolutePath());
			c = c.useGradleVersion(gradleVersion).forProjectDirectory(
					getGradleProjectDirectory());

			if (gradleInstallationDir != null) {
				getLog().info(
						"gradleInstallation: "
								+ gradleInstallationDir.getAbsolutePath());
				c = c.useInstallation(gradleInstallationDir);
			}

			if (gradleUserHomeDir != null) {
				getLog().info(
						"gradleUserHome: "
								+ gradleUserHomeDir.getAbsolutePath());
				c = c.useGradleUserHomeDir(gradleUserHomeDir);
			}
			if (gradleDistribution != null) {
				getLog().info("gradleDistributionUri: " + gradleDistribution);
				c = c.useDistribution(new URI(gradleDistribution));
			}

			connection = c.connect();

			BuildLauncher launcher = connection.newBuild();
			launcher.forTasks(getTasks());

			// Make sure to setStandardOut & Error otherwise
			// basic gradle build output will be lost
			// making troubleshooting hard
            launcher.setStandardOutput(System.out);
            launcher.setStandardError(System.err);

			if (jvmArgs != null && jvmArgs.length > 0) {
				launcher.setJvmArguments(jvmArgs);
			}

			String[] finalArgs = buildFinalArgs(args);

			if (finalArgs != null && finalArgs.length > 0) {
				launcher.withArguments(finalArgs);
			}
			if (javaHome != null) {
				launcher.setJavaHome(javaHome);
			}

			launcher.addProgressListener(new MyProgressListener());

			// launcher will not block
			launcher.run(new MyResultHandler());

			waitForGradleToComplete();

			if (gradleConnectionException != null) {
				throw new MojoFailureException(gradleConnectionException,
						gradleConnectionException.toString(),
						gradleConnectionException.toString());
			}
		}
		catch (URISyntaxException e) {
			throw new MojoFailureException("gradleDistribution is not in URI syntax");
		} finally {
			if (connection != null) {
				connection.close();
			}
			NewMojoLogger.detachMojo();
		}
	}

	/*
	 * Right now this is only conditionally adding
	 * gradle offline option to command line, but 
	 * there is additional functionality we can do
	 * in the future.
	 */
	private String[] buildFinalArgs(String[] args) {

		List<String> argList = new ArrayList<String>();

		if (args != null) {
			for (int i=0; i < args.length; i++) {
				argList.add(args[i]);
			}
		}

		boolean offline = session.getSettings().isOffline();

		// If we're offline in maven, let's be offline
		// in gradle as well.
		if (offline && !GradleArgs.OFFLINE.exists(argList)) {
			argList.add(GradleArgs.OFFLINE.getLongValue());
		}

		// convert back to array
		return argList.toArray(new String[argList.size()]);
	}

	synchronized void waitForGradleToComplete() {
		while (!invocationComplete) {
			try {
				this.wait(50L);
			} catch (InterruptedException e) {
			}
			getLog().debug("waiting....");
		}
	}
}
