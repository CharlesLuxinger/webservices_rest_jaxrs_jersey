package br.com.alura.loja.resource;

import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

@Path("/carrinhos")
public class CarinhoResource {

	// Retorna um XML
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Carrinho busca(@PathParam("id") long id) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		return carrinho;
	}

	/*
	 * //Retorna um Json
	 * 
	 * @Path("/{id}")
	 * 
	 * @GET
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public String busca(@PathParam("id")
	 * long id) { Carrinho carrinho = new CarrinhoDAO().busca(id); return
	 * carrinho.toJSON(); }
	 */

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	public Response adiciona(Carrinho carrinho) {
		new CarrinhoDAO().adiciona(carrinho);
		URI uri = URI.create("/carrinhos/" + carrinho.getId());
		return Response.created(uri).build();
	}

	@Path("/{id}/produtos/{produtoId}")
	@DELETE
	public Response removeProduto(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);
		carrinho.remove(produtoId);

		return Response.ok().build();
	}

	@Path("/{id}")
	@DELETE
	public Response removeCarrinho(@PathParam("id") long id) {
		new CarrinhoDAO().remove(id);
		return Response.ok().build();
	}

	@Path("/{id}/produtos/{produtoId}")
	@PUT
	public Response alteraProduto(Produto produto, @PathParam("id") long id, @PathParam("produtoId") long produtoId) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);

		carrinho.troca(produto);

		return Response.ok().build();
	}

	@Path("/{id}/produtos/{produtoId}/quantidade/{quantidade}")
	@PUT
	public Response alteraProduto(Produto produto, @PathParam("id") long id, @PathParam("produtoId") long produtoId,
			@PathParam("quantidade") long quantidade) {
		Carrinho carrinho = new CarrinhoDAO().busca(id);

		carrinho.trocaQuantidade(produto);

		return Response.ok().build();
	}
}
