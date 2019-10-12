package br.com.caelum.estoque.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import br.com.caelum.estoque.modelo.item.Filtro;
import br.com.caelum.estoque.modelo.item.Filtros;
import br.com.caelum.estoque.modelo.item.Item;
import br.com.caelum.estoque.modelo.item.ItemDao;
import br.com.caelum.estoque.modelo.item.ListaItens;
import br.com.caelum.estoque.modelo.usuario.AutorizacaoException;
import br.com.caelum.estoque.modelo.usuario.TokenDao;
import br.com.caelum.estoque.modelo.usuario.TokenUsuario;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED)
public class EstoqueWS {

	private ItemDao dao = new ItemDao();


	@WebMethod(operationName = "TodosItens")
	@WebResult(name = "itens")
	public ListaItens getItens(@WebParam(name = "filtros") Filtros filtros) {

		List<Filtro> listaFiltros = filtros.getLista();
		List<Item> itens = dao.todosItens(listaFiltros);

		System.out.println("Chamando getItens()");
		return new ListaItens(itens);
	}

	@WebMethod(operationName = "CadatrarItem")
	@WebResult(name = "item")
	public Item cadastrarItem(@WebParam(name = "token", header = true) TokenUsuario token,
			@WebParam(name = "item") Item item) throws AutorizacaoException {
		System.out.println("Cadastrando Item");

		boolean tokenValido = new TokenDao().ehValido(token);
		if (tokenValido) {
			dao.cadastrar(item);
			return item;
		}
		throw new AutorizacaoException("Token Invalido");
	}
}