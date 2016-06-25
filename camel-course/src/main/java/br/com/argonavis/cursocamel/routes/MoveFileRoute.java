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
 *
 * @author helderdarocha
 */
public class MoveFileRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        this.from("file:lab/inbox?noop=true")
                .process(new LoggingProcessor()) // usando processador para interceptrar rota e logar nome do arquivo copiado
                //to("file:lab/outbox?noop=true");
                .to("mock:result");
    }
}
