# gradle-maven-plugin

[![Circle CI](https://circleci.com/gh/LendingClub/gradle-maven-plugin.svg?style=svg)](https://circleci.com/gh/LendingClub/gradle-maven-plugin)


This is a maven plugin that makes it easy to invoke Gradle tasks from within Maven.  


# Objective

Gradle is an awesome general-purpose tool.  If your project/organization is committed to maven, switching to gradle may not 
be a practical option.  The plugin is conceptually similar to the maven-antrun-plugin, which allows ant to be invoked from maven.

Now instead of using Ant to perform ad-hoc tasks from within Maven, you can use Groovy/Gradle instead!

# Usage

The gradle-maven-plugin is now in Maven Central, so there is no need to declare a custom repository.

To use the plugin, simply declare the plugin and bind it to the maven lifecycle phase of your choice:

```xml
	<plugin>
		<groupId>org.fortasoft</groupId>
		<artifactId>gradle-maven-plugin</artifactId>
		<version>1.0.8</version>
		<configuration>
			<tasks>
				<!-- this would effectively call "gradle doSomething" -->
				<task>doSomething</task>
			</tasks>
		</configuration>
		<executions>
			<execution>
				<!-- You can bind this to any phase you like -->
				<phase>compile</phase>
				<goals>
					<!-- goal must be "invoke" -->
					<goal>invoke</goal>
				</goals>
			</execution>
		</executions>
	</plugin>
```

Now when you run maven, gradle will be invoked and execute the "doSomething" task defined in `build.gradle`.

Obviously you can change the task(s) to suit your needs.

In this example, the gradle invocation will happen during the maven "compile" phase, but this can be easily changed by changing
the &lt;phase&gt; element value.

## Options
These options can be given in the &lt;configuration&gt; element:

<table>
<tr><th>option</th><th>required</th><th>description</th><th>example</th></tr>

<tr><td>task</td><td>yes (or tasks) </td><td>gradle task to be invoked</td><td><pre>&lt;task&gt;myTask&lt;/task&gt;</pre> </td></tr>
<tr><td>tasks</td><td>yes (or task) </td><td>gradle tasks to be invoked</td><td><pre>&lt;tasks&gt;<br> &lt;task&gt;task1&lt;/task&gt;<br/> &lt;task&gt;task2&lt;/task&gt; <br/>&lt;/tasks&gt;</pre></td></tr>
<tr><td>gradleVersion</td><td>no</td><td>version of gradle to use</td><td><pre>&lt;gradleVersion&gt;1.6&lt;/gradleVersion&gt;</pre></td></tr>
<tr><td>gradleProjectDirectory</td><td>no</td><td>path to the location of your build.gradle</td><td><pre>&lt;gradleProjectDirectory&gt;<br /> ${project.basedir}/another/path<br/> &lt;/gradleProjectDirectory&gt;</pre></td></tr>
<tr><td>javaHome</td><td>no</td><td>give and explicit path to a JAVA_HOME</td><td><pre>&lt;javaHome&gt; /my/path/to/jdk &lt;/javaHome&gt;</td></pre></tr>
<tr><td>args</td><td>no</td><td>pass argument to gradle</td><td><pre>&lt;args&gt;<br> &lt;arg&gt;-q&lt;/arg&gt; <br/>&lt;/args&gt;</td></pre></tr>
<tr><td>jvmArgs</td><td>no</td><td>pass JVM arg to gradle</td><td><pre>&lt;jvmArgs&gt;<br/> &lt;jvmArg&gt;-XX:MaxPermSize=128m&lt;/jvmArg&gt;<br/> 
&lt;jvmArg&gt;-Xmx256m&lt;/jvmArg&gt; <br/>&lt;/jvmArgs&gt;</pre></td></tr>
</table>

# Plugin Testing

Here is some information on maven plugin testing.  This project is currently using the maven-invoker-plugin for testing.

* http://maven.apache.org/plugin-developers/plugin-testing.html
* http://maven.apache.org/plugins/maven-invoker-plugin/usage.html
