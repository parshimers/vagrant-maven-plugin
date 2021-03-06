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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.codehaus.plexus.util.StringUtils.isEmpty;

/**
 * Invokes Vagrant {@code ssh} command.
 *
 * @author <a href="http://github.com/nicoulaj">Julien Nicoulaud</a>
 * @goal ssh
 * @since 1.0
 */
@SuppressWarnings("unused")
public final class SshMojo extends AbstractVagrantMojo {

    /** Mojo/Vagrant command name. */
    public static final String NAME = "ssh";

    /**
     * VM name.
     *
     * @parameter
     */
    protected String vm;

    /**
     * Execute an SSH command directly.
     *
     * @parameter
     */
    protected String command;

    /**
     * Plain mode, leaves authentication up to user.
     *
     * @parameter default-value="false"
     */
    protected boolean plain;

    /**
     * Extra SSH arguments.
     *
     * @parameter
     */
    protected String sshArgs;

    @Override
    protected void doExecute() throws IOException{

        final List<String> args = new ArrayList<String>();

        args.add(NAME);

        if (!isEmpty(vm))
            args.add(vm);

        if (!isEmpty(command)) {
            args.add("--command");
            args.add(command);
        }

        if (plain)
            args.add("--plain");

        if (!isEmpty(sshArgs)) {
            args.add("--");
            args.add(sshArgs);
        }

        cli(args);
    }
}
