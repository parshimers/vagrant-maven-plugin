/*
 * Copyright 2013 vagrant-maven-plugin contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.nicoulaj.maven.plugins.vagrant;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.project.MavenProject;
import org.apache.maven.repository.RepositorySystem;
import org.codehaus.classworlds.ClassRealm;
import org.codehaus.plexus.archiver.UnArchiver;

import java.io.File;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.lang.ProcessBuilder;

import static java.util.Arrays.asList;
import static org.codehaus.plexus.util.StringUtils.isNotBlank;

/**
 * Base class for {@code Mojo}s invoking Vagrant.
 *
 * @author <a href="http://github.com/nicoulaj">Julien Nicoulaud</a>
 * @since 1.0
 */
abstract class AbstractVagrantMojo extends AbstractMojo {

    /**
     * Custom {@code VAGRANT_HOME}, which is the directory where Vagrant boxes are stored.
     * <p/>
     * Modifying this property has an impact on isolation/build portability, eg:
     * <ul>
     * <li>In {@code project.build.directory} (default): boxes must be imported every time, but no file is created outside of build directory.</li>
     * <li>In {@code project.basedir}: boxes can be imported once for all, but files are created outside of build directory.</li>
     * <li>In {@code ~/.vagrant.d} (Vagrant default): user boxes can be directly used, but files are created outside of project structure.</li>
     * </ul>
     *
     * @parameter default-value="${project.build.directory}/vagrant/vagrant.d"
     * @since 1.0
     */
    protected File vagrantHome;

    /**
     * Custom {@code VAGRANT_RC}, which is the configuration file used by Vagrant.
     * <p/>
     * Modifying this property has an impact on isolation/build portability, eg:
     * <ul>
     * <li>In {@code project.build.directory} (default): user installation can not impact build.</li>
     * <li>In {@code project.basedir}: user installation can not impact build.</li>
     * <li>In {@code ~/.vagrantrc} (Vagrant default): user installation can impact build.</li>
     * </ul>
     *
     * @parameter default-value="${project.build.directory}/vagrant/vagrantrc"
     * @since 1.0
     */
    protected File vagrantRc;

    /**
     * Required by {@link AbstractGemMojo}.
     *
     * @parameter property="project"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Required by {@link AbstractGemMojo}.
     *
     * @parameter property="plugin"
     * @readonly
     */
    private PluginDescriptor plugin;

    /**
     * Required by {@link AbstractGemMojo}.
     *
     * @component
     */
    private RepositorySystem repositorySystem;

    /**
     * Required by {@link AbstractGemMojo}.
     *
     * @parameter default-value="${localRepository}"
     * @required
     * @readonly
     */
    private ArtifactRepository localRepository;

    /**
     * Required by {@link AbstractGemMojo}.
     *
     * @parameter property="dummyExpression"
     * @readonly
     */
    private ClassRealm classRealm;

    /**
     * Required by {@link AbstractGemMojo}.
     *
     * @component role-hint="zip"
     */
    private UnArchiver unzip;

    public final void execute() throws MojoExecutionException, MojoFailureException {
        try{
            doExecute();
        }
        catch (IOException e){
            throw new MojoFailureException("Vagrant execution failed", e);
        }
    }


    protected void doExecute() throws IOException {}

    protected final void cli(String... args) throws IOException{
        cli(asList(args));
    }

    protected final void cli(Iterable<String> args) throws IOException{
        ArrayList<String> command = new ArrayList<String>();
        command.add("vagrant");
        for (String arg : args) if (isNotBlank(arg)) command.add(arg);
        ProcessBuilder pb = new ProcessBuilder(command);
        Map<String,String> env = pb.environment();
        env.put("VAGRANT_HOME", vagrantHome.toString());
        env.put("VAGRANT_RC", vagrantRc.toString());
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        InputStreamReader read = new InputStreamReader(proc.getInputStream());
        int ch;
        while((ch = read.read())!=-1) System.out.print((char)ch);

        read.close();
/*
        factory.addEnv("VAGRANT_HOME", vagrantHome);
        factory.addEnv("VAGRANT_RC", vagrantRc);
        final Script script = factory.newScriptFromSearchPath("vagrant");
        for (String arg : args) if (isNotBlank(arg)) script.addArg(arg);
        script.execute();
*/
    }
}
