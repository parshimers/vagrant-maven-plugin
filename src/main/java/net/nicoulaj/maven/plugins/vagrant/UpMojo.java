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
import static org.codehaus.plexus.util.StringUtils.join;

/**
 * Invokes Vagrant {@code up} command.
 *
 * @author <a href="http://github.com/nicoulaj">Julien Nicoulaud</a>
 * @goal up
 * @since 1.0
 */
@SuppressWarnings("unused")
public final class UpMojo extends AbstractVagrantMojo {

    /** Mojo/Vagrant command name. */
    public static final String NAME = "up";

    /**
     * VM name.
     *
     * @parameter
     */
    protected String vm;

    /**
     * Enable or disable provisioning.
     *
     * @parameter default-value="true"
     */
    protected boolean provision;

    /**
     * Enable only certain provisioners, by type.
     *
     * @parameter
     */
    protected List<String> provisioners;

    @Override
    protected void doExecute() throws IOException {

        final List<String> args = new ArrayList<String>();

        args.add(NAME);

        if (!isEmpty(vm))
            args.add(vm);

        if (!provision)
            args.add("--no-provision");

        else if (provisioners != null && !provisioners.isEmpty()) {
            args.add("--provision");
            args.add("--provision-with");
            args.add(join(provisioners.iterator(), ","));

        } else
            args.add("--provision");

        cli(args);
    }
}
