/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.superbiz.moviefun;

import org.apache.cxf.jaxrs.client.WebClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.superbiz.moviefun.rest.ApplicationConfig;
import org.superbiz.moviefun.rest.MoviesResource;

import javax.ws.rs.core.Response;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Arquillian.class)
public class MoviesTest {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        final WebArchive webArchive = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Movie.class,  MoviesBean.class)
                .addClasses(MoviesResource.class, ApplicationConfig.class)
                .addAsWebInfResource(new StringAsset("<beans/>"), "beans.xml");


        System.out.println(webArchive.toString(true));

        return webArchive;
    }

    @ArquillianResource
    private URL base;


    @Test
    @RunAsClient
    public void TestMoviesCount() throws Exception{



        final WebClient webClient = WebClient.create(base.toExternalForm());


        //This Works fine
        final Response response = webClient.path("/api/movies/count").get();
        assertEquals(200, response.getStatus());
        final String content = slurp ((InputStream) response.getEntity());
        assertTrue(Integer.parseInt(content)  >= 5);


    }


    /**
     * Reusable utility method
     * Move to a shared class or replace with equivalent
     */
    public static String slurp (final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        out.flush();
        return new String(out.toByteArray());
    }

}
