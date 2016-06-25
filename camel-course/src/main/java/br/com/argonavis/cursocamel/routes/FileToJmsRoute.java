/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.com.argonavis.cursocamel.routes;

import br.com.argonavis.cursocamel.components.LoggingProcessor;
import org.apache.camel.builder.RouteBuilder;

/**
 * OK!
 */
public class FileToJmsRoute extends RouteBuilder {
        
        String filaDeCoisas = "jms:filaDeCoisas";

        @Override
        public void configure() throws Exception {
            this.from("file:lab/outbox?noop=true")
                    .process(new LoggingProcessor())
                    .to(filaDeCoisas);
        }
    }
