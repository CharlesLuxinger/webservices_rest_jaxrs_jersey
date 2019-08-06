package br.com.alura.loja;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

public class ClienteTest {
	private HttpServer server;
	private WebTarget target;
	private Client client;

	@Before
	public void setUpClass() {
		server = Servidor.inicializaServidor();
		client = ClientBuilder.newClient();
		target = client.target("http://localhost:8080");
	}

	@After
	public  void tearDownClass() {
		server.shutdownNow();
	}

	@Test
	public void testeConexaoServidor() {
		Client client1 = ClientBuilder.newClient();
		WebTarget target1 = client1.target("http://www.mocky.io");
		String conteudo = target1.path("/v2/52aaf5deee7ba8c70329fb7d").request().get(String.class);
		assertTrue(conteudo.contains("<rua>Rua Vergueiro 3185"));
	}

	@Test
	public void testeConexaoServidorCarrinhoEsperado() {
		String conteudo = target.path("/carrinhos/1").request().get(String.class);
		Carrinho carrinho = (Carrinho) new XStream().fromXML(conteudo);

		assertEquals("Rua Vergueiro 3185, 8 andar", carrinho.getRua());
	}

	@Test
	public void testeNovosCarrinhos() {
		Carrinho carrinho = new Carrinho();
		carrinho.adiciona(new Produto(314, "Microfone", 37, 1));
		carrinho.setRua("Rua Vergueiro 100");
		carrinho.setCidade("Serra");

		String xml = carrinho.toXML();
		
		System.out.println(xml);
		Entity<String> entity = Entity.entity(xml, MediaType.APPLICATION_XML);

		Response response = target.path("/carrinhos").request().post(entity);

		assertEquals(201, response.getStatus());

		String location = response.getHeaderString("Location");
		String conteudo = client.target(location).request().get(String.class);

		assertTrue(conteudo.contains("Microfone"));
	}

}
