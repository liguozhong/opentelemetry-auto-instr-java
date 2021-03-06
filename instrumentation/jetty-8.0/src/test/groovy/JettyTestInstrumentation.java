/*
 * Copyright The OpenTelemetry Authors
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
import static net.bytebuddy.matcher.ElementMatchers.named;

import com.google.auto.service.AutoService;
import io.opentelemetry.auto.test.base.HttpServerTestAdvice;
import io.opentelemetry.auto.tooling.Instrumenter;
import net.bytebuddy.agent.builder.AgentBuilder;

@AutoService(Instrumenter.class)
public class JettyTestInstrumentation implements Instrumenter {

  @Override
  public AgentBuilder instrument(final AgentBuilder agentBuilder) {
    return agentBuilder
        // Jetty 8.0
        .type(named("org.eclipse.jetty.server.HttpConnection"))
        .transform(
            new AgentBuilder.Transformer.ForAdvice()
                .advice(
                    named("handleRequest"), HttpServerTestAdvice.ServerEntryAdvice.class.getName()))
        // Jetty 8.?
        .type(named("org.eclipse.jetty.server.AbstractHttpConnection"))
        .transform(
            new AgentBuilder.Transformer.ForAdvice()
                .advice(
                    named("headerComplete"),
                    HttpServerTestAdvice.ServerEntryAdvice.class.getName()))
        // Jetty 9
        .type(named("org.eclipse.jetty.server.HttpChannel"))
        .transform(
            new AgentBuilder.Transformer.ForAdvice()
                .advice(named("handle"), HttpServerTestAdvice.ServerEntryAdvice.class.getName()));
  }
}
