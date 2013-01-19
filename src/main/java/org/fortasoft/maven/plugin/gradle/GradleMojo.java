package org.fortasoft.maven.plugin.gradle;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which invokes gradle!
 * 
 * @goal invoke
 * 
 */
public class GradleMojo extends AbstractMojo {
	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 */
	private File outputDirectory;

	/**
	 * @parameter expression="1.3"
	 * @required
	 */
	private String gradleVersion;

	/**
	 * @parameter  expression="${tasks}"
	 */
	private String[] tasks;

	/**
	 * @parameter  expression="${task}"
	 */
	private String task;
	
	
	/**
	 * @parameter  expression="${project.basedir}"
	 */
	private File gradleProjectDirectory;

	
	/**
	 * 
	 * @parameter
	 */
	private String [] args;
	
	/**
	 * 
	 * @parameter
	 */
	private String [] jvmArgs;
	
	
	/**
	 * @parameter 
	 */
	private File javaHome;
	
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

	public void execute() throws MojoExecutionException, MojoFailureException {

		ProjectConnection connection = null;
		try {
			NewMojoLogger.attachMojo(this);
			GradleConnector c = GradleConnector.newConnector();
			getLog().info("jvmArgs: "+args);
			getLog().info("gradleProjectDirectory: "+getGradleProjectDirectory().getAbsolutePath());
			c = c.useGradleVersion(gradleVersion).forProjectDirectory(
					getGradleProjectDirectory());

		
			
			connection = c.connect();

			BuildLauncher launcher = connection.newBuild();
			launcher.forTasks(getTasks());

			if (jvmArgs!=null && jvmArgs.length>0) {
				launcher.setJvmArguments(jvmArgs);
			}
			if (args!=null && args.length>0) {
				launcher.withArguments(args);
			}
			if (javaHome!=null) {
				launcher.setJavaHome(javaHome);
			}
			launcher.addProgressListener(new MyProgressListener());

			// launcher will not block
			launcher.run(new MyResultHandler());

			waitForGradleToComplete();

			if (gradleConnectionException != null) {
				throw new MojoFailureException(gradleConnectionException,
						gradleConnectionException.toString(), gradleConnectionException.toString());
			}

		} finally {
			if (connection != null) {
				connection.close();
			}
			NewMojoLogger.detachMojo();
		}
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